package de.uni.freiburg.iig.telematik.sepia.util.mg.cpn;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.abstr.AbstractMarkingGraphState;

public abstract class AbstractCPNMarkingGraphState<M extends AbstractCPNMarking> extends AbstractMarkingGraphState<M, Multiset<String>>{

	public AbstractCPNMarkingGraphState(String name, M element) {
		super(name, element);
	}

	public AbstractCPNMarkingGraphState(String name) {
		super(name);
	}

}
