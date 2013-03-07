package petrinet.cwn.abstr;

import java.util.Collection;
import java.util.Set;

import petrinet.AbstractFlowRelation;
import petrinet.AbstractPetriNet;
import petrinet.AbstractTransition;
import petrinet.cpn.abstr.AbstractCPN;
import traverse.TraversalUtils;
import types.Multiset;
import util.ReachabilityUtils;
import validate.ParameterException;
import event.RelationConstraintEvent;
import event.TransitionEvent;
import exception.PNException;
import exception.PNSoundnessException;
import exception.PNValidationException;

public abstract class AbstractCWN<P extends AbstractCWNPlace<F>,
								  T extends AbstractCWNTransition<F>, 
								  F extends AbstractCWNFlowRelation<P,T>, 
								  M extends AbstractCWNMarking> 

									extends AbstractCPN<P,T,F,M>{

	public AbstractCWN() {
		super();
	}

	public AbstractCWN(Set<String> places, Set<String> transitions, M initialMarking) 
			throws ParameterException {
		super(places, transitions, initialMarking);
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
			T connector = createNewTransition("connector", "connector");
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
	 * @see AbstractCPN#checkSoundness();
	 */
	@Override
	public void checkSoundness() throws PNSoundnessException, PNValidationException {
		super.checkSoundness();
		
		
		// Requirement 1: Option to complete
		try {
			AbstractCWNUtils.validCompletion(this);			
		} catch (ParameterException e1) {}
		
		// Requirement 2: No dead transitions
		try {
			Set<T> deadTransitions = ReachabilityUtils.getDeadTransitions(this);
			if(!deadTransitions.isEmpty())
				throw new PNSoundnessException("CWN has dead transitions: " + deadTransitions);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		
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
	
	//------- toString ----------------------------------------------------------------------
	
	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
