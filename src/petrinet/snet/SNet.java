package petrinet.snet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import petrinet.AbstractFlowRelation;
import petrinet.cpn.abstr.AbstractCPN;
import petrinet.cwn.abstr.AbstractCWN;
import types.Multiset;
import util.PNUtils;
import validate.InconsistencyException;
import validate.ParameterException;
import validate.ParameterException.ErrorCode;
import validate.Validate;
import event.RelationConstraintEvent;
import exception.PNSoundnessException;
import exception.PNValidationException;

public class SNet extends AbstractCWN<SNetPlace, AbstractSNetTransition, SNetFlowRelation, SNetMarking> {
	
	private Map<String, RegularSNetTransition> regularTransitions;
	private Map<String, DeclassificationTransition> declassificationTransitions;
	
	/**
	 * The analysis context of the S-Net.<br>
	 * This context contains information about:
	 * <ul>
	 * <li>Activity classification: The security level of process activities (S-Net transitions).</li>
	 * <li>Attribute classification: The security level of process attributes (Colored tokens of the S-Net).</li>
	 * <li>Subject clearance: The clearance level of subjects executing process activities.</li>
	 * <li>Subject descriptors: Subjects assigned to process activities.</li>
	 * </ul>
	 */
	private AnalysisContext analysisContext;
	
	public SNet() {
		super();
		initialMarking = new SNetMarking();
		marking = new SNetMarking();
	}

	public SNet(Set<String> places,  
			    Set<String> transitions, 
			    SNetMarking initialMarking) throws ParameterException {
		super(places, transitions, initialMarking);
	}
	
	@Override
	protected void initialize(){
		super.initialize();
		regularTransitions = new HashMap<String, RegularSNetTransition>();
		declassificationTransitions = new HashMap<String, DeclassificationTransition>();
		analysisContext = new AnalysisContext();		
	}
	
	public Collection<RegularSNetTransition> getRegularTransitions(){
		return Collections.unmodifiableCollection(regularTransitions.values());
	}
	
	public Collection<DeclassificationTransition> getDeclassificationTransitions(){
		return Collections.unmodifiableCollection(declassificationTransitions.values());
	}
	
	@Override
	protected void addTransition(AbstractSNetTransition transition) throws ParameterException {
		super.addTransition(transition);
		
		if(transition instanceof RegularSNetTransition) {				
			
			regularTransitions.put(transition.getName(), (RegularSNetTransition) transition);
		} else if(transition instanceof DeclassificationTransition) {
			declassificationTransitions.put(transition.getName(), (DeclassificationTransition) transition);
		}
		analysisContext.getLabeling().addActivities(transition.getName());
	}
	 
	@Override
	public boolean removeTransition(String transitionName) throws ParameterException{
		if(super.removeTransition(transitionName)){
			regularTransitions.remove(transitionName);
			declassificationTransitions.remove(transitionName);
			analysisContext.getLabeling().removeActivities(transitionName);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Adds a declassification transition with the given name to the S-Net.<br>
	 * Transitions names have to be unique. In case the net already contains a transition with
	 * the given name, no transition is added to the net.
	 * @param transitionName The name for the declassification transition.
	 * @return <code>true</code> if the transition was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the transition name is <code>null</code>.
	 */
	public boolean addDeclassificationTransition(String transitionName) throws ParameterException{
		return addDeclassificationTransition(transitionName, transitionName);
	}
	
	/**
	 * Adds a declassification transition with the given name to the S-Net.<br>
	 * Transitions names have to be unique. In case the net already contains a transition with
	 * the given name, no transition is added to the net.
	 * @param transitionName The name for the declassification transition.
	 * @param transitionLabel The label for the declassification transition.
	 * @return <code>true</code> if the transition was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the transition name is <code>null</code>.
	 */
	public boolean addDeclassificationTransition(String transitionName, String transitionLabel) throws ParameterException{
		if(containsTransition(transitionName)){
			return false;
		}
		addTransition(new DeclassificationTransition(this, transitionName, transitionLabel));
		getAnalysisContext().getLabeling().setActivityClassification(transitionName, SecurityLevel.HIGH);
		return true;
	}
	
	
	//------- Functionality -------------------------------------------------------------------------
	
	public Set<String> getSubjectDescriptors(){
		if(getAnalysisContext() == null)
			return new HashSet<String>();
		return getAnalysisContext().getSubjects();
	}
	
	public AnalysisContext getAnalysisContext(){
		return analysisContext;
	}
	
	public void setAnalysisContext(AnalysisContext analysisContext) throws ParameterException{
		Validate.notNull(analysisContext);
		if(!analysisContext.getActivities().containsAll(PNUtils.getListFromTransitions(getTransitions())))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Analysis context must contain all Petri net transitions as activities.");
		this.analysisContext = analysisContext;
	}
	
	//------- Interface methods ---------------------------------------------------------------------

	@Override
	public void relationConstraintChanged(RelationConstraintEvent<? extends AbstractFlowRelation<SNetPlace, AbstractSNetTransition, Multiset<String>>> e) {
		super.relationConstraintChanged(e);
		try {
			getAnalysisContext().getLabeling().addAttributes(getTokenColors());
		} catch (ParameterException e1) {
			e1.printStackTrace();
		}
	}

	
	//------- Creation methods ----------------------------------------------------------------------
	
	@Override
	public void checkValidity() throws PNValidationException {
		super.checkValidity();
		
		try{			
			super.checkSoundness(false);
		} catch(PNSoundnessException e){
			throw new PNValidationException("The underlying CWN of this S-Net is not sound.\n:" + e.getMessage());
		}
		
		checkAnalysisContextValidity();
	}
	
	protected void checkAnalysisContextValidity() throws PNValidationException{
		
		//Check if there is a subject descriptor for every transition
		try{
			for(AbstractSNetTransition transition: getTransitions()){
				if(getAnalysisContext().getSubjectDescriptor(transition.getName()) == null)
					throw new PNValidationException("Transition without subject descriptor: "  + transition.getName());
			}
		} catch(ParameterException e){
			e.printStackTrace();
		}
		
		// Check security level consistency for regular transitions.
		for(String attribute: getAnalysisContext().getAttributes()){
			for(RegularSNetTransition transition: getRegularTransitions()){
				if(transition.processesColor(attribute)){
					// If the access modes of an activity contain CREATE for an attribute,
					// the classification of the attribute must equal the clearance of the assigned subject.
					try{
					if(transition.getAccessModes(attribute).contains(AccessMode.CREATE)){
						try {							
							if(!getAnalysisContext().getLabeling().getAttributeClassification(attribute).equals(getAnalysisContext().getLabeling().getSubjectClearance(getAnalysisContext().getSubjectDescriptor(transition.getName()))))
								throw new InconsistencyException("Security level of attribute \""+attribute+"\" does not match the security level of the subject creating it.");
						} catch (ParameterException e) {
							throw new PNValidationException("Inconsistency exception in assigned analysis context:\n" + e.getMessage());
						}
					}
					}catch(ParameterException e){
						e.printStackTrace();
					}
				}
			}
		}
		
		
		// Check security level consistency for declassification transitions
		for(DeclassificationTransition transition: getDeclassificationTransitions()){
			
			// Check if all produced colors have label LOW
			Set<String> producedColors = transition.getProducedColors();
			producedColors.remove(AbstractCPN.CONTROL_FLOW_TOKEN_COLOR);
			for(String outputColor: producedColors){
				try {
					if(getAnalysisContext().getLabeling().getAttributeClassification(outputColor) != SecurityLevel.LOW)
						throw new PNValidationException("Generated attributes of declassification transitions must be LOW");
				} catch (ParameterException e) {
					throw new PNValidationException("Inconsistency exception in assigned analysis context:\n" + e.getMessage());
 				} 
			}
			
			// Check if transition is classified HIGH
			try {
				if(getAnalysisContext().getLabeling().getActivityClassification(transition.getName()) != SecurityLevel.HIGH)
					throw new PNValidationException("All declassification transitions must have classification HIGH.");
			} catch (ParameterException e) {
				throw new PNValidationException("Internal inconsistency exception:\n" + e.getMessage());
			}
		}
		
	}

	@Override
	protected SNetMarking createNewMarking() {
		return new SNetMarking();
	}	

	@Override
	protected AbstractSNetTransition createNewTransition(String name, String label, boolean isSilent) 
			throws ParameterException {
		

		
		return new RegularSNetTransition(name, label, isSilent);
	}

	@Override
	protected SNetPlace createNewPlace(String name, String label) 
			throws ParameterException {
		return new SNetPlace(name, label);
	}

	@Override
	protected SNetFlowRelation createNewFlowRelation(SNetPlace place, AbstractSNetTransition transition) 
			throws ParameterException {
		return new SNetFlowRelation(place, transition);
	}

	@Override
	protected SNetFlowRelation createNewFlowRelation(AbstractSNetTransition transition, SNetPlace place) 
			throws ParameterException {
		return new SNetFlowRelation(transition, place);
	}
	

	//------- toString -----------------------------------------------------------------------------
	
	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}
	
//	public static void main(String[] args) throws Exception{
//		SNet snet = new SNet();
//		snet.addPlace("p1");
//		snet.addPlace("p2");
//		snet.addPlace("p3");
//		snet.addPlace("p4");
//		snet.addPlace("p5");
//		snet.addPlace("p6");
//		snet.addTransition("t1");
//		snet.addTransition("t2");
//		snet.addTransition("t3");
//		snet.addTransition("t4");
//		snet.addFlowRelationPT("p1", "t1");
//		snet.addFlowRelationTP("t1", "p2");
//		snet.addFlowRelationTP("t1", "p3");
//		snet.addFlowRelationPT("p2", "t2");
//		snet.addFlowRelationPT("p3", "t3");
//		snet.addFlowRelationTP("t2", "p4");
//		snet.addFlowRelationTP("t3", "p5");
//		snet.addFlowRelationPT("p4", "t4");
//		snet.addFlowRelationPT("p5", "t4");
//		snet.addFlowRelationTP("t4", "p6");
//		SNetMarking initialMarking = new SNetMarking();
//		Multiset<String> markingInput = new Multiset<String>();
//		markingInput.add("black");
////		markingInput.put("green", 1);
//		initialMarking.set("p1", markingInput);
//		snet.setInitialMarking(initialMarking);
//		
//		System.out.println(snet);
//		System.out.println(snet.getEnabledTransitions());
//		snet.fire("t1");
//		System.out.println(snet.getEnabledTransitions());
//		snet.fire("t2");
//		System.out.println(snet.getEnabledTransitions());
//		snet.fire("t3");
//		System.out.println(snet.getEnabledTransitions());
//		snet.fire("t4");
//		System.out.println(snet.getEnabledTransitions());
//		snet.setBoundedness(Boundedness.BOUNDED);
//		
//		snet.checkSoundness();
//	}

	@Override
	public SNet newInstance() {
		return new SNet();
	}

}
