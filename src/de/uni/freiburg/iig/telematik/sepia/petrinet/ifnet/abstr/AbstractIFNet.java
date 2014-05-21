package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.InconsistencyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.RelationConstraintEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.AbstractIFNetMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.AbstractIFNetMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.AbstractIFNetMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;

/**
 * 
 * @author Thomas Stocker
 *
 * @param <P>
 * @param <T>
 * @param <F>
 * @param <M>
 * @param <R> Type of regular transitions. MUST be a subtype of T!
 * @param <D> Type of declassification transitions. MUST be a subtype of T!
 */
public abstract class AbstractIFNet<P extends AbstractIFNetPlace<F>,
						   			T extends AbstractIFNetTransition<F>, 
						   			F extends AbstractIFNetFlowRelation<P,T>, 
						   			M extends AbstractIFNetMarking,
						   			R extends AbstractRegularIFNetTransition<F>,
						   			D extends AbstractDeclassificationTransition<F>,
						   			X extends AbstractIFNetMarkingGraphState<M>,
									Y extends AbstractIFNetMarkingGraphRelation<M, X>> 
 
							 		  extends AbstractCWN<P,T,F,M,X,Y>{
	
	protected Map<String, R> regularTransitions;
	protected Map<String, D> declassificationTransitions;
	
	/**
	 * The analysis context of the IF-Net.<br>
	 * This context contains information about:
	 * <ul>
	 * <li>Activity classification: The security level of process activities (IF-Net transitions).</li>
	 * <li>Attribute classification: The security level of process attributes (Colored tokens of the IF-Net).</li>
	 * <li>Subject clearance: The clearance level of subjects executing process activities.</li>
	 * <li>Subject descriptors: Subjects assigned to process activities.</li>
	 * </ul>
	 */
	protected AnalysisContext analysisContext;
	
	public AbstractIFNet() {
		super();
		initialMarking = createNewMarking();
		marking = createNewMarking();
	}

	public AbstractIFNet(Set<String> places,  
			    		 Set<String> transitions, 
			    		 M initialMarking) {
		super(places, transitions, initialMarking);
	}
	
	@Override
	public NetType getNetType() {
		return NetType.IFNet;
	}
	
	@Override
	protected void initialize(){
		super.initialize();
		regularTransitions = new HashMap<String, R>();
		declassificationTransitions = new HashMap<String, D>();
		analysisContext = new AnalysisContext();
	}
	
	public Collection<R> getRegularTransitions(){
		return Collections.unmodifiableCollection(regularTransitions.values());
	}
	
	public Collection<D> getDeclassificationTransitions(){
		return Collections.unmodifiableCollection(declassificationTransitions.values());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTransition(T transition) {
		super.addTransition(transition);
		
		if(transition instanceof AbstractRegularIFNetTransition) {				
			regularTransitions.put(transition.getName(), (R) transition);
		} else if(transition instanceof AbstractDeclassificationTransition) {
			declassificationTransitions.put(transition.getName(), (D) transition);
		}
		analysisContext.getLabeling().addActivities(transition.getName());
	}
	 
	@Override
	public boolean removeTransition(String transitionName) {
		if(super.removeTransition(transitionName)){
			regularTransitions.remove(transitionName);
			declassificationTransitions.remove(transitionName);
			analysisContext.getLabeling().removeActivities(transitionName);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Adds a declassification transition with the given name to the IF-Net.<br>
	 * Transitions names have to be unique. In case the net already contains a transition with
	 * the given name, no transition is added to the net.
	 * @param transitionName The name for the declassification transition.
	 * @return <code>true</code> if the transition was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the transition name is <code>null</code>.
	 */
	public boolean addDeclassificationTransition(String transitionName) {
		return addDeclassificationTransition(transitionName, transitionName);
	}
	
	/**
	 * Adds a declassification transition with the given name to the IF-Net.<br>
	 * Transitions names have to be unique. In case the net already contains a transition with
	 * the given name, no transition is added to the net.
	 * @param transitionName The name for the declassification transition.
	 * @param transitionLabel The label for the declassification transition.
	 * @return <code>true</code> if the transition was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the transition name is <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public boolean addDeclassificationTransition(String transitionName, String transitionLabel) {
		if(containsTransition(transitionName)){
			return false;
		}
		addTransition((T) createNewDeclassificationTransition(transitionName, transitionLabel, false));
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
	
	public void setAnalysisContext(AnalysisContext analysisContext) {
		Validate.notNull(analysisContext);
		if(!analysisContext.getActivities().containsAll(PNUtils.getLabelSetFromTransitions(getTransitions(), false)))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Analysis context must contain all Petri net transitions as activities.");
		this.analysisContext = analysisContext;
	}
	
	//------- Interface methods ---------------------------------------------------------------------

	@Override
	public void relationConstraintChanged(RelationConstraintEvent<? extends AbstractFlowRelation<P, T, Multiset<String>>> e) {
		super.relationConstraintChanged(e);
		try {
			getAnalysisContext().getLabeling().addAttributes(getTokenColors());
		} catch (ParameterException e1) {
			e1.printStackTrace();
		}
	}

	
	//------- Creation methods ----------------------------------------------------------------------
	
	protected abstract R createNewRegularTransition(String name, String label, boolean isSilent);
	
	protected abstract D createNewDeclassificationTransition(String name, String label, boolean isSilent);
	
	
	//------- Validation methods --------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public void checkValidity() throws PNValidationException {
		super.checkValidity();
		
		try{			
			super.checkSoundness(false);
		} catch(PNSoundnessException e){
			throw new PNValidationException("The underlying CWN of this IF-Net is not sound.\n:" + e.getMessage());
		}
		
		// Check property 4 for declassification transitions: 
		// For each declassification transition t, the following condition must hold:
		// No other net transition creates a token with the same color than any of the produced colors of t
		// (Either as regular transition with CREATE mode or as declassification transition)
		for(D declassificationTransition: getDeclassificationTransitions()){
			Set<T> otherNetTransitions = new HashSet<T>();
			otherNetTransitions.addAll(getTransitions());
			otherNetTransitions.remove(declassificationTransition);
			for(T otherTransition: otherNetTransitions){
				if(otherTransition.isDeclassificator()){
					for(String color: declassificationTransition.getProducedAttributes()){
						if(otherTransition.producesColor(color))
							throw new PNValidationException("There is another declassification transition which produces color \""+color+"\"");
					}
				} else {
					try{
					for(String color: declassificationTransition.getProducedAttributes()){
						if(otherTransition.producesColor(color) && ((R) otherTransition).getAccessModes(color).contains(AccessMode.CREATE))
							throw new PNValidationException("There is another net transition which creates tokens of color \""+color+"\"");
					}
					}catch(ParameterException e){
						e.printStackTrace();
					}
				}
			}
		}
		
		checkAnalysisContextValidity();
	}
	
	protected void checkAnalysisContextValidity() throws PNValidationException{
		
		if(getAnalysisContext() == null)
			return;
		
		// Check if there is a subject descriptor for every transition
		for (T transition : getTransitions()) {
			try {
				getAnalysisContext().getSubjectDescriptor(transition.getName());
			} catch (ParameterException e) {
				throw new PNValidationException("Transition without subject descriptor: " + transition.getName());
			}
		}

		// Check security level consistency for regular transitions.
		for(String attribute: getAnalysisContext().getAttributes()){
			for(AbstractRegularIFNetTransition<F> transition: getRegularTransitions()){
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
		for(AbstractDeclassificationTransition<F> transition: getDeclassificationTransitions()){
			
			// Check if all produced colors have label LOW
			Set<String> producedColors = transition.getProducedColors();
			producedColors.remove(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR);
			for(String outputColor: producedColors){
				if(getAnalysisContext().getLabeling().getAttributeClassification(outputColor) != SecurityLevel.LOW)
					throw new PNValidationException("Generated attributes of declassification transitions must be LOW");
			}
			
			// Check if transition is classified HIGH
			if(getAnalysisContext().getLabeling().getActivityClassification(transition.getName()) != SecurityLevel.HIGH)
				throw new PNValidationException("All declassification transitions must have classification HIGH.");
		}
		
	}
	
	@Override
	public AbstractIFNetMarkingGraph<M,X,Y> getMarkingGraph() throws PNException{
		return (AbstractIFNetMarkingGraph<M, X, Y>) super.getMarkingGraph();
	}
	
	@Override
	public AbstractIFNetMarkingGraph<M,X,Y> buildMarkingGraph() throws PNException{
		return (AbstractIFNetMarkingGraph<M, X, Y>) super.buildMarkingGraph();
	}

}
