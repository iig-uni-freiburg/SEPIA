package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet.Boundedness;

public class BoundednessCheckResult<P extends AbstractPlace<F,S>, 
									T extends AbstractTransition<F,S>, 
									F extends AbstractFlowRelation<P,T,S>, 
									M extends AbstractMarking<S>, 
									S extends Object> {
	
	private Boundedness boundedness = null;
	private AbstractMarkingGraph<M,S,?,?> markingGraph = null;
	
	public BoundednessCheckResult(Boundedness boundedness, AbstractMarkingGraph<M,S,?,?> markingGraph) {
		super();
		this.boundedness = boundedness;
		this.markingGraph = markingGraph;
	}

	public Boundedness getBoundedness() {
		return boundedness;
	}

	public AbstractMarkingGraph<M, S, ?, ?> getMarkingGraph() {
		return markingGraph;
	}
	
}
