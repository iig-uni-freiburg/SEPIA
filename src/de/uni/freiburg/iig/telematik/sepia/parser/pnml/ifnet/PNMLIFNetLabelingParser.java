package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;

/**
 * <p>
 * Parser for labeling files for {@link IFNet}s.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLIFNetLabelingParser {

	/** URL to the RelaxNG schema of the labeling */
	public final static String LABELING_SCHEMA = "http://ifnml.telematik.uni-freiburg.de/ifnml/grammar/v1.0/labeling.rng";

	/**
	 * <p>
	 * Parses a given labeling file for IF-nets and returns the {@link Labeling}.
	 * </p>
	 */
	public static AnalysisContext parse(File labelingFile) throws ParserException, ParameterException, IOException {
		return parse(labelingFile, true);
	}

	/**
	 * <p>
	 * Parses a given labeling file for IF-nets and returns the {@link Labeling}.
	 * </p>
	 */
	public static AnalysisContext parse(String labelingFilePath) throws ParserException, ParameterException, IOException {
		return parse(new File(labelingFilePath));
	}

	/**
	 * <p>
	 * Parses a given labeling file for IF-nets and returns the {@link Labeling}.
	 * </p>
	 */
	public static AnalysisContext parse(File labelingFile, boolean validate) throws ParserException, ParameterException, IOException {
		Validate.notNull(labelingFile);

		if (validate)
			PNMLParser.verifySchema(labelingFile, new URL(LABELING_SCHEMA));

		if (!labelingFile.exists())
			throw new IOException("The given PNML file doesn't exist.");
		if (!labelingFile.canRead())
			throw new IOException("The given PNML file exists but is not readable.");

		Document labelingDocument = null;
		try {
			labelingDocument = PNMLParser.readRNGFile(labelingFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Labeling labeling = new Labeling();

		// read token labels
		Map<String, SecurityLevel> tokenLabels = readLabeling(labelingDocument, "tokenlabels", "tokenlabel", "color");
		labeling.addAttributes(tokenLabels.keySet());
		for (Entry<String, SecurityLevel> attributeClassification : tokenLabels.entrySet())
			labeling.setAttributeClassification(attributeClassification.getKey(), attributeClassification.getValue());
		// read clearances
		Map<String, SecurityLevel> clearances = readLabeling(labelingDocument, "clearances", "clearance", "subject");
		labeling.addSubjects(clearances.keySet());
		for (Entry<String, SecurityLevel> clearance : clearances.entrySet())
			labeling.setSubjectClearance(clearance.getKey(), clearance.getValue());
		// read activity classifications
		Map<String, SecurityLevel> activityClassifications = readLabeling(labelingDocument, "classifications", "activityClassification", "activity");
		labeling.addActivities(activityClassifications.keySet());
		for (Entry<String, SecurityLevel> activityClassification : activityClassifications.entrySet())
			labeling.setActivityClassification(activityClassification.getKey(), activityClassification.getValue());

		AnalysisContext analysisContext = new AnalysisContext(labeling);

		// read activity descriptors
		NodeList subjectDescriptorsList = labelingDocument.getElementsByTagName("subjectdescriptors");
		if (subjectDescriptorsList.getLength() > 0) {
			Element subjectDescriptorsElement = (Element) subjectDescriptorsList.item(0);
			NodeList subjectDescriptorList = subjectDescriptorsElement.getElementsByTagName("subjectdescriptor");
			for (int sd = 0; sd < subjectDescriptorList.getLength(); sd++) {
				if (subjectDescriptorList.item(sd).getNodeType() == Node.ELEMENT_NODE && subjectDescriptorList.item(sd).getParentNode().equals(subjectDescriptorsElement)) {
					Element subjectDescriptorElement = (Element) subjectDescriptorList.item(sd);
					// read activity
					String activity = null;
					NodeList activityList = subjectDescriptorElement.getElementsByTagName("activity");
					if (activityList.getLength() > 0)
						activity = ((Element) activityList.item(0)).getTextContent();
					// read subject
					String subject = null;
					NodeList subjectList = subjectDescriptorElement.getElementsByTagName("subject");
					if (subjectList.getLength() > 0)
						subject = ((Element) subjectList.item(0)).getTextContent();

					if (activity != null && subject != null)
						analysisContext.setSubjectDescriptor(activity, subject);
				}
			}
		}

		return analysisContext;
	}

	/**
	 * <p>
	 * Parses a given labeling file for IF-nets and returns the {@link Labeling}.
	 * </p>
	 */
	public static AnalysisContext parse(String labelingFilePath, boolean validate) throws ParserException, ParameterException, IOException {
		return parse(new File(labelingFilePath), validate);
	}

	private static Map<String, SecurityLevel> readLabeling(Document doc, String labelingListTypeName, String labelingTypeName, String objectDescriptorName) throws ParameterException {
		Validate.notNull(doc);
		Validate.notEmpty(labelingListTypeName);
		Validate.notEmpty(labelingTypeName);
		Validate.notEmpty(objectDescriptorName);

		Map<String, SecurityLevel> labeling = new HashMap<String, SecurityLevel>();

		NodeList labelingListTypeList = doc.getElementsByTagName(labelingListTypeName);
		if (labelingListTypeList.getLength() > 0) {
			Element labelingListTypeElement = (Element) labelingListTypeList.item(0);
			NodeList labelingTypeList = labelingListTypeElement.getElementsByTagName(labelingTypeName);
			for (int l = 0; l < labelingTypeList.getLength(); l++) {
				if (labelingTypeList.item(l).getNodeType() == Node.ELEMENT_NODE && labelingTypeList.item(l).getParentNode().equals(labelingListTypeElement)) {
					Element labelingTypeElement = (Element) labelingTypeList.item(l);
					String objectDescriptor = null;
					SecurityLevel securityLevel = null;

					// read object descriptor
					NodeList objectDescriptorList = labelingTypeElement.getElementsByTagName(objectDescriptorName);
					if (objectDescriptorList.getLength() > 0)
						objectDescriptor = ((Element) objectDescriptorList.item(0)).getTextContent();

					// read security level
					NodeList securityLevelList = labelingTypeElement.getElementsByTagName("classification");
					if (securityLevelList.getLength() > 0) {
						String securityLevelStr = ((Element) securityLevelList.item(0)).getTextContent();
						if (securityLevelStr.equals("low"))
							securityLevel = SecurityLevel.LOW;
						else if (securityLevelStr.equals("high"))
							securityLevel = SecurityLevel.HIGH;
					}

					if (objectDescriptor != null && objectDescriptor.length() > 0 && securityLevel != null)
						labeling.put(objectDescriptor, securityLevel);
				}
			}
		}

		return labeling;
	}
}
