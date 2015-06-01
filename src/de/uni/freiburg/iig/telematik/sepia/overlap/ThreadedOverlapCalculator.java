package de.uni.freiburg.iig.telematik.sepia.overlap;

import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class ThreadedOverlapCalculator<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										E extends LogEntry> 

										extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,
																				  OverlapResult<E>,
																				  OverlapResult<E>,
																				  OverlapException>{
	
	protected ThreadedOverlapCalculator(OverlapCallableGenerator<P,T,F,M,S,E> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected OverlapCallableGenerator<P,T,F,M,S,E> getGenerator() {
		return (OverlapCallableGenerator<P,T,F,M,S,E>) super.getGenerator();
	}

	@Override
	protected AbstractCallable<OverlapResult<E>> createCallable() {
		return new OverlapCallable<P,T,F,M,S,E>(getGenerator());
	}

	@Override
	protected OverlapException createException(String message, Throwable cause) {
		return new OverlapException(message, cause);
	}

	@Override
	protected OverlapException executionException(ExecutionException e) {
		if(e.getCause() instanceof OverlapException)
			return (OverlapException) e.getCause();
		return new OverlapException("Exception during overlap calculation", e);
	}

	@Override
	protected OverlapResult<E> getResultFromCallableResult(OverlapResult<E> callableResult) throws Exception {
		return callableResult;
	}
	

}
