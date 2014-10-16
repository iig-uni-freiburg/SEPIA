package de.uni.freiburg.iig.telematik.sepia.mg.ifnet;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;

public abstract class AbstractIFNetMarkingGraph<M extends AbstractIFNetMarking, S extends AbstractIFNetMarkingGraphState<M>, R extends AbstractIFNetMarkingGraphRelation<M, S>> extends AbstractCPNMarkingGraph<M, S, R> {

	private static final long serialVersionUID = 7815350302838795970L;

	public AbstractIFNetMarkingGraph() {
		super();
	}

	public AbstractIFNetMarkingGraph(Collection<String> states, Collection<String> events) {
		super(states, events);
	}

	public AbstractIFNetMarkingGraph(Collection<String> states) {
		super(states);
	}

	public AbstractIFNetMarkingGraph(String name, Collection<String> states, Collection<String> events) {
		super(name, states, events);
	}

	public AbstractIFNetMarkingGraph(String name, Collection<String> states) {
		super(name, states);
	}

	public AbstractIFNetMarkingGraph(String name) {
		super(name);
	}
}
