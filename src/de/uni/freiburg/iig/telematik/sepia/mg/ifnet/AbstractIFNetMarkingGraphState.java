package de.uni.freiburg.iig.telematik.sepia.mg.ifnet;

import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;

public abstract class AbstractIFNetMarkingGraphState<M extends AbstractIFNetMarking> extends AbstractCPNMarkingGraphState<M>{

	private static final long serialVersionUID = -7638849038852523376L;

	public AbstractIFNetMarkingGraphState(String name, M element) {
		super(name, element);
	}

	public AbstractIFNetMarkingGraphState(String name) {
		super(name);
	}

}
