package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

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

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPetriNet;
import de.uni.freiburg.iig.telematik.sepia.parser.ParserInterface;

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

	/** net type/PNTD reference table */
	private static HashMap<String, String> NettypePNTDsRefs = null;
	/** URI/net type reference table */
	private static HashMap<String, String> URINettypeRefs = null;

	/**
	 * Returns the net type/PNTD reference table.<br>
	 * Because of the lazy initialization of a static variable, the method must be synchronized.
	 */
	public synchronized static HashMap<String, String> getNettypePNTDsRefs() {
		if (NettypePNTDsRefs == null) {
			NettypePNTDsRefs = new HashMap<String, String>();

			NettypePNTDsRefs.put("ptnet", "http://www.pnml.org/version-2009/grammar/ptnet.pntd");
			NettypePNTDsRefs.put("cpnet", "http://ifnml.telematik.uni-freiburg.de/ifnml/grammar/v1.0/cpnet.pntd");
			NettypePNTDsRefs.put("cwnet", "http://ifnml.telematik.uni-freiburg.de/ifnml/grammar/v1.0/cwnet.pntd");
			NettypePNTDsRefs.put("ifnet", "http://ifnml.telematik.uni-freiburg.de/ifnml/grammar/v1.0/ifnet.pntd");
			NettypePNTDsRefs.put("yasperptnet", "http://www.yasper.org/specs/epnml-2.0/epnml20.pntd");
		}

		return NettypePNTDsRefs;
	}

	/**
	 * TODO
	 * 
	 * @param pntdURI
	 * @return
	 * @throws SAXException
	 *             If the net type can't be determined.
	 */
	public static String getPNMLType(String pntdURI) throws SAXException {
		String pnmlType = getURINettypeRefs().get(pntdURI);
		if (pnmlType == null) {
			throw new SAXException("Can't determine the net type.");
		} else {
			return pnmlType;
		}
	}

	/**
	 * TODO
	 * 
	 * @param pnmlDocument
	 * @return
	 * @throws SAXException
	 *             If the net element can't be found or the net element has no type-attribute
	 */
	public static String getPNMLTypeURI(Document pnmlDocument) throws SAXException {
		// Get all elements named net, which should result in only one element
		NodeList netElement = pnmlDocument.getElementsByTagName("net");
		if (netElement.getLength() != 1)
			throw new SAXException("The net element can't be found.");
		Node netTypeURINode = netElement.item(0).getAttributes().getNamedItem("type");
		if (netTypeURINode == null)
			throw new SAXException("The net element has no type attribute.");
		// Read and return type attribute
		String netTypeURI = netTypeURINode.getNodeValue();

		return netTypeURI;
	}

	/**
	 * Returns the URI/net type reference table.<br>
	 * Because of the lazy initialization of a static variable, the method must be synchronized.
	 */
	public synchronized static HashMap<String, String> getURINettypeRefs() {
		if (URINettypeRefs == null) {
			URINettypeRefs = new HashMap<String, String>();

			// P/T nets from the official specification
			URINettypeRefs.put("http://www.pnml.org/version-2009/grammar/ptnet", "ptnet");

			// IFNML net types
			URINettypeRefs.put("http://files.telematik.uni-freiburg.de/ifnml/grammar/v1.0/cpnet", "cpnet");
			URINettypeRefs.put("http://files.telematik.uni-freiburg.de/ifnml/grammar/v1.0/cwnet", "cwnet");
			URINettypeRefs.put("http://files.telematik.uni-freiburg.de/ifnml/grammar/v1.0/ifnet", "ifnet");

			// Yasper nets, interpreted as P/T nets
			URINettypeRefs.put("http://www.yasper.org/specs/epnml-1.1", "yasperptnet");
			URINettypeRefs.put("http://www.yasper.org/specs/epnml-2.0", "yasperptnet");
		}

		return URINettypeRefs;
	}

	/**
	 * TODO
	 * 
	 * @param pnmlFile
	 * @return
	 */
	public GraphicalPetriNet<?, ?, ?, ?, ?> parse(File pnmlFile) throws Exception {
		validatePNML(pnmlFile); // TODO

		Document pnmlDocument = readPNMLFile(pnmlFile);
		String pnmlType = getPNMLType(getPNMLTypeURI(pnmlDocument));

		GraphicalPetriNet<?, ?, ?, ?, ?> petriNet = null;

		if (pnmlType.equals("ptnet")) {
			petriNet = PNMLPTNetParser.parse(pnmlDocument);
		}

		return petriNet;
	}

	/**
	 * TODO
	 * 
	 * @param pnmlFile
	 *            PNML file to read
	 * @return Readable, well-formed and normalized PNML document
	 * @throws IOException
	 *             If the given PNML file doesn't exist or isn't readable
	 * @throws ParserConfigurationException
	 *             If the XML parser is not configured very well
	 * @throws SAXException
	 *             If the given XML file is not well-formed
	 */
	public static Document readPNMLFile(File pnmlFile) throws IOException, ParserConfigurationException, SAXException {
		if (pnmlFile == null) {
			throw new NullPointerException("The given PNML file is null.");
		} else {
			// Check if pnmlFile exists and is readable
			if (!pnmlFile.exists())
				throw new IOException("The given PNML file doesn't exist.");
			if (!pnmlFile.canRead())
				throw new IOException("The given PNML file exists but is not readable.");

			// Check if PNML file is well-formed and return the DOM document
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document pnmlDocument = dBuilder.parse(pnmlFile);
			pnmlDocument.getDocumentElement().normalize();

			return pnmlDocument;
		}
	}

	/**
	 * TODO
	 * 
	 * @param pnmlFile
	 * @return <code>true</code> if the pnml is valid, <code>false</code> otherwise
	 * @throws IOException
	 *             The PNML file doesn't exist or is not readable
	 * @throws ParserConfigurationException
	 *             The parser isn't configured very well
	 * @throws SAXException
	 *             Parsing error (not well-formed or no readable net type)
	 * @throws VerifierConfigurationException
	 *             The Relax NG verifier isn't configured very well
	 */
	public static boolean validatePNML(File pnmlFile) throws IOException, ParserConfigurationException, SAXException, VerifierConfigurationException {
		if (pnmlFile == null) {
			throw new NullPointerException("The given PNML file is null.");
		} else {
			// Read PNML document and check if it is well-formed
			Document pnmlDocument = readPNMLFile(pnmlFile);

			// Get type
			String pnmlTypeURI = getPNMLTypeURI(pnmlDocument);
			String pnmlType = getPNMLType(pnmlTypeURI);

			// Validate
			URL pntdUrl = new URL(getNettypePNTDsRefs().get(pnmlType));

			return verifyPNMLonPNTD(pnmlFile, pntdUrl);
		}
	}

	/**
	 * TODO
	 * 
	 * @param pnmlFile
	 * @param pntdUrl
	 * @return <code>true</code> if the PNML is valid, <code>false</code> otherwise
	 * @throws IOException
	 *             The PNML file doesn't exist or is not readable
	 * @throws ParserConfigurationException
	 *             The parser isn't configured very well
	 * @throws SAXException
	 *             Parsing error (not well-formed or no readable net type)
	 * @throws VerifierConfigurationException
	 *             The Relax NG verifier isn't configured very well
	 */
	public static boolean validatePNML(File pnmlFile, URL pntdUrl) throws IOException, ParserConfigurationException, SAXException, VerifierConfigurationException {
		if (pnmlFile == null) {
			throw new NullPointerException("The given PNML file is null.");
		} else if (pntdUrl == null) {
			throw new NullPointerException("The given PNTD URL is null.");
		} else {
			// Read PNML document and check if it is well-formed. Return value can be ignored.
			readPNMLFile(pnmlFile);

			// Validate
			return verifyPNMLonPNTD(pnmlFile, pntdUrl);
		}
	}

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
	 * @throws SAXException
	 *             If the PNML doesn't fit the PNTD
	 */
	private static boolean verifyPNMLonPNTD(File pnmlFile, URL pntdUrl) throws VerifierConfigurationException, IOException, SAXException {
		// Create verifier factory instance with PNML namespace
		VerifierFactory factory = VerifierFactory.newInstance(RNG_NAMESPACE);
		// Open stream to PNTD
		URLConnection connection = pntdUrl.openConnection();
		InputStream in = connection.getInputStream();
		// Create verifier with PNTD input stream and validate
		Verifier verifier = factory.newVerifier(in);

		return verifier.verify(pnmlFile);
	}
}
