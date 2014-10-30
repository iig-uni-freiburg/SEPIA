package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.w3c.dom.Element;

import de.uni.freiburg.iig.telematik.jawl.context.Context;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;

public class AnalysisContextSerializer {
	
	private AnalysisContext analysisContext = null;
	private Labeling labeling = null;
	private XMLSerializationSupport support = null;
	
	public AnalysisContextSerializer(AnalysisContext analysisContext){
		this.analysisContext = analysisContext;
		this.labeling = analysisContext.getLabeling();
		support = new XMLSerializationSupport("analysis-context");
	}
	
	private void addContent() {
		support.getRootElement().setAttribute("id", analysisContext.getName());
//		// Add acmodel name
//		Element acModelElement = support.createElement("acmodel");
//		acModelElement.setAttribute("name", analysisContext.getACModelName());
//		support.getRootElement().appendChild(acModelElement);
		
		// Add subject descriptors
		Element subjectDescriptorsElement = support.createElement("subjectdescriptors");
		Map<String, String> descriptors = analysisContext.getsubjectDescriptors();
		for (String activity : descriptors.keySet()) {
			Element subjectDescriptorElement = support.createElement("subjectdescriptor");
			subjectDescriptorElement.appendChild(support.createTextElement("activity", activity));
			subjectDescriptorElement.appendChild(support.createTextElement("subject", descriptors.get(activity)));
			subjectDescriptorsElement.appendChild(subjectDescriptorElement);
		}
		support.getRootElement().appendChild(subjectDescriptorsElement);
		
		if (labeling != null) {
			// Add context name
			Element contextElement = support.createElement("context");
			contextElement.setAttribute("requires-context", labeling.requiresContext() ? "true" : "false");
			if(labeling.requiresContext()){
				Element contextNameElement = support.createElement("context-name");	
				contextNameElement.setTextContent(labeling.getContext().getName());
				contextElement.appendChild(contextNameElement);
				Element contextTypeElement = support.createElement("context-type");	
				contextTypeElement.setTextContent(labeling.getContext().getClass().toString());
				contextElement.appendChild(contextTypeElement);
			}
			support.getRootElement().appendChild(contextElement);
			
			// Add activity classifications
			Element classificationsElement = support.createElement("classifications");
			for (String activity : labeling.getActivities()) {
				Element classificationElement = support.createElement("classification");
				classificationElement.appendChild(support.createTextElement("activity", activity));
				classificationElement.appendChild(support.createTextElement("securitydomain", labeling.getActivityClassification(activity).toString().toLowerCase()));
				classificationsElement.appendChild(classificationElement);
			}
			support.getRootElement().appendChild(classificationsElement);
			
			// Add token classifications
			Element tokenLabelsElement = support.createElement("tokenlabels");
			for(String attribute: labeling.getAttributes()){
				Element tokenLabelElement = support.createElement("tokenlabel");
				tokenLabelElement.appendChild(support.createTextElement("color", attribute));
				tokenLabelElement.appendChild(support.createTextElement("securitydomain", labeling.getAttributeClassification(attribute).toString().toLowerCase()));
				tokenLabelsElement.appendChild(tokenLabelElement);
			}
			support.getRootElement().appendChild(tokenLabelsElement);
			
			// Add subject clearances
			Element clearancesElement = support.createElement("clearances");
			for(String subject: labeling.getSubjects()){
				Element clearanceElement = support.createElement("clearance");
				clearanceElement.appendChild(support.createTextElement("subject", subject));
				clearanceElement.appendChild(support.createTextElement("securitydomain", labeling.getSubjectClearance(subject).toString().toLowerCase()));
				clearancesElement.appendChild(clearanceElement);
			}
			support.getRootElement().appendChild(clearancesElement);
		}
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
		return "xml";
	}
	
	public static void main(String[] args) throws Exception {
		Context context = new Context("context1");
		context.setActivities(Arrays.asList("act1","act2"));
		context.setSubjects(Arrays.asList("Gerd"));
		AnalysisContext c = new AnalysisContext(context);
		c.setName("AnalysisContext1");
		c.setSubjectDescriptor("act1", "Gerd");
		AnalysisContextSerializer serializer = new AnalysisContextSerializer(c);
		serializer.serialize("/Users/stocker/Desktop/", "test");
	}
}
