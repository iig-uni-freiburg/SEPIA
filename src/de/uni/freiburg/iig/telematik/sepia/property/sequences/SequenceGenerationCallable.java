package de.uni.freiburg.iig.telematik.sepia.property.sequences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.misc.ListUtils;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.traverse.TraversalUtils;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.StateNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractPNPropertyCheckerCallable;
import de.uni.freiburg.iig.telematik.sepia.property.mg.MGConstructorCallable;
import de.uni.freiburg.iig.telematik.sepia.property.mg.MGConstructorCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.property.mg.MarkingGraphException;
import de.uni.freiburg.iig.telematik.sepia.property.mg.StateSpaceException;

public class SequenceGenerationCallable< P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										X extends AbstractMarkingGraphState<M,S>,
										Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractPNPropertyCheckerCallable<P,T,F,M,S,X,Y,MGTraversalResult> {

	public SequenceGenerationCallable(SequenceGenerationCallableGenerator<P,T,F,M,S,X,Y> generator) {
		super(generator);
	}
	
	@Override
	protected SequenceGenerationCallableGenerator<P,T,F,M,S,X,Y> getGenerator() {
		return (SequenceGenerationCallableGenerator<P,T,F,M,S,X,Y>) super.getGenerator();
	}

	@Override
	public MGTraversalResult callRoutine() throws SequenceGenerationException, InterruptedException {
		// Check if marking graph is available and construct it in case it is not
		if(getGenerator().getMarkingGraph() == null){
			MGConstructorCallableGenerator<P,T,F,M,S,X,Y> generator = new MGConstructorCallableGenerator<P,T,F,M,S,X,Y>(getGenerator().getPetriNet());
			MGConstructorCallable<P,T,F,M,S,X,Y> mgConstructionCallable = new MGConstructorCallable<P,T,F,M,S,X,Y>(generator);
			try {
				getGenerator().setMarkingGraph(mgConstructionCallable.callRoutine());
			} catch(MarkingGraphException e){
				// Abort when Petri net is unbounded
				if(e.getCause() != null && e.getCause() instanceof StateSpaceException){
					throw new SequenceGenerationException("Cannot generate sequences of unbounded net", e);
				}
				throw new SequenceGenerationException(e);
			}
		}
		
		// check whether marking graph contains cycles
		if(TraversalUtils.hasCycle(getGenerator().getMarkingGraph()))
			throw new SequenceGenerationException("Cannot generate sequences of Petri net whose markign graph contains cycles");
		
		M actualMarking = (M) getGenerator().getPetriNet().getMarking().clone();
		Set<List<String>> sequences = new HashSet<List<String>>();
		Set<List<String>> completeSequences = new HashSet<List<String>>();
		
		try {
			for (List<X> detSequence : getStateSequences()) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				Set<List<String>> activitySequences = getActivitySequences(detSequence);
				for (List<String> activitySequence : activitySequences) {
					if (Thread.currentThread().isInterrupted()) {
						throw new InterruptedException();
					}
					sequences.addAll(getSequences(activitySequence));
					if (getGenerator().getMarkingGraph().isEndState(detSequence.get(detSequence.size() - 1).getName())) {
						completeSequences.add(activitySequence);
					}
				}
			}

		} catch (InterruptedException e) {
			throw e;
		} catch (Exception e) {
			throw new SequenceGenerationException("Exception during sequence generation.<br>Reason: " + e.getMessage());
		}
		
		if(!getGenerator().isIncludeSilentTransitions()){
			sequences = removeSilentTransitions(sequences);	
			completeSequences = removeSilentTransitions(completeSequences);	
		}
		getGenerator().getPetriNet().setMarking(actualMarking);
		return new MGTraversalResult(sequences, completeSequences);
	}
	
	private List<List<X>> getStateSequences() throws InterruptedException{
		List<List<X>> stateSequences = new ArrayList<List<X>>();
		for(List<X> continuation: getContinuationsRec(getGenerator().getMarkingGraph().getInitialState())){
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			List<X> newContinuation = new ArrayList<X>();
			newContinuation.add(getGenerator().getMarkingGraph().getInitialState());
			newContinuation.addAll(continuation);
			stateSequences.add(newContinuation);
		}
		return stateSequences;
	}
	
	private List<List<String>> getSequences(List<String> completeSequence) throws InterruptedException{
		List<List<String>> result = new ArrayList<List<String>>();
		
		if(completeSequence.size() == 1){
			result.add(completeSequence);
		} else {
			for(int i=0; i<completeSequence.size(); i++){
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				result.add(ListUtils.copyOfRange(completeSequence, 0, i));
			}
		}
		
		return result;
	}
	
	private Set<List<String>> getActivitySequences(List<X> stateList) throws InterruptedException {
		return getActivitySequencesRec(new HashSet<List<String>>(), stateList, 0);
	}
	
	private List<List<X>> getContinuationsRec(X actualState) throws InterruptedException{
//		System.out.println("sequences: " + sequences);
//		System.out.println("actual state: " + actualState.getName());
	
		Set<X> children = null;
		try {
			children = getGenerator().getMarkingGraph().getChildren(actualState.getName());
		} catch (VertexNotFoundException e) {
			e.printStackTrace();
		}
//		System.out.println("children: " + children);
		if(children == null || children.isEmpty()){
			return null;
		}
		
		List<List<X>> continuations = new ArrayList<List<X>>();
		for(X child: children){
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			List<List<X>> childContinuations = getContinuationsRec(child);
			if(childContinuations != null){
				for(List<X> childContinuation: childContinuations){
					List<X> newContinuation = new ArrayList<X>();
					newContinuation.add(child);
					newContinuation.addAll(childContinuation);
					continuations.add(newContinuation);
				}
			} else {
				List<X> newContinuation = new ArrayList<X>();
				newContinuation.add(child);
				continuations.add(newContinuation);
			}
		}
//		System.out.println(continuations);
		return continuations;
//		
//			
//			for(X child: children){
//				for(List<X> sequence: sequences){
//					sequence.add(child);
//				}
//				List<List<X>> childPaths = getContinuationsRec(sequences, child);
//				List<List<X>> oldSequences = new ArrayList<List<X>>(sequences);
//				sequences.clear();
//				for(List<X> childPath: childPaths){
//					for(List<X> oldSequence: oldSequences){
//						List<X> newSequence = new ArrayList<X>(oldSequence);
//						newSequence.addAll(childPath);
//						sequences.add(newSequence);
//					}
//				}
//			}
//		
//		
//		return sequences;
	}
	
	private Set<List<String>> removeSilentTransitions(Set<List<String>> sequences) throws InterruptedException {
		Set<List<String>> result = new HashSet<List<String>>();
		if(sequences.isEmpty())
			return result;
		
		for(List<String> sequence: sequences){
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException();
			}
			Set<String> activitiesToRemove = new HashSet<String>();
			for(String activityLabel: sequence){
				for(T transition: getGenerator().getPetriNet().getTransitions(activityLabel)){
					if(transition.isSilent()){
						activitiesToRemove.add(activityLabel);
					}
				}
			}
//			System.out.println(sequence);
//			System.out.println(activitiesToRemove);
//			System.out.println("---");
			if(activitiesToRemove.size() < sequence.size()){
				sequence.removeAll(activitiesToRemove);
				result.add(sequence);
			}
		}
		return result;
	}
	
	private Set<List<String>> getActivitySequencesRec(Set<List<String>> sequences, List<X> stateList, int index) throws InterruptedException {
//		System.out.println(sequences + " " + stateList + " " + index);
		
		if(stateList.size() < 2)
			return new HashSet<List<String>>();
		
		if(stateList.size() == 2){
			for(String eventName: getEventsBetweenStates(stateList.get(0).getName(), stateList.get(1).getName())){
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				sequences.add(Arrays.asList(eventName));
			}
			return sequences;
		}
		
		if(index == stateList.size()){
//			System.out.println("last case");
			return sequences;
		}
		
		if(index == 0){
//			System.out.println("first case");
			sequences.add(new ArrayList<String>());
			return getActivitySequencesRec(sequences, stateList, 1);
		} else {
//			System.out.println("regular case");
			// Find all events leading from the last state to the actual state
			String lastStateName = stateList.get(index-1).getName();
			String actualStateName = stateList.get(index).getName();
			List<String> eventNames = getEventsBetweenStates(lastStateName, actualStateName);
//			System.out.println(lastStateName + eventNames + actualStateName);

			// For each event, copy all existing sequences and add the event.
			if(eventNames.size() == 1){
				for(List<String> sequence: sequences){
					sequence.add(eventNames.get(0));
				}
			} else {
				Set<List<String>> oldSequences = new HashSet<List<String>>(sequences);
				sequences.clear();
				for(String eventName: eventNames){
					for(List<String> oldSequence: oldSequences){
						List<String> clonedSequence = new ArrayList<String>(oldSequence);
						clonedSequence.add(eventName);
						sequences.add(clonedSequence);
					}
				}
			}
			return getActivitySequencesRec(sequences, stateList, index + 1);
		}
	}
	
	private List<String> getEventsBetweenStates(String fromStateName, String toStateName) throws InterruptedException{
		List<String> eventNames = new ArrayList<String>();
		try {
			for (AbstractLabeledTransitionRelation<X,Event,M> relation : getGenerator().getMarkingGraph().getIncomingRelationsFor(toStateName)) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				if (relation.getSource().getName().equals(fromStateName)) {
					eventNames.add(relation.getEvent().getLabel());
				}
			}
		} catch (StateNotFoundException e) {
			e.printStackTrace();
		}
		return eventNames;
	}

	public AbstractMarkingGraph<M,S,X,Y> getMarkingGraph(){
		return getGenerator().getMarkingGraph();
	}
	
}
