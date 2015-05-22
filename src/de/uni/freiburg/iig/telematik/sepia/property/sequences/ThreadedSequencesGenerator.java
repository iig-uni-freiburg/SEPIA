package de.uni.freiburg.iig.telematik.sepia.property.sequences;

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

public class ThreadedSequencesGenerator<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										X extends AbstractMarkingGraphState<M,S>,
										Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,MGTraversalResult>{

	protected ThreadedSequencesGenerator(SequenceGenerationCallableGenerator<P,T,F,M,S,X,Y> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected SequenceGenerationCallableGenerator<P,T,F,M,S,X,Y> getGenerator() {
		return (SequenceGenerationCallableGenerator<P, T, F, M, S, X, Y>) super.getGenerator();
	}

	@Override
	protected AbstractCallable<MGTraversalResult> getCallable() {
		return new SequenceGenerationCallable<P,T,F,M,S,X,Y>(getGenerator());
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
