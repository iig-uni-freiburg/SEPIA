package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.dead;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractCallableGenerator;

public class DeadTransitionCheckCallableGenerator<	P extends AbstractPlace<F,S>, 
													T extends AbstractTransition<F,S>, 
													F extends AbstractFlowRelation<P,T,S>, 
													M extends AbstractMarking<S>, 
													S extends Object> extends AbstractCallableGenerator<P,T,F,M,S>{

	private AbstractMarkingGraph<M,S,?,?> markingGraph = null;
	
	public DeadTransitionCheckCallableGenerator(AbstractPetriNet<P,T,F,M,S> petriNet) {
		super(petriNet);
	}
	
	public AbstractMarkingGraph<M,S,?,?> getMarkingGraph() {
		return markingGraph;
	}
	
	public void setMarkingGraph(AbstractMarkingGraph<M,S,?,?> markingGraph) {
		this.markingGraph = markingGraph;
	}

}
