package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
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
