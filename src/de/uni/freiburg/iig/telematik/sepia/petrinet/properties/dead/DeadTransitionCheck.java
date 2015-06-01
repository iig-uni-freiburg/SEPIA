package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public class DeadTransitionCheck {
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

					void initiateDeadTransitionCheck(DeadTransitionCheckCallableGenerator<P,T,F,M,S> generator, ExecutorListener<DeadTransitionCheckResult> listener)
							throws DeadTransitionCheckException {

		ThreadedDeadTransitionsChecker<P,T,F,M,S> calculator = new ThreadedDeadTransitionsChecker<P,T,F,M,S>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

					void initiateDeadTransitionCheck(AbstractPetriNet<P,T,F,M,S> petriNet, ExecutorListener<DeadTransitionCheckResult> listener)
							throws DeadTransitionCheckException {

		initiateDeadTransitionCheck(new DeadTransitionCheckCallableGenerator<P,T,F,M,S>(petriNet), listener);
}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

					DeadTransitionCheckResult checkDeadTransitions(DeadTransitionCheckCallableGenerator<P,T,F,M,S> generator)
							throws DeadTransitionCheckException {

		ThreadedDeadTransitionsChecker<P,T,F,M,S> calculator = new ThreadedDeadTransitionsChecker<P,T,F,M,S>(generator);
		calculator.runCalculation();

		return calculator.getResult();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

					DeadTransitionCheckResult checkDeadTransitions(AbstractPetriNet<P,T,F,M,S> petriNet)
							throws DeadTransitionCheckException {

		return checkDeadTransitions(new DeadTransitionCheckCallableGenerator<P,T,F,M,S>(petriNet));
	}
}
