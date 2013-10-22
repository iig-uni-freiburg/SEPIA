package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.IOException;

import org.w3c.dom.Element;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;

public class ACSerializer {
	
	private AnalysisContext analysisContext = null;
	private Labeling labeling = null;
	private XMLSerializationSupport support = null;
	
	public ACSerializer(AnalysisContext analysisContext){
		this.analysisContext = analysisContext;
		this.labeling = analysisContext.getLabeling();
		support = new XMLSerializationSupport("labeling");
	}
	
	private void addContent() throws ParameterException{
		// Add activity classifications
		Element classificationsElement = support.createElement("classifications");
		for(String activity: labeling.getActivities()){
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
		
		// Add subject descriptors
		Element subjectDescriptorsElement = support.createElement("subjectdescriptors");
		for(String activity: labeling.getActivities()){
			Element subjectDescriptorElement = support.createElement("subjectdescriptor");
			subjectDescriptorElement.appendChild(support.createTextElement("activity", activity));
			subjectDescriptorElement.appendChild(support.createTextElement("subject", analysisContext.getSubjectDescriptor(activity)));
			subjectDescriptorsElement.appendChild(subjectDescriptorElement);
		}
		support.getRootElement().appendChild(subjectDescriptorsElement);
	}
	
	public String serialize() throws SerializationException, ParameterException{
		addContent();
		return support.serialize();
	}
	
	public void serialize(String path, String fileName) throws ParameterException, SerializationException, IOException {
		support.serialize(path, fileName, getFileExtension());
	}
	
	protected String getFileExtension(){
		return "xml";
	}

}
