package de.uni.freiburg.iig.telematik.sepia.mg.pt;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;

public abstract class AbstractPTMarkingGraphRelation<M extends AbstractPTMarking, T extends AbstractPTMarkingGraphState<M>> extends AbstractMarkingGraphRelation<M,T,Integer> {

	public AbstractPTMarkingGraphRelation(T source, T target, Event event) {
		super(source, target, event);
	}

	public AbstractPTMarkingGraphRelation(T source, T target) {
		super(source, target);
	}

}
