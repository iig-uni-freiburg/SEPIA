package de.uni.freiburg.iig.telematik.sepia.util.mg.cwn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.cpn.AbstractCPNMarkingGraphState;

public abstract class AbstractCWNMarkingGraphState<M extends AbstractCWNMarking> extends AbstractCPNMarkingGraphState<M>{

	public AbstractCWNMarkingGraphState(String name, M element) {
		super(name, element);
	}

	public AbstractCWNMarkingGraphState(String name) {
		super(name);
	}

}
