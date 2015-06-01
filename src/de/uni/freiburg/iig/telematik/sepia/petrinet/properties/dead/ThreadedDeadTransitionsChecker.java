package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead;

import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;

public class ThreadedDeadTransitionsChecker<P extends AbstractPlace<F,S>, 
											T extends AbstractTransition<F,S>, 
											F extends AbstractFlowRelation<P,T,S>, 
											M extends AbstractMarking<S>, 
											S extends Object> 

											extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,
																					  DeadTransitionCheckResult,
																					  DeadTransitionCheckResult, DeadTransitionCheckException>{
	
	public ThreadedDeadTransitionsChecker(DeadTransitionCheckCallableGenerator<P,T,F,M,S> generator){
		super(generator);
	}
	
	@Override
	protected DeadTransitionCheckCallableGenerator<P,T,F,M,S> getGenerator() {
		return (DeadTransitionCheckCallableGenerator<P,T,F,M,S>) super.getGenerator();
	}
	
	@Override
	protected AbstractCallable<DeadTransitionCheckResult> createCallable() {
		return new DeadTransitionCheckingCallable<P,T,F,M,S>(getGenerator());
	}

	@Override
	protected DeadTransitionCheckException createException(String message, Throwable cause) {
		return new DeadTransitionCheckException(message, cause);
	}

	@Override
	protected DeadTransitionCheckException executionException(ExecutionException e) {
		if(e.getCause() instanceof DeadTransitionCheckException)
			return (DeadTransitionCheckException) e.getCause();
		return new DeadTransitionCheckException("Exception during dead transition check", e);
	}

	@Override
	protected DeadTransitionCheckResult getResultFromCallableResult(DeadTransitionCheckResult callableResult) throws Exception {
		return callableResult;
	}
	

}
