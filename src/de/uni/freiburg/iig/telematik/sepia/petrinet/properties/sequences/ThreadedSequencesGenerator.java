package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.sequences;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.StateSpaceException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;

public class ThreadedSequencesGenerator<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,MGTraversalResult>{

	protected ThreadedSequencesGenerator(SequenceGenerationCallableGenerator<P,T,F,M,S> generator){
		super(generator);
	}
	
	@Override
	protected SequenceGenerationCallableGenerator<P,T,F,M,S> getGenerator() {
		return (SequenceGenerationCallableGenerator<P, T, F, M, S>) super.getGenerator();
	}

	@Override
	protected AbstractCallable<MGTraversalResult> getCallable() {
		return new SequenceGenerationCallable<P,T,F,M,S>(getGenerator());
	}
	
	public void runCalculation(){
		setUpAndRun();
	}
	
	public MGTraversalResult getSequences() throws SequenceGenerationException{
		try {
			return getResult();
		} catch (CancellationException e) {
			throw new SequenceGenerationException("Sequence generation cancelled.", e);
		} catch (InterruptedException e) {
			throw new SequenceGenerationException("Sequence generation interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new SequenceGenerationException("Exception during sequence generation.\n" + e.getMessage(), e);
			}
			if(cause instanceof StateSpaceException){
				throw new SequenceGenerationException("Exception during sequence generation.\nCannot build whole marking graph", cause);
			}
			if(cause instanceof SequenceGenerationException){
				throw (SequenceGenerationException) cause;
			}
			throw new SequenceGenerationException("Exception during sequence generation.\n" + e.getMessage(), e);
		} catch(Exception e){
			throw new SequenceGenerationException("Exception during sequence generation.\n" + e.getMessage(), e);
		}
	}
}
