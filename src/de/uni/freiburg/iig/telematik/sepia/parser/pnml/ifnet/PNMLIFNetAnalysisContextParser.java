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
 * Parser for analysis context files for {@link IFNet}s.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLIFNetAnalysisContextParser {

	/** URL to the RelaxNG schema of the analysis context */
	public final static String ANALYSIS_CONTEXT_SCHEMA = "http://ifnml.process-security.de/grammar/v1.0/analysiscontext.rng";

	/**
	 * <p>
	 * Parses a given analysis context file for IF-nets and returns the {@link AnalysisContext}.
	 * </p>
	 */
	public static AnalysisContext parse(File analysisContextFile) throws ParserException, ParameterException, IOException {
		return parse(analysisContextFile, true);
	}

	/**
	 * <p>
	 * Parses a given analysis context file for IF-nets and returns the {@link AnalysisContext}.
	 * </p>
	 */
	public static AnalysisContext parse(String analysisContextFilePath) throws ParserException, ParameterException, IOException {
		return parse(new File(analysisContextFilePath));
	}

	/**
	 * <p>
	 * Parses a given analysis context file for IF-nets and returns the {@link AnalysisContext}.
	 * </p>
	 */
	public static AnalysisContext parse(File analysisContextFile, boolean validate) throws ParserException, ParameterException, IOException {
		Validate.notNull(analysisContextFile);

		if (validate)
			PNMLParser.verifySchema(analysisContextFile, new URL(ANALYSIS_CONTEXT_SCHEMA));

		if (!analysisContextFile.exists())
			throw new IOException("The given analysis context file doesn't exist.");
		if (!analysisContextFile.canRead())
			throw new IOException("The given analysis context file exists but is not readable.");

		Document analysisContextDocument = null;
		try {
			analysisContextDocument = PNMLParser.readRNGFile(analysisContextFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (analysisContextDocument != null) {
			Labeling labeling = new Labeling();

			// read token labels
			Map<String, SecurityLevel> tokenLabels = readLabeling(analysisContextDocument, "tokenlabels", "tokenlabel", "color");
			labeling.addAttributes(tokenLabels.keySet());
			for (Entry<String, SecurityLevel> attributeClassification : tokenLabels.entrySet())
				labeling.setAttributeClassification(attributeClassification.getKey(), attributeClassification.getValue());
			// read clearances
			Map<String, SecurityLevel> clearances = readLabeling(analysisContextDocument, "clearances", "clearance", "subject");
			labeling.addSubjects(clearances.keySet());
			for (Entry<String, SecurityLevel> clearance : clearances.entrySet())
				labeling.setSubjectClearance(clearance.getKey(), clearance.getValue());
			// read activity classifications
			Map<String, SecurityLevel> activityClassifications = readLabeling(analysisContextDocument, "classifications", "classification", "activity");
			labeling.addActivities(activityClassifications.keySet());
			for (Entry<String, SecurityLevel> activityClassification : activityClassifications.entrySet())
				labeling.setActivityClassification(activityClassification.getKey(), activityClassification.getValue());

			AnalysisContext analysisContext = new AnalysisContext(labeling);

			// read activity descriptors
			NodeList subjectDescriptorsList = analysisContextDocument.getElementsByTagName("subjectdescriptors");
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
		return null;
	}

	/**
	 * <p>
	 * Parses a given analysis context file for IF-nets and returns the {@link AnalysisContext}.
	 * </p>
	 */
	public static AnalysisContext parse(String analysisContextFilePath, boolean validate) throws ParserException, ParameterException, IOException {
		return parse(new File(analysisContextFilePath), validate);
	}

	/**
	 * <p>
	 * Reads a labeling list of the following structure and returns a {@link Map} of the object descriptor and its {@link SecurityLevel}:
	 * </p>
	 * 
	 * <pre>
	 * &lt;labelingListTypeName&gt;
	 *   &lt;labelingTypeName&gt;
	 *     &lt;objectDescriptorName&gt;objectDescriptor1&lt;objectDescriptorName&gt;
	 *     &lt;securitydomain&gt;securityLevel1&lt;securitydomain&gt;
	 *   &lt;/labelingTypeName&gt;
	 *   &lt;labelingTypeName&gt;
	 *     &lt;objectDescriptorName&gt;objectDescriptor2&lt;objectDescriptorName&gt;
	 *     &lt;securitydomain&gt;securityLevel2&lt;securitydomain&gt;
	 *   &lt;/labelingTypeName&gt;
	 * &lt;/labelingListTypeName&gt;
	 * </pre>
	 */
	protected static Map<String, SecurityLevel> readLabeling(Document doc, String labelingListTypeName, String labelingTypeName, String objectDescriptorName) throws ParameterException {
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
					NodeList securityLevelList = labelingTypeElement.getElementsByTagName("securitydomain");
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
