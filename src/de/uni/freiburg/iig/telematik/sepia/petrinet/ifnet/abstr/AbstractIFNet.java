package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.InconsistencyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CWNChecker.CWNPropertyFlag;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
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
						   			D extends AbstractDeclassificationTransition<F>> 
 
							 		  extends AbstractCPN<P,T,F,M>{

	private static final long serialVersionUID = 7710837900551942698L;
	
	public static final String CONTROL_FLOW_TOKEN_COLOR = "black";
	
	protected Map<String, R> regularTransitions;
	protected Map<String, D> declassificationTransitions;
	
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
	public String defaultTokenColor(){
		return CONTROL_FLOW_TOKEN_COLOR;
	}
	
	@Override
	protected void initialize(){
		super.initialize();
		regularTransitions = new HashMap<String, R>();
		declassificationTransitions = new HashMap<String, D>();
	}
	
	public Collection<R> getRegularTransitions(){
		return Collections.unmodifiableCollection(regularTransitions.values());
	}
	
	public Collection<D> getDeclassificationTransitions(){
		return Collections.unmodifiableCollection(declassificationTransitions.values());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean addTransition(T transition, boolean notifyListeners) {
		if(hasAnalysisContext()){
			if(!analysisContext.getACModel().getContext().getActivities().contains(transition.getLabel()))
				throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot add transition with label \"" +transition.getLabel()+"\".\nReason: The connected analysis context does not contain an activity with this name." );
		}
		boolean superResult = super.addTransition(transition, notifyListeners);
		if(!superResult)
			return false;
		
		if(transition instanceof AbstractRegularIFNetTransition) {				
			regularTransitions.put(transition.getName(), (R) transition);
		} else if(transition instanceof AbstractDeclassificationTransition) {
			declassificationTransitions.put(transition.getName(), (D) transition);
		}
		return true;
	}
	 
	@Override
	public boolean removeTransition(String transitionName) {
		if(super.removeTransition(transitionName)){
			regularTransitions.remove(transitionName);
			declassificationTransitions.remove(transitionName);
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
		return addDeclassificationTransition(transitionName, true);
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
	public boolean addDeclassificationTransition(String transitionName, boolean notifyListeners) {
		return addDeclassificationTransition(transitionName, transitionName, notifyListeners);
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
	public boolean addDeclassificationTransition(String transitionName, String transitionLabel) {
		return addDeclassificationTransition(transitionName, transitionLabel, true);
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
	public boolean addDeclassificationTransition(String transitionName, String transitionLabel, boolean notifyListeners) {
		if(containsTransition(transitionName)){
			return false;
		}
		addTransition((T) createNewDeclassificationTransition(transitionName, transitionLabel, false), notifyListeners);
		if(hasAnalysisContext()){
			getAnalysisContext().getLabeling().setActivityClassification(transitionName, SecurityLevel.HIGH);
		}
		return true;
	}
	
	@Override
	public M getInitialMarking(){
		return super.getInitialMarking();
	}
	
	@Override
	public M getMarking(){
		return super.getMarking();
	}
	
	//------- Functionality -------------------------------------------------------------------------
	
	public Set<String> getSubjectDescriptors(){
		if(getAnalysisContext() == null)
			return new HashSet<String>();
		return getAnalysisContext().getACModel().getContext().getSubjects();
	}
	
	public AnalysisContext getAnalysisContext(){
		return analysisContext;
	}
	
	public boolean hasAnalysisContext(){
		return analysisContext != null;
	}
	
	public void setAnalysisContext(AnalysisContext analysisContext) {
		validateAnalysisContext(analysisContext);
		this.analysisContext = analysisContext;
	}
	
	public void removeAnalysisContext(){
		this.analysisContext = null;
	}
	
	public void validateAnalysisContext(AnalysisContext analysisContext){
		Validate.notNull(analysisContext);
		if(!analysisContext.getACModel().getContext().getActivities().containsAll(PNUtils.getLabelSetFromTransitions(getTransitions(), false)))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Analysis context must contain all Petri net transitions as activities.");
		if(!analysisContext.getACModel().getContext().getObjects().containsAll(getTokenColors()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Analysis context must contain all token colors as attributes.");
	}
	
	public boolean acceptsAnalysisContext(AnalysisContext analysisContext){
		try {
			validateAnalysisContext(analysisContext);
			return true;
		} catch(ParameterException e){
			return false;
		}
	}

	
	//------- Creation methods ----------------------------------------------------------------------
	
	protected abstract R createNewRegularTransition(String name, String label, boolean isSilent);
	
	protected abstract D createNewDeclassificationTransition(String name, String label, boolean isSilent);
	
	
	//------- Validation methods --------------------------------------------------------------------

	@Override
	public void checkValidity() throws PNValidationException {
		checkValidity(CWNPropertyFlag.ACCEPT_REMAINING_CF_TOKENS);
	}
	
	@SuppressWarnings("unchecked")
	public void checkValidity(CWNPropertyFlag... flags) throws PNValidationException {
		super.checkValidity();
		
		System.out.println("before check cwn soundness");
//		CWNProperties cwnProperties = CWNChecker.checkCWNSoundness(this, true, flags);
//		if(!cwnProperties.isSoundCWN())
//			throw new PNValidationException("The underlying CWN of this IF-Net is not sound.\nReason: " + cwnProperties.exception.getMessage());
//		
		System.out.println("after check cwn soundness");
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
					for(String color: declassificationTransition.getProducedAttributes()){
						if(otherTransition.producesColor(color) && ((R) otherTransition).getAccessModes(color).contains(AccessMode.CREATE))
							throw new PNValidationException("There is another net transition which creates tokens of color \""+color+"\"");
					}
				}
			}
		}
		
		checkAnalysisContextValidity();
	}
	
	protected void checkAnalysisContextValidity() throws PNValidationException{
		
		if(getAnalysisContext() == null)
			return;
		
		// Check if all token colors are contained in the analysis context in form of attributes.
		for(String tokenColor: getTokenColors()){
			if(tokenColor.equals(defaultTokenColor()))
				continue;
			if(!getAnalysisContext().getACModel().getContext().getObjects().contains(tokenColor))
				throw new PNValidationException("Analysis context does not contain attribute: " + tokenColor);
		}
		
		for(T transition: getTransitions(false)){
			if(!getAnalysisContext().getACModel().getContext().getActivities().contains(transition.getLabel()))
				throw new PNValidationException("Analysis context does not contain activity " + transition.getLabel());
		}
		
		// Check if there is a subject descriptor for every transition
		for (T transition : getTransitions()) {
			try {
				getAnalysisContext().getSubjectDescriptor(transition.getName());
			} catch (ParameterException e) {
				throw new PNValidationException("Transition without subject descriptor: " + transition.getName());
			}
		}

		// Check security level consistency for regular transitions.
		for(String attribute: getAnalysisContext().getACModel().getContext().getObjects()){
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
			
			// Check property 6 for declassification transitions: 
			// -> All produced colors must have label LOW
			Set<String> producedColors = transition.getProducedColors();
			producedColors.remove(CONTROL_FLOW_TOKEN_COLOR);
			for(String outputColor: producedColors){
				if(getAnalysisContext().getLabeling().getAttributeClassification(outputColor) != SecurityLevel.LOW)
					throw new PNValidationException("Generated attributes of declassification transitions must be LOW");
			}
			
			// Check property 7 for declassification transitions: 
			// -> Transition is classified HIGH
			if(getAnalysisContext().getLabeling().getActivityClassification(transition.getName()) != SecurityLevel.HIGH)
				throw new PNValidationException("All declassification transitions must have classification HIGH.");
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((declassificationTransitions == null) ? 0 : declassificationTransitions.hashCode());
		result = prime * result + ((regularTransitions == null) ? 0 : regularTransitions.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		AbstractIFNet other = (AbstractIFNet) obj;
		if (declassificationTransitions == null) {
			if (other.declassificationTransitions != null) {
				return false;
			}
		} else if (!declassificationTransitions.equals(other.declassificationTransitions)) {
			return false;
		}
		if (regularTransitions == null) {
			if (other.regularTransitions != null) {
				return false;
			}
		} else if (!regularTransitions.equals(other.regularTransitions)) {
			return false;
		}
		return true;
	}

}
