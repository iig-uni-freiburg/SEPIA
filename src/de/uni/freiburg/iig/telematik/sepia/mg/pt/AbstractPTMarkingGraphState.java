package de.uni.freiburg.iig.telematik.sepia.mg.pt;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;

public abstract class AbstractPTMarkingGraphState<M extends AbstractPTMarking> extends AbstractMarkingGraphState<M,Integer>{

	private static final long serialVersionUID = -276267538184053068L;

	public AbstractPTMarkingGraphState(String name, M element) {
		super(name, element);
	}

	public AbstractPTMarkingGraphState(String name) {
		super(name);
	}

}
