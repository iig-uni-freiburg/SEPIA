package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.util.List;
import java.util.Random;

import de.uni.freiburg.iig.telematik.sepia.event.TimedTransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TimedTransitionListener;
import de.uni.freiburg.iig.telematik.sepia.event.TimedTransitionListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.traversal.PNTraverser;

public abstract class TimedNetExecutor<P extends AbstractPlace<F,S>, 
							  T extends AbstractTransition<F,S>, 
							  F extends AbstractFlowRelation<P,T,S>,
							  M extends AbstractMarking<S>,
							  S extends Object,
							  X extends AbstractMarkingGraphState<M,S>, 
							  Y extends AbstractMarkingGraphRelation<M,X,S>> {
	
	private static final int MAX_FIRE_TRANSITIONS = 2;

	private Random rand = new Random();
	private AbstractPetriNet<P,T,F,M,S,X,Y> petriNet = null;
	private PNTraverser<T> traverser = null;
	private TimeMachine<P,T,F,M,S,X,Y> timeMachine = null;
	
	private TimedTransitionListenerSupport<T> listenerSupport = new TimedTransitionListenerSupport<T>();
	
	private TimedNetExecutor(TimeMachine<P,T,F,M,S,X,Y> timeMachine, PNTraverser<T> traverser){
		this.timeMachine = timeMachine;
		this.petriNet = timeMachine.getPetriNet();
		this.traverser = traverser;
	}
	
	public void addTimedTransitionListener(TimedTransitionListener<T> l) {
		listenerSupport.addTimedTransitionListener(l);
	}
	
	public void removeTimedTransitionListener(TimedTransitionListener<T> l) {
		listenerSupport.removeTimedTransitionListener(l);
	}
	
	public void reset(){
		timeMachine.reset();
	}
	
	public void continueExecution() throws PNException{
		if(timeMachine.getTime() == 0){
			if(petriNet.getEnabledTransitions().isEmpty())
				throw new PNException("No enabled Transitions in initial state");
		} else {
			boolean continueIncrement = petriNet.getEnabledTransitions().isEmpty();
			while(continueIncrement){
				timeMachine.incTime();
				boolean enabledTransitions = petriNet.getEnabledTransitions().isEmpty();
				if(enabledTransitions)
					break;
				continueIncrement = timeMachine.hasPendingActions();
			}
			if(!petriNet.getEnabledTransitions().isEmpty())
				fireTransitions();
		}
	}
	
	private void fireTransitions() throws PNException{
		List<T> firstEnabledTransitions = petriNet.getEnabledTransitions();
		List<T> enabledTransitions = petriNet.getEnabledTransitions();
		if(firstEnabledTransitions.isEmpty())
			throw new PNException("No enabled transitions to fire");
		
		int firedTransitions = 0;
		boolean continueFiring = true;
		int targetFireTransitions = rand.nextInt(MAX_FIRE_TRANSITIONS) + 1;
		while(continueFiring){
			enabledTransitions = petriNet.getEnabledTransitions();
			enabledTransitions.retainAll(firstEnabledTransitions);
			if(enabledTransitions.isEmpty())
				break;
			T firingTransition = traverser.chooseNextTransition(enabledTransitions);
			timeMachine.fire(firingTransition.getName());
			TimedTransitionEvent<T> firingEvent = new TimedTransitionEvent<T>(firingTransition);
			firingEvent.setTime(timeMachine.getTime());
			listenerSupport.notifyFiring(firingEvent);
			continueFiring = ++firedTransitions < targetFireTransitions;
		}
	}

}
