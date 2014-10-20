package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.PNPropertiesChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.PNPropertiesChecker.InOutPlaces;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.util.ReachabilityUtils;

public class CWNChecker {
	
	public static 	<P extends AbstractCPNPlace<F>, 
	   				 T extends AbstractCPNTransition<F>, 
	   				 F extends AbstractCPNFlowRelation<P,T>,
	   				 M extends AbstractCPNMarking,
	   				 X extends AbstractCPNMarkingGraphState<M>, 
	   				 Y extends AbstractCPNMarkingGraphRelation<M,X>,
	   				 N extends AbstractCPN<P,T,F,M,X,Y>> 
	
	InOutPlaces checkCWNStructure(N petriNet) throws PNValidationException {
		Validate.notNull(petriNet);
		
		// Check if there is only one input/output place
		InOutPlaces places = PNPropertiesChecker.validateInputOutputPlace(petriNet);
		P input = petriNet.getPlace(places.getInput());
		
		// check strongly connectedness of short-circuited net
		PNPropertiesChecker.validateStrongConnectedness(petriNet, places);
		
		// Check initial marking
		checkSingleCFTokenInInitialMarking(petriNet, input.getName());
	
		// Check control flow dependency
		String CFTokenColor = petriNet.defaultTokenColor();
		if(AbstractIFNet.class.isAssignableFrom(petriNet.getClass())){
			CFTokenColor = AbstractIFNet.DEFAULT_TOKEN_COLOR;
		}
		for(T transition: petriNet.getTransitions()){
			if(!transition.getConsumedColors().contains(CFTokenColor))
				throw new PNValidationException("Transition \""+transition+"\" does not consume control flow token");
			if(!transition.getProducedColors().contains(CFTokenColor))
				throw new PNValidationException("Transition \""+transition+"\" does not produce control flow token");
		}
		
		return places;
	}
	
	public static 	<P extends AbstractCPNPlace<F>, 
		 			 T extends AbstractCPNTransition<F>, 
		 			 F extends AbstractCPNFlowRelation<P,T>,
		 			 M extends AbstractCPNMarking,
		 			 X extends AbstractCPNMarkingGraphState<M>, 
		 			 Y extends AbstractCPNMarkingGraphRelation<M,X>,
		 			 N extends AbstractCPN<P,T,F,M,X,Y>> 

	boolean hasCWNStructure(N petriNet) {
		try {
			checkCWNStructure(petriNet);
		} catch (PNValidationException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Checks if the initial marking of the given Petri net contains only the given place<br>
	 * and for this place only contains exactly one control flow token.<br>
	 * In case the method is called with a CPN, the control flow token color is determined using {@link AbstractCPN#defaultTokenColor()};<br>
	 * in case of an IFNet using {@link AbstractIFNet#CONTROL_FLOW_TOKEN_COLOR}
	 * @param cpn
	 * @param placeName
	 * @throws PNValidationException
	 */
	public static 	<P extends AbstractCPNPlace<F>, 
		 			 T extends AbstractCPNTransition<F>, 
		 			 F extends AbstractCPNFlowRelation<P,T>,
		 			 M extends AbstractCPNMarking,
		 			 X extends AbstractCPNMarkingGraphState<M>, 
		 			 Y extends AbstractCPNMarkingGraphRelation<M,X>,
		 			 N extends AbstractCPN<P,T,F,M,X,Y>> 

	void checkSingleCFTokenInInitialMarking(N cpn, String placeName) throws PNValidationException{
		if(!cpn.containsPlace(placeName))
			throw new PNValidationException("Petri net does not contain place " + placeName);
		
		P place = cpn.getPlace(placeName);
		String cfTokenColor = cpn.defaultTokenColor();
		if(AbstractIFNet.class.isAssignableFrom(cpn.getClass())){
			cfTokenColor = AbstractIFNet.CONTROL_FLOW_TOKEN_COLOR;
		}
		M initialMarking = cpn.getInitialMarking();
		if(!initialMarking.contains(place.getName()))
			throw new PNValidationException("Initial marking must contain input place " + place);
		if(initialMarking.places().size() > 1)
			throw new PNValidationException("Initial marking must only contain input place " + place);
	
		Multiset<String> tokensInitialPlace = initialMarking.get(place.getName());
		if(!tokensInitialPlace.contains(cfTokenColor))
			throw new PNValidationException("Initial marking must contain at least one control flow token for input place " + place);
		if(tokensInitialPlace.support().size() > 1)
			throw new PNValidationException("Initial marking must contain at least one control flow token for input place " + place);
	}
	
	/**
	 * Soundness checking of CWNs requires to build the marking graph of the net under consideration.
	 * This only works for bounded nets, i.e. nets for which the {@link AbstractPetriNet#isBounded()} method returns @code{true}
	 */
	public static <P extends AbstractCPNPlace<F>, 
		 		   T extends AbstractCPNTransition<F>, 
		 		   F extends AbstractCPNFlowRelation<P,T>,
		 		   M extends AbstractCPNMarking,
		 		   X extends AbstractCPNMarkingGraphState<M>, 
		 		   Y extends AbstractCPNMarkingGraphRelation<M,X>,
		 		   N extends AbstractCPN<P,T,F,M,X,Y>> 

	void checkCWNSoundness(N cpn, boolean checkStructure) throws PNSoundnessException {
		try {
			PNPropertiesChecker.validateBoundedness(cpn);
		} catch (PNValidationException e1) {
			throw new PNSoundnessException("Net is not bounded.");
		}
		
		InOutPlaces places = null;
		try {
			if (checkStructure) {
				places = checkCWNStructure(cpn);
			} else {
				places = PNPropertiesChecker.validateInputOutputPlace(cpn);
			}
		} catch(PNValidationException e){
			throw new PNSoundnessException(e.getMessage());
		}
		
		// Requirement 1: Option to complete + proper completion
		checkValidCompletion(cpn, places.getOutput());
		
		// Requirement 2: No dead transitions
		try {
			ReachabilityUtils.checkDeadTransitions(cpn);
		} catch (PNException e) {
			throw new PNSoundnessException("PN-Exception during soundness check: Cannot extract dead transitions.\nReason: " + e.getMessage());
		}
	}
	
	/**
	 * Checks the "option to complete" and "proper completion" soundness property for CPNs.<br>
	 * "Option to complete" requires that from each reachable marking m there is a
	 * reachable marking m' which is an end state of the CPN, i.e.<br>
	 * it contains the output place which in turn contains exactly one token.<br>
	 * "Proper completion" requires, that when an end state is reached, no transition can be fired anymore.<br>
	 * <br>
	 * Attention: Only works for bounded nets, i.e. nets for which the {@link AbstractPetriNet#isBounded()} method returns {@link Boundedness#BOUNDED}<br>
	 * If the boundedness property is set explicitly, the method may not halt.
	 * 
	 * @param cpn The basic CPN for operation
	 * @return <code>true</code> if the given CWN fulfills the property;<br>
	 * <code>false</code> otherwise. 
	 * @throws PNValidationException 
	 */
	private static <	P extends AbstractCPNPlace<F>,
	  				T extends AbstractCPNTransition<F>, 
	  				F extends AbstractCPNFlowRelation<P,T>, 
	  				M extends AbstractCPNMarking,
	  				X extends AbstractCPNMarkingGraphState<M>,
	   				Y extends AbstractCPNMarkingGraphRelation<M,X>,
	   				N extends AbstractCPN<P,T,F,M,X,Y>> 
	
	void checkValidCompletion(N cpn, String outputPlaceName) throws PNSoundnessException{
		Validate.notNull(cpn);
		Validate.notNull(outputPlaceName);
		if(!cpn.containsPlace(outputPlaceName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "CPN does not contain a place with name \"" + outputPlaceName + "\"");
	
		AbstractCPNMarkingGraph<M,X,Y> markingGraph = null;
		try {
			markingGraph = cpn.getMarkingGraph();
		} catch (PNException e) {
			throw new PNSoundnessException("Cannot build marking graph.\nReason: " + e.getMessage());
		}
		Set<AbstractCPNMarkingGraphState<M>> drains = new HashSet<AbstractCPNMarkingGraphState<M>>(markingGraph.getDrains());
		for(AbstractCPNMarkingGraphState<M> drainVertex : drains){
			try{
				checkEndStateProperty(cpn, drainVertex.getElement(), outputPlaceName);
			} catch(PNValidationException e){
				throw new PNSoundnessException("At least one drain in the marking graph of the given cwn is not a valid end state.\nDrain: " + drainVertex.getElement() + "\nReason: " + e.getMessage());
			}
		}
		Set<AbstractCPNMarkingGraphState<M>> otherVertexes = new HashSet<AbstractCPNMarkingGraphState<M>>(markingGraph.getVertices());
		otherVertexes.removeAll(drains);
		for(AbstractCPNMarkingGraphState<M> otherVertex : otherVertexes){			
			try {
				checkEndStateProperty(cpn, otherVertex.getElement(), outputPlaceName);
			} catch(PNValidationException e){
				throw new PNSoundnessException("At least one non-drain in the marking graph of the given cwn is an end state.\nNon-Drain: " + otherVertex.getElement() + "\nReason: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Checks if the given marking is an end state of the CPN, i.e.<br>
	 * it contains the output place which in turn contains exactly one token.
	 * 
	 * @param traversalMarking The marking of interest.
	 * @param cpn The corresponding CPN.
	 * @return <code>true</code> if the given marking is an end state;<br>
	 * <code>false</code> otherwise. 
	 * @throws PNValidationException 
	 */
	private static <P extends AbstractCPNPlace<F>,
					T extends AbstractCPNTransition<F>, 
					F extends AbstractCPNFlowRelation<P,T>, 
					M extends AbstractCPNMarking,
					X extends AbstractCPNMarkingGraphState<M>,
					Y extends AbstractCPNMarkingGraphRelation<M,X>,
					N extends AbstractCPN<P,T,F,M,X,Y>> 

	void checkEndStateProperty(N cpn, M traversalMarking, String outputPlaceName) throws PNValidationException{
		
		// Check Option to complete property
		if(!traversalMarking.contains(outputPlaceName))
			throw new PNValidationException("Marking does not contain tokens for output place \"" + outputPlaceName + "\"");
		
		String cfTokenColor = cpn.defaultTokenColor();
		if(AbstractIFNet.class.isAssignableFrom(cpn.getClass())){
			cfTokenColor = AbstractIFNet.CONTROL_FLOW_TOKEN_COLOR;
		}
		if(!traversalMarking.get(outputPlaceName).support().contains(cfTokenColor))
			throw new PNValidationException("Marking does not contain control flow token for output place \"" + outputPlaceName + "\"");
		
		// Check proper completion property
		for(P place: cpn.getPlaces()){
			if(place.getName().equals(outputPlaceName))
				continue;
			if(traversalMarking.contains(place.getName())){
				if(traversalMarking.get(place.getName()).support().contains(cfTokenColor))
					throw new PNValidationException("Remaining control flow token in place \"" + place.getName() + "\"");
			}
		}
	}

}
