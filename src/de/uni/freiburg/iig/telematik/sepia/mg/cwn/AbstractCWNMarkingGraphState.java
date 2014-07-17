package de.uni.freiburg.iig.telematik.sepia.mg.cwn;

import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;

public abstract class AbstractCWNMarkingGraphState<M extends AbstractCWNMarking> extends AbstractCPNMarkingGraphState<M>{

	private static final long serialVersionUID = 7408750566963034740L;

	public AbstractCWNMarkingGraphState(String name, M element) {
		super(name, element);
	}

	public AbstractCWNMarkingGraphState(String name) {
		super(name);
	}

}
