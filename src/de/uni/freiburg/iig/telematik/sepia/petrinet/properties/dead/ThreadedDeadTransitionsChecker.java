package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.StateSpaceException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;

public class ThreadedDeadTransitionsChecker<P extends AbstractPlace<F,S>, 
											T extends AbstractTransition<F,S>, 
											F extends AbstractFlowRelation<P,T,S>, 
											M extends AbstractMarking<S>, 
											S extends Object> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,DeadTransitionCheckResult>{
	
	public ThreadedDeadTransitionsChecker(DeadTransitionCheckCallableGenerator<P,T,F,M,S> generator){
		super(generator);
	}
	
	@Override
	protected DeadTransitionCheckCallableGenerator<P,T,F,M,S> getGenerator() {
		return (DeadTransitionCheckCallableGenerator<P,T,F,M,S>) super.getGenerator();
	}
	
	@Override
	protected AbstractCallable<DeadTransitionCheckResult> getCallable() {
		return new DeadTransitionCheckingCallable<P,T,F,M,S>(getGenerator());
	}
	
	public void runCalculation(){
		setUpAndRun();
	}
	
	public DeadTransitionCheckResult getTransitionCheckingResult() throws DeadTransitionCheckException{
		try {
			return getResult();
		} catch (CancellationException e) {
			throw new DeadTransitionCheckException("Dead transition check cancelled.", e);
		} catch (InterruptedException e) {
			throw new DeadTransitionCheckException("Dead transition check interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new DeadTransitionCheckException("Exception during dead transition check.\n" + e.getMessage(), e);
			}
			if(cause instanceof StateSpaceException){
				throw new DeadTransitionCheckException("Exception during dead transition check.\nCannot build whole marking graph", cause);
			}
			if(cause instanceof DeadTransitionCheckException){
				throw (DeadTransitionCheckException) cause;
			}
			throw new DeadTransitionCheckException("Exception during dead transition check.\n" + e.getMessage(), e);
		} catch(Exception e){
			throw new DeadTransitionCheckException("Exception during dead transition check.\n" + e.getMessage(), e);
		}
	}
}
