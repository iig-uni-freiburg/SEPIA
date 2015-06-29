package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.IOException;
import java.util.Arrays;

import org.w3c.dom.Element;

import de.invation.code.toval.misc.soabase.SOABase;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.LabelingParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;

public class LabelingSerializer {
	
	public static final String TYPE_URI = "http://ifnml.process-security.de/grammar/v1.0/labeling";
        public static final String LABELING_FILE_EXTENSION = "labeling";
	
	private Labeling labeling = null;
	private XMLSerializationSupport support = null;
	
	public LabelingSerializer(Labeling labeling){
		this.labeling = labeling;
		support = new XMLSerializationSupport("labeling");
	}

	private void addContent() {
		support.getRootElement().setAttribute("id", labeling.getName());
		support.getRootElement().setAttribute("type", TYPE_URI);
		
		// Add analysis context name
		Element analysisContextElement = support.createElement("analysiscontext");
		analysisContextElement.setTextContent(labeling.getAnalysisContext().getName());
		support.getRootElement().appendChild(analysisContextElement);
		
		// Add default security level
		Element defaultLevelElement = support.createElement("defaultlevel");
		defaultLevelElement.setTextContent(labeling.getDefaultSecurityLevel().toString().toLowerCase());
		support.getRootElement().appendChild(defaultLevelElement);

		// Add activity classifications
		Element classificationsElement = support.createElement("classifications");
		for (String activity : labeling.getAnalysisContext().getACModel().getContext().getActivities()) {
			Element classificationElement = support.createElement("classification");
			classificationElement.appendChild(support.createTextElement("activity", activity));
			classificationElement.appendChild(support.createTextElement("securitydomain", labeling.getActivityClassification(activity).toString().toLowerCase()));
			classificationsElement.appendChild(classificationElement);
		}
		support.getRootElement().appendChild(classificationsElement);

		// Add token classifications
		Element tokenLabelsElement = support.createElement("tokenlabels");
		for (String attribute : labeling.getAnalysisContext().getACModel().getContext().getObjects()) {
			Element tokenLabelElement = support.createElement("tokenlabel");
			tokenLabelElement.appendChild(support.createTextElement("color", attribute));
			tokenLabelElement.appendChild(support.createTextElement("securitydomain", labeling.getAttributeClassification(attribute).toString().toLowerCase()));
			tokenLabelsElement.appendChild(tokenLabelElement);
		}
		support.getRootElement().appendChild(tokenLabelsElement);

		// Add subject clearances
		Element clearancesElement = support.createElement("clearances");
		for (String subject : labeling.getAnalysisContext().getACModel().getContext().getSubjects()) {
			Element clearanceElement = support.createElement("clearance");
			clearanceElement.appendChild(support.createTextElement("subject", subject));
			clearanceElement.appendChild(support.createTextElement("securitydomain", labeling.getSubjectClearance(subject).toString().toLowerCase()));
			clearancesElement.appendChild(clearanceElement);
		}
		support.getRootElement().appendChild(clearancesElement);
	}
	
	public String serialize() throws SerializationException {
		addContent();
		return support.serialize();
	}
	
	public void serialize(String path, String fileName) throws SerializationException, IOException {
		addContent();
		support.serialize(path, fileName, getFileExtension());
	}
	
	protected String getFileExtension(){
		return LABELING_FILE_EXTENSION;
	}
	
	public static void main(String[] args) throws Exception {
		SOABase context = new SOABase("context1");
		context.setActivities(Arrays.asList("act1","act2"));
		context.setSubjects(Arrays.asList("Gerd"));
		ACLModel acModel = new ACLModel("acModel1", context);
		AnalysisContext analysisContext = new AnalysisContext("analysisContext1", acModel, true, SecurityLevel.LOW);
		
		Labeling labeling = new Labeling("labeling1", analysisContext);
		
		LabelingSerializer serializer = new LabelingSerializer(labeling);
		serializer.serialize("/Users/stocker/Desktop/", "test");
		
		Labeling parsedLabeling = LabelingParser.parse("/Users/stocker/Desktop/test.labeling", Arrays.asList(analysisContext));
		System.out.println(parsedLabeling);
//		AnalysisContext parsedLabeling = PNMLIFNetAnalysisContextParser.parse("/Users/stocker/Desktop/test.labeling", false);

	}
}
