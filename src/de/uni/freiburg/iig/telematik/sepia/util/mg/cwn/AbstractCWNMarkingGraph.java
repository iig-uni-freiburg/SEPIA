package de.uni.freiburg.iig.telematik.sepia.util.mg.cwn;

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.cpn.AbstractCPNMarkingGraph;

public abstract class AbstractCWNMarkingGraph<M extends AbstractCWNMarking, T extends AbstractCWNMarkingGraphState<M>> extends AbstractCPNMarkingGraph<M, T> {

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
