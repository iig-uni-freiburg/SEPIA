package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public abstract class PNSerializer_XML<P extends AbstractPlace<F,S>, 
	   								   T extends AbstractTransition<F,S>, 
	   								   F extends AbstractFlowRelation<P,T,S>, 
	   								   M extends AbstractMarking<S>, 
	   								   S extends Object>  extends PNSerializer<P, T, F, M, S> {
	
	private Document document = null;
	protected Element rootElement = null;
	
	
	public PNSerializer_XML(AbstractGraphicalPN<P, T, F, M, S> petriNet) throws ParameterException {
		super(petriNet);
		createDocumentInstance();
	}

	public PNSerializer_XML(AbstractPetriNet<P, T, F, M, S> petriNet) throws ParameterException {
		super(petriNet);
		createDocumentInstance();
	}

	/**
	 * Returns a new {@link Document} instance from a {@link DocumentBuilderFactory}.
	 */
	protected void createDocumentInstance() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		document = db.newDocument();
		rootElement = createRootElement();
		document.appendChild(rootElement);
	}
	
	protected abstract Element createRootElement();

	@Override
	public String serialize() throws SerializationException {
		addContent();
		return document.getTextContent();
	}
	
	protected Element createTextElement(String elementName, String elementText) {
		Element textElement = document.createElement(elementName);
		textElement.setTextContent(elementText);
		return textElement;
	}
	
	protected Element createElement(String elementName){
		return document.createElement(elementName);
	}
	
	protected abstract void addContent() throws SerializationException;
	
	@Override
	public void serialize(String path, String fileName) throws ParameterException, SerializationException, IOException {
		validatePathAndFileName(path, fileName);
		
		try {
			File fileOutput = new File(path+fileName+"."+getFileExtension());
			StreamResult streamResult = new StreamResult(fileOutput);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			serialize();
			serializer.transform(new DOMSource(document), streamResult);
		} catch (TransformerConfigurationException e) {
			throw new SerializationException(e.getMessage());
		} catch (TransformerException e) {
			throw new SerializationException(e.getMessage());
		}
	}
	
	protected abstract String getFileExtension();
	
}
