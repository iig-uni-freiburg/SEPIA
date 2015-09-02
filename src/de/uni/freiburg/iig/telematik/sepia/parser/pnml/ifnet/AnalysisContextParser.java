package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.parser.PNParsing;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;

/**
 * <p>
 * Parser for analysis context files for {@link IFNet}s.
 * </p>
 *
 * @author Adrian Lange
 */
public class AnalysisContextParser {

    /**
     * URL to the RelaxNG schema of the analysis context
     */
    public final static String ANALYSIS_CONTEXT_SCHEMA = "http://ifnml.process-security.de/grammar/v1.0/analysiscontext.rng";

    /**
     * <p>
     * Parses a given analysis context file for IF-nets and returns the
     * {@link AnalysisContext}.
     * </p>
     *
     * @param analysisContextFile Analysis context file to parse
     * @param acModels Collection of access control models
     * @return Parsed analysis context
     * @throws ParserException
     * @throws IOException
     */
    public static AnalysisContext parse(File analysisContextFile, @SuppressWarnings("rawtypes") Collection<? extends AbstractACModel> acModels) throws ParserException, IOException {
        return parse(analysisContextFile, true, acModels);
    }

    /**
     * <p>
     * Parses a given analysis context file for IF-nets and returns the
     * {@link AnalysisContext}.
     * </p>
     *
     * @param analysisContextFilePath Analysis context file to parse
     * @param acModels Collection of access control models
     * @return Parsed analysis context
     * @throws ParserException
     * @throws IOException
     */
    public static AnalysisContext parse(String analysisContextFilePath, @SuppressWarnings("rawtypes") Collection<? extends AbstractACModel> acModels) throws ParserException, IOException {
        return parse(new File(analysisContextFilePath), acModels);
    }

    /**
     * <p>
     * Parses a given analysis context file for IF-nets and returns the
     * {@link AnalysisContext}.
     * </p>
     *
     * @param analysisContextFilePath Analysis context file to parse
     * @param validate Set <code>true</code> if analysis context should be
     * validated
     * @param acModels Collection of access control models
     * @return Parsed analysis context
     * @throws ParserException
     * @throws IOException
     */
    public static AnalysisContext parse(String analysisContextFilePath, boolean validate, @SuppressWarnings("rawtypes") Collection<? extends AbstractACModel> acModels) throws ParserException, IOException {
        return parse(new File(analysisContextFilePath), validate, acModels);
    }

    /**
     * <p>
     * Parses a given analysis context file for IF-nets and returns the
     * {@link AnalysisContext}.
     * </p>
     *
     * @param analysisContextFile Analysis context file to parse
     * @param validate Set <code>true</code> if analysis context should be
     * validated
     * @param acModels Collection of access control models
     * @return Parsed analysis context
     * @throws ParserException
     * @throws IOException
     */
    public static AnalysisContext parse(File analysisContextFile, boolean validate, @SuppressWarnings("rawtypes") Collection<? extends AbstractACModel> acModels) throws ParserException, IOException {
        Validate.notNull(analysisContextFile);

        if (validate) {
            PNMLParser.verifySchema(analysisContextFile, new URL(ANALYSIS_CONTEXT_SCHEMA));
        }

        if (!analysisContextFile.exists()) {
            throw new IOException("The given analysis context file doesn't exist.");
        }
        if (!analysisContextFile.canRead()) {
            throw new IOException("The given analysis context file exists but is not readable.");
        }

        Document contextDocument = null;
        try {
            contextDocument = PNMLParser.readRNGFile(analysisContextFile);
        } catch (IOException e) {
            throw new ParserException("I/O Exception: Cannot access labeling file.\nReason: " + e.getMessage());
        }

        if (contextDocument == null) {
            throw new ParserException("Cannot create XML document.");
        }

        // Read activity descriptors
        Map<String, String> subjectDescriptorMap = readSubjectDescriptors(contextDocument);

        // Check if correct access control model can be found
        @SuppressWarnings("rawtypes")
        AbstractACModel acModel = chooseACModel(contextDocument, acModels, subjectDescriptorMap);

        String name = contextDocument.getDocumentElement().getAttribute("id");
        if (name == null) {
            throw new ParserException("Cannot parse analysis context name.");
        }

        AnalysisContext analysisContext = new AnalysisContext(name, acModel, false);
        for (String activity : subjectDescriptorMap.keySet()) {
            analysisContext.setSubjectDescriptor(activity, subjectDescriptorMap.get(activity));
        }

        return analysisContext;
    }

    private static Map<String, String> readSubjectDescriptors(Document contextDocument) {
        Map<String, String> subjectDescriptorMap = new HashMap<>();
        NodeList subjectDescriptorsList = contextDocument.getElementsByTagName("subjectdescriptors");
        if (subjectDescriptorsList.getLength() > 0) {
            Element subjectDescriptorsElement = (Element) subjectDescriptorsList.item(0);
            NodeList subjectDescriptorList = subjectDescriptorsElement.getElementsByTagName("subjectdescriptor");
            for (int sd = 0; sd < subjectDescriptorList.getLength(); sd++) {
                if (subjectDescriptorList.item(sd).getNodeType() == Node.ELEMENT_NODE && subjectDescriptorList.item(sd).getParentNode().equals(subjectDescriptorsElement)) {
                    Element subjectDescriptorElement = (Element) subjectDescriptorList.item(sd);
                    // read activity
                    String activity = null;
                    NodeList activityList = subjectDescriptorElement.getElementsByTagName("activity");
                    if (activityList.getLength() > 0) {
                        activity = PNParsing.sanitizeElementName(((Element) activityList.item(0)).getTextContent(), "t");
                    }
                    // read subject
                    String subject = null;
                    NodeList subjectList = subjectDescriptorElement.getElementsByTagName("subject");
                    if (subjectList.getLength() > 0) {
                        subject = ((Element) subjectList.item(0)).getTextContent();
                    }

                    if (activity != null && subject != null) {
                        subjectDescriptorMap.put(activity, subject);
                    }
                }
            }
        }
        return subjectDescriptorMap;
    }

    @SuppressWarnings("rawtypes")
    private static AbstractACModel chooseACModel(Document contextDocument, Collection<? extends AbstractACModel> acModels, Map<String, String> subjectDescriptorMap) throws ParserException {
        NodeList acModelNodes = contextDocument.getElementsByTagName("acmodel");
        if (acModelNodes == null || acModelNodes.getLength() == 0) {
            throw new ParserException("Cannot parse access control model information.");
        }
        if (acModelNodes.getLength() > 1) {
            throw new ParserException("XML contains more than one access control model elements.");
        }
        Node acModelNode = acModelNodes.item(0);
        String acModelName = acModelNode.getTextContent();
        List<AbstractACModel> acModelCandidates = new ArrayList<>();
        for (AbstractACModel acModel : acModels) {
            if (acModel.getName().equals(acModelName)) {
                acModelCandidates.add(acModel);
            }
        }
        AbstractACModel acModel = null;
        for (AbstractACModel acModelCandidate : acModelCandidates) {
            if (!acModelCandidate.getContext().getActivities().containsAll(subjectDescriptorMap.keySet())) {
                continue;
            }
            if (!acModelCandidate.getContext().getSubjects().containsAll(subjectDescriptorMap.values())) {
                continue;
            }
            acModel = acModelCandidate;
            break;
        }
        if (acModel == null) {
            throw new ParserException("No matching access control model found.");
        }
        return acModel;
    }

}
