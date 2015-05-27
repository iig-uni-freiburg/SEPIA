package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.validity;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public abstract class PNValidity {
	
	/**
	 * Checks if the Petri net is valid.<br>
	 * Validity of a net relates to basic structural properties.<br>
	 * This method defines the default Petri net validity which requires all net places and transitions to be valid.<br>
	 * 
	 * @see #checkPlaceValidity(AbstractPetriNet)
	 * @see #checkTransitionValidity(AbstractPetriNet)
	 * @throws PNValidationException
	 */
	protected static <P extends AbstractPlace<F,S>, 
	  			   	  T extends AbstractTransition<F,S>, 
	  			   	  F extends AbstractFlowRelation<P,T,S>,
	  			   	  M extends AbstractMarking<S>,
	  			   	  S extends Object>
	
		void defaultValidity(AbstractPetriNet<P,T,F,M,S> petriNet) 
				throws PNValidationException{
		checkPlaceValidity(petriNet);
		checkTransitionValidity(petriNet);
	}

	/**
	 * Checks if all places of a Petri net are valid.<br>
	 * 
	 * @see AbstractPlace#checkValidity()
	 * @throws PNValidationException
	 */
	protected static <P extends AbstractPlace<F,S>, 
	   				  T extends AbstractTransition<F,S>, 
	   				  F extends AbstractFlowRelation<P,T,S>,
	   				  M extends AbstractMarking<S>,
	   				  S extends Object>

		void checkPlaceValidity(AbstractPetriNet<P,T,F,M,S> petriNet) 
				throws PNValidationException{
		for(P place: petriNet.getPlaces()){
			place.checkValidity();
		}
	}
	
	/**
	 * Checks if all transitions of a Petri net are valid.<br>
	 * 
	 * @see AbstractTransition#checkValidity()
	 * @throws PNValidationException
	 */
	protected static <P extends AbstractPlace<F,S>, 
		  			  T extends AbstractTransition<F,S>, 
		  			  F extends AbstractFlowRelation<P,T,S>,
		  			  M extends AbstractMarking<S>,
		  			  S extends Object>

		void checkTransitionValidity(AbstractPetriNet<P,T,F,M,S> petriNet) 
				throws PNValidationException{
		for(T transition: petriNet.getTransitions()){
			transition.checkValidity();
		}
	}

}
