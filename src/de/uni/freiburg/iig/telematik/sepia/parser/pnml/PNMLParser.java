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
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.parser.ParserInterface;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;

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
	 * Returns the net type name by its URI.
	 * 
	 * @param pntdURI
	 * @return
	 * @throws SAXException If the net type can't be determined.
	 * @throws ParameterException 
	 * @throws PNMLParserException 
	 */
	private NetType getPNMLType(String pntdURI) throws ParameterException, PNMLParserException {
		Validate.notNull(pntdURI);
		
		return NetType.getNetType(pntdURI);
	}

	/**
	 * TODO
	 * 
	 * @param pnmlDocument
	 * @return
	 * @throws SAXException
	 *             If the net element can't be found or the net element has no type-attribute
	 * @throws PNMLParserException 
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
	
	public <P extends AbstractPlace<F,S>, 
			T extends AbstractTransition<F,S>, 
			F extends AbstractFlowRelation<P,T,S>, 
			M extends AbstractMarking<S>, 
			S extends Object> 

			GraphicalPN<P, T, F, M, S> 

		parse(String pnmlFile) throws IOException, ParserException, ParameterException {

		return parse(pnmlFile, true, true);
	}
	
	public <P extends AbstractPlace<F,S>, 
			T extends AbstractTransition<F,S>, 
			F extends AbstractFlowRelation<P,T,S>, 
			M extends AbstractMarking<S>, 
			S extends Object> 

			GraphicalPN<P, T, F, M, S> 

		parse(String pnmlFile, boolean requireNetType, boolean verifySchema) throws IOException, ParserException, ParameterException {
		
		File inputFile = new File(pnmlFile);
		if(inputFile.isDirectory())
			throw new IOException("I/O Error on opening file: File is a directory!");
		if(!inputFile.exists())
			throw new IOException("I/O Error on opening file: File does not exist!");
		if(!inputFile.canRead())
			throw new IOException("I/O Error on opening file: Unable to read file!");
		
		return parse(inputFile, requireNetType, verifySchema);
	}
	
	public <P extends AbstractPlace<F,S>, 
			T extends AbstractTransition<F,S>, 
			F extends AbstractFlowRelation<P,T,S>, 
			M extends AbstractMarking<S>, 
			S extends Object> 

			GraphicalPN<P, T, F, M, S> 

		parse(File pnmlFile) throws IOException, ParserException, ParameterException {
		return parse(pnmlFile, true, true);
	}

	/**
	 * TODO
	 * 
	 * @param pnmlFile
	 * @return
	 * @throws ParameterException 
	 */
	@SuppressWarnings("unchecked")
	private <P extends AbstractPlace<F,S>, 
			T extends AbstractTransition<F,S>, 
			F extends AbstractFlowRelation<P,T,S>, 
			M extends AbstractMarking<S>, 
			S extends Object> 
	
			GraphicalPN<P, T, F, M, S> 
	
			parse(File pnmlFile, boolean requireNetType, boolean verifySchema) throws IOException, ParserException, ParameterException {
		
		Validate.notNull(pnmlFile);
		
		Document pnmlDocument = readPNMLFile(pnmlFile);
		
		//Try to find out the net type
		String netTypeStringURI = getPNMLTypeURI(pnmlDocument);
		NetType netType = getPNMLType(netTypeStringURI);
		if(requireNetType && netType == null)
			throw new PNMLParserException(ErrorCode.INVALID_NET_TYPE);
		if(netType == null){
			netType = NetType.PTNet;
		}
		
		if(verifySchema){
			verifySchema(pnmlFile, NetType.getURL(netType));
		}
		
		switch(netType){
		case PTNet: return (GraphicalPN<P, T, F, M, S>) PNMLPTNetParser.parse(pnmlDocument);
		case CPN: //TODO:
			break;
		case CWN: //TODO:
			break;
		case IFNet: //TODO:
			break;
		}
		
		throw new ParserException("Couldn't determine a suitable PNML parser.");
	}

	/**
	 * TODO
	 * 
	 * @param pnmlFile
	 *            PNML file to read
	 * @return Readable, well-formed and normalized PNML document
	 * @throws ParameterException 
	 * @throws IOException
	 *             If the given PNML file doesn't exist or isn't readable
	 * @throws XMLParserException 
	 * @throws ParserConfigurationException
	 *             If the XML parser is not configured very well
	 * @throws SAXException
	 *             If the given XML file is not well-formed
	 */
	private Document readPNMLFile(File pnmlFile) throws ParameterException, IOException, XMLParserException {
		Validate.notNull(pnmlFile);

		// Check if pnmlFile exists and is readable
		if (!pnmlFile.exists())
			throw new IOException("The given PNML file doesn't exist.");
		if (!pnmlFile.canRead())
			throw new IOException("The given PNML file exists but is not readable.");
		
		Document pnmlDocument = null;
		try{
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

//	/**
//	 * TODO
//	 * 
//	 * @param pnmlFile
//	 * @return <code>true</code> if the pnml is valid, <code>false</code> otherwise
//	 * @throws IOException
//	 *             The PNML file doesn't exist or is not readable
//	 * @throws ParserConfigurationException
//	 *             The parser isn't configured very well
//	 * @throws SAXException
//	 *             Parsing error (not well-formed or no readable net type)
//	 * @throws VerifierConfigurationException
//	 *             The Relax NG verifier isn't configured very well
//	 */
//	public static boolean validatePNML(File pnmlFile) throws IOException, ParserConfigurationException, SAXException, VerifierConfigurationException {
//		
//		if (pnmlFile == null) {
//			throw new NullPointerException("The given PNML file is null.");
//		} else {
//			// Read PNML document and check if it is well-formed
//			Document pnmlDocument = readPNMLFile(pnmlFile);
//
//			// Get type
//			String pnmlTypeURI = getPNMLTypeURI(pnmlDocument);
//			String pnmlType = getPNMLType(pnmlTypeURI);
//
//			// Validate
//			URL pntdUrl = new URL(getNettypePNTDsRefs().get(pnmlType));
//
//			return verifySchema(pnmlFile, pntdUrl);
//		}
//	}
	


//	/**
//	 * TODO
//	 * 
//	 * @param pnmlFile
//	 * @param pntdUrl
//	 * @return <code>true</code> if the PNML is valid, <code>false</code> otherwise
//	 * @throws IOException
//	 *             The PNML file doesn't exist or is not readable
//	 * @throws ParserConfigurationException
//	 *             The parser isn't configured very well
//	 * @throws SAXException
//	 *             Parsing error (not well-formed or no readable net type)
//	 * @throws VerifierConfigurationException
//	 *             The Relax NG verifier isn't configured very well
//	 */
//	public static boolean validatePNML(File pnmlFile, URL pntdUrl) throws IOException, ParserConfigurationException, SAXException, VerifierConfigurationException {
//		if (pnmlFile == null) {
//			throw new NullPointerException("The given PNML file is null.");
//		} else if (pntdUrl == null) {
//			throw new NullPointerException("The given PNTD URL is null.");
//		} else {
//			// Read PNML document and check if it is well-formed. Return value can be ignored.
//			readPNMLFile(pnmlFile);
//
//			// Validate
//			return verifyPNMLonPNTD(pnmlFile, pntdUrl);
//		}
//	}

	/**
	 * TODO
	 * 
	 * @param pnmlFile
	 * @param pntdUrl
	 * @return
	 * @throws VerifierConfigurationException
	 *             If the Relax NG verifier is not configured very well
	 * @throws IOException
	 *             If the PNTD can't be found or is not readable
	 * @throws PNMLParserException 
	 * @throws SAXException
	 *             If the PNML doesn't fit the PNTD
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
