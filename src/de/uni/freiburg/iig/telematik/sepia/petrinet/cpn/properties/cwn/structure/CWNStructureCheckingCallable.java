package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractPNPropertyCheckerCallable;

public class CWNStructureCheckingCallable<P extends AbstractCPNPlace<F>,
								 T extends AbstractCPNTransition<F>, 
								 F extends AbstractCPNFlowRelation<P,T>, 
								 M extends AbstractCPNMarking>  extends AbstractPNPropertyCheckerCallable<P,T,F,M,Multiset<String>,CWNProperties> {
		
	public CWNStructureCheckingCallable(CWNStructureCheckingCallableGenerator<P,T,F,M> generator){
		super(generator);
	}
	
	@Override
	protected CWNStructureCheckingCallableGenerator<P,T,F,M> getGenerator() {
		return (CWNStructureCheckingCallableGenerator<P,T,F,M>) super.getGenerator();
	}

	@Override
	public CWNProperties callRoutine() throws CWNException, InterruptedException {
		CWNProperties result = new CWNProperties();
		result.hasCWNStructure = PropertyCheckingResult.FALSE;
		try {
			// Check if there is only one input/output place
			try {
				result.inOutPlaces = PNProperties.validateInputOutputPlace(getGenerator().getPetriNet());
			} catch (PNValidationException e) {
				result.exception = e;
				result.validInOutPlaces = PropertyCheckingResult.FALSE;
				throw new CWNException("Exception while checking input/output place property", e, result);
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
				throw new CWNException("Exception while checking strong connectedness", e, result);
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
				throw new CWNException("Exception while checking initial marking", e, result);
			}
			result.validInitialMarking = PropertyCheckingResult.TRUE;
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}

			// Check control flow dependency
			try {
				String CFTokenColor = getGenerator().getPetriNet().defaultTokenColor();
				if (AbstractIFNet.class.isAssignableFrom(getGenerator().getPetriNet().getClass())) {
					CFTokenColor = AbstractIFNet.CONTROL_FLOW_TOKEN_COLOR;
				}
				for (T transition : getGenerator().getPetriNet().getTransitions()) {
					
					if (Thread.currentThread().isInterrupted()) {
						throw new InterruptedException();
					}
					
					if (!transition.getConsumedColors().contains(CFTokenColor))
						throw new PNValidationException("Transition \"" + transition + "\" does not consume control flow token");
					if (!transition.getProducedColors().contains(CFTokenColor))
						throw new PNValidationException("Transition \"" + transition + "\" does not produce control flow token");
				}
			} catch (PNValidationException e) {
				result.exception = e;
				result.controlFlowDependency = PropertyCheckingResult.FALSE;
				throw new CWNException("Exception while checking control flow dependency", e, result);
			}
			result.controlFlowDependency = PropertyCheckingResult.TRUE;
			
			result.hasCWNStructure = PropertyCheckingResult.TRUE;

		} catch(InterruptedException e){
			throw e;
		} catch (Exception e) {
			if(e instanceof CWNException)
				throw e;
			throw new CWNException("Exception during cwn property checks.<br>Reason: " + e.getMessage(), e, result);
		}
		return result;
	}
	
	private void checkSingleCFTokenInInitialMarking(String placeName) throws PNValidationException{
		if(!getGenerator().getPetriNet().containsPlace(placeName))
			throw new PNValidationException("Petri net does not contain place " + placeName);
		
		P place = getGenerator().getPetriNet().getPlace(placeName);
		String cfTokenColor = getGenerator().getPetriNet().defaultTokenColor();
		if(AbstractIFNet.class.isAssignableFrom(getGenerator().getPetriNet().getClass())){
			cfTokenColor = AbstractIFNet.CONTROL_FLOW_TOKEN_COLOR;
		}
		M initialMarking = getGenerator().getPetriNet().getInitialMarking();
		if(!initialMarking.contains(place.getName()))
			throw new PNValidationException("Initial marking must contain input place " + place);
		if(initialMarking.places().size() > 1)
			throw new PNValidationException("Initial marking must only contain input place " + place);
	
		Multiset<String> tokensInitialPlace = initialMarking.get(place.getName());
		if(!tokensInitialPlace.contains(cfTokenColor))
			throw new PNValidationException("Initial marking must contain at least one control flow token for input place " + place);
		if(tokensInitialPlace.support().size() > 1)
			throw new PNValidationException("Initial marking must contain exactly one control flow token for input place " + place);
	}

}
