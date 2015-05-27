package de.uni.freiburg.iig.telematik.sepia.overlap;

import java.util.Map;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.sequences.SequenceGeneration;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.replay.Replay;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayCallable.TerminationCriteria;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class OverlapCallableGenerator< 	P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										E extends LogEntry> extends AbstractCallableGenerator<P,T,F,M,S>{
	
	private AbstractMarkingGraph<M,S,?,?> markingGraph = null;
	private boolean includeSilentTransitions = SequenceGeneration.DEFAULT_INCLUDE_SILENT_TRANSITIONS;
	private Map<String, String> transitionLabelRelation = null;
	private TerminationCriteria terminationCriteria = null;
	
	public OverlapCallableGenerator(AbstractPetriNet<P,T,F,M,S> petriNet) {
		super(petriNet);
		this.transitionLabelRelation = Replay.getDefaultTransitionLabelRelation(petriNet);
	}

	public AbstractMarkingGraph<M,S,?,?> getMarkingGraph() {
		return markingGraph;
	}
	
	public void setMarkingGraph(AbstractMarkingGraph<M,S,?,?> markingGraph) {
		this.markingGraph = markingGraph;
	}
	
	public boolean isIncludeSilentTransitions() {
		return includeSilentTransitions;
	}
	
	public void setIncludeSilentTransitions(boolean includeSilentTransitions) {
		this.includeSilentTransitions = includeSilentTransitions;
	}
	
	public Map<String, String> getTransitionLabelRelation() {
		return transitionLabelRelation;
	}
	
	public void setTransitionLabelRelation(Map<String, String> transitionLabelRelation) {
		this.transitionLabelRelation = transitionLabelRelation;
	}
	
	public TerminationCriteria getTerminationCriteria() {
		return terminationCriteria;
	}
	
	public void setTerminationCriteria(TerminationCriteria terminationCriteria) {
		this.terminationCriteria = terminationCriteria;
	}

}
