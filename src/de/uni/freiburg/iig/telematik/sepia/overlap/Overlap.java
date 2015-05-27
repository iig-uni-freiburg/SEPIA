package de.uni.freiburg.iig.telematik.sepia.overlap;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class Overlap {
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					E extends LogEntry>

		void 
	
		initiateOverlapCalculation( OverlapCallableGenerator<P,T,F,M,S,E> generator,
									ExecutorListener listener)
								
		throws OverlapException {

		ThreadedOverlapCalculator<P,T,F,M,S,E> calculator = new ThreadedOverlapCalculator<P,T,F,M,S,E>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					E extends LogEntry>

		OverlapResult<E> 

		calculateOverlap(OverlapCallableGenerator<P,T,F,M,S,E> generator)

		throws OverlapException {

		ThreadedOverlapCalculator<P,T,F,M,S,E> calculator = new ThreadedOverlapCalculator<P,T,F,M,S,E>(generator);
		calculator.runCalculation();
		return calculator.getOverlapResult();
	}

}
