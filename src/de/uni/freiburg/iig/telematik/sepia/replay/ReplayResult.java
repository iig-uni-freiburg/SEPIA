package de.uni.freiburg.iig.telematik.sepia.replay;

import java.util.Collection;
import java.util.List;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;

public class ReplayResult<E extends LogEntry> {

	private Collection<LogTrace<E>> fittingTraces = null;
	private Collection<LogTrace<E>> nonFittingTraces = null;
	private Collection<List<String>> fittingSequences = null;
	private Collection<List<String>> nonFittingSequences = null;
	private double portionFitting = 0.0;
	private double portionNonFitting = 0.0;
	private int numTraces = 0;
	private int numSequences = 0;
	
	public ReplayResult(Collection<LogTrace<E>> fittingTraces, 
						Collection<LogTrace<E>> nonFittingTraces,
						Collection<List<String>> fittingSequences, 
						Collection<List<String>> nonFittingSequences) {
		Validate.notNull(fittingTraces);
		Validate.notNull(nonFittingTraces);
		Validate.notNull(fittingSequences);
		Validate.notNull(nonFittingSequences);
		this.numTraces = fittingTraces.size() + nonFittingTraces.size();
		this.numSequences = fittingSequences.size() + nonFittingSequences.size();
		this.fittingTraces = fittingTraces;
		this.nonFittingTraces = nonFittingTraces;
		this.fittingSequences = fittingSequences;
		this.nonFittingSequences = nonFittingSequences;
		portionFitting = (fittingTraces.size() + 0.0) / getCount();
		portionNonFitting = (nonFittingTraces.size() + 0.0) / getCount();
	}

	public Collection<LogTrace<E>> getFittingTraces() {
		return fittingTraces;
	}

	public Collection<LogTrace<E>> getNonFittingTraces() {
		return nonFittingTraces;
	}
	
	public Collection<List<String>> getFittingSequences() {
		return fittingSequences;
	}

	public Collection<List<String>> getNonFittingSequences() {
		return nonFittingSequences;
	}
	
	public int getNumTraces(){
		return numTraces;
	}
	
	public int getNumSequences(){
		return numSequences;
	}
	
	public int getCount(){
		return Math.max(numTraces, numSequences);
	}

	public double portionFitting(){
		return portionFitting;
	}
	
	public double portionNonFitting(){
		return portionNonFitting;
	}
	
}
