package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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
import de.uni.freiburg.iig.telematik.sepia.parser.PNParsing;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;

/**
 * <p>
 * Parser for labeling files ({@link IFNet} concept).
 * </p>
 *
 * @author Adrian Lange
 */
public class LabelingParser {

    /**
     * URL to the labeling RelaxNG schema
     */
    public final static String LABELING_SCHEMA = "http://ifnml.process-security.de/grammar/v1.0/labeling.rng";

    /**
     * <p>
     * Parses a given labeling file for IF-nets and returns the
     * {@link Labeling}.
     * </p>
     *
     * @param labelingFile Labeling file to parse
     * @param contexts Collection of {@link AnalysisContext} objects
     * @return Parsed labeling
     * @throws ParserException
     * @throws IOException
     */
    public static Labeling parse(File labelingFile, Collection<AnalysisContext> contexts) throws ParserException, IOException {
        return parse(labelingFile, true, contexts);
    }

    /**
     * <p>
     * Parses a given labeling file for IF-nets and returns the
     * {@link Labeling}.
     * </p>
     *
     * @param labelingFile Labeling file to parse
     * @param contexts Collection of {@link AnalysisContext} objects
     * @return Parsed labeling
     * @throws ParserException
     * @throws IOException
     */
    public static Labeling parse(String labelingFile, Collection<AnalysisContext> contexts) throws ParserException, IOException {
        return parse(new File(labelingFile), contexts);
    }

    /**
     * <p>
     * Parses a given labeling file for IF-nets and returns the
     * {@link Labeling}.
     * </p>
     *
     * @param labelingFile Labeling file to parse
     * @param validate Set <code>true</code> if labeling file should be
     * validated
     * @param contexts Collection of {@link AnalysisContext} objects
     * @return Parsed labeling
     * @throws de.invation.code.toval.parser.ParserException
     */
    public static Labeling parse(File labelingFile, boolean validate, Collection<AnalysisContext> contexts) throws ParserException {
        Validate.notNull(labelingFile);
        Validate.notNull(contexts);
        Validate.notEmpty(contexts);
        Validate.noNullElements(contexts);

        if (validate) {
            try {
                PNMLParser.verifySchema(labelingFile, new URL(LABELING_SCHEMA));
            } catch (IOException | PNMLParserException e) {
                throw new ParserException("Cannot verify XML schema.\nReason: " + e.getMessage());
            }
        }

        if (!labelingFile.exists()) {
            throw new ParserException("The given labeling file doesn't exist.");
        }
        if (!labelingFile.canRead()) {
            throw new ParserException("The given labeling file exists but is not readable.");
        }

        Document labelingDocument = null;
        try {
            labelingDocument = PNMLParser.readRNGFile(labelingFile);
        } catch (IOException e) {
            throw new ParserException("I/O Exception: Cannot access labeling file.\nReason: " + e.getMessage());
        }

        if (labelingDocument == null) {
            throw new ParserException("Cannot create XML document.");
        }

        // Read token labels
        List<String> colors = new ArrayList<>();
        Map<String, SecurityLevel> tokenLabels = readLabeling(labelingDocument, "tokenlabels", "tokenlabel", "color", colors);
        if (colors.contains(IFNet.CONTROL_FLOW_TOKEN_COLOR)) {
            throw new ParserException("Control flow tokens aren't allowed to be listed within the token labels.");
        }
        // Read clearances
        Map<String, SecurityLevel> clearances = readLabeling(labelingDocument, "clearances", "clearance", "subject", new ArrayList<>());
        // Read activity classifications
        Map<String, SecurityLevel> activityClassifications = readLabeling(labelingDocument, "classifications", "classification", "activity", new ArrayList<>());

        // Check if correct AnalysisContext can be found
        NodeList contextNodes = labelingDocument.getElementsByTagName("analysiscontext");
        if (contextNodes == null || contextNodes.getLength() == 0) {
            throw new ParserException("Cannot parse context information.");
        }
        if (contextNodes.getLength() > 1) {
            throw new ParserException("XML contains more than one context elements.");
        }
        Node contextNode = contextNodes.item(0);
        String contextName = contextNode.getTextContent();
        List<AnalysisContext> contextCandidates = new ArrayList<>();
        for (AnalysisContext base : contexts) {
            if (base.getName().equals(contextName)) {
                contextCandidates.add(base);
            }
        }
        AnalysisContext analysisContext = null;
        for (AnalysisContext context : contextCandidates) {
            if (!context.getACModel().getContext().getObjects().containsAll(tokenLabels.keySet())) {
                continue;
            }
            if (!context.getACModel().getContext().getSubjects().containsAll(clearances.keySet())) {
                continue;
            }
            if (!context.getACModel().getContext().getActivities().containsAll(activityClassifications.keySet())) {
                continue;
            }
            analysisContext = context;
            break;
        }

        if (analysisContext == null) {
            throw new ParserException("No matching analysis context found.");
        }

        NodeList defaultLevelNodes = labelingDocument.getElementsByTagName("defaultlevel");
        if (defaultLevelNodes == null || defaultLevelNodes.getLength() == 0) {
            throw new ParserException("Cannot parse default security level information.");
        }
        if (defaultLevelNodes.getLength() > 1) {
            throw new ParserException("XML contains more than one default security level elements.");
        }
        Node defaultLevelNode = defaultLevelNodes.item(0);
        SecurityLevel defaultSecurityLevel = null;
        try {
            defaultSecurityLevel = SecurityLevel.valueOf(defaultLevelNode.getTextContent().toUpperCase());
        } catch (Exception e) {
            throw new ParserException("Cannot parse default security level: invalid value.");
        }

        String name = labelingDocument.getDocumentElement().getAttribute("id");
        if (name == null) {
            throw new ParserException("Cannot parse labeling name.");
        }

        // Create new Labeling
        Labeling labeling = new Labeling(name, analysisContext, defaultSecurityLevel);

        // set token labels
        for (Entry<String, SecurityLevel> attributeClassification : tokenLabels.entrySet()) {
            labeling.setAttributeClassification(attributeClassification.getKey(), attributeClassification.getValue());
        }
        // set clearances
        for (Entry<String, SecurityLevel> clearance : clearances.entrySet()) {
            labeling.setSubjectClearance(clearance.getKey(), clearance.getValue());
        }
        // set activity classifications
        for (Entry<String, SecurityLevel> activityClassification : activityClassifications.entrySet()) {
            labeling.setActivityClassification(activityClassification.getKey(), activityClassification.getValue());
        }

        return labeling;
    }

    /**
     * <p>
     * Parses a given labeling file for IF-nets and returns the
     * {@link Labeling}.
     * </p>
     *
     * @param labelingFilePath Labeling file to parse
     * @param validate Set <code>true</code> if labeling file should be
     * validated
     * @param contexts Collection of {@link AnalysisContext} objects
     * @return Parsed labeling
     * @throws de.invation.code.toval.parser.ParserException
     * @throws java.io.IOException
     */
    public static Labeling parse(String labelingFilePath, boolean validate, Collection<AnalysisContext> contexts) throws ParserException, IOException {
        return parse(new File(labelingFilePath), validate, contexts);
    }

    /**
     * <p>
     * Reads a labeling list of the following structure and returns a
     * {@link Map} of the object descriptor and its {@link SecurityLevel}:
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
     *
     * @param doc Document containing the labeling
     * @param labelingListTypeName Type name of the labeling list
     * @param labelingTypeName Type name of the labeling
     * @param objectDescriptorName Name of the object descriptor
     * @param labelList List of labels
     * @return
     */
    protected static Map<String, SecurityLevel> readLabeling(Document doc, String labelingListTypeName, String labelingTypeName, String objectDescriptorName, List<String> labelList) {
        Validate.notNull(doc);
        Validate.notEmpty(labelingListTypeName);
        Validate.notEmpty(labelingTypeName);
        Validate.notEmpty(objectDescriptorName);

        Map<String, SecurityLevel> labeling = new HashMap<>();

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
                    if (objectDescriptorList.getLength() > 0) {
                        if (objectDescriptorName.equals("activity")) {
                            objectDescriptor = PNParsing.sanitizeElementName(((Element) objectDescriptorList.item(0)).getTextContent(), "t");
                        } else {
                            objectDescriptor = ((Element) objectDescriptorList.item(0)).getTextContent();
                        }
                    }

                    // read security level
                    NodeList securityLevelList = labelingTypeElement.getElementsByTagName("securitydomain");
                    if (securityLevelList.getLength() > 0) {
                        String securityLevelStr = ((Element) securityLevelList.item(0)).getTextContent();
                        switch (securityLevelStr) {
                            case "low":
                                securityLevel = SecurityLevel.LOW;
                                break;
                            case "high":
                                securityLevel = SecurityLevel.HIGH;
                                break;
                            default:
                                throw new ParameterException("\"" + securityLevelStr + "\" is not a valid security level. Only \"low\" and \"high\" are allowed.");
                        }
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
