package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.seram.context.Context;

/**
 * Analysis context of IF-Nets.<br>
 * Analysis contexts contain information about:
 * <ul>
 * <li>Activity classification: The security level of process activities (IF-Net transitions).</li>
 * <li>Attribute classification: The security level of process attributes (Colored tokens of the IF-Net).</li>
 * <li>Subject clearance: The clearance level of subjects executing process activities.</li>
 * <li>Subject descriptors: Subjects assigned to process activities.</li>
 * </ul>
 */
public class AnalysisContext {
	
	private String name = null;
	private Labeling labeling = null;
	private Map<String, String> subjectDescriptors = new HashMap<String, String>();
	
	public AnalysisContext(Labeling labeling) {
		Validate.notNull(labeling);
		this.labeling = labeling;
	} 
	
	public AnalysisContext(Context context) {
		this(new Labeling(context));
	}
	
	public AnalysisContext(Context context, SecurityLevel defaultSecurityLevel) {
		this(new Labeling(context, defaultSecurityLevel));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getActivities(){
		return labeling.getActivities();
	}
	
	public Set<String> getAttributes(){
		return labeling.getAttributes();
	}
	
	public Set<String> getSubjects(){
		return labeling.getSubjects();
	}
	
	public Labeling getLabeling(){
		return labeling;
	}
	
	public void setLabeling(Labeling labeling) {
		Validate.notNull(labeling);
		if(!labeling.getSubjects().containsAll(subjectDescriptors.values()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Labeling must contain all subjects assigned to transitions.");
		this.labeling = labeling;
	}
	  
	public void setSubjectDescriptor(String activity, String subject) {
		labeling.validateActivity(activity);
		labeling.validateSubject(subject);
		
		if(labeling.getSubjectClearance(subject) == SecurityLevel.LOW && labeling.getActivityClassification(activity) == SecurityLevel.HIGH)
			throw new ParameterException(ErrorCode.INCONSISTENCY, "Cannot assign a subject with LOW clearance to an activity with HIGH classification.");
		
		subjectDescriptors.put(activity, subject);
	}
	
	public String getSubjectDescriptor(String activity) {
		labeling.validateActivity(activity);
		return subjectDescriptors.get(activity);
	}
	
	public Map<String, String> getsubjectDescriptors(){
		return Collections.unmodifiableMap(subjectDescriptors);
	}
	
	public void setContext(Context context, boolean reset){
		labeling.setContext(context, reset);
	}
	
	public String getContextName(){
		return labeling.getContextName();
	}
	
	public Class<?> getContextClass(){
		return labeling.getContextClass();
	}
}
