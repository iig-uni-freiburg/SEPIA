package de.uni.freiburg.iig.telematik.sepia.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.MarkingGraphException;
import de.uni.freiburg.iig.telematik.sepia.exception.StateSpaceException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class MGConstructorCallable< P extends AbstractPlace<F,S>, 
							T extends AbstractTransition<F,S>, 
							F extends AbstractFlowRelation<P,T,S>, 
							M extends AbstractMarking<S>, 
							S extends Object,
							X extends AbstractMarkingGraphState<M,S>,
							Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractCallable<AbstractMarkingGraph<M,S,X,Y>> {
	
	public static final int MAX_RG_CALCULATION_STEPS = Integer.MAX_VALUE;
	private static final String rgGraphNodeFormat = "s%s";
	
	private AbstractPetriNet<P,T,F,M,S,X,Y> petriNet = null;
	
	public  MGConstructorCallable(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet){
		Validate.notNull(petriNet);
		this.petriNet = petriNet;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AbstractMarkingGraph<M,S,X,Y> callRoutine() throws MarkingGraphException {
		M savedMarking = (M) petriNet.getMarking().clone();
		ArrayBlockingQueue<M> queue = new ArrayBlockingQueue<M>(10);
		Set<M> allKnownStates = new HashSet<M>();

		allKnownStates.add(petriNet.getInitialMarking());
		AbstractMarkingGraph<M,S,X,Y> markingGraph = petriNet.createNewMarkingGraph();
		int stateCount = 0;
		Map<String, String> stateNames = new HashMap<String, String>();
		M initialMarking = petriNet.getInitialMarking();
		queue.offer(initialMarking);
		String stateName = String.format(rgGraphNodeFormat, stateCount++);
		markingGraph.addState(stateName, (M) initialMarking.clone());
		markingGraph.setInitialState(markingGraph.getState(stateName));
		markingGraph.addStartState(stateName);
		stateNames.put(initialMarking.toString(), stateName);
		allKnownStates.add((M) initialMarking.clone());
		
		int calculationSteps = 0;
		try {
			while (!queue.isEmpty()) {
				if (Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
				calculationSteps++;
				if((calculationSteps >= MAX_RG_CALCULATION_STEPS)){
					petriNet.setMarking(savedMarking);
					throw new StateSpaceException("Reached maximum calculation steps for building marking graph.");
				}
				M nextMarking = queue.poll();
				petriNet.setMarking(nextMarking);
//				M marking = (M) nextMarking.clone();
				String nextStateName = stateNames.get(nextMarking.toString());
//				System.out.println("Next marking (" + nextStateName + "): " + nextMarking);

				if(petriNet.hasEnabledTransitions()){
					String newStateName = null;
					for (T enabledTransition : petriNet.getEnabledTransitions()) {

//						System.out.println("   enabled: " + enabledTransition.getName());
						M newMarking = petriNet.fireCheck(enabledTransition.getName());
						
						// Check if this marking is already known
						M equalMarking = null;
						for(M storedMarking: allKnownStates){
							if(storedMarking.equals(newMarking)){
								equalMarking = storedMarking;
								break;
							}
						}

//						System.out.println("   new marking: " + newMarking);
						if(equalMarking == null) {
							// This is a new marking
//							System.out.println("   -> New marking");
							queue.offer(newMarking);
							allKnownStates.add((M) newMarking.clone());
							newStateName = String.format(rgGraphNodeFormat, stateCount++);
							markingGraph.addState(newStateName, (M) newMarking.clone());
							stateNames.put(newMarking.toString(), newStateName);
						} else {
							// This marking is already known
//							System.out.println("   -> Known marking");
							newStateName = stateNames.get(newMarking.toString());
						}
						if (!markingGraph.containsEvent(enabledTransition.getName())) {
							markingGraph.addEvent(enabledTransition.getName(), enabledTransition.getLabel());
						}
//						System.out.println("   add relation: " + nextStateName + " to " + newStateName + " via " + enabledTransition.getName());
						markingGraph.addRelation(nextStateName, newStateName, enabledTransition.getName());
					}
				} else {
					markingGraph.addEndState(nextStateName);
				}
			}
		} catch (Exception e) {
			throw new MarkingGraphException("Exception during marking graph construction.<br>Reason: " + e.getMessage());
		}
		petriNet.setMarking(savedMarking);
		return markingGraph;
	}

}
