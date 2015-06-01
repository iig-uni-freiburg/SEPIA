package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness;

import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.StateSpaceException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;

public class ThreadedBoundednessChecker<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object> 

										extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,
																				  AbstractMarkingGraph<M,S,?,?>,
																				  BoundednessCheckResult<P,T,F,M,S>,
																				  BoundednessException>{
	
	private Boundedness boundedness = Boundedness.UNKNOWN;
	
	public ThreadedBoundednessChecker(BoundednessCheckGenerator<P,T,F,M,S> generator){
		super(generator);
	}
	
	@Override
	protected BoundednessCheckGenerator<P,T,F,M,S> getGenerator() {
		return (BoundednessCheckGenerator<P,T,F,M,S>) super.getGenerator();
	}
	
	@Override
	protected AbstractCallable<AbstractMarkingGraph<M,S,?,?>> createCallable() {
		return new BoundednessCheckCallable<P,T,F,M,S>(getGenerator());
	}

	@Override
	protected BoundednessException createException(String message, Throwable cause) {
		return new BoundednessException(message, cause);
	}

	@Override
	protected BoundednessException executionException(ExecutionException e) {
		if(e.getCause() instanceof BoundednessException)
			return (BoundednessException) e.getCause();
		return new BoundednessException("Exception during marking graph construction", e);
	}

	@Override
	public void callableException(Exception e) {
		if(e.getCause() != null && e.getCause() instanceof StateSpaceException){
			boundedness = Boundedness.UNBOUNDED;
		}
	}
	
	

	@Override
	public void callableFinished(AbstractMarkingGraph<M,S,?,?> result) {
		super.callableFinished(result);
		boundedness = Boundedness.BOUNDED;
	}

	@Override
	protected BoundednessCheckResult<P,T,F,M,S> getResultFromCallableResult(AbstractMarkingGraph<M,S,?,?> callableResult) throws Exception {
		return new BoundednessCheckResult<P,T,F,M,S>(boundedness, callableResult);
	}
	
}
