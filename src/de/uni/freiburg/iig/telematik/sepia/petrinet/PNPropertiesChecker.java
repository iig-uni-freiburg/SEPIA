package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.util.Collection;
import java.util.UUID;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.traverse.TraversalUtils;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class PNPropertiesChecker {

	/** temporary transition name with unique ID */
	private static final String CONNECTOR_NAME = "connector" + UUID.randomUUID().toString();

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
			throw new PNValidationException("Net has no input place.");

		if (sourcePlaces.size() > 1)
			throw new PNValidationException("Net has more than one input place: " + sourcePlaces);
		input = sourcePlaces.iterator().next();

		P output = null;
		Collection<P> drainPlaces = petriNet.getDrainPlaces();
		if (drainPlaces.isEmpty())
			throw new PNValidationException("Net has no output place.");
		if (drainPlaces.size() > 1)
			throw new PNValidationException("Net has more than one output place: " + drainPlaces);
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

		if (petriNet instanceof IFNet) {
			IFNet ifnet = (IFNet) petriNet;
			if (ifnet.getAnalysisContext() != null) {
				ifnet.getAnalysisContext().getLabeling().getAnalysisContext().getACModel().getContext().addActivity(CONNECTOR_NAME);
			}
		}

		P input = petriNet.getPlace(inputPlaceName);
		P output = petriNet.getPlace(outputPlaceName);
		// Check connectedness of short-circuited net.
		petriNet.addSilentTransition(CONNECTOR_NAME, false);
		petriNet.addFlowRelationPT(output.getName(), CONNECTOR_NAME, false);
		petriNet.addFlowRelationTP(CONNECTOR_NAME, input.getName(), false);

		if (!TraversalUtils.isStronglyConnected(petriNet, input)) {
			throw new PNValidationException("Net is not strongly connected.");
		}
		petriNet.removeTransition(CONNECTOR_NAME, false);

		if (petriNet instanceof IFNet) {
			IFNet ifnet = (IFNet) petriNet;
			if (ifnet.getAnalysisContext() != null) {
				ifnet.getAnalysisContext().getLabeling().getAnalysisContext().getACModel().getContext().removeActivity(CONNECTOR_NAME);
			}
		}
	}
	
	public static <P extends AbstractPlace<F,S>, 
	 			   T extends AbstractTransition<F,S>, 
	 			   F extends AbstractFlowRelation<P,T,S>,
	 			   M extends AbstractMarking<S>,
	 			   S extends Object,
	 			   X extends AbstractMarkingGraphState<M,S>, 
	 			   Y extends AbstractMarkingGraphRelation<M,X,S>,
	 			   N extends AbstractPetriNet<P,T,F,M,S,X,Y>> 

	void validateBoundedness(N petriNet) throws PNValidationException {
		if (petriNet.getBoundedness() == Boundedness.UNKNOWN) {
			try {
				petriNet.checkBoundedness();
			} catch (PNException e) {
				throw new PNValidationException("Cannot check boundedness of net.\nReason: " + e.getMessage());
			}
		}
		if (!petriNet.isBounded())
			throw new PNValidationException("Net is not bounded.");
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
