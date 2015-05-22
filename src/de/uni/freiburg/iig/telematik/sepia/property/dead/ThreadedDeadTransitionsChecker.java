package de.uni.freiburg.iig.telematik.sepia.property.dead;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sepia.property.mg.StateSpaceException;

public class ThreadedDeadTransitionsChecker<P extends AbstractPlace<F,S>, 
											T extends AbstractTransition<F,S>, 
											F extends AbstractFlowRelation<P,T,S>, 
											M extends AbstractMarking<S>, 
											S extends Object,
											X extends AbstractMarkingGraphState<M,S>,
											Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,DeadTransitionCheckResult>{
	
	public ThreadedDeadTransitionsChecker(DeadTransitionCheckCallableGenerator<P,T,F,M,S,X,Y> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected DeadTransitionCheckCallableGenerator<P,T,F,M,S,X,Y> getGenerator() {
		return (DeadTransitionCheckCallableGenerator<P,T,F,M,S,X,Y>) super.getGenerator();
	}
	
	@Override
	protected AbstractCallable<DeadTransitionCheckResult> getCallable() {
		return new DeadTransitionCheckingCallable<P,T,F,M,S,X,Y>(getGenerator());
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
