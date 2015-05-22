package de.uni.freiburg.iig.telematik.sepia.property.sequences;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.overlap.OverlapCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.property.AbstractCallableGenerator;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class SequenceGenerationCallableGenerator< 	P extends AbstractPlace<F,S>, 
													T extends AbstractTransition<F,S>, 
													F extends AbstractFlowRelation<P,T,S>, 
													M extends AbstractMarking<S>, 
													S extends Object,
													X extends AbstractMarkingGraphState<M,S>,
													Y extends AbstractMarkingGraphRelation<M,X,S>> extends AbstractCallableGenerator<P,T,F,M,S>{
	
	private AbstractMarkingGraph<M,S,X,Y> markingGraph = null;
	private boolean includeSilentTransitions = SequenceGeneration.DEFAULT_INCLUDE_SILENT_TRANSITIONS;
	
	public SequenceGenerationCallableGenerator(AbstractPetriNet<P,T,F,M,S> petriNet) {
		super(petriNet);
	}
	
	public <E extends LogEntry> SequenceGenerationCallableGenerator(OverlapCallableGenerator<P,T,F,M,S,X,Y,E> generator) {
		super(generator);
		this.markingGraph = generator.getMarkingGraph();
		this.includeSilentTransitions = generator.isIncludeSilentTransitions();
	}

	public AbstractMarkingGraph<M, S, X, Y> getMarkingGraph() {
		return markingGraph;
	}
	
	public void setMarkingGraph(AbstractMarkingGraph<M, S, X, Y> markingGraph) {
		this.markingGraph = markingGraph;
	}
	
	public boolean isIncludeSilentTransitions() {
		return includeSilentTransitions;
	}
	
	public void setIncludeSilentTransitions(boolean includeSilentTransitions) {
		this.includeSilentTransitions = includeSilentTransitions;
	}

}
