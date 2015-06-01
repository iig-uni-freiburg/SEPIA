package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class ThreadedReplayer<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										E extends LogEntry> 

										extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,
																				  ReplayResult<E>,
																				  ReplayResult<E>,
																				  ReplayException>{
	
	protected ThreadedReplayer(ReplayCallableGenerator<P,T,F,M,S,E> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ReplayCallableGenerator<P,T,F,M,S,E> getGenerator() {
		return (ReplayCallableGenerator<P,T,F,M,S,E>) super.getGenerator();
	}

	@Override
	protected AbstractCallable<ReplayResult<E>> createCallable() {
		return new ReplayCallable<P,T,F,M,S,E>(getGenerator());
	}
	
	@Override
	protected ReplayException createException(String message, Throwable cause) {
		return new ReplayException(message, cause);
	}

	@Override
	protected ReplayException executionException(ExecutionException e) {
		if(e.getCause() instanceof ReplayException)
			return (ReplayException) e.getCause();
		return new ReplayException("Exception during replay", e);
	}

	@Override
	protected ReplayResult<E> getResultFromCallableResult(ReplayResult<E> callableResult) throws Exception {
		return callableResult;
	}
	
}
