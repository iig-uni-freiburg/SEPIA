package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.CompatibilityException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jawl.context.Context;
import de.uni.freiburg.iig.telematik.jawl.context.ContextListener;



public class Labeling implements ContextListener {
	
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

	private Context context = null;
	private String contextName = null;
	private Class<?> contextclass = null;
	
	private boolean requireContext = true;
	
	public Labeling(){}
	
	public Labeling(Context context) {
		setContext(context, true);
	}
	
	public Labeling(Context context, SecurityLevel defaultSecurityLevel) {
		this(context);
		Validate.notNull(defaultSecurityLevel);
		this.defaultSecurityLevel = defaultSecurityLevel;
	}
	
	public Context getContext(){
		return context;
	}
	
	public void setContext(Context context, boolean reset){
		Validate.notNull(context);
		if(this.context != null){
			this.context.removeContextListener(this);
		}
		if(reset){
			reset();
		}
		this.context = context;
		this.context.addContextListener(this);
		this.contextName = context.getName();
		
		List<String> activitiesToRemove = new ArrayList<String>();
		for(String activity: activiyClassification.keySet()){
			if(!getActivities().contains(activity))
				activitiesToRemove.add(activity);
		}
		for(String activityToRemove: activitiesToRemove)
			activiyClassification.remove(activityToRemove);
		for(String activity: getActivities()){
			if(!activiyClassification.containsKey(activity))
				setDefaultActivityClassification(activity);
		}
		
		List<String> attributesToRemove = new ArrayList<String>();
		for(String attribute: attributeClassification.keySet()){
			if(!getAttributes().contains(attribute))
				attributesToRemove.add(attribute);
		}
		for(String attributeToRemove: attributesToRemove)
			attributeClassification.remove(attributeToRemove);
		for(String attribute: getAttributes()){
			if(!attributeClassification.containsKey(attribute))
				setDefaultAttributeClassification(attribute);
		}
		
		List<String> subjectsToRemove = new ArrayList<String>();
		for(String subject: subjectClearance.keySet()){
			if(!getSubjects().contains(subject))
				subjectsToRemove.add(subject);
		}
		for(String subjectToRemove: subjectsToRemove)
			subjectClearance.remove(subjectToRemove);
		for(String subject: getSubjects()){
			if(!subjectClearance.containsKey(subject))
				setDefaultSubjectClearance(subject);
		}
	}
	
	public String getContextName() {
		if(context != null)
			return context.getName();
		return contextName;
	}

	public boolean setContextName(String contextName) {
		if(requireContext)
			return false;
		this.contextName = contextName;
		return true;
	}

	public Class<?> getContextClass() {
		if(context != null)
			return context.getClass();
		return contextclass;
	}

	public boolean setContextclass(Class<?> contextclass) {
		if(requireContext)
			return false;
		this.contextclass = contextclass;
		return true;
	}

	private void setDefaultSubjectClearance(String subject) {
		subjectClearance.put(subject, defaultSecurityLevel);
	}

	private void setDefaultAttributeClassification(String attribute) {
		attributeClassification.put(attribute, defaultSecurityLevel);
	}

	private void setDefaultActivityClassification(String activity) {
		activiyClassification.put(activity, defaultSecurityLevel);
	}

	public void reset(){
		activiyClassification.clear();
		attributeClassification.clear();
		subjectClearance.clear();
	}
	
	public boolean requiresContext() {
		return requireContext;
	}

	public void setRequireContext(boolean requireContext) {
		this.requireContext = requireContext;
	}

	public Set<String> getActivities(){
		if(requiresContext()) {
			if (context != null)
				return Collections.unmodifiableSet(context.getActivities());
			else
				return Collections.unmodifiableSet(new HashSet<String>());
		}
		return Collections.unmodifiableSet(activiyClassification.keySet());
	}
	
	public Set<String> getSubjects(){
		if(requiresContext()) {
			if (context != null)
				return Collections.unmodifiableSet(context.getSubjects());
			else
				return Collections.unmodifiableSet(new HashSet<String>());
		}
		return Collections.unmodifiableSet(subjectClearance.keySet());
	}
	
	public Set<String> getAttributes(){
		if(requiresContext()) {
			if (context != null)
				return Collections.unmodifiableSet(context.getObjects());
			else
				return Collections.unmodifiableSet(new HashSet<String>());
		}
		return Collections.unmodifiableSet(attributeClassification.keySet());
	}
	
	public void setActivityClassification(String activity, SecurityLevel securityLevel) {
		validateActivity(activity);
		Validate.notNull(securityLevel);
		activiyClassification.put(activity, securityLevel);
	}
	
	public SecurityLevel getActivityClassification(String activity) {
		validateActivity(activity);
		return activiyClassification.get(activity);
	}
	
	public void setAttributeClassification(String attribute, SecurityLevel securityLevel) {
		validateAttribute(attribute);
		Validate.notNull(securityLevel);
		attributeClassification.put(attribute, securityLevel);
	}
	
	public SecurityLevel getAttributeClassification(String attribute) {
		validateAttribute(attribute);
		return attributeClassification.get(attribute);
	}

	public void setSubjectClearance(String subject, SecurityLevel securityLevel) {
		validateSubject(subject);
		Validate.notNull(securityLevel);
		subjectClearance.put(subject, securityLevel);
	}
	
	public SecurityLevel getSubjectClearance(String subject) {
		validateSubject(subject);
		return subjectClearance.get(subject);
	}
	
	public SecurityLevel getDefaultSecurityLevel() {
		return defaultSecurityLevel;
	}

	public void setDefaultSecurityLevel(SecurityLevel defaultSecurityLevel) {
		this.defaultSecurityLevel = defaultSecurityLevel;
	}

	@Override
	public void subjectAdded(String subject) {
		setDefaultSubjectClearance(subject);
	}

	@Override
	public void subjectRemoved(String subject) {
		subjectClearance.remove(subject);
	}

	@Override
	public void objectAdded(String object) {
		setDefaultAttributeClassification(object);
	}

	@Override
	public void objectRemoved(String object) {
		attributeClassification.remove(object);
	}

	@Override
	public void activityAdded(String transaction) {
		setDefaultActivityClassification(transaction);
	}

	@Override
	public void activityRemoved(String transaction) {
		activiyClassification.remove(transaction);
	}
	
	public void validateActivity(String activity) throws CompatibilityException{
		if(requireContext)
			getContext().validateActivity(activity);
	}
	
	public void validateAttribute(String attribute) throws CompatibilityException{
		if(requireContext)
			getContext().validateObject(attribute);
	}
	
	public void validateSubject(String subject) throws CompatibilityException{
		if(requireContext)
			getContext().validateSubject(subject);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Activities: ");
		for (String activity : getActivities()) {
			builder.append(activity);
			builder.append('[');
			builder.append(getActivityClassification(activity));
			builder.append(']');
			builder.append(' ');
		}
		builder.append('\n');

		builder.append("Attributes: ");
		for (String attribute : getAttributes()) {
			builder.append(attribute);
			builder.append('[');
			builder.append(getAttributeClassification(attribute));
			builder.append(']');
			builder.append(' ');
		}
		builder.append('\n');

		builder.append("Subjects: ");
		for (String subject : getSubjects()) {
			builder.append(subject);
			builder.append('[');
			builder.append(getSubjectClearance(subject));
			builder.append(']');
			builder.append(' ');
		}
		return builder.toString();
	}
	
}
