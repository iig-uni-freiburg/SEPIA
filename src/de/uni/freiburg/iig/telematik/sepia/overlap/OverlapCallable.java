package de.uni.freiburg.iig.telematik.sepia.overlap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.StateSpaceException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.sequences.MGTraversalResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.sequences.SequenceGenerationCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.sequences.SequenceGenerationCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractPNPropertyCheckerCallable;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayCallable;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayResult;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class OverlapCallable< 	P extends AbstractPlace<F,S>, 
								T extends AbstractTransition<F,S>, 
								F extends AbstractFlowRelation<P,T,S>, 
								M extends AbstractMarking<S>, 
								S extends Object,
								E extends LogEntry> extends AbstractPNPropertyCheckerCallable<P,T,F,M,S,OverlapResult<E>> {
	
	protected OverlapCallable(OverlapCallableGenerator<P,T,F,M,S,E> generator) {
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected OverlapCallableGenerator<P,T,F,M,S,E> getGenerator() {
		return (OverlapCallableGenerator<P, T, F, M, S, E>) super.getGenerator();
	}

	@Override
	protected OverlapResult<E> callRoutine() throws OverlapException, InterruptedException {
		// Generate Sequences
		MGTraversalResult traversalResult = null;
		try {
			SequenceGenerationCallable<P,T,F,M,S> sequenceGeneratorCallable = new SequenceGenerationCallable<P,T,F,M,S>(new SequenceGenerationCallableGenerator<P,T,F,M,S>(getGenerator()));
			traversalResult = sequenceGeneratorCallable.callRoutine();
			getGenerator().setMarkingGraph(sequenceGeneratorCallable.getMarkingGraph());
		} catch(Exception e){
			// Abort when Petri net is unbounded
			if(e.getCause() != null && e.getCause() instanceof StateSpaceException){
				throw new OverlapException("Cannot generate sequences of unbounded net", e);
			}
			throw new OverlapException("Exception during marking graph construction", e);
		}
		
		// Perform replay
		ReplayResult<E> replayResult = null;
		try {
			ReplayCallable<P,T,F,M,S,E> replayingCallable = new ReplayCallable<P,T,F,M,S,E>(new ReplayCallableGenerator<P,T,F,M,S,E>(getGenerator()));
			replayResult = replayingCallable.callRoutine();
		} catch(Exception e){
			throw new OverlapException("Exception during replay", e);
		}
		
		return new OverlapResult<E>(traversalResult, replayResult);
	}
	
}
