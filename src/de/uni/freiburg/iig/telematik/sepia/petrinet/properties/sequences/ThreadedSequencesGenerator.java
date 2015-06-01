package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.sequences;

import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;

public class ThreadedSequencesGenerator<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object> 

										extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,
																				  MGTraversalResult,
																				  MGTraversalResult,
																				  SequenceGenerationException>{

	protected ThreadedSequencesGenerator(SequenceGenerationCallableGenerator<P,T,F,M,S> generator){
		super(generator);
	}
	
	@Override
	protected SequenceGenerationCallableGenerator<P,T,F,M,S> getGenerator() {
		return (SequenceGenerationCallableGenerator<P, T, F, M, S>) super.getGenerator();
	}

	@Override
	protected AbstractCallable<MGTraversalResult> createCallable() {
		return new SequenceGenerationCallable<P,T,F,M,S>(getGenerator());
	}

	@Override
	protected SequenceGenerationException createException(String message, Throwable cause) {
		return new SequenceGenerationException(message, cause);
	}

	@Override
	protected SequenceGenerationException executionException(ExecutionException e) {
		if(e.getCause() instanceof SequenceGenerationException)
			return (SequenceGenerationException) e.getCause();
		return new SequenceGenerationException("Exception during sequence generation", e);
	}

	@Override
	protected MGTraversalResult getResultFromCallableResult(MGTraversalResult callableResult) throws Exception {
		return callableResult;
	}

}
