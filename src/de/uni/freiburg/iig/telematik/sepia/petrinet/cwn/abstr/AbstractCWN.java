package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr;

import java.util.Collection;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.traverse.TraversalUtils;
import de.uni.freiburg.iig.telematik.sepia.event.RelationConstraintEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.cwn.AbstractCWNMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.cwn.AbstractCWNMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.cwn.AbstractCWNMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.util.ReachabilityUtils;


public abstract class AbstractCWN<P extends AbstractCWNPlace<F>,
								  T extends AbstractCWNTransition<F>, 
								  F extends AbstractCWNFlowRelation<P,T>, 
								  M extends AbstractCWNMarking,
								  X extends AbstractCWNMarkingGraphState<M>,
								  Y extends AbstractCWNMarkingGraphRelation<M,X>> 

									extends AbstractCPN<P,T,F,M,X,Y>{
	
	public static final String CONTROL_FLOW_TOKEN_COLOR = "black";

	public AbstractCWN() {
		super();
	}

	public AbstractCWN(Set<String> places, Set<String> transitions, M initialMarking) 
			throws ParameterException {
		super(places, transitions, initialMarking);
	}	
	
	@Override
	public NetType getNetType() {
		return NetType.CWN;
	}
	
	@Override
	public String defaultTokenColor(){
		return CONTROL_FLOW_TOKEN_COLOR;
	}
	
	public Collection<P> getInputPlaces(){
		return getSourcePlaces();
	}
	
	public P getInputPlace(){
		if(sourcePlaces.isEmpty())
			return null;
		return getSourcePlaces().iterator().next();
	}
	
	public Collection<P> getOutputPlaces(){
		return getDrainPlaces();
	}
	
	public P getOutputPlace(){
		if(drainPlaces.isEmpty())
			return null;
		return getDrainPlaces().iterator().next();
	}
	
	
	//------- Interface methods ---------------------------------------------------------------------
	
	@Override
	public void relationConstraintChanged(RelationConstraintEvent<? extends AbstractFlowRelation<P, T, Multiset<String>>> e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void transitionFired(TransitionEvent<? extends AbstractTransition<F, Multiset<String>>> e) {
		// TODO Auto-generated method stub
	}
	
	//------- Soundness and validity ---------------------------------------------------------------

	@Override
	public void checkValidity() throws PNValidationException {
		super.checkValidity();
		
		// Check if there is only one input/output place
		P input = null;
		Collection<P> sourcePlaces = getSourcePlaces();

		 
		if(sourcePlaces.isEmpty())			
			throw new PNValidationException("CWN has no input place.");
		
		
		if(sourcePlaces.size() > 1)
			throw new PNValidationException("CWN has more than one input place: " + sourcePlaces);
		input = sourcePlaces.iterator().next();
		
		P output = null;
		Collection<P> drainPlaces = getDrainPlaces(); 
		if(drainPlaces.isEmpty())
			throw new PNValidationException("CWN has no output place.");
		if(drainPlaces.size() > 1)
			throw new PNValidationException("CWN has more than one output place: " + drainPlaces);
		output = drainPlaces.iterator().next();
		
		// Check initial marking
		if(initialMarking.isEmpty())
			throw new PNValidationException("Initial marking must contain input place "+input);
		if(initialMarking.places().size() > 1 || !initialMarking.contains(input.getName()))
			throw new PNValidationException("Initial marking must only contain input place "+input);

		try {
		if(initialMarking.get(input.getName()).support().size() > 1 || 
		   !initialMarking.get(input.getName()).support().contains(CONTROL_FLOW_TOKEN_COLOR) ||
		   initialMarking.get(input.getName()).multiplicity(CONTROL_FLOW_TOKEN_COLOR) > 1)
			throw new PNValidationException("Initial marking must only contain one control flow token in input place " + input);
		} catch(ParameterException e){}
			
		// Check connectedness of short-circuited net.
		try {
			T connector = createNewTransition("connector", "connector", true);
			addTransition(connector);
			addFlowRelationPT(output.getName(), "connector");
			addFlowRelationTP("connector", input.getName());
			
			
			if(!TraversalUtils.isStronglyConnected(this, input)){
				throw new PNValidationException("CWN is not strongly connected.");
			}
			removeTransition("connector");
		} catch(ParameterException e){}
		
		if(isBounded() != Boundedness.BOUNDED)
			throw new PNValidationException("CWN is not bounded.");
	}
	
	/**
	 * Soundness checking of CWNs requires to build the marking graph of the net under consideration.
	 * This only works for bounded nets, i.e. nets for which the {@link AbstractPetriNet#isBounded()} method returns {@link Boundedness#BOUNDED}<br>
	 * If the boundedness property is set explicitly, the method may not halt.
	 * @see AbstractCPN#checkSoundness(boolean)
	 */
	@Override
	public void checkSoundness(boolean checkValidity) throws PNValidationException, PNSoundnessException {
		super.checkSoundness(checkValidity);
		
		// Requirement 1: Option to complete + proper completion
		try {
			AbstractCWNUtils.validCompletion(this);

			// Requirement 2: No dead transitions
			Set<T> deadTransitions = null;
			try {
				deadTransitions = ReachabilityUtils.getDeadTransitions(this);
			} catch (PNException e) {
				throw new PNSoundnessException("PN-Exception during soundness check: Cannot extract dead transitions.<br>Reason: " + e.getMessage());
			}
			if (!deadTransitions.isEmpty())
				throw new PNSoundnessException("CWN has dead transitions: " + deadTransitions);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public AbstractCWNMarkingGraph<M,X,Y> getMarkingGraph() throws PNException{
		return (AbstractCWNMarkingGraph<M, X, Y>) super.getMarkingGraph();
	}
	
	@Override
	public AbstractCWNMarkingGraph<M,X,Y> buildMarkingGraph() throws PNException{
		return (AbstractCWNMarkingGraph<M, X, Y>) super.buildMarkingGraph();
	}
	
	@Override
	public M fireCheck(String transitionName) throws ParameterException, PNException {
		validateFireTransition(transitionName);
		M newMarking = cloneMarking();
		T transition = getTransition(transitionName);
		for(F relation: transition.getIncomingRelations()){
			String inputPlaceName = relation.getPlace().getName();			
			Multiset<String> oldState = (newMarking.get(inputPlaceName) == null ? new Multiset<String>() : newMarking.get(inputPlaceName).clone());
			newMarking.set(inputPlaceName, oldState.difference(relation.getConstraint()));
		
		}
		for(F relation: transition.getOutgoingRelations()){
			String outputPlaceName = relation.getPlace().getName();
			Multiset<String> oldState = (newMarking.get(outputPlaceName) == null ? new Multiset<String>() : newMarking.get(outputPlaceName).clone());
			newMarking.set(outputPlaceName, oldState.sum(relation.getConstraint()));
		}
		return newMarking;
	}
	

}
