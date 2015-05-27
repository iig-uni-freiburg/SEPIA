package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.concurrent.CancellationException;
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
										E extends LogEntry> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,ReplayResult<E>>{
	
	protected ThreadedReplayer(ReplayCallableGenerator<P,T,F,M,S,E> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ReplayCallableGenerator<P,T,F,M,S,E> getGenerator() {
		return (ReplayCallableGenerator<P,T,F,M,S,E>) super.getGenerator();
	}

	@Override
	protected AbstractCallable<ReplayResult<E>> getCallable() {
		return new ReplayCallable<P,T,F,M,S,E>(getGenerator());
	}
	
	public void runCalculation(){
		setUpAndRun();
	}
	
	public ReplayResult<E> getReplayResult() throws ReplayException{
		try {
			return getResult();
		} catch (CancellationException e) {
			throw new ReplayException("Replaying cancelled.", e);
		} catch (InterruptedException e) {
			throw new ReplayException("Replaying interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new ReplayException("Exception during replay.\n" + e.getMessage(), e);
			}
			if(cause instanceof ReplayException){
				throw (ReplayException) cause;
			}
			throw new ReplayException("Exception during replay.\n" + e.getMessage(), e);
		} catch(Exception e){
			throw new ReplayException("Exception during replay.\n" + e.getMessage(), e);
		}
	}
}
