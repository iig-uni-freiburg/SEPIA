package de.uni.freiburg.iig.telematik.sepia.util.mg.ifnet;

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.cwn.AbstractCWNMarkingGraph;

public abstract class AbstractIFNetMarkingGraph<M extends AbstractIFNetMarking, S extends AbstractIFNetMarkingGraphState<M>, R extends AbstractIFNetMarkingGraphRelation<M, S>> extends AbstractCWNMarkingGraph<M, S, R> {

	public AbstractIFNetMarkingGraph() {
		super();
	}

	public AbstractIFNetMarkingGraph(Collection<String> states, Collection<String> events) throws ParameterException {
		super(states, events);
	}

	public AbstractIFNetMarkingGraph(Collection<String> states) throws ParameterException {
		super(states);
	}

	public AbstractIFNetMarkingGraph(String name, Collection<String> states, Collection<String> events) throws ParameterException {
		super(name, states, events);
	}

	public AbstractIFNetMarkingGraph(String name, Collection<String> states) throws ParameterException {
		super(name, states);
	}

	public AbstractIFNetMarkingGraph(String name) throws ParameterException {
		super(name);
	}

}
