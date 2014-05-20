package de.uni.freiburg.iig.telematik.sepia.mg.cwn;

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;

public abstract class AbstractCWNMarkingGraph<M extends AbstractCWNMarking, S extends AbstractCWNMarkingGraphState<M>, R extends AbstractCWNMarkingGraphRelation<M, S>> extends AbstractCPNMarkingGraph<M, S, R> {

	public AbstractCWNMarkingGraph() {
		super();
	}

	public AbstractCWNMarkingGraph(Collection<String> states, Collection<String> events) throws ParameterException {
		super(states, events);
	}

	public AbstractCWNMarkingGraph(Collection<String> states) throws ParameterException {
		super(states);
	}

	public AbstractCWNMarkingGraph(String name, Collection<String> states, Collection<String> events) throws ParameterException {
		super(name, states, events);
	}

	public AbstractCWNMarkingGraph(String name, Collection<String> states) throws ParameterException {
		super(name, states);
	}

	public AbstractCWNMarkingGraph(String name) throws ParameterException {
		super(name);
	}

}
