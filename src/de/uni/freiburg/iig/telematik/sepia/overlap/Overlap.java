package de.uni.freiburg.iig.telematik.sepia.overlap;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class Overlap {
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		void 
	
		initiateOverlapCalculation( OverlapCallableGenerator<P,T,F,M,S,X,Y,E> generator,
									ExecutorListener listener)
								
		throws OverlapException {

		ThreadedOverlapCalculator<P,T,F,M,S,X,Y,E> calculator = new ThreadedOverlapCalculator<P,T,F,M,S,X,Y,E>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>,
					E extends LogEntry>

		OverlapResult<E> 

		calculateOverlap(OverlapCallableGenerator<P,T,F,M,S,X,Y,E> generator)

		throws OverlapException {

		ThreadedOverlapCalculator<P,T,F,M,S,X,Y,E> calculator = new ThreadedOverlapCalculator<P,T,F,M,S,X,Y,E>(generator);
		calculator.runCalculation();
		return calculator.getOverlapResult();
	}

}
