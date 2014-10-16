package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.traverse.TraversalUtils;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;

public class PNPropertiesChecker {
	
	private static final String CONNECTOR_NAME = "connector";

	public static 	<P extends AbstractPlace<F,S>, 
		 			 T extends AbstractTransition<F,S>, 
		 			 F extends AbstractFlowRelation<P,T,S>,
		 			 M extends AbstractMarking<S>,
		 			 S extends Object,
		 			 X extends AbstractMarkingGraphState<M,S>, 
		 			 Y extends AbstractMarkingGraphRelation<M,X,S>,
		 			 N extends AbstractPetriNet<P,T,F,M,S,X,Y>> 

		InOutPlaces validateInputOutputPlace(N petriNet) throws PNValidationException {
		Validate.notNull(petriNet);

		// Check if there is only one input/output place
		P input = null;
		Collection<P> sourcePlaces = petriNet.getSourcePlaces();

		if (sourcePlaces.isEmpty())
			throw new PNValidationException("CWN has no input place.");

		if (sourcePlaces.size() > 1)
			throw new PNValidationException("CWN has more than one input place: " + sourcePlaces);
		input = sourcePlaces.iterator().next();

		P output = null;
		Collection<P> drainPlaces = petriNet.getDrainPlaces();
		if (drainPlaces.isEmpty())
			throw new PNValidationException("CWN has no output place.");
		if (drainPlaces.size() > 1)
			throw new PNValidationException("CWN has more than one output place: " + drainPlaces);
		output = drainPlaces.iterator().next();
		
		return new InOutPlaces(input.getName(), output.getName());
	}
	
	public static 	<P extends AbstractPlace<F,S>, 
	 				 T extends AbstractTransition<F,S>, 
	 				 F extends AbstractFlowRelation<P,T,S>,
	 				 M extends AbstractMarking<S>,
	 				 S extends Object,
	 				 X extends AbstractMarkingGraphState<M,S>, 
	 				 Y extends AbstractMarkingGraphRelation<M,X,S>,
	 				 N extends AbstractPetriNet<P,T,F,M,S,X,Y>> 

	void validateStrongConnectedness(N petriNet, InOutPlaces places) throws PNValidationException {
		validateStrongConnectednessOfShortCircuitedNet(petriNet, places.input, places.output);
	}

	public static 	<P extends AbstractPlace<F,S>, 
	 				 T extends AbstractTransition<F,S>, 
	 				 F extends AbstractFlowRelation<P,T,S>,
	 				 M extends AbstractMarking<S>,
	 				 S extends Object,
	 				 X extends AbstractMarkingGraphState<M,S>, 
	 				 Y extends AbstractMarkingGraphRelation<M,X,S>,
	 				 N extends AbstractPetriNet<P,T,F,M,S,X,Y>> 

	void validateStrongConnectednessOfShortCircuitedNet(N petriNet, String inputPlaceName, String outputPlaceName) throws PNValidationException {
		Validate.notNull(petriNet);
		Validate.notNull(inputPlaceName);
		Validate.notNull(outputPlaceName);
		if(!petriNet.containsPlace(inputPlaceName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a place with name \""+inputPlaceName+"\"");
		if(!petriNet.containsPlace(outputPlaceName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a place with name \""+outputPlaceName+"\"");
		
		P input = petriNet.getPlace(inputPlaceName);
		P output = petriNet.getPlace(outputPlaceName);
		// Check connectedness of short-circuited net.
		petriNet.addTransition(CONNECTOR_NAME, true);
		petriNet.addFlowRelationPT(output.getName(), CONNECTOR_NAME);
		petriNet.addFlowRelationTP(CONNECTOR_NAME, input.getName());

		if (!TraversalUtils.isStronglyConnected(petriNet, input)) {
			throw new PNValidationException("CWN is not strongly connected.");
		}
		petriNet.removeTransition(CONNECTOR_NAME);

		if (petriNet.isBounded())
			throw new PNValidationException("CWN is not bounded.");
	}
	
	
	public static class InOutPlaces {
		private String input = null;
		private String output = null;
		
		public InOutPlaces(String input, String output) {
			super();
			this.input = input;
			this.output = output;
		}
		
		public String getInput() {
			return input;
		}
		public String getOutput() {
			return output;
		}
		
	}

}
