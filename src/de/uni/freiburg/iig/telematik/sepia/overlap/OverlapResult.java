package de.uni.freiburg.iig.telematik.sepia.overlap;

import de.uni.freiburg.iig.telematik.sepia.property.sequences.MGTraversalResult;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayResult;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class OverlapResult<E extends LogEntry> {
	
	private double fitness = 0.0;
	private double precision = 0.0;
	private MGTraversalResult traversalResult = null;
	private ReplayResult<E> replayResult = null;
	
	public OverlapResult(MGTraversalResult traversalResult, ReplayResult<E> replayResult) {
		super();
		this.traversalResult = traversalResult;
		this.replayResult = replayResult;
//		System.out.println("Log sequences:");
//		for(Object logSequence: logSequences){
//			System.out.println(logSequence);
//		}

//		System.out.println("Fitting sequences:");
//		for(Object fittingSequence: replayResult.getFittingSequences()){
//			System.out.println(fittingSequence);
//		}
		
//		System.out.println("Complete sequences:");
//		for(Object completeSequence: traversalResult.getCompleteSequences()){
//			System.out.println(completeSequence);
//		}
		
//		System.out.println("Complete fitting sequences:");
//		for(Object completeFittingSequence: fittingCompleteSequences){
//			System.out.println(completeFittingSequence);
//		}
		
		fitness = replayResult.getFittingSequences().size() / (replayResult.getNumSequences() + 0.0);
//		if(fitness != 1){
//			System.out.println("Difference sequences (fitness):");
//			for(Object differenceSequence: replayResult.getNonFittingSequences()){
//				System.out.println(differenceSequence);
//			}
//		}
//		System.out.println("Model sequences:");
//		for(List<String> modelSequence: traversalResult.getCompleteSequences())
//			System.out.println(modelSequence);
		
		int nonFittingCompleteSequences = traversalResult.getCompleteSequences().size() - replayResult.getNonFittingSequences().size();
		
		precision = 1.0 - (nonFittingCompleteSequences / (traversalResult.getCompleteSequences().size() + 0.0));
//		if(precision != 1){
//			System.out.println("Difference sequences (precision):");
//			for(Object differenceSequence: nonFittingCompleteSequences){
//				System.out.println(differenceSequence);
//			}
//		}
	}

	public double getFitness() {
		return fitness;
	}

	public double getPrecision() {
		return precision;
	}

	public MGTraversalResult getTraversalResult() {
		return traversalResult;
	}

	public ReplayResult<E> getReplayResult() {
		return replayResult;
	}

}
