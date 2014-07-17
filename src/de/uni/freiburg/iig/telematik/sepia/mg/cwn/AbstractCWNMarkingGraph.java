package de.uni.freiburg.iig.telematik.sepia.mg.cwn;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;

public abstract class AbstractCWNMarkingGraph<M extends AbstractCWNMarking, S extends AbstractCWNMarkingGraphState<M>, R extends AbstractCWNMarkingGraphRelation<M, S>> extends AbstractCPNMarkingGraph<M, S, R> {

	private static final long serialVersionUID = -3000793765683261387L;

	public AbstractCWNMarkingGraph() {
		super();
	}

	public AbstractCWNMarkingGraph(Collection<String> states, Collection<String> events) {
		super(states, events);
	}

	public AbstractCWNMarkingGraph(Collection<String> states) {
		super(states);
	}

	public AbstractCWNMarkingGraph(String name, Collection<String> states, Collection<String> events) {
		super(name, states, events);
	}

	public AbstractCWNMarkingGraph(String name, Collection<String> states) {
		super(name, states);
	}

	public AbstractCWNMarkingGraph(String name) {
		super(name);
	}

}
