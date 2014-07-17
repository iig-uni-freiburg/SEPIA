package de.uni.freiburg.iig.telematik.sepia.mg.pt;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;

public abstract class AbstractPTMarkingGraph<M extends AbstractPTMarking, 
											 S extends AbstractPTMarkingGraphState<M>, 
											 R extends AbstractPTMarkingGraphRelation<M,S>> 

											   extends AbstractMarkingGraph<M,Integer,S,R> {

	private static final long serialVersionUID = 5037378617375529917L;

	public AbstractPTMarkingGraph() {
		super();
	}

	public AbstractPTMarkingGraph(Collection<String> states, Collection<String> events) {
		super(states, events);
	}

	public AbstractPTMarkingGraph(Collection<String> states) {
		super(states);
	}

	public AbstractPTMarkingGraph(String name, Collection<String> states, Collection<String> events) {
		super(name, states, events);
	}

	public AbstractPTMarkingGraph(String name, Collection<String> states) {
		super(name, states);
	}

	public AbstractPTMarkingGraph(String name) {
		super(name);
	}

}
