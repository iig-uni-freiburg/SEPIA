package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;



public class Labeling {
	
	/**
	 * Default security level used for initializing classification-, clearance- and labeling-maps.<br>
	 */
	public static final SecurityLevel DEFAULT_SECURITY_LEVEL = SecurityLevel.LOW;

	/**
	 * Default security level used for initializing classification-, clearance- and labeling-maps which
	 * can be changed for each instance.<br>
	 */
	private SecurityLevel defaultSecurityLevel = DEFAULT_SECURITY_LEVEL;
	
	
	/**
	 * This map contains the classification of process activities (Petri net transitions).<br>
	 * Transitions are indexed by their name.
	 */
	private Map<String, SecurityLevel> activiyClassification = new HashMap<String, SecurityLevel>();
	/**
	 * This map contains the labeling of attributes used during process execution.<br>
	 * Attributes are indexed by their name (token colors of S-Nets).
	 */
	private Map<String, SecurityLevel> attributeClassification = new HashMap<String, SecurityLevel>();
	/**
	 * This map contains the clearance of subjects executing process activities.<br>
	 * Subjects are indexed by name.
	 */
	private Map<String, SecurityLevel> subjectClearance = new HashMap<String, SecurityLevel>();
	/**
	 * Process activities (Names of Petri net transitions).
	 */
	private Set<String> activities = new HashSet<String>();
	/**
	 * Data elements processed during process execution.
	 */
	private Set<String> attributes = new HashSet<String>();
	/**
	 * Subjects executing process activities (subject descriptors of Petri net transitions).
	 */
	private Set<String> subjects = new HashSet<String>();
	
	public Labeling(){};
	
	public Labeling(IFNet sNet, Collection<String> subjects) throws ParameterException{
		this(PNUtils.getLabelSetFromTransitions(sNet.getTransitions()), sNet.getTokenColors(), subjects, DEFAULT_SECURITY_LEVEL);
	}
	
	public Labeling(IFNet sNet, 
					Collection<String> subjects, 
				    SecurityLevel defaultSecurityLevel) throws ParameterException{
		this(PNUtils.getLabelSetFromTransitions(sNet.getTransitions()), sNet.getTokenColors(), subjects, defaultSecurityLevel);
	}
	
	public Labeling(Collection<String> activities,
					Collection<String> attributes,
					Collection<String> subjects, 
					SecurityLevel defaultSecurityLevel) throws ParameterException{
				
		
		initialize(activities, attributes, subjects, defaultSecurityLevel);
	}
	
	public Labeling(Collection<String> activities,
			   		Collection<String> attributes,
			   		Collection<String> subjects) throws ParameterException{
		this(activities, attributes, subjects, DEFAULT_SECURITY_LEVEL);
	}
	
	private void initialize(Collection<String> activities,
			   				Collection<String> attributes,
			   				Collection<String> subjects, 
			   				SecurityLevel defaultSecurityLevel) throws ParameterException{
		
		this.defaultSecurityLevel = defaultSecurityLevel;
		addActivities(activities);
		addAttributes(attributes);
		addSubjects(subjects);
	
	}
	
	public Set<String> getActivities(){
		return Collections.unmodifiableSet(activities);
	}
	
	public boolean addActivities(String... activities) throws ParameterException{
		return addActivities(Arrays.asList(activities));
	}
	
	public boolean addActivities(Collection<String> activities) throws ParameterException{
		Validate.notNull(activities);
		Validate.noNullElements(activities);
		
		if(activities.isEmpty())
			return false;
		
		boolean modified = false;
		for(String activity: activities){
			if(this.activities.add(activity)){
				activiyClassification.put(activity, defaultSecurityLevel);
				modified = true;
			}
		}
		return modified;
	}
	
	public boolean removeActivities(String... activities) throws ParameterException{
		return removeActivities(Arrays.asList(activities));
	}
	
	public boolean removeActivities(Collection<String> activities) throws ParameterException{
		Validate.notNull(activities);
		Validate.noNullElements(activities);
				
		
		if(activities.isEmpty())
			return false;
		
		if(this.activities.removeAll(activities)){
			for(String activity: activities){
				activiyClassification.remove(activity);
			}
			return true;
		}
		return false;
	}
	
	public Set<String> getSubjects(){
		return Collections.unmodifiableSet(subjects);
	}
	
	public boolean addSubjects(String... subjects) throws ParameterException{
		return addSubjects(Arrays.asList(subjects));
	}
	
	public boolean addSubjects(Collection<String> subjects) throws ParameterException{
		Validate.notNull(subjects);
		Validate.noNullElements(subjects);
		
		if(subjects.isEmpty())
			return false;
		
		boolean modified = false;
		for(String subject: subjects){
			if(this.subjects.add(subject)){
				subjectClearance.put(subject, defaultSecurityLevel);
				modified = true;
			}
		}
		return modified;
	}
	
	public boolean removeSubjects(String... subjects) throws ParameterException{
		return removeSubjects(Arrays.asList(subjects));
	}
	
	public boolean removeSubjects(Collection<String> subjects) throws ParameterException{
		Validate.notNull(subjects);
		Validate.noNullElements(subjects);
		
		if(subjects.isEmpty())
			return false;
		
		if(this.subjects.removeAll(subjects)){
			for(String subject: subjects){
				subjectClearance.remove(subject);
			}
			return true;
		}
		return false;
	}
	
	public Set<String> getAttributes(){
		return Collections.unmodifiableSet(attributes);
	}
	
	public boolean addAttributes(String... attributes) throws ParameterException{
		return addAttributes(Arrays.asList(attributes));
	}
	
	public boolean addAttributes(Collection<String> attributes) throws ParameterException{
		Validate.notNull(attributes);
		Validate.noNullElements(attributes);
		
		if(attributes.isEmpty())
			return false;
		
		boolean modified = false;
		for(String attribute: attributes){
			
			if(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR.equals(attribute))
				continue;
								
			if(this.attributes.add(attribute)){
				attributeClassification.put(attribute, defaultSecurityLevel);
				modified = true;
			}
		}
		return modified;
	}
	
	public boolean removeAttribute(String... attributes) throws ParameterException{
		return removeAttributes(Arrays.asList(attributes));
	}
	
	public boolean removeAttributes(Collection<String> attributes) throws ParameterException{
		Validate.notNull(attributes);
		Validate.noNullElements(attributes);
		
		if(attributes.isEmpty())
			return false;
		
		if(this.attributes.removeAll(attributes)){
			for(String attribute: attributes){
				attributeClassification.remove(attribute);
			}
			return true;
		}
		return false;
	}
	
	public void setActivityClassification(String activity, SecurityLevel securityLevel) throws ParameterException{
		validateActivity(activity);
		Validate.notNull(securityLevel);
		
		activiyClassification.put(activity, securityLevel);
	}
	
	public SecurityLevel getActivityClassification(String activity) throws ParameterException{
		validateActivity(activity);
		return activiyClassification.get(activity);
	}
	
	public void setAttributeClassification(String attribute, SecurityLevel securityLevel) throws ParameterException{
		validateAttribute(attribute);
		Validate.notNull(securityLevel);
		
		attributeClassification.put(attribute, securityLevel);
	}
	
	public SecurityLevel getAttributeClassification(String attribute) throws ParameterException{
		validateAttribute(attribute);
		return attributeClassification.get(attribute);
	}

	public void setSubjectClearance(String subject, SecurityLevel securityLevel) throws ParameterException{
		validateSubject(subject);
		Validate.notNull(securityLevel);
		
		subjectClearance.put(subject, securityLevel);
	}
	
	public SecurityLevel getSubjectClearance(String subjectDescriptor) throws ParameterException{
		validateSubject(subjectDescriptor);
		return subjectClearance.get(subjectDescriptor);
	}
	
	 
	//------- Parameter validation -----------------------------------------------------------------------------------
	
	protected void validateActivity(String activity) throws ParameterException{
		Validate.notNull(activity);
		if(!activities.contains(activity))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unknown process activity: " + activity);
	}
	
	protected void validateAttribute(String attribute) throws ParameterException{
		Validate.notNull(attribute);
		if(!attributes.contains(attribute))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unknown data element: " + attribute);
	}
	
	protected void validateSubject(String subject) throws ParameterException{
		Validate.notNull(subject);
		if(!subjects.contains(subject))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unknown subject descriptor: " + subject);
	}
	
	
	
	
	public SecurityLevel getDefaultSecurityLevel() {
		return defaultSecurityLevel;
	}

	public void setDefaultSecurityLevel(SecurityLevel defaultSecurityLevel) {
		this.defaultSecurityLevel = defaultSecurityLevel;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		try{ 
			builder.append("Activities: ");
			for(String activity: activities){
				builder.append(activity);
				builder.append('[');
				builder.append(getActivityClassification(activity));
				builder.append(']');
				builder.append(' ');
			}
			builder.append('\n');
			
			builder.append("Attributes: ");
			for(String attribute: attributes){
				builder.append(attribute);
				builder.append('[');
				builder.append(getAttributeClassification(attribute));
				builder.append(']');
				builder.append(' ');
			}
			builder.append('\n');
			
			builder.append("Subjects: ");
			for(String subject: subjects){
				builder.append(subject);
				builder.append('[');
				builder.append(getSubjectClearance(subject));
				builder.append(']');
				builder.append(' ');
			}
		} catch(ParameterException e){
			// Cannot happen.
		}
		return builder.toString();
	}

}
