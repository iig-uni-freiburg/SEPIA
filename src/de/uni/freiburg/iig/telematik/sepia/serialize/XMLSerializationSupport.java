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

public class XMLSerializationSupport implements SerializationSupport{
	
	protected Document document = null;
	protected Element rootElement = null;
	
	protected XMLSerializationSupport(){}
	
	public XMLSerializationSupport(String rootElementName){
		createDocumentInstance(rootElementName);
	}
	
	protected void createDocumentInstance(String rootElementName) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		document = db.newDocument();
		rootElement = createElement(rootElementName);
		document.appendChild(rootElement);
	}
	
	public Element createElement(String elementName){
		return document.createElement(elementName);
	}
	
	public Element getRootElement(){
		return rootElement;
	}
	
	public String serialize() throws SerializationException {
		return document.getTextContent();
	}
	
	public void serialize(String path, String fileName, String extension) throws ParameterException, SerializationException, IOException {
		try {
			File fileOutput = new File(path+fileName+"."+extension);
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
	
	public Element createTextElement(String elementName, String elementText) {
		Element textElement = document.createElement(elementName);
		textElement.setTextContent(elementText);
		return textElement;
	}

}
