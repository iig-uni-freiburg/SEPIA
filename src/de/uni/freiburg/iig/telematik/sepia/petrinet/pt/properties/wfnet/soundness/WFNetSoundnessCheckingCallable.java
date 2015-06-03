package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.soundness;

import java.util.HashSet;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PNProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.PropertyCheckingResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheckGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheckResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.ThreadedBoundednessChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead.DeadTransitionCheckCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead.DeadTransitionCheckException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead.DeadTransitionCheckResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead.DeadTransitionCheckingCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.ThreadedMGCalculator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractPNPropertyCheckerCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.WFNetProperties;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.structure.WFNetStructureCheckingCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.wfnet.structure.WFNetStructureCheckingCallableGenerator;

public class WFNetSoundnessCheckingCallable<P extends AbstractPTPlace<F>,
								 		  T extends AbstractPTTransition<F>, 
								 		  F extends AbstractPTFlowRelation<P,T>, 
								 		  M extends AbstractPTMarking>  extends AbstractPNPropertyCheckerCallable<P,T,F,M,Integer,WFNetProperties> {
		
	public WFNetSoundnessCheckingCallable(WFNetSoundnessCheckingCallableGenerator<P,T,F,M> generator){
		super(generator);
	}
	
	@Override
	protected WFNetSoundnessCheckingCallableGenerator<P,T,F,M> getGenerator() {
		return (WFNetSoundnessCheckingCallableGenerator<P,T,F,M>) super.getGenerator();
	}

	@Override
	public WFNetProperties callRoutine() throws WFNetException, InterruptedException {
		WFNetProperties result = null;
		try {
			if (getGenerator().isCheckCWNStructure()) {
				try{
					WFNetStructureCheckingCallableGenerator<P,T,F,M> generator = new WFNetStructureCheckingCallableGenerator<P,T,F,M>(getGenerator().getPetriNet());
					WFNetStructureCheckingCallable<P,T,F,M> structureCheckCallable = new WFNetStructureCheckingCallable<P,T,F,M>(generator);
					result = structureCheckCallable.callRoutine();
				} catch(WFNetException e){
					e.getProperties().isSoundWFNet = PropertyCheckingResult.FALSE;
					throw new WFNetException("Exception during cwn structure check.", e, e.getProperties());
				}
			} else {
				result = new WFNetProperties();
				try {
					result.inOutPlaces = PNProperties.validateInputOutputPlace(getGenerator().getPetriNet());
					result.validInOutPlaces = PropertyCheckingResult.TRUE;
				} catch(PNValidationException e){
					result.validInOutPlaces = PropertyCheckingResult.FALSE;
					result.isSoundWFNet = PropertyCheckingResult.FALSE;
					result.exception = e;
					throw new WFNetException("Exception during input/output place check.", e, result);
				}
			}
			result.isSoundWFNet = PropertyCheckingResult.FALSE;
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			if(getGenerator().isCheckBoundedness()){
				if(getGenerator().getMarkingGraph() != null){
					result.isBounded = PropertyCheckingResult.TRUE;
				} else {
					BoundednessCheckGenerator<P,T,F,M,Integer> generator = new BoundednessCheckGenerator<P,T,F,M,Integer>(getGenerator().getPetriNet());
					ThreadedBoundednessChecker<P,T,F,M,Integer> checker = new ThreadedBoundednessChecker<P,T,F,M,Integer>(generator);
					checker.runCalculation();
					
					BoundednessCheckResult<P,T,F,M,Integer> boundednessCheckResult = null;
					try{
						boundednessCheckResult = checker.getResult();
					} catch (BoundednessException e) {
						throw new WFNetException("Exception during boundedness check.", e, result);
					}
					switch(boundednessCheckResult.getBoundedness()){
					case BOUNDED:
						result.isBounded = PropertyCheckingResult.TRUE;
						break;
					case UNBOUNDED:
						result.isBounded = PropertyCheckingResult.FALSE;
						throw new WFNetException("Net is unbounded.", result);
					default:
						result.isBounded = PropertyCheckingResult.UNKNOWN;
						throw new WFNetException("Unknown boundedness of net, calculation cancelled?.", result);
					}
					getGenerator().setMarkingGraph(boundednessCheckResult.getMarkingGraph());
				}
			}
			result.markingGraph = getGenerator().getMarkingGraph();
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			// Requirement 1: Option to complete + proper completion
			try {
				checkValidCompletion(result.inOutPlaces.getOutput());
				result.optionToCompleteAndProperCompletion = PropertyCheckingResult.TRUE;
			} catch (PNValidationException e) {
				result.optionToCompleteAndProperCompletion = PropertyCheckingResult.FALSE;
				result.exception = e;
				throw new WFNetException("Exception during option to complete and proper completion check.", e, result);
			}
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			
			// Requirement 2: No dead transitions
			DeadTransitionCheckCallableGenerator<P,T,F,M,Integer> generator = new DeadTransitionCheckCallableGenerator<P,T,F,M,Integer>(getGenerator().getPetriNet());
			if(getGenerator().getMarkingGraph() != null)
				generator.setMarkingGraph(getGenerator().getMarkingGraph());
			
			DeadTransitionCheckingCallable<P,T,F,M,Integer> deadCallable = new DeadTransitionCheckingCallable<P,T,F,M,Integer>(generator);
			DeadTransitionCheckResult deadTransitionCheckResult = null;
			try {
				deadTransitionCheckResult = deadCallable.callRoutine();
			} catch(DeadTransitionCheckException e){
				result.noDeadTransitions = PropertyCheckingResult.FALSE;
				throw new WFNetException("Exception during dead transition check.", e, result);
			}
			
			result.noDeadTransitions = deadTransitionCheckResult.existDeadTransitions() ? PropertyCheckingResult.FALSE : PropertyCheckingResult.TRUE;
			
			result.isSoundWFNet = PropertyCheckingResult.TRUE;

		} catch(InterruptedException e){
			throw e;
		} catch (Exception e) {
			if(e instanceof WFNetException)
				throw e;
			throw new WFNetException("Exception during cwn property checks.<br>Reason: " + e.getMessage(), e, result);
		}
		return result;
	}
	
	private void checkValidCompletion(String outputPlaceName) throws PNValidationException, InterruptedException {
		if (!getGenerator().getPetriNet().containsPlace(outputPlaceName))
			throw new PNValidationException("CPN does not contain a place with name \"" + outputPlaceName + "\"");

		if(getGenerator().getMarkingGraph() == null){
			ThreadedMGCalculator<P,T,F,M,Integer> checker = new ThreadedMGCalculator<P,T,F,M,Integer>(getGenerator().getPetriNet());
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
		
		Set<AbstractMarkingGraphState<M,Integer>> drains = new HashSet<AbstractMarkingGraphState<M,Integer>>(getGenerator().getMarkingGraph().getDrains());
		for (AbstractMarkingGraphState<M,Integer> drainVertex : drains) {
			checkEndStateProperty(drainVertex.getElement(), outputPlaceName);
		}
		Set<AbstractMarkingGraphState<M,Integer>> otherVertexes = new HashSet<AbstractMarkingGraphState<M,Integer>>(getGenerator().getMarkingGraph().getVertices());
		otherVertexes.removeAll(drains);
		for (AbstractMarkingGraphState<M,Integer> otherVertex : otherVertexes) {
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
			throw new PNValidationException("Final marking \"" + traversalMarking + "\" does not contain output place \"" + outputPlaceName + "\"");
		}
		
		if(traversalMarking.get(outputPlaceName) == 0)
			throw new PNValidationException("Final marking \"" + traversalMarking + "\" does not contain token in output place \"" + outputPlaceName + "\"");
		
		if(traversalMarking.get(outputPlaceName) > 1)
			throw new PNValidationException("Final marking \"" + traversalMarking + "\" does contain more than one token in output place \"" + outputPlaceName + "\"");
		
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
		
		boolean checkRemainingCFTokens = true;
		if(getGenerator().containsPropertyFlags()){
			for(WFNetSoundnessPropertyFlag flag: getGenerator().getPropertyFlags()){
				if(flag == WFNetSoundnessPropertyFlag.ACCEPT_REMAINING_TOKENS){
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
				if(traversalMarking.get(place.getName()) > 0)
					throw new PNValidationException("Remaining control flow token in place \"" + place.getName() + "\"");
			}
			
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
		}
		
	}

}
