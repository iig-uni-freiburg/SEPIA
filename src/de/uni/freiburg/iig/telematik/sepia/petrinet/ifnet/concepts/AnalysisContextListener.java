package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts;

public interface AnalysisContextListener {
	
	public void acModelChanged();
	
	public void subjectDescriptorAdded(String activity, String subjectDescriptor);
	
	public void subjectDescriptorRemoved(String activity);
	
	public void subjectDescriptorchanged(String activity, String oldSubjectDescriptor, String newSubjectDescriptor);

}
