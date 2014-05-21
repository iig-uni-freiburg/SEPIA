package de.uni.freiburg.iig.telematik.sepia.mg.pt;

import java.util.Collection;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;

public abstract class AbstractPTMarkingGraph<M extends AbstractPTMarking, 
											 S extends AbstractPTMarkingGraphState<M>, 
											 R extends AbstractPTMarkingGraphRelation<M,S>> 

											   extends AbstractMarkingGraph<M,Integer,S,R> {

	public AbstractPTMarkingGraph() {
		super();
	}

	public AbstractPTMarkingGraph(Collection<String> states, Collection<String> events) throws ParameterException {
		super(states, events);
	}

	public AbstractPTMarkingGraph(Collection<String> states) throws ParameterException {
		super(states);
	}

	public AbstractPTMarkingGraph(String name, Collection<String> states, Collection<String> events) throws ParameterException {
		super(name, states, events);
	}

	public AbstractPTMarkingGraph(String name, Collection<String> states) throws ParameterException {
		super(name, states);
	}

	public AbstractPTMarkingGraph(String name) throws ParameterException {
		super(name);
	}

}
