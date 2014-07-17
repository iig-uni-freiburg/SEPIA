package de.uni.freiburg.iig.telematik.sepia.mg.cpn;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;

public abstract class AbstractCPNMarkingGraphState<M extends AbstractCPNMarking> extends AbstractMarkingGraphState<M, Multiset<String>>{

	private static final long serialVersionUID = 8768103138913591438L;

	public AbstractCPNMarkingGraphState(String name, M element) {
		super(name, element);
	}

	public AbstractCPNMarkingGraphState(String name) {
		super(name);
	}

}
