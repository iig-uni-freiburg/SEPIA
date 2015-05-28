package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.sequences;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public class SequenceGeneration {
	
	public static final boolean DEFAULT_INCLUDE_SILENT_TRANSITIONS = true;
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

					void initiateSequenceGeneration(SequenceGenerationCallableGenerator<P,T,F,M,S> generator, ExecutorListener listener)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S> calculator = new ThreadedSequencesGenerator<P,T,F,M,S>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

					MGTraversalResult getFiringSequences(SequenceGenerationCallableGenerator<P,T,F,M,S> generator)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S> calculator = new ThreadedSequencesGenerator<P,T,F,M,S>(generator);
		calculator.runCalculation();
		
		return calculator.getSequences();
	}

}
