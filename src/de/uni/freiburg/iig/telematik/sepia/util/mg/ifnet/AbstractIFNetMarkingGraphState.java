package de.uni.freiburg.iig.telematik.sepia.util.mg.ifnet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.cwn.AbstractCWNMarkingGraphState;

public abstract class AbstractIFNetMarkingGraphState<M extends AbstractIFNetMarking> extends AbstractCWNMarkingGraphState<M>{

	public AbstractIFNetMarkingGraphState(String name, M element) {
		super(name, element);
	}

	public AbstractIFNetMarkingGraphState(String name) {
		super(name);
	}

}
