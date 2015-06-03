package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.structure;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractPNPropertyCheckerCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;

public class WFNetStructureCheckingCallable<P extends AbstractPTPlace<F>,
								 			T extends AbstractPTTransition<F>, 
								 			F extends AbstractPTFlowRelation<P,T>, 
								 			M extends AbstractPTMarking>  extends AbstractPNPropertyCheckerCallable<P,T,F,M,Integer,WFNetProperties> {
		
	public WFNetStructureCheckingCallable(WFNetStructureCheckingCallableGenerator<P,T,F,M> generator){
		super(generator);
	}
	
	@Override
	protected WFNetStructureCheckingCallableGenerator<P,T,F,M> getGenerator() {
		return (WFNetStructureCheckingCallableGenerator<P,T,F,M>) super.getGenerator();
	}

	@Override
	public WFNetProperties callRoutine() throws WFNetException, InterruptedException {
		WFNetProperties result = new WFNetProperties();
		result.hasWFNetStructure = PropertyCheckingResult.FALSE;
		try {
			// Check if there is only one input/output place
			try {
				result.inOutPlaces = PNProperties.validateInputOutputPlace(getGenerator().getPetriNet());
			} catch (PNValidationException e) {
				result.exception = e;
				result.validInOutPlaces = PropertyCheckingResult.FALSE;
				throw new WFNetException("Exception while checking input/output place property", e, result);
			}
			P input = getGenerator().getPetriNet().getPlace(result.inOutPlaces.getInput());
			result.validInOutPlaces = PropertyCheckingResult.TRUE;
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}

			// Check strongly connectedness of short-circuited net
			try {
				PNProperties.validateStrongConnectedness(getGenerator().getPetriNet(), result.inOutPlaces);
			} catch (PNValidationException e) {
				result.exception = e;
				result.strongConnectedness = PropertyCheckingResult.FALSE;
				throw new WFNetException("Exception while checking strong connectedness", e, result);
			}
			result.strongConnectedness = PropertyCheckingResult.TRUE;
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}

			// Check initial marking
			try {
				checkSingleCFTokenInInitialMarking(input.getName());
			} catch (PNValidationException e) {
				result.exception = e;
				result.validInitialMarking = PropertyCheckingResult.FALSE;
				throw new WFNetException("Exception while checking initial marking", e, result);
			}
			result.validInitialMarking = PropertyCheckingResult.TRUE;
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			result.hasWFNetStructure = PropertyCheckingResult.TRUE;

		} catch(InterruptedException e){
			throw e;
		} catch (Exception e) {
			throw new WFNetException("Exception during cwn property checks.<br>Reason: " + e.getMessage(), e, result);
		}
		return result;
	}
	
	private void checkSingleCFTokenInInitialMarking(String placeName) throws PNValidationException{
		if(!getGenerator().getPetriNet().containsPlace(placeName))
			throw new PNValidationException("Petri net does not contain place " + placeName);
		
		M initialMarking = getGenerator().getPetriNet().getInitialMarking();
		if(!initialMarking.contains(placeName))
			throw new PNValidationException("Initial marking must contain input place " + placeName);
		if(initialMarking.places().size() > 1)
			throw new PNValidationException("Initial marking must only contain input place " + placeName);
	
		if(initialMarking.get(placeName) == 0)
			throw new PNValidationException("Initial marking must contain one token for input place " + placeName);
		if(initialMarking.get(placeName) > 1)
			throw new PNValidationException("Initial marking must contain exactly one token for input place " + placeName);
	}

}
