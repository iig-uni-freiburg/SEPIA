package petrinet.snet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import misc.SetUtils;
import validate.ParameterException;
import validate.ParameterException.ErrorCode;
import validate.Validate;

public class AnalysisContext {
	
	private Labeling labeling = new Labeling();
	private Map<String, String> subjectDescriptors = new HashMap<String, String>();
	
	public AnalysisContext(){}
	
	public AnalysisContext(Labeling labeling) throws ParameterException{
		Validate.notNull(labeling);
		this.labeling = labeling;
	} 
	
	public AnalysisContext(SNet sNet, Collection<String> subjects) throws ParameterException{
		this(new Labeling(sNet, subjects));
	}
	
	public AnalysisContext(SNet sNet, Collection<String> subjects, SecurityLevel defaultSecurityLevel) throws ParameterException{
		this(new Labeling(sNet, subjects, defaultSecurityLevel));
	}
	
	public AnalysisContext(Collection<String> activities,
						   Collection<String> attributes,
						   Collection<String> subjects, 
						   SecurityLevel defaultSecurityLevel) throws ParameterException{
		this(new Labeling(activities, attributes, subjects, defaultSecurityLevel));
	}
	
	public AnalysisContext(Collection<String> activities,
			   Collection<String> attributes,
			   Collection<String> subjects) throws ParameterException{
		this(new Labeling(activities, attributes, subjects));
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
	
	@SuppressWarnings("unchecked")
	public void setLabeling(Labeling labeling) throws ParameterException{
		Validate.notNull(labeling);
		if(!labeling.getSubjects().containsAll(subjectDescriptors.values()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Labeling must contain all subjects assigned to transitions.");
		this.labeling = labeling;
	}
	  
	public void setSubjectDescriptor(String activity, String subject) throws ParameterException{
		labeling.validateActivity(activity);
		labeling.validateSubject(subject);
		
		if(labeling.getSubjectClearance(subject) == SecurityLevel.LOW && labeling.getActivityClassification(activity) == SecurityLevel.HIGH)
			throw new ParameterException(ErrorCode.INCONSISTENCY, "Cannot assign a subject with LOW clearance to an activity with HIGH classification.");
		
		subjectDescriptors.put(activity, subject);
	}
	
	public String getSubjectDescriptor(String activity) throws ParameterException{
		labeling.validateActivity(activity);
		return subjectDescriptors.get(activity);
	}

}
