package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.graphic.dialog.DialogObject;
import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.types.DataUsage;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.event.ACModelListener;

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
public class AnalysisContext implements ACModelListener, Cloneable, DialogObject<AnalysisContext> {
	
	private static final String DEFAULT_LABELING_NAME = "defaultLabeling";
	private static final String DEFAULT_NAME = "AnalysisContext";
	
	private String name = null;
	private Labeling labeling = null;
	private Map<String, String> subjectDescriptors = new HashMap<String, String>();
	@SuppressWarnings("rawtypes")
	private AbstractACModel acModel = null;
	
	private Set<AnalysisContextListener> listeners = new HashSet<AnalysisContextListener>();
	
	@SuppressWarnings("rawtypes")
	public AnalysisContext(AbstractACModel acModel, boolean createDefaultLabeling) {
		this(DEFAULT_NAME, acModel, createDefaultLabeling);
	}
	
	@SuppressWarnings("rawtypes")
	public AnalysisContext(String name, AbstractACModel acModel, boolean createDefaultLabeling) {
		setName(name);
		Validate.notNull(acModel);
		this.acModel = acModel;
		acModel.addACModelListener(this);
		if(createDefaultLabeling)
			this.labeling = new Labeling(DEFAULT_LABELING_NAME, this);
	}
	
	@SuppressWarnings("rawtypes")
	public AnalysisContext(String name, AbstractACModel acModel, boolean createDefaultLabeling, SecurityLevel defaultSecurityLevel) {
		setName(name);
		Validate.notNull(acModel);
		this.acModel = acModel;
		if(createDefaultLabeling)
			this.labeling = new Labeling(DEFAULT_LABELING_NAME, this, defaultSecurityLevel);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		Validate.notNull(name);
		this.name = name;
	}
	
	@SuppressWarnings("rawtypes")
	public AbstractACModel getACModel(){
		return acModel;
	}
	
	public Labeling getLabeling(){
		return labeling;
	}
	
	public Labeling createNewLabeling(String name){
		return new Labeling(name, this);
	}
	
	public Labeling createNewLabeling(String name, SecurityLevel defaultSecurityLevel){
		return new Labeling(name, this, defaultSecurityLevel);
	}
	
	public void setLabeling(Labeling labeling) {
		Validate.notNull(labeling);
		if(labeling.getAnalysisContext() != this)
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Labeling has different analysis context.");
		this.labeling = labeling;
	}
	  
	public boolean setSubjectDescriptor(String activity, String subject) {
		acModel.getContext().validateActivity(activity);
		acModel.getContext().validateSubject(subject);
		validate(activity, subject);
		boolean existingEntry = subjectDescriptors.containsKey(activity);
		String oldSubjectDescriptor = null;
		if(existingEntry){
			if(subjectDescriptors.get(activity).equals(subject))
				return false;
			oldSubjectDescriptor = subjectDescriptors.get(activity);
		}
			
		subjectDescriptors.put(activity, subject);
			for(AnalysisContextListener listener: listeners){
				if(existingEntry){
					listener.subjectDescriptorchanged(activity, oldSubjectDescriptor, subject);
				} else {
					listener.subjectDescriptorAdded(activity, subject);
				}
			}
		return true;
	}
	
	public void removeSubjectDescriptor(String activity) {
		acModel.getContext().validateActivity(activity);
		subjectDescriptors.remove(activity);
		for(AnalysisContextListener listener: listeners){
			listener.subjectDescriptorRemoved(activity);
		}
	}
	
	public String getSubjectDescriptor(String activity) {
		acModel.getContext().validateActivity(activity);
		return subjectDescriptors.get(activity);
	}
	
	public Map<String, String> getSubjectDescriptors(){
		return Collections.unmodifiableMap(subjectDescriptors);
	}
	
	@SuppressWarnings("rawtypes")
	public void setACModel(AbstractACModel acModel, boolean reset){
		Validate.notNull(acModel);
		if(acModel == this.acModel)
			return;
		this.acModel.removeACModelListener(this);
		acModel.addACModelListener(this);
		this.acModel = acModel;
		for(AnalysisContextListener listener: listeners)
			listener.acModelChanged();
	}
	
	public boolean isValid(){
		try{
			validate();
		}catch(ParameterException e){
			return false;
		}
		return true;
	}
	
	public void validate(){
		for(String activity: subjectDescriptors.keySet()){
			validate(activity, subjectDescriptors.get(activity));
		}
	}
	
	public void validate(String activity, String subject) {
		if (!acModel.isAuthorizedForTransaction(subject, activity))
			throw new ParameterException(ErrorCode.INCONSISTENCY, "Subject \"" + subject + "\" is not authorized for activity \"" + activity + "\".");

		if (labeling != null) {
			if (labeling.getSubjectClearance(subject) == SecurityLevel.LOW && labeling.getActivityClassification(activity) == SecurityLevel.HIGH)
				throw new ParameterException(ErrorCode.INCONSISTENCY, "Cannot assign a subject with LOW clearance to an activity with HIGH classification.");
		}
	}
	
	public void addAnalysisContextListener(AnalysisContextListener listener){
		listeners.add(listener);
	}
	
	public void removeAnalysisContextListener(AnalysisContextListener listener){
		listeners.remove(listener);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void validUsageModesChanged(AbstractACModel sender, Set<DataUsage> oldModes, Set<DataUsage> newModes) {}

	@SuppressWarnings("rawtypes")
	@Override
	public void contextChanged(AbstractACModel sender, SOABase context) {
		if(labeling != null){
			labeling.acModelChanged();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void accessPermissionAdded(AbstractACModel sender, String subject, String object, Collection<DataUsage> dataUsageModes) {}

	@SuppressWarnings("rawtypes")
	@Override
	public void accessPermissionRemoved(AbstractACModel sender, String subject, String object, Collection<DataUsage> dataUsageModes) {}

	@SuppressWarnings("rawtypes")
	@Override
	public void executionPermissionAdded(AbstractACModel sender, String subject, String transaction) {}

	@SuppressWarnings("rawtypes")
	@Override
	public void executionPermissionRemoved(AbstractACModel sender, String subject, String transaction) {
		if(subjectDescriptors.containsKey(transaction)){
			if(subjectDescriptors.get(transaction).equals(subject)){
				removeSubjectDescriptor(transaction);
			}
		}
	}
	
	@Override
	public void takeoverValues(AnalysisContext other) {
		setName(other.getName());
		if(other.getLabeling() != null){
			setLabeling(other.getLabeling());
		}
		if(other.getACModel() != null){
			setACModel(other.getACModel(), true);
		}
		subjectDescriptors.clear();
		for(String activity: getACModel().getContext().getActivities()){
			if(other.getSubjectDescriptor(activity) != null){
				setSubjectDescriptor(activity, other.getSubjectDescriptor(activity));
			}
		}
	}
	
	@Override
	public AnalysisContext clone(){
		AnalysisContext clone = new AnalysisContext(getName(), acModel, false);
		clone.takeoverValues(this);
		return clone;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("AnalysisContext {");
		builder.append('\n');
		builder.append("Name: ");
		builder.append(getName());
		builder.append('\n');
		builder.append("SubjectDescriptors:");
		builder.append('\n');
		for(String activity: subjectDescriptors.keySet()){
			builder.append(activity);
			builder.append(" -> ");
			builder.append(getSubjectDescriptor(activity));
			builder.append('\n');
		}
		builder.append('\n');
		if(getACModel() != null){
			builder.append("ACModel: ");
			builder.append(getACModel().getName());
			builder.append('\n');
		}
		if(getLabeling() != null){
			builder.append("Labeling: ");
			builder.append(getLabeling().getName());
			builder.append('\n');
		}
		builder.append("}");
		builder.append('\n');
		return builder.toString();	
	}
	
}
