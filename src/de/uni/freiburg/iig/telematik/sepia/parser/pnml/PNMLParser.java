package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import static de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.AnalysisContextParser.ANALYSIS_CONTEXT_SCHEMA;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.iso_relax.verifier.VerifierFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.parser.XMLParserException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.PNParserInterface;
import de.uni.freiburg.iig.telematik.sepia.parser.PNParsingFormat;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.PNMLCPNParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.LabelingParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.PNMLIFNetParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt.PNMLPTNetParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ptc.PNMLPTCNetParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.timedNet.PNMLTimedNetParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

/**
 * <p>
 * This class estimates the PNML type and chooses a fitting parser.
 * </p>
 * <p>
 * The process of parsing a PNML file is the following:
 * </p>
 * <ol>
 * <li>Check if the document is well-formed XML.</li>
 * <li>Determine net type by reading the net type URI (get type from URINettypeRefs table).</li>
 * <li>Read the net type specific net components. To avoid violating a constraint, the objects must be read in multiple iterations:
 * <ol>
 * <li>Read nodes (places and transitions) with their marking and labeling.</li>
 * <li>Read edges (arcs) with their annotations and specific starting and ending nodes.</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * @author Adrian Lange
 * @param <P>
 * @param <T>
 * @param <F>
 * @param <M>
 * @param <S>
 * @param <N>
 * @param <G>
 */
public class PNMLParser <P extends AbstractPlace<F, S>,
						 T extends AbstractTransition<F, S>,
						 F extends AbstractFlowRelation<P, T, S>,
						 M extends AbstractMarking<S>,
						 S extends Object,
						 N extends AbstractPetriNet<P, T, F, M, S>,
						 G extends AbstractPNGraphics<P, T, F, M, S>> implements PNParserInterface {

	/** Relax NG namespace */
	public final static String RNG_NAMESPACE = "http://relaxng.org/ns/structure/1.0";

	public final static String PNMLCOREMODEL_SCHEMA = "http://www.pnml.org/version-2009/grammar/pnmlcoremodel";
	public final static String PTNET_SCHEMA = "http://www.pnml.org/version-2009/grammar/ptnet";

        private static final String PNTD_PATH = "/pntd/";
        private static final String PNML_PNTD_PATH = "pnmlorg/";
        public static final String PTNET_PNTD = PNTD_PATH + "ptnet.pntd";
        public static final String CPNET_PNTD = PNTD_PATH + "cpnet.pntd";
        public static final String IFNET_PNTD = PNTD_PATH + "ifnet.pntd";
        public static final String RTPNET_PNTD = PNTD_PATH + "rtpnet.pntd";
        public static final String PTCNET_PNTD = PNTD_PATH + "ptcnet.pntd";
        public static final String ANALYSISCONTEXT_RNG = PNTD_PATH + "analysiscontext.rng";
        public static final String LABELING_RNG = PNTD_PATH + "labeling.rng";
        public static final String PNMLORG_PNMLCOREMODEL_RNG = PNTD_PATH + PNML_PNTD_PATH + "pnmlcoremodel.rng";
        public static final String PNMLORG_PTNET_PNTD = PNTD_PATH + PNML_PNTD_PATH + "ptnet.pntd";

	/**
	 * Returns the net type name by its URI.
	 */
	private NetType getPNMLType(String pntdURI) throws PNMLParserException {
		Validate.notNull(pntdURI);
		return NetType.getNetType(pntdURI);
	}

	/**
	 * Returns the PNML type URI from a given DOM {@link Document}.
	 */
	private String getPNMLTypeURI(Document pnmlDocument) throws PNMLParserException {
		// Get all elements named net, which should result in only one element
		NodeList netElement = pnmlDocument.getElementsByTagName("net");
		if (netElement == null || netElement.getLength() != 1)
			throw new PNMLParserException(ErrorCode.MISSING_NET_TAG);
		Node netTypeURINode = netElement.item(0).getAttributes().getNamedItem("type");
		if (netTypeURINode == null)
			throw new PNMLParserException(ErrorCode.MISSING_NET_TYPE_ATTRIBUTE);
		// Read and return type attribute
		String netTypeURI = netTypeURINode.getNodeValue();

		return netTypeURI;
	}

	/**
	 * Parse a PNML file and require it to be valid.
	 * 
	 * @param pnmlFile
	 *            Path to the file to be parsed
	 * @return A {@link AbstractGraphicalPN}, acting as container for a petri net and its graphical information.
         * @throws IOException
         * @throws ParserException
	 */
	public AbstractGraphicalPN<P,T,F,M,S,N,G> parse(String pnmlFile) throws IOException, ParserException {

		return this.parse(pnmlFile, true, true);
	}

	/**
	 * Parses a PNML {@link File}.
	 * 
	 * @param pnmlFile
	 *            File to parse
	 * @param requireNetType
	 *            Set to <code>true</code> if the net type should be required. If the net type can't be determined an exception will be thrown. If set to <code>false</code>, the parser will try to read the file as P/T-net.
	 * @param verifySchema
	 *            Set to <code>true</code> if the given file should be validated by the petri net type definition of the given file.
	 * @return A {@link AbstractGraphicalPN}, acting as container for a petri net and its graphical information.
         * @throws IOException
         * @throws ParserException
	 */
	public AbstractGraphicalPN<P,T,F,M,S,N,G> parse(String pnmlFile, boolean requireNetType, boolean verifySchema) throws IOException, ParserException{
	File inputFile = new File(pnmlFile);
		if (inputFile.isDirectory())
			throw new IOException("I/O Error on opening file: File is a directory!");
		if (!inputFile.exists())
			throw new IOException("I/O Error on opening file: File does not exist!");
		if (!inputFile.canRead())
			throw new IOException("I/O Error on opening file: Unable to read file!");

		return this.parse(inputFile, requireNetType, verifySchema);
	}

	/**
	 * Parse a PNML file and require it to be valid.
	 * 
	 * @param pnmlFile
	 *            File to be parsed
	 * @return A {@link AbstractGraphicalPN}, acting as container for a petri net and its graphical information.
         * @throws IOException
         * @throws ParserException
	 */
	@Override
	public AbstractGraphicalPN<P,T,F,M,S,N,G> parse(File pnmlFile) throws IOException, ParserException {
		return this.parse(pnmlFile, true, true);
	}

	/**
	 * Parses a PNML {@link File}.
	 * 
	 * @param pnmlFile
	 *            File to parse
	 * @param requireNetType
	 *            Set to <code>true</code> if the net type should be required. If the net type can't be determined an exception will be thrown. If set to <code>false</code>, the parser will try to read the file as P/T-net.
	 * @param verifySchema
	 *            Set to <code>true</code> if the given file should be validated by the petri net type definition of the given file.
	 * @return A {@link AbstractGraphicalPN}, acting as container for a petri net and its graphical information.
         * @throws IOException
         * @throws ParserException
	 */
	public AbstractGraphicalPN<P,T,F,M,S,N,G> parse(File pnmlFile, boolean requireNetType, boolean verifySchema) throws IOException, ParserException {

		Validate.notNull(pnmlFile);

		Document pnmlDocument = readRNGFile(pnmlFile);

		// Try to find out the net type
		String netTypeStringURI = getPNMLTypeURI(pnmlDocument);
		NetType netType = getPNMLType(netTypeStringURI);
		if (requireNetType && netType == NetType.Unknown)
			throw new PNMLParserException(ErrorCode.INVALID_NET_TYPE);
		if (netType == NetType.Unknown) {
			netType = NetType.PTNet;
		}

		if (verifySchema) {
			verifySchema(pnmlFile, NetType.getVerificationURL(netType));
		}

		// ugly unbounded wildcard as work-around for bug JDK-6932571
		Object graphicalPN = null;

		switch (netType) {
		case PTNet:
		case Unknown:
			PNMLPTNetParser ptnetParser = new PNMLPTNetParser();
			graphicalPN = ptnetParser.parse(pnmlDocument);
			break;
		case CPN:
			PNMLCPNParser cpnParser = new PNMLCPNParser();
			graphicalPN = cpnParser.parse(pnmlDocument);
			break;
		case IFNet:
			PNMLIFNetParser ifnetParser = new PNMLIFNetParser();
			graphicalPN = ifnetParser.parse(pnmlDocument);
			break;
		case RTPTnet:
			PNMLTimedNetParser rtpnParser = new PNMLTimedNetParser();
			graphicalPN = rtpnParser.parse(pnmlDocument);
			break;
		case PTCNet:
			PNMLPTCNetParser ptcnetParser = new PNMLPTCNetParser();
			graphicalPN = ptcnetParser.parse(pnmlDocument);
			break;
		}

		if (graphicalPN != null)
			return (AbstractGraphicalPN<P, T, F, M, S, N, G>) graphicalPN;
		else
			throw new ParserException("Couldn't determine a proper PNML parser.");
	}

	/**
	 * Reads a RelaxNG file and converts it to a DOM {@link Document}.
	 * 
	 * @param pnmlFile
	 *            PNML file to read
	 * @return Readable, well-formed and normalized PNML document
         * @throws IOException
         * @throws XMLParserException
	 */
	public static Document readRNGFile(File pnmlFile) throws IOException, XMLParserException {
		Validate.notNull(pnmlFile);

		// Check if pnmlFile exists and is readable
		if (!pnmlFile.exists())
			throw new IOException("The given PNML file doesn't exist.");
		if (!pnmlFile.canRead())
			throw new IOException("The given PNML file exists but is not readable.");

		Document pnmlDocument = null;
		try {
			// Check if PNML file is well-formed and return the DOM document
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			pnmlDocument = dBuilder.parse(pnmlFile);
			pnmlDocument.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			throw new XMLParserException();
		} catch (SAXException e) {
			throw new XMLParserException(de.invation.code.toval.parser.XMLParserException.ErrorCode.TAGSTRUCTURE);
		}

		return pnmlDocument;
	}

	/**
	 * Verifies a PNML file based on a given petri net type definition. An exception will be thrown if the PNML file is not verified.
	 * 
	 * @param pnmlFile
	 *            File to verify
	 * @param pntdUrl
	 *            {@link URL} of the petri net type definition
         * @throws IOException
         * @throws PNMLParserException
	 */
	public static void verifySchema(File pnmlFile, URL pntdUrl) throws IOException, PNMLParserException {
		// Create verifier factory instance with PNML namespace
		VerifierFactory factory = null;
		try {
			factory = VerifierFactory.newInstance(RNG_NAMESPACE);
		} catch (VerifierConfigurationException e) {
			throw new PNMLParserException(ErrorCode.VALIDATION_CONFIGURATION_ERROR, e.getMessage());
		}
		// Open stream to PNTD
                InputStream in;
                if (pntdUrl.equals(NetType.getVerificationURL(NetType.PTNet))) {
                        in = PNMLParser.class.getResourceAsStream(PTNET_PNTD);
                } else if (pntdUrl.equals(NetType.getVerificationURL(NetType.CPN))) {
                        in = PNMLParser.class.getResourceAsStream(CPNET_PNTD);
                } else if (pntdUrl.equals(NetType.getVerificationURL(NetType.IFNet))) {
                        in = PNMLParser.class.getResourceAsStream(IFNET_PNTD);
                } else if (pntdUrl.equals(NetType.getVerificationURL(NetType.RTPTnet))) {
                        in = PNMLParser.class.getResourceAsStream(RTPNET_PNTD);
                } else if (pntdUrl.equals(NetType.getVerificationURL(NetType.PTCNet))) {
                    in = PNMLParser.class.getResourceAsStream(PTCNET_PNTD);
                } else if (pntdUrl.equals(new URL(ANALYSIS_CONTEXT_SCHEMA))) {
                        in = PNMLParser.class.getResourceAsStream(ANALYSISCONTEXT_RNG);
                } else if (pntdUrl.equals(new URL(LabelingParser.LABELING_SCHEMA))) {
                        in = PNMLParser.class.getResourceAsStream(LABELING_RNG);
                } else if (pntdUrl.equals(new URL(PNMLCOREMODEL_SCHEMA))) {
                        in = PNMLParser.class.getResourceAsStream(PNMLORG_PNMLCOREMODEL_RNG);
                } else if (pntdUrl.equals(new URL(PTNET_SCHEMA))) {
                        in = PNMLParser.class.getResourceAsStream(PNMLORG_PTNET_PNTD);
                } else {
                        URLConnection connection = pntdUrl.openConnection();
                        in = connection.getInputStream();
                }
		// Create verifier with PNTD input stream and validate
		Verifier verifier = null;
		try {
			verifier = factory.newVerifier(in);
		} catch (VerifierConfigurationException | SAXException | IOException e) {
			throw new PNMLParserException(ErrorCode.VALIDATION_CONFIGURATION_ERROR, e.getMessage());
		}

		try {
			verifier.verify(pnmlFile);
		} catch (SAXException e) {
			throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, e.getMessage());
		}
	}

	@Override
	public PNParsingFormat getParsingFormat() {
		return PNParsingFormat.PNML;
	}
}
