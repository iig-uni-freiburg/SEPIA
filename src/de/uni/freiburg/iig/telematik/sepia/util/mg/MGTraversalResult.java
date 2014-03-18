package de.uni.freiburg.iig.telematik.sepia.util.mg;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MGTraversalResult {

	private Set<List<String>> sequences = new HashSet<List<String>>();
	private Set<List<String>> completeSequences = new HashSet<List<String>>();
	
	public MGTraversalResult(Set<List<String>> sequences, Set<List<String>> completeSequences) {
		super();
		this.sequences = sequences;
		this.completeSequences = completeSequences;
	}

	public Set<List<String>> getSequences() {
		return sequences;
	}

	public Set<List<String>> getCompleteSequences() {
		return completeSequences;
	}
}
