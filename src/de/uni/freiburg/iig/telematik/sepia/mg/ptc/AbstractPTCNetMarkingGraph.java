package de.uni.freiburg.iig.telematik.sepia.mg.ptc;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetMarking;

public abstract class AbstractPTCNetMarkingGraph<M extends AbstractPTCNetMarking, S extends AbstractPTCNetMarkingGraphState<M>, R extends AbstractPTCNetMarkingGraphRelation<M, S>>

		extends AbstractMarkingGraph<M, Integer, S, R> {

	private static final long serialVersionUID = 5037378617375529917L;

	public AbstractPTCNetMarkingGraph() {
		super();
	}

	public AbstractPTCNetMarkingGraph(Collection<String> states, Collection<String> events) {
		super(states, events);
	}

	public AbstractPTCNetMarkingGraph(Collection<String> states) {
		super(states);
	}

	public AbstractPTCNetMarkingGraph(String name, Collection<String> states, Collection<String> events) {
		super(name, states, events);
	}

	public AbstractPTCNetMarkingGraph(String name, Collection<String> states) {
		super(name, states);
	}

	public AbstractPTCNetMarkingGraph(String name) {
		super(name);
	}

}
