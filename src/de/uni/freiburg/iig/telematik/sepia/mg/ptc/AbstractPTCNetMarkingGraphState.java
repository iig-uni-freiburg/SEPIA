package de.uni.freiburg.iig.telematik.sepia.mg.ptc;

import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetMarking;

public abstract class AbstractPTCNetMarkingGraphState<M extends AbstractPTCNetMarking>
		extends AbstractMarkingGraphState<M, Integer> {

	private static final long serialVersionUID = -276267538184053068L;

	public AbstractPTCNetMarkingGraphState(String name, M element) {
		super(name, element);
	}

	public AbstractPTCNetMarkingGraphState(String name) {
		super(name);
	}

}
