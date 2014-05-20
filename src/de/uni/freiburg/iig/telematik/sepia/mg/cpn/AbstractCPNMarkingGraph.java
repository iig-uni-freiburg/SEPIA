package de.uni.freiburg.iig.telematik.sepia.mg.cpn;

import java.util.Collection;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;

public abstract class AbstractCPNMarkingGraph<M extends AbstractCPNMarking, S extends AbstractCPNMarkingGraphState<M>, R extends AbstractCPNMarkingGraphRelation<M, S>> extends AbstractMarkingGraph<M, Multiset<String>, S, R> {

	public AbstractCPNMarkingGraph() {
		super();
	}

	public AbstractCPNMarkingGraph(Collection<String> states, Collection<String> events) throws ParameterException {
		super(states, events);
	}

	public AbstractCPNMarkingGraph(Collection<String> states) throws ParameterException {
		super(states);
	}

	public AbstractCPNMarkingGraph(String name, Collection<String> states, Collection<String> events) throws ParameterException {
		super(name, states, events);
	}

	public AbstractCPNMarkingGraph(String name, Collection<String> states) throws ParameterException {
		super(name, states);
	}

	public AbstractCPNMarkingGraph(String name) throws ParameterException {
		super(name);
	}

}
