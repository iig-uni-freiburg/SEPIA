package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.sequences;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.overlap.OverlapCallableGenerator;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractCallableGenerator;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class SequenceGenerationCallableGenerator< 	P extends AbstractPlace<F,S>, 
													T extends AbstractTransition<F,S>, 
													F extends AbstractFlowRelation<P,T,S>, 
													M extends AbstractMarking<S>, 
													S extends Object> extends AbstractCallableGenerator<P,T,F,M,S>{
	
	private AbstractMarkingGraph<M,S,?,?> markingGraph = null;
	private boolean includeSilentTransitions = SequenceGeneration.DEFAULT_INCLUDE_SILENT_TRANSITIONS;
	
	public SequenceGenerationCallableGenerator(AbstractPetriNet<P,T,F,M,S> petriNet) {
		super(petriNet);
	}
	
	public <E extends LogEntry> SequenceGenerationCallableGenerator(OverlapCallableGenerator<P,T,F,M,S,E> generator) {
		super(generator);
		this.markingGraph = generator.getMarkingGraph();
		this.includeSilentTransitions = generator.isIncludeSilentTransitions();
	}

	@SuppressWarnings("unchecked")
	public <X extends AbstractMarkingGraphState<M,S>, Y extends AbstractMarkingGraphRelation<M,X,S>> AbstractMarkingGraph<M,S,X,?> getMarkingGraph() {
		return (AbstractMarkingGraph<M,S,X,Y>) markingGraph;
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

}
