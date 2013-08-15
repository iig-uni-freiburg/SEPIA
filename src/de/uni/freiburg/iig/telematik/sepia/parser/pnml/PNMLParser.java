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
import de.uni.freiburg.iig.telematik.sepia.export.NetType;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.parser.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.parser.ParserInterface;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * <p>
 * TODO
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
	 * TODO
	 * 
	 * @param pntdURI
	 * @return
	 * @throws ParameterException 
	 * @throws PNMLParserException 
	 */
	public static NetType getPNMLType(String pntdURI) throws ParameterException, PNMLParserException {
		Validate.notNull(pntdURI);
		
		NetType netType = NetType.getNetType(pntdURI);
		if (netType == null) {
			throw new PNMLParserException(ErrorCode.INVALID_NET_TYPE, pntdURI);
		}
		return netType;
	}

	/**
	 * TODO
	 * 
	 * @param pnmlDocument
	 * @return
	 * @throws PNMLParserException 
	 */
	public static String getPNMLTypeURI(Document pnmlDocument) throws PNMLParserException {
		// Get all elements named net, which should result in only one element
		NodeList netElement = pnmlDocument.getElementsByTagName("net");
		if (netElement.getLength() != 1)
			throw new PNMLParserException(ErrorCode.MISSING_NET_TAG);
		Node netTypeURINode = netElement.item(0).getAttributes().getNamedItem("type");
		if (netTypeURINode == null)
			throw new PNMLParserException(ErrorCode.MISSING_NET_TYPE_ATTRIBUTE);
		// Read and return type attribute
		String netTypeURI = netTypeURINode.getNodeValue();

		return netTypeURI;
	}
	
	public <P extends AbstractPlace<F,S>, 
			T extends AbstractTransition<F,S>, 
			F extends AbstractFlowRelation<P,T,S>, 
			M extends AbstractMarking<S>, 
			S extends Object> 

			GraphicalPN<P, T, F, M, S> 

	parse(File pnmlFile) throws IOException, ParserException, ParameterException {
		return parse(pnmlFile, true);
	}

	/**
	 * TODO
	 * 
	 * @param pnmlFile
	 * @return
	 * @throws ParameterException 
	 */
	@SuppressWarnings("unchecked")
	public <P extends AbstractPlace<F,S>, 
			T extends AbstractTransition<F,S>, 
			F extends AbstractFlowRelation<P,T,S>, 
			M extends AbstractMarking<S>, 
			S extends Object> 
	
			GraphicalPN<P, T, F, M, S> 
	
			parse(File pnmlFile, boolean verifySchema) throws IOException, ParserException, ParameterException {

		Validate.notNull(pnmlFile);

		Document pnmlDocument = readPNMLFile(pnmlFile);
		String netTypeStringURI = getPNMLTypeURI(pnmlDocument);
		NetType netType = getPNMLType(netTypeStringURI);

		if (verifySchema) {
			verifySchema(pnmlFile, NetType.getURL(netType));
		}

		switch (netType) {
		case PTNet:
			return (GraphicalPN<P, T, F, M, S>) PNMLPTNetParser.parse(pnmlDocument);
		case CPN: // TODO:
			break;
		case CWN: // TODO:
			break;
		case IFNet: // TODO:
			break;
		}

		return null;
	}

	/**
	 * TODO
	 * 
	 * @param pnmlFile
	 *            PNML file to read
	 * @return Readable, well-formed and normalized PNML document
	 */
	public static Document readPNMLFile(File pnmlFile) throws ParameterException, IOException, XMLParserException {
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
	 * TODO
	 * 
	 * @param pnmlFile
	 * @param pntdUrl
	 * @return
	 * @throws IOException
	 *             If the PNTD can't be found or is not readable
	 * @throws PNMLParserException 
	 */
	private static void verifySchema(File pnmlFile, URL pntdUrl) throws IOException, PNMLParserException {
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
