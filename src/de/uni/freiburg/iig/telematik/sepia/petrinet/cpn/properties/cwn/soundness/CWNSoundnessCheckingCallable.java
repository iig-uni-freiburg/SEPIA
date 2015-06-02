package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.soundness;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure.CWNStructureCheckingCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure.CWNStructureCheckingCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.structure.CWNStructureProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheckGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheckResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.ThreadedBoundednessChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead.DeadTransitionCheckCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead.DeadTransitionCheckResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead.ThreadedDeadTransitionsChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.ThreadedMGCalculator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractPNPropertyCheckerCallable;

public class CWNSoundnessCheckingCallable<P extends AbstractCPNPlace<F>,
								 T extends AbstractCPNTransition<F>, 
								 F extends AbstractCPNFlowRelation<P,T>, 
								 M extends AbstractCPNMarking>  extends AbstractPNPropertyCheckerCallable<P,T,F,M,Multiset<String>,CWNSoundnessProperties> {
		
	public CWNSoundnessCheckingCallable(CWNSoundnessCheckingCallableGenerator<P,T,F,M> generator){
		super(generator);
	}
	
	@Override
	protected CWNSoundnessCheckingCallableGenerator<P,T,F,M> getGenerator() {
		return (CWNSoundnessCheckingCallableGenerator<P,T,F,M>) super.getGenerator();
	}

	@Override
	public CWNSoundnessProperties callRoutine() throws CWNException, InterruptedException {
		CWNSoundnessProperties result = null;
		try {
			if (getGenerator().isCheckCWNStructure()) {
				CWNStructureProperties structureProperties = null;
				try {
					CWNStructureCheckingCallableGenerator<P,T,F,M> generator = new CWNStructureCheckingCallableGenerator<P,T,F,M>(getGenerator().getPetriNet());
					CWNStructureCheckingCallable<P,T,F,M> structureCheckCallable = new CWNStructureCheckingCallable<P,T,F,M>(generator);
					structureProperties = structureCheckCallable.callRoutine();
				} catch(InterruptedException e){
					throw e;
				}
				if(structureProperties.exception != null)
					throw new CWNException("Exception during cwn structure check", structureProperties.exception, new CWNSoundnessProperties(structureProperties));
			} else {
				result = new CWNSoundnessProperties();
				try {
					result.inOutPlaces = PNProperties.validateInputOutputPlace(getGenerator().getPetriNet());
					result.validInOutPlaces = PropertyCheckingResult.TRUE;
				} catch(PNValidationException e){
					result.validInOutPlaces = PropertyCheckingResult.FALSE;
					result.exception = e;
					throw new CWNException("Exception during inout/output place check", result.exception, result);
				}
			}
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			if(getGenerator().isCheckBoundedness()){
				if(getGenerator().getMarkingGraph() != null){
					result.isBounded = PropertyCheckingResult.TRUE;
				} else {
					BoundednessCheckGenerator<P,T,F,M,Multiset<String>> generator = new BoundednessCheckGenerator<P,T,F,M,Multiset<String>>(getGenerator().getPetriNet());
					ThreadedBoundednessChecker<P,T,F,M,Multiset<String>> checker = new ThreadedBoundednessChecker<P,T,F,M,Multiset<String>>(generator);
					checker.runCalculation();
					
					BoundednessCheckResult<P,T,F,M,Multiset<String>> boundednessCheckResult = null;
					try{
						boundednessCheckResult = checker.getResult();
					} catch (BoundednessException e) {
						throw new CWNException("Exception during boundedness check.\nReason: " + e.getMessage(), e, result);
					}
					switch(boundednessCheckResult.getBoundedness()){
					case BOUNDED:
						result.isBounded = PropertyCheckingResult.TRUE;
						break;
					case UNBOUNDED:
						result.isBounded = PropertyCheckingResult.FALSE;
						throw new CWNException("Net is not bounded.", result);
					default:
						result.isBounded = PropertyCheckingResult.UNKNOWN;
						throw new CWNException("Unknown boundedness of net, calculation cancelled?.", result);
					}
					getGenerator().setMarkingGraph(boundednessCheckResult.getMarkingGraph());
				}
			}
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			// Requirement 1: Option to complete + proper completion
			try {
				checkValidCompletion(result.inOutPlaces.getOutput());
				result.optionToCompleteAndProperCompletion = PropertyCheckingResult.TRUE;
			} catch (PNValidationException e1) {
				result.optionToCompleteAndProperCompletion = PropertyCheckingResult.FALSE;
				result.exception = e1;
				throw new CWNException("Exception during option to complete and valid completion check.\nReason: " + e1.getMessage(),  e1, result);
			}
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			// Requirement 2: No dead transitions
			DeadTransitionCheckCallableGenerator<P,T,F,M,Multiset<String>> generator = new DeadTransitionCheckCallableGenerator<P,T,F,M,Multiset<String>>(getGenerator().getPetriNet());
			if(getGenerator().getMarkingGraph() != null)
				generator.setMarkingGraph(getGenerator().getMarkingGraph());
			ThreadedDeadTransitionsChecker<P,T,F,M,Multiset<String>> checker = new ThreadedDeadTransitionsChecker<P,T,F,M,Multiset<String>>(generator);
			checker.runCalculation();
			
			DeadTransitionCheckResult deadTransitionCheckResult = null;
			try{
				deadTransitionCheckResult = checker.getResult();
			} catch (Exception e) {
				throw new CWNException("Exception during dead transition check.\nReason: " + e.getMessage(), e, result);
			}
			
			if(!deadTransitionCheckResult.existDeadTransitions()){
				result.noDeadTransitions = PropertyCheckingResult.TRUE;
			} else {
				result.noDeadTransitions = PropertyCheckingResult.FALSE;
				throw new CWNException("Net contains dead transitions", result);
			}

		} catch(InterruptedException e){
			throw e;
		} catch (Exception e) {
			throw new CWNException("Exception during cwn property checks.<br>Reason: " + e.getMessage(), e, result);
		}
		return result;
	}
	
	private void checkValidCompletion(String outputPlaceName) throws PNValidationException, InterruptedException {
		if (!getGenerator().getPetriNet().containsPlace(outputPlaceName))
			throw new PNValidationException("CPN does not contain a place with name \"" + outputPlaceName + "\"");

		if(getGenerator().getMarkingGraph() == null){
			ThreadedMGCalculator<P,T,F,M,Multiset<String>> checker = new ThreadedMGCalculator<P,T,F,M,Multiset<String>>(getGenerator().getPetriNet());
			checker.runCalculation();
			try{
				getGenerator().setMarkingGraph(checker.getMarkingGraph());
			} catch (Exception e) {
				throw new PNValidationException("Exception during marking graph calculation.\nReason: " + e.getMessage(), e);
			}
		}
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		Set<AbstractMarkingGraphState<M,Multiset<String>>> drains = new HashSet<AbstractMarkingGraphState<M,Multiset<String>>>(getGenerator().getMarkingGraph().getDrains());
		for (AbstractMarkingGraphState<M,Multiset<String>> drainVertex : drains) {
			checkEndStateProperty(drainVertex.getElement(), outputPlaceName);
		}
		Set<AbstractMarkingGraphState<M,Multiset<String>>> otherVertexes = new HashSet<AbstractMarkingGraphState<M,Multiset<String>>>(getGenerator().getMarkingGraph().getVertices());
		otherVertexes.removeAll(drains);
		for (AbstractMarkingGraphState<M,Multiset<String>> otherVertex : otherVertexes) {
			boolean throwException = false;
			try {
				checkEndStateProperty(otherVertex.getElement(), outputPlaceName);
				throwException = true; // only reached if non-drain has end state properties
			} catch (PNValidationException e) {
				// must throw exception since non-drains shouldn't have end-state properties
			}
			if (throwException) {
				throw new PNValidationException("Final marking \"" + otherVertex.getElement() + "\" contains tokens for non-output place \"" + outputPlaceName + "\"");
			}
		}
	}
	
	void checkEndStateProperty(M traversalMarking, String outputPlaceName) throws PNValidationException, InterruptedException{
		// Check Option to complete property
		if(!traversalMarking.contains(outputPlaceName)) {
			throw new PNValidationException("Final marking \"" + traversalMarking + "\" does not contain tokens for output place \"" + outputPlaceName + "\"");
		}
		
		String cfTokenColor = getGenerator().getPetriNet().defaultTokenColor();
		if(AbstractIFNet.class.isAssignableFrom(getGenerator().getPetriNet().getClass())){
			cfTokenColor = AbstractIFNet.CONTROL_FLOW_TOKEN_COLOR;
		}
		if(!traversalMarking.get(outputPlaceName).support().contains(cfTokenColor))
			throw new PNValidationException("Final marking \"" + traversalMarking + "\" does not contain control flow token for output place \"" + outputPlaceName + "\"");
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		boolean checkRemainingCFTokens = true;
		if(getGenerator().containsPropertyFlags()){
			for(CWNPropertyFlag flag: getGenerator().getPropertyFlags()){
				if(flag == CWNPropertyFlag.ACCEPT_REMAINING_CF_TOKENS){
					checkRemainingCFTokens = false;
				}
			}
		}
		if(!checkRemainingCFTokens)
			return;
		
		// Check proper completion property
		for(P place: getGenerator().getPetriNet().getPlaces()){
			if(place.getName().equals(outputPlaceName))
				continue;
			if(traversalMarking.contains(place.getName())){
				if(traversalMarking.get(place.getName()).support().contains(cfTokenColor))
					throw new PNValidationException("Remaining control flow token in place \"" + place.getName() + "\"");
			}
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
		}
		
	}

}
