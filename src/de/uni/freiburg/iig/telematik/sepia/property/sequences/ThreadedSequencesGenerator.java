package de.uni.freiburg.iig.telematik.sepia.property.sequences;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
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
										Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,X,Y,MGTraversalResult>{

	private AbstractMarkingGraph<M,S,X,Y> markingGraph = null;
	private Boolean includeSilentTransitions = null;
	
	protected ThreadedSequencesGenerator(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet){
		super(petriNet);
	}
	
	protected ThreadedSequencesGenerator(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, boolean includeSilentTransitions){
		this(petriNet);
		this.includeSilentTransitions = includeSilentTransitions;
	}
	
	protected ThreadedSequencesGenerator(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph){
		this(petriNet);
		Validate.notNull(markingGraph);
		this.markingGraph = markingGraph;
	}
	
	protected ThreadedSequencesGenerator(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet, AbstractMarkingGraph<M,S,X,Y> markingGraph, boolean includeSilentTransitions){
		this(petriNet, markingGraph);
		this.includeSilentTransitions = includeSilentTransitions;
	}
	
	@Override
	protected AbstractCallable<MGTraversalResult> getCallable() {
		if(markingGraph != null){
			if(includeSilentTransitions != null){
				return new SequenceGeneratorCallable<P,T,F,M,S,X,Y>(petriNet, markingGraph, includeSilentTransitions);
			} else {
				return new SequenceGeneratorCallable<P,T,F,M,S,X,Y>(petriNet, markingGraph);
			}
		} else {
			if(includeSilentTransitions != null){
				return new SequenceGeneratorCallable<P,T,F,M,S,X,Y>(petriNet, includeSilentTransitions);
			} else {
				return new SequenceGeneratorCallable<P,T,F,M,S,X,Y>(petriNet);
			}
		}
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
