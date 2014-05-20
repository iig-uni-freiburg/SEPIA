package de.uni.freiburg.iig.telematik.sepia.mg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.misc.ListUtils;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.exception.StateNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class MGSequenceGenerator<	P extends AbstractPlace<F, S>, 
									T extends AbstractTransition<F, S>, 
									F extends AbstractFlowRelation<P, T, S>, 
									M extends AbstractMarking<S>, 
									S extends Object, 
									X extends AbstractMarkingGraphState<M, S>,
									Y extends AbstractMarkingGraphRelation<M, X, S>> {
	
	private AbstractPetriNet<P,T,F,M,S> petriNet = null;
	private AbstractMarkingGraph<M,S,X,Y> markingGraph = null;
	private boolean includeSilentTransitions = false;
	
	public MGSequenceGenerator(AbstractPetriNet<P,T,F,M,S> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph, boolean includeSilentTransitions){
		this.petriNet = petriNet;
		this.markingGraph = markingGraph;
		this.includeSilentTransitions = includeSilentTransitions;
	}
	
	public MGTraversalResult getSequences(){
		Set<List<String>> sequences = new HashSet<List<String>>();
		Set<List<String>> completeSequences = new HashSet<List<String>>();
		
		for(List<X> detSequence: getStateSequences()){
			Set<List<String>> activitySequences = getActivitySequences(detSequence);
			for(List<String> activitySequence: activitySequences){
				sequences.addAll(getSequences(activitySequence));
				if(markingGraph.isEndState(detSequence.get(detSequence.size()-1).getName())){
					completeSequences.add(activitySequence);
				}
			}
		}
		
		return new MGTraversalResult(sequences, completeSequences);
	}
	
	private List<List<X>> getStateSequences(){
		List<List<X>> stateSequences = new ArrayList<List<X>>();
		for(List<X> continuation: getContinuationsRec(markingGraph.getInitialState())){
			List<X> newContinuation = new ArrayList<X>();
			newContinuation.add(markingGraph.getInitialState());
			newContinuation.addAll(continuation);
			stateSequences.add(newContinuation);
		}
		return stateSequences;
	}
	
	private List<List<String>> getSequences(List<String> completeSequence){
		List<List<String>> result = new ArrayList<List<String>>();
		
		if(completeSequence.size() == 1){
			result.add(completeSequence);
		} else {
			for(int i=1; i<completeSequence.size(); i++){
				result.add(ListUtils.copyOfRange(completeSequence, 0, i));
			}
		}
		
		return result;
	}
	
	private Set<List<String>> getActivitySequences(List<X> stateList) {
		Set<List<String>> sequences = getActivitySequencesRec(new HashSet<List<String>>(), stateList, 0);
		if(!includeSilentTransitions)
			removeSilentTransitions(sequences);
		return sequences;
	}
	
	private List<List<X>> getContinuationsRec(X actualState){
//		System.out.println("sequences: " + sequences);
//		System.out.println("actual state: " + actualState.getName());
	
		Set<X> children = null;
		try {
			children = markingGraph.getChildren(actualState.getName());
		} catch (VertexNotFoundException e) {
			e.printStackTrace();
		}
//		System.out.println("children: " + children);
		if(children.isEmpty()){
			return null;
		}
		
		List<List<X>> continuations = new ArrayList<List<X>>();
		for(X child: children){
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
	
	private void removeSilentTransitions(Set<List<String>> sequences) {
		if(sequences.isEmpty())
			return;
		for(List<String> sequence: sequences){
			Set<String> activitiesToRemove = new HashSet<String>();
			for(String activityLabel: sequence){
				for(T transition: petriNet.getTransitions(activityLabel)){
					if(transition.isSilent()){
						activitiesToRemove.add(activityLabel);
					}
				}
			}
			sequence.removeAll(activitiesToRemove);
		}
	}
	
	private Set<List<String>> getActivitySequencesRec(Set<List<String>> sequences, List<X> stateList, int index) {
//		System.out.println(sequences + " " + stateList + " " + index);
		
		if(stateList.size() < 2)
			return new HashSet<List<String>>();
		
		if(stateList.size() == 2){
			for(String eventName: getEventsBetweenStates(stateList.get(0).getName(), stateList.get(1).getName())){
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
	
	private List<String> getEventsBetweenStates(String fromStateName, String toStateName){
		List<String> eventNames = new ArrayList<String>();
		try {
			for (AbstractLabeledTransitionRelation<X,Event,M> relation : markingGraph.getIncomingRelationsFor(toStateName)) {
				if (relation.getSource().getName().equals(fromStateName)) {
					eventNames.add(relation.getEvent().getLabel());
				}
			}
		} catch (StateNotFoundException e) {
			e.printStackTrace();
		}
		return eventNames;
	}
	
}
