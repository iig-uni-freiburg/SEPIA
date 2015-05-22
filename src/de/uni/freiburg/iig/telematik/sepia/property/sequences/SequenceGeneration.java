package de.uni.freiburg.iig.telematik.sepia.property.sequences;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class SequenceGeneration {
	
	public static final boolean DEFAULT_INCLUDE_SILENT_TRANSITIONS = true;
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					void initiateSequenceGeneration(SequenceGenerationCallableGenerator<P,T,F,M,S,X,Y> generator, ExecutorListener listener)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(generator);
		calculator.addExecutorListener(listener);
		calculator.runCalculation();
	}
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					MGTraversalResult getFiringSequences(SequenceGenerationCallableGenerator<P,T,F,M,S,X,Y> generator)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(generator);
		calculator.runCalculation();
		
		return calculator.getSequences();
	}

}
