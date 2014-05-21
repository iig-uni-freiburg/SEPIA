package de.uni.freiburg.iig.telematik.sepia.mg.cpn;

import java.util.Collection;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;

public abstract class AbstractCPNMarkingGraph<M extends AbstractCPNMarking, 
											  X extends AbstractCPNMarkingGraphState<M>, 
											  Y extends AbstractCPNMarkingGraphRelation<M,X>> 

												extends AbstractMarkingGraph<M,Multiset<String>,X,Y> {

	public AbstractCPNMarkingGraph() {
		super();
	}

	public AbstractCPNMarkingGraph(Collection<String> states, Collection<String> events) {
		super(states, events);
	}

	public AbstractCPNMarkingGraph(Collection<String> states) {
		super(states);
	}

	public AbstractCPNMarkingGraph(String name, Collection<String> states, Collection<String> events) {
		super(name, states, events);
	}

	public AbstractCPNMarkingGraph(String name, Collection<String> states) {
		super(name, states);
	}

	public AbstractCPNMarkingGraph(String name) {
		super(name);
	}

}
