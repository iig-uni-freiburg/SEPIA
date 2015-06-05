package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.w3c.dom.Element;

import de.invation.code.toval.misc.soabase.SOABase;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.AnalysisContextParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;

public class AnalysisContextSerializer {
	
	public static final String TYPE_URI = "http://ifnml.process-security.de/grammar/v1.0/analysiscontext";
	
	private AnalysisContext analysisContext = null;
	private XMLSerializationSupport support = null;
	
	public AnalysisContextSerializer(AnalysisContext analysisContext){
		this.analysisContext = analysisContext;
		support = new XMLSerializationSupport("analysiscontext");
	}
	
	private void addContent() {
		support.getRootElement().setAttribute("id", analysisContext.getName());
		support.getRootElement().setAttribute("type", TYPE_URI);
		
		// Add AC model name
		Element acModelElement = support.createElement("acmodel");
		acModelElement.setTextContent(analysisContext.getACModel().getName());
		support.getRootElement().appendChild(acModelElement);
		
		// Add subject descriptors
		Element subjectDescriptorsElement = support.createElement("subjectdescriptors");
		Map<String, String> descriptors = analysisContext.getSubjectDescriptors();
		for (String activity : descriptors.keySet()) {
			Element subjectDescriptorElement = support.createElement("subjectdescriptor");
			subjectDescriptorElement.appendChild(support.createTextElement("activity", activity));
			subjectDescriptorElement.appendChild(support.createTextElement("subject", descriptors.get(activity)));
			subjectDescriptorsElement.appendChild(subjectDescriptorElement);
		}
		support.getRootElement().appendChild(subjectDescriptorsElement);
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
		return "acon";
	}
	
	public static void main(String[] args) throws Exception {
		SOABase context = new SOABase("context1");
		context.setActivities(Arrays.asList("act1","act2"));
		context.setSubjects(Arrays.asList("Gerd"));
		ACLModel acl = new ACLModel("acl1", context);
		acl.addActivityPermission("Gerd", "act1");
		AnalysisContext c = new AnalysisContext("analysisContext1", acl, true);
		c.setSubjectDescriptor("act1", "Gerd");
		
		AnalysisContextSerializer serializer = new AnalysisContextSerializer(c);
		serializer.serialize("/Users/stocker/Desktop/", "test");
		
		AnalysisContext parsedContext = AnalysisContextParser.parse("/Users/stocker/Desktop/test.acon", true, Arrays.asList(acl));
		System.out.println(parsedContext);
	}
}
