package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.ArrayList;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.mg.MGTraversalResult;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.replay.Replayer.TerminationCriteria;
import de.uni.freiburg.iig.telematik.sepia.util.ReachabilityUtils;

public class Overlap {

	private double fitness = 0.0;
	private double precision = 0.0;
	
	public Overlap(AbstractPetriNet net, List<List<String>> logSequences) throws PNException{
		Replayer replayer = new Replayer(net, TerminationCriteria.ESCAPABLE_WITH_SILENT_TRANSITIONS);
		ReplayResult replayResult = replayer.replaySequences(logSequences);
		MGTraversalResult traversalResult = ReachabilityUtils.getFiringSequences(net, false);
		
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
		
		fitness = replayResult.getFittingSequences().size() / (logSequences.size() + 0.0);
//		if(fitness != 1){
//			System.out.println("Difference sequences (fitness):");
//			for(Object differenceSequence: replayResult.getNonFittingSequences()){
//				System.out.println(differenceSequence);
//			}
//		}
//		System.out.println("Model sequences:");
//		for(List<String> modelSequence: traversalResult.getCompleteSequences())
//			System.out.println(modelSequence);
		
		List<List<String>> nonFittingCompleteSequences = new ArrayList<List<String>>();
		nonFittingCompleteSequences.addAll(traversalResult.getCompleteSequences());
		nonFittingCompleteSequences.removeAll(logSequences);
		
		precision = 1.0 - (nonFittingCompleteSequences.size() / (traversalResult.getCompleteSequences().size() + 0.0));
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

}
