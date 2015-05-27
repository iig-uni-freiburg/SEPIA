package de.uni.freiburg.iig.telematik.sepia.petrinet.properties;

import java.util.Collection;
import java.util.UUID;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.traverse.TraversalUtils;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;

public class PNProperties {

	/** temporary transition name with unique ID */
	private static final String CONNECTOR_NAME = "connector" + UUID.randomUUID().toString();

	public static 	<P extends AbstractPlace<F,S>, 
		 			 T extends AbstractTransition<F,S>, 
		 			 F extends AbstractFlowRelation<P,T,S>,
		 			 M extends AbstractMarking<S>,
		 			 S extends Object> 

		InOutPlaces validateInputOutputPlace(AbstractPetriNet<P,T,F,M,S> petriNet) throws PNValidationException {
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
	 				 S extends Object> 

	void validateStrongConnectedness(AbstractPetriNet<P,T,F,M,S> petriNet, InOutPlaces places) throws PNValidationException {
		validateStrongConnectednessOfShortCircuitedNet(petriNet, places.input, places.output);
	}

	public static 	<P extends AbstractPlace<F,S>, 
	 				 T extends AbstractTransition<F,S>, 
	 				 F extends AbstractFlowRelation<P,T,S>,
	 				 M extends AbstractMarking<S>,
	 				 S extends Object> 

	void validateStrongConnectednessOfShortCircuitedNet(AbstractPetriNet<P,T,F,M,S> petriNet, String inputPlaceName, String outputPlaceName) throws PNValidationException {
		Validate.notNull(petriNet);
		Validate.notNull(inputPlaceName);
		Validate.notNull(outputPlaceName);
		if(!petriNet.containsPlace(inputPlaceName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a place with name \""+inputPlaceName+"\"");
		if(!petriNet.containsPlace(outputPlaceName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a place with name \""+outputPlaceName+"\"");

		if (petriNet.getNetType() == NetType.IFNet) {
			@SuppressWarnings("rawtypes")
			AbstractIFNet ifnet = (AbstractIFNet) petriNet;
			if (ifnet.getAnalysisContext() != null) {
				ifnet.getAnalysisContext().getACModel().getContext().addActivity(CONNECTOR_NAME);
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

		if (petriNet.getNetType() == NetType.IFNet) {
			@SuppressWarnings("rawtypes")
			AbstractIFNet ifnet = (AbstractIFNet) petriNet;
			if (ifnet.getAnalysisContext() != null) {
				ifnet.getAnalysisContext().getACModel().getContext().removeActivity(CONNECTOR_NAME);
			}
		}
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
