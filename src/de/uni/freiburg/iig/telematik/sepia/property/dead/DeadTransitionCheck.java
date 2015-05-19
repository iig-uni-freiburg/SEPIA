package de.uni.freiburg.iig.telematik.sepia.property.dead;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class DeadTransitionCheck {
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					void initiateDeadTransitionCheck(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, ExecutorListener listener)
							throws DeadTransitionCheckException {

		ThreadedDeadTransitionsChecker<P,T,F,M,S,X,Y> calculator = new ThreadedDeadTransitionsChecker<P,T,F,M,S,X,Y>(petriNet);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					void initiateDeadTransitionCheck(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph, ExecutorListener listener)
							throws DeadTransitionCheckException {

		ThreadedDeadTransitionsChecker<P,T,F,M,S,X,Y> calculator = new ThreadedDeadTransitionsChecker<P,T,F,M,S,X,Y>(petriNet, markingGraph);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}


	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					DeadTransitionCheckResult checkDeadTransitions(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet)
							throws DeadTransitionCheckException {

		ThreadedDeadTransitionsChecker<P,T,F,M,S,X,Y> calculator = new ThreadedDeadTransitionsChecker<P,T,F,M,S,X,Y>(petriNet);
		calculator.runCalculation();

		return calculator.getTransitionCheckingResult();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					DeadTransitionCheckResult checkDeadTransitions(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph)
							throws DeadTransitionCheckException {

		ThreadedDeadTransitionsChecker<P,T,F,M,S,X,Y> calculator = new ThreadedDeadTransitionsChecker<P,T,F,M,S,X,Y>(petriNet, markingGraph);
		calculator.runCalculation();

		return calculator.getTransitionCheckingResult();
	}


}
