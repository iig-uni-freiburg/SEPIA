package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	public static AnalysisContext parse(File analysisContextFile) throws ParserException, IOException {
		return parse(analysisContextFile, true);
	}

	/**
	 * <p>
	 * Parses a given analysis context file for IF-nets and returns the {@link AnalysisContext}.
	 * </p>
	 */
	public static AnalysisContext parse(String analysisContextFilePath) throws ParserException, IOException {
		return parse(new File(analysisContextFilePath));
	}

	/**
	 * <p>
	 * Parses a given analysis context file for IF-nets and returns the {@link AnalysisContext}.
	 * </p>
	 */
	public static AnalysisContext parse(File analysisContextFile, boolean validate) throws ParserException, IOException {
		Validate.notNull(analysisContextFile);

		List<String> activities = new ArrayList<String>();
		List<String> colors = new ArrayList<String>();
		List<String> subjects = new ArrayList<String>();

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
			labeling.setRequireContext(false);

			// read token labels
			Map<String, SecurityLevel> tokenLabels = readLabeling(analysisContextDocument, "tokenlabels", "tokenlabel", "color", colors);
			for (Entry<String, SecurityLevel> attributeClassification : tokenLabels.entrySet())
				labeling.setAttributeClassification(attributeClassification.getKey(), attributeClassification.getValue());
			if (colors.contains("black"))
				throw new ParserException("Control flow tokens aren't allowed to be listed within the token labels.");
			// read clearances
			Map<String, SecurityLevel> clearances = readLabeling(analysisContextDocument, "clearances", "clearance", "subject", subjects);
			for (Entry<String, SecurityLevel> clearance : clearances.entrySet())
				labeling.setSubjectClearance(clearance.getKey(), clearance.getValue());
			// read activity classifications
			Map<String, SecurityLevel> activityClassifications = readLabeling(analysisContextDocument, "classifications", "classification", "activity", activities);
			for (Entry<String, SecurityLevel> activityClassification : activityClassifications.entrySet())
				labeling.setActivityClassification(activityClassification.getKey(), activityClassification.getValue());

			AnalysisContext analysisContext = new AnalysisContext(labeling);
			String name = analysisContextDocument.getDocumentElement().getAttribute("id");
			analysisContext.setName(name);
			
			NodeList contextList = analysisContextDocument.getElementsByTagName("context");
			if(contextList == null || contextList.getLength() == 0)
				throw new ParserException("Cannot parse context information.");
			if(contextList.getLength() > 1)
				throw new ParserException("XML contains more than one context elements.");
			Element contextElement = (Element) contextList.item(0);
			Boolean requiresContext = new Boolean(contextElement.getAttribute("requirescontext"));
			if(requiresContext){
				Element contextNameElement = null;
				String contextName = null;
				try {
					contextNameElement = (Element) contextElement.getElementsByTagName("contextname").item(0);
					contextName = contextNameElement.getTextContent();
				} catch(Exception e){
					throw new ParserException("Cannot parse context name.\nReason: " + e.getMessage());
				}
				if(contextName == null || contextName.isEmpty())
					throw new ParserException("Cannot parse context name.");
				labeling.setContextName(contextName);
				
				Element contextClassElement = null;
				String contextClassName = null;
				try {
					contextClassElement = (Element) contextElement.getElementsByTagName("contexttype").item(0);
					contextClassName = contextClassElement.getTextContent();
				} catch(Exception e){
					throw new ParserException("Cannot parse context class.\nReason: " + e.getMessage());
				}
				if(contextClassName == null || contextClassName.isEmpty())
					throw new ParserException("Cannot parse context class.");
				
				Class<?> contextClass = null;
				try{
					contextClass = Class.forName(contextClassName);
				} catch(Exception e){
					throw new ParserException("Cannot load context class.");
				}
				labeling.setContextclass(contextClass);
			}
			
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

						if (activities.contains(activity) == false)
							throw new ParserException("The activity \""+activity+"\" used in the subject descriptors wasn't listed in the classifications.");
						if (subjects.contains(subject) == false)
							throw new ParserException("The subject \""+subject+"\" used in the subject descriptors wasn't listed in the clearances.");
						
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
	public static AnalysisContext parse(String analysisContextFilePath, boolean validate) throws ParserException, IOException {
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
	protected static Map<String, SecurityLevel> readLabeling(Document doc, String labelingListTypeName, String labelingTypeName, String objectDescriptorName, List<String> labelList) {
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
						else
							throw new ParameterException("\"" + securityLevelStr + "\" is not a valid security level. Only \"low\" and \"high\" are allowed.");
					}

					if (objectDescriptor != null && objectDescriptor.length() > 0 && securityLevel != null) {
						labeling.put(objectDescriptor, securityLevel);
						labelList.add(objectDescriptor);
					}
				}
			}
		}

		return labeling;
	}
}
