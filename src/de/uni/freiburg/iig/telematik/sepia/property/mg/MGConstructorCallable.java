package de.uni.freiburg.iig.telematik.sepia.property.mg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractPNPropertyCheckerCallable;

public class MGConstructorCallable< P extends AbstractPlace<F,S>, 
									T extends AbstractTransition<F,S>, 
									F extends AbstractFlowRelation<P,T,S>, 
									M extends AbstractMarking<S>, 
									S extends Object,
									X extends AbstractMarkingGraphState<M,S>,
									Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractPNPropertyCheckerCallable<P,T,F,M,S,X,Y,AbstractMarkingGraph<M,S,X,Y>> {
	
	private static final String rgGraphNodeFormat = "s%s";
	
	public MGConstructorCallable(MGConstructorCallableGenerator<P,T,F,M,S,X,Y> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AbstractMarkingGraph<M,S,X,Y> callRoutine() throws MarkingGraphException, InterruptedException {
		
		M savedMarking = (M) getGenerator().getPetriNet().getMarking().clone();
		ArrayBlockingQueue<M> queue = new ArrayBlockingQueue<M>(10);
		Set<M> allKnownStates = new HashSet<M>();

		allKnownStates.add(getGenerator().getPetriNet().getInitialMarking());
		AbstractMarkingGraph<M,S,X,Y> markingGraph = null;
		try{
			markingGraph = (AbstractMarkingGraph<M,S,X,Y>) getGenerator().getPetriNet().getMarkingGraphClass().newInstance();
		} catch (Exception e) {
			throw new MarkingGraphException("Cannot create new instance of markign graph class", e);
		}
		int stateCount = 0;
		Map<String, String> stateNames = new HashMap<String, String>();
		M initialMarking = getGenerator().getPetriNet().getInitialMarking();
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
				if((calculationSteps >= MGConstruction.MAX_RG_CALCULATION_STEPS)){
					getGenerator().getPetriNet().setMarking(savedMarking);
					throw new StateSpaceException("Reached maximum calculation steps for building marking graph.");
				}
				M nextMarking = queue.poll();
				getGenerator().getPetriNet().setMarking(nextMarking);
//				M marking = (M) nextMarking.clone();
				String nextStateName = stateNames.get(nextMarking.toString());
//				System.out.println("Next marking (" + nextStateName + "): " + nextMarking);

				if(getGenerator().getPetriNet().hasEnabledTransitions()){
					String newStateName = null;
					for (T enabledTransition : getGenerator().getPetriNet().getEnabledTransitions()) {

//						System.out.println("   enabled: " + enabledTransition.getName());
						M newMarking = getGenerator().getPetriNet().fireCheck(enabledTransition.getName());
						
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
		} catch(InterruptedException e){
			throw e;
		} catch (Exception e) {
			throw new MarkingGraphException("Exception during marking graph construction.<br>Reason: " + e.getMessage(), e);
		}
		getGenerator().getPetriNet().setMarking(savedMarking);
		return markingGraph;
	}

}
