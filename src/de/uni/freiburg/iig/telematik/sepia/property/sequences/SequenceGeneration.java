package de.uni.freiburg.iig.telematik.sepia.property.sequences;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class SequenceGeneration {
	
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					void initiateSequenceGeneration(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, boolean includeSilentTransitions, ExecutorListener listener)
							throws SequenceGenerationException {
		
		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(petriNet, includeSilentTransitions);
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

					void initiateSequenceGeneration(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, ExecutorListener listener)
							throws SequenceGenerationException {
		
		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(petriNet);
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

					void initiateSequenceGeneration(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph, ExecutorListener listener)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(petriNet, markingGraph);
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

					void initiateSequenceGeneration(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph, boolean includeSilentTransitions, ExecutorListener listener)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(petriNet, markingGraph, includeSilentTransitions);
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

					MGTraversalResult getFiringSequences(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, boolean includeSilentTransitions)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(petriNet, includeSilentTransitions);
		calculator.runCalculation();
		
		return calculator.getSequences();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					MGTraversalResult getFiringSequences(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(petriNet);
		calculator.runCalculation();
		
		return calculator.getSequences();
	}


	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					MGTraversalResult getFiringSequences(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(petriNet, markingGraph);
		calculator.runCalculation();
		
		return calculator.getSequences();
	}

	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object,
					X extends AbstractMarkingGraphState<M,S>,
					Y extends AbstractMarkingGraphRelation<M,X,S>>

					MGTraversalResult getFiringSequences(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph, boolean includeSilentTransitions)
							throws SequenceGenerationException {

		ThreadedSequencesGenerator<P,T,F,M,S,X,Y> calculator = new ThreadedSequencesGenerator<P,T,F,M,S,X,Y>(petriNet, markingGraph, includeSilentTransitions);
		calculator.runCalculation();
		
		return calculator.getSequences();
	}
}
