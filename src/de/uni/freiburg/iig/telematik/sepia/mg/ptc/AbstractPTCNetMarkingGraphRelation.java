package de.uni.freiburg.iig.telematik.sepia.mg.ptc;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetMarking;

public abstract class AbstractPTCNetMarkingGraphRelation<M extends AbstractPTCNetMarking, T extends AbstractPTCNetMarkingGraphState<M>>
		extends AbstractMarkingGraphRelation<M, T, Integer> {

	private static final long serialVersionUID = -2935362128969182038L;

	public AbstractPTCNetMarkingGraphRelation(T source, T target, Event event) {
		super(source, target, event);
	}

	public AbstractPTCNetMarkingGraphRelation(T source, T target) {
		super(source, target);
	}

}
