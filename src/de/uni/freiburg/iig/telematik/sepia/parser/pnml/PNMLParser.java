package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

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
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.parser.ParserInterface;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;

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
 */
public class PNMLParser implements ParserInterface {

	/** Relax NG namespace */
	public final static String RNG_NAMESPACE = "http://relaxng.org/ns/structure/1.0";

	/**
	 * Returns the net type name by its URI.
	 */
	private NetType getPNMLType(String pntdURI) throws ParameterException, PNMLParserException {
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
	 */
	public <P extends AbstractPlace<F, S>,
			T extends AbstractTransition<F, S>,
			F extends AbstractFlowRelation<P, T, S>,
			M extends AbstractMarking<S>,
			S extends Object>

	AbstractGraphicalPN<P, T, F, M, S>

	parse(String pnmlFile) throws IOException, ParserException, ParameterException {

		return parse(pnmlFile, true, true);
	}

	/**
	 * Parses a PNML {@link File}.
	 * 
	 * @param pnmlFile
	 *            File to parse
	 * @param requireNetType
	 *            Set to <code>true</code> if the net type should be required. If the net type can't be determined an exception will be thrown. If set to
	 *            <code>false</code>, the parser will try to read the file as P/T-net.
	 * @param verifySchema
	 *            Set to <code>true</code> if the given file should be validated by the petri net type definition of the given file.
	 * @return A {@link AbstractGraphicalPN}, acting as container for a petri net and its graphical information.
	 */
	public <P extends AbstractPlace<F, S>,
			T extends AbstractTransition<F, S>,
			F extends AbstractFlowRelation<P, T, S>,
			M extends AbstractMarking<S>,
			S extends Object>

	AbstractGraphicalPN<P, T, F, M, S>

	parse(String pnmlFile, boolean requireNetType, boolean verifySchema) throws IOException, ParserException, ParameterException {

		File inputFile = new File(pnmlFile);
		if (inputFile.isDirectory())
			throw new IOException("I/O Error on opening file: File is a directory!");
		if (!inputFile.exists())
			throw new IOException("I/O Error on opening file: File does not exist!");
		if (!inputFile.canRead())
			throw new IOException("I/O Error on opening file: Unable to read file!");

		return parse(inputFile, requireNetType, verifySchema);
	}

	/**
	 * Parse a PNML file and require it to be valid.
	 * 
	 * @param pnmlFile
	 *            File to be parsed
	 * @return A {@link AbstractGraphicalPN}, acting as container for a petri net and its graphical information.
	 */
	@Override
	public <P extends AbstractPlace<F, S>,
			T extends AbstractTransition<F, S>,
			F extends AbstractFlowRelation<P, T, S>,
			M extends AbstractMarking<S>,
			S extends Object>

	AbstractGraphicalPN<P, T, F, M, S>

	parse(File pnmlFile) throws IOException, ParserException, ParameterException {
		return parse(pnmlFile, true, true);
	}

	/**
	 * Parses a PNML {@link File}.
	 * 
	 * @param pnmlFile
	 *            File to parse
	 * @param requireNetType
	 *            Set to <code>true</code> if the net type should be required. If the net type can't be determined an exception will be thrown. If set to
	 *            <code>false</code>, the parser will try to read the file as P/T-net.
	 * @param verifySchema
	 *            Set to <code>true</code> if the given file should be validated by the petri net type definition of the given file.
	 * @return A {@link AbstractGraphicalPN}, acting as container for a petri net and its graphical information.
	 */
	@SuppressWarnings("unchecked")
	public <P extends AbstractPlace<F, S>,
			T extends AbstractTransition<F, S>,
			F extends AbstractFlowRelation<P, T, S>,
			M extends AbstractMarking<S>,
			S extends Object>

	AbstractGraphicalPN<P, T, F, M, S>

	parse(File pnmlFile, boolean requireNetType, boolean verifySchema) throws IOException, ParserException, ParameterException {

		Validate.notNull(pnmlFile);

		Document pnmlDocument = readPNMLFile(pnmlFile);

		// Try to find out the net type
		String netTypeStringURI = getPNMLTypeURI(pnmlDocument);
		NetType netType = getPNMLType(netTypeStringURI);
		if (requireNetType && netType == null)
			throw new PNMLParserException(ErrorCode.INVALID_NET_TYPE);
		if (netType == null) {
			netType = NetType.PTNet;
		}

		if (verifySchema) {
			verifySchema(pnmlFile, NetType.getURL(netType));
		}

		switch (netType) {
		case PTNet:
			PNMLPTNetParser ptnetParser = new PNMLPTNetParser();
			return (AbstractGraphicalPN<P, T, F, M, S>) ptnetParser.parse(pnmlDocument);
		case CPN:
			PNMLCPNParser cpnParser = new PNMLCPNParser();
			return (AbstractGraphicalPN<P, T, F, M, S>) cpnParser.parse(pnmlDocument);
		case CWN:
			PNMLCWNParser cwnParser = new PNMLCWNParser();
			return (AbstractGraphicalPN<P, T, F, M, S>) cwnParser.parse(pnmlDocument);
		case IFNet:
			PNMLIFNetParser ifnetParser = new PNMLIFNetParser();
			return (AbstractGraphicalPN<P, T, F, M, S>) ifnetParser.parse(pnmlDocument);
		}

		throw new ParserException("Couldn't determine a proper PNML parser.");
	}

	/**
	 * Reads a PNML file and converts it to a DOM {@link Document}.
	 * 
	 * @param pnmlFile
	 *            PNML file to read
	 * @return Readable, well-formed and normalized PNML document
	 */
	private Document readPNMLFile(File pnmlFile) throws ParameterException, IOException, XMLParserException {
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
	 */
	private void verifySchema(File pnmlFile, URL pntdUrl) throws IOException, PNMLParserException {
		// Create verifier factory instance with PNML namespace
		VerifierFactory factory = null;
		try {
			factory = VerifierFactory.newInstance(RNG_NAMESPACE);
		} catch (VerifierConfigurationException e) {
			throw new PNMLParserException(ErrorCode.VALIDATION_CONFIGURATION_ERROR, e.getMessage());
		}
		// Open stream to PNTD
		URLConnection connection = pntdUrl.openConnection();
		InputStream in = connection.getInputStream();
		// Create verifier with PNTD input stream and validate
		Verifier verifier = null;
		try {
			verifier = factory.newVerifier(in);
		} catch (Exception e) {
			throw new PNMLParserException(ErrorCode.VALIDATION_CONFIGURATION_ERROR, e.getMessage());
		}

		try {
			verifier.verify(pnmlFile);
		} catch (SAXException e) {
			throw new PNMLParserException(ErrorCode.VALIDATION_FAILED, e.getMessage());
		}
	}
}
