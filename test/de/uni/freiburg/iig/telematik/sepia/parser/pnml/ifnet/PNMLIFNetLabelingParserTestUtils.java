package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLPNParserTestUtils;

/**
 * @author Adrian Lange
 */
public class PNMLIFNetLabelingParserTestUtils {

	public static Document createLabeling(String labelingListTypeName, String labelingTypeName, String objectDescriptorName) {
		Document doc = AbstractPNMLPNParserTestUtils.createDocumentInstance();

		// create list element
		Element listElement = doc.createElement(labelingListTypeName);
		doc.appendChild(listElement);
		// add entries
		listElement.appendChild(createListEntry(doc, labelingTypeName, objectDescriptorName, "first_activity", "low"));
		listElement.appendChild(createListEntry(doc, labelingTypeName, objectDescriptorName, "second_activity", "low"));
		listElement.appendChild(createListEntry(doc, labelingTypeName, objectDescriptorName, "third_activity", "high"));
		listElement.appendChild(createListEntry(doc, labelingTypeName, objectDescriptorName, "fourth_activity", "low"));
		listElement.appendChild(createListEntry(doc, labelingTypeName, objectDescriptorName, "fifth_activity", "high"));

		return doc;
	}

	private static Element createListEntry(Document doc, String labelingTypeName, String objectDescriptorName, String objectDescriptorNameValue, String objectDescriptorSecurityDomain) {
		Element entryElement = doc.createElement(labelingTypeName);

		// Object descriptor
		Element objectDescriptorElement = doc.createElement(objectDescriptorName);
		objectDescriptorElement.setTextContent(objectDescriptorNameValue);
		entryElement.appendChild(objectDescriptorElement);

		// Object descriptor
		Element objectDescriptorSecurityDomainElement = doc.createElement("securitydomain");
		objectDescriptorSecurityDomainElement.setTextContent(objectDescriptorSecurityDomain);
		entryElement.appendChild(objectDescriptorSecurityDomainElement);

		return entryElement;
	}

	public static void main(String[] args) throws TransformerFactoryConfigurationError, TransformerException {
		System.out.println(AbstractPNMLPNParserTestUtils.toXML(createLabeling("classifications", "classification", "activity")));
	}
}
