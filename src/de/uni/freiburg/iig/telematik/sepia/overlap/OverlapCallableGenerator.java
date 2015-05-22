package de.uni.freiburg.iig.telematik.sepia.overlap;

import java.util.Map;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.property.sequences.SequenceGeneration;
import de.uni.freiburg.iig.telematik.sepia.replay.Replay;
import de.uni.freiburg.iig.telematik.sepia.replay.ReplayCallable.TerminationCriteria;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class OverlapCallableGenerator< 	P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										X extends AbstractMarkingGraphState<M,S>,
										Y extends AbstractMarkingGraphRelation<M,X,S>,
										E extends LogEntry> extends AbstractCallableGenerator<P,T,F,M,S>{
	
	private AbstractMarkingGraph<M,S,X,Y> markingGraph = null;
	private boolean includeSilentTransitions = SequenceGeneration.DEFAULT_INCLUDE_SILENT_TRANSITIONS;
	private Map<String, String> transitionLabelRelation = null;
	private TerminationCriteria terminationCriteria = null;
	
	public OverlapCallableGenerator(AbstractPetriNet<P,T,F,M,S> petriNet) {
		super(petriNet);
		this.transitionLabelRelation = Replay.getDefaultTransitionLabelRelation(petriNet);
	}

	public AbstractMarkingGraph<M,S,X,Y> getMarkingGraph() {
		return markingGraph;
	}
	
	public void setMarkingGraph(AbstractMarkingGraph<M,S,X,Y> markingGraph) {
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
