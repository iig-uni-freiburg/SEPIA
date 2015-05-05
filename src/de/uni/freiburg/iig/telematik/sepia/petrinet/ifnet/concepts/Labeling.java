package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.invation.code.toval.graphic.dialog.DialogObject;
import de.invation.code.toval.misc.soabase.SOABaseChangeReply;
import de.invation.code.toval.misc.soabase.SOABaseListener;
import de.invation.code.toval.validate.Validate;



public class Labeling implements SOABaseListener, AnalysisContextListener, Cloneable, DialogObject<Labeling> {
	
	/**
	 * Default security level used for initializing classification-, clearance- and labeling-maps.<br>
	 */
	public static final SecurityLevel DEFAULT_SECURITY_LEVEL = SecurityLevel.LOW;

	/**
	 * Default security level used for initializing classification-, clearance- and labeling-maps which
	 * can be changed for each instance.<br>
	 */
	private SecurityLevel defaultSecurityLevel = DEFAULT_SECURITY_LEVEL;
	
	public static final String DEFAULT_NAME = "Labeling";
	
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

	private AnalysisContext context = null;

	protected String name;
	
	public Labeling(AnalysisContext context) {
		this(DEFAULT_NAME, context);
	}
	
	public Labeling(String name, AnalysisContext context) {
		setName(name);
		setAnalysisContext(context, true);
	}
	
	public Labeling(String name, AnalysisContext context, SecurityLevel defaultSecurityLevel) {
		this(name, context);
		Validate.notNull(defaultSecurityLevel);
		this.defaultSecurityLevel = defaultSecurityLevel;
	}
	
	public AnalysisContext getAnalysisContext(){
		return context;
	}
	
	private void setAnalysisContext(AnalysisContext context, boolean reset){
		Validate.notNull(context);
		if(this.context != null){
			this.context.removeAnalysisContextListener(this);
			this.context.getACModel().getContext().removeContextListener(this);
		}
		this.context = context;
		this.context.addAnalysisContextListener(this);
		this.context.getACModel().getContext().addContextListener(this);
		
		if(reset){
			reset();
		} else {
			List<String> activitiesToRemove = new ArrayList<String>();
			for(String activity: activiyClassification.keySet()){
				if(!context.getACModel().getContext().getActivities().contains(activity))
					activitiesToRemove.add(activity);
			}
			for(String activityToRemove: activitiesToRemove)
				activiyClassification.remove(activityToRemove);
			
			List<String> attributesToRemove = new ArrayList<String>();
			for(String attribute: attributeClassification.keySet()){
				if(!context.getACModel().getContext().getObjects().contains(attribute))
					attributesToRemove.add(attribute);
			}
			for(String attributeToRemove: attributesToRemove)
				attributeClassification.remove(attributeToRemove);
			
			List<String> subjectsToRemove = new ArrayList<String>();
			for(String subject: subjectClearance.keySet()){
				if(!context.getACModel().getContext().getSubjects().contains(subject))
					subjectsToRemove.add(subject);
			}
			for(String subjectToRemove: subjectsToRemove)
				subjectClearance.remove(subjectToRemove);
		}
		
		for(String activity: context.getACModel().getContext().getActivities()){
			if(!activiyClassification.containsKey(activity))
				setDefaultActivityClassification(activity);
		}
		
		for(String attribute: context.getACModel().getContext().getObjects()){
			if(!attributeClassification.containsKey(attribute))
				setDefaultAttributeClassification(attribute);
		}
		
		for(String subject: context.getACModel().getContext().getSubjects()){
			if(!subjectClearance.containsKey(subject))
				setDefaultSubjectClearance(subject);
		}
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name) {
		Validate.notNull(name);
		Validate.notEmpty(name);
		if(this.name != null && this.name.equals(name))
			return;
		this.name = name;
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
	
	public void setActivityClassification(String activity, SecurityLevel securityLevel) {
		context.getACModel().getContext().validateActivity(activity);
		Validate.notNull(securityLevel);
		activiyClassification.put(activity, securityLevel);
	}
	
	public SecurityLevel getActivityClassification(String activity) {
		context.getACModel().getContext().validateActivity(activity);
		return activiyClassification.get(activity);
	}
	
	public void setAttributeClassification(String attribute, SecurityLevel securityLevel) {
		context.getACModel().getContext().validateObject(attribute);
		Validate.notNull(securityLevel);
		attributeClassification.put(attribute, securityLevel);
	}
	
	public SecurityLevel getAttributeClassification(String attribute) {
		context.getACModel().getContext().validateObject(attribute);
		return attributeClassification.get(attribute);
	}

	public void setSubjectClearance(String subject, SecurityLevel securityLevel) {
		context.getACModel().getContext().validateSubject(subject);
		Validate.notNull(securityLevel);
		subjectClearance.put(subject, securityLevel);
	}
	
	public SecurityLevel getSubjectClearance(String subject) {
		context.getACModel().getContext().validateSubject(subject);
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
	
	public void takeoverValues(Labeling labeling){
		setName(labeling.getName());
		if(labeling.getAnalysisContext() != getAnalysisContext())
			setAnalysisContext(labeling.getAnalysisContext(), true);
		setDefaultSecurityLevel(labeling.getDefaultSecurityLevel());
		for(String activity: getAnalysisContext().getACModel().getContext().getActivities()){
			setActivityClassification(activity, labeling.getActivityClassification(activity));
		}
		for(String subject: getAnalysisContext().getACModel().getContext().getSubjects()){
			setSubjectClearance(subject, labeling.getSubjectClearance(subject));
		}
		for(String attribute: getAnalysisContext().getACModel().getContext().getObjects()){
			setAttributeClassification(attribute, labeling.getAttributeClassification(attribute));
		}
	}
	
	@Override
	public Labeling clone(){
		Labeling clone = new Labeling(getName(), getAnalysisContext());
		clone.takeoverValues(this);
		return clone;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Labeling {");
		builder.append('\n');
		builder.append("Name: ");
		builder.append(getName());
		builder.append('\n');
		
		if (context.getACModel().getContext().containsActivities()) {
			builder.append("Activities: ");
			for (String activity : context.getACModel().getContext().getActivities()) {
				builder.append(activity);
				builder.append('[');
				builder.append(getActivityClassification(activity));
				builder.append(']');
				builder.append(' ');
			}
			builder.append('\n');
		}

		if (context.getACModel().getContext().containsObjects()) {
			builder.append("Attributes: ");
			for (String attribute : context.getACModel().getContext().getObjects()) {
				builder.append(attribute);
				builder.append('[');
				builder.append(getAttributeClassification(attribute));
				builder.append(']');
				builder.append(' ');
			}
			builder.append('\n');
		}

		if(context.getACModel().getContext().containsSubjects()){
			builder.append("Subjects: ");
			for (String subject : context.getACModel().getContext().getSubjects()) {
				builder.append(subject);
				builder.append('[');
				builder.append(getSubjectClearance(subject));
				builder.append(']');
				builder.append(' ');
			}
			builder.append('\n');	
		}
		
		builder.append("}");
		builder.append('\n');
		return builder.toString();
	}

	@Override
	public SOABaseChangeReply allowSubjectRemoval(String subject) {
		return new SOABaseChangeReply(Labeling.this, true, subject);
	}

	@Override
	public SOABaseChangeReply allowObjectRemoval(String object) {
		return new SOABaseChangeReply(Labeling.this, true, object);
	}

	@Override
	public SOABaseChangeReply allowActivityRemoval(String activity) {
		return new SOABaseChangeReply(Labeling.this, true, activity);
	}

	@Override
	public void nameChanged(String oldName, String newName) {}

	@Override
	public String getListenerDescription() {
		return "labeling " + getName();
	}

	@Override
	public void acModelChanged() {
		setAnalysisContext(this.context, false);
	}

	@Override
	public void subjectDescriptorAdded(String activity, String subjectDescriptor) {}
	
	@Override
	public void subjectDescriptorRemoved(String activity) {}

	@Override
	public void subjectDescriptorchanged(String activity, String oldSubjectDescriptor, String newSubjectDescriptor) {}
	
}
