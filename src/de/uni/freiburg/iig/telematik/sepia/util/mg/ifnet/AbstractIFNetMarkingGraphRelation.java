package de.uni.freiburg.iig.telematik.sepia.util.mg.ifnet;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.cwn.AbstractCWNMarkingGraphRelation;

public abstract class AbstractIFNetMarkingGraphRelation<M extends AbstractIFNetMarking, T extends AbstractIFNetMarkingGraphState<M>> extends AbstractCWNMarkingGraphRelation<M, T> {

	public AbstractIFNetMarkingGraphRelation(T source, T target, Event event) {
		super(source, target, event);
	}

	public AbstractIFNetMarkingGraphRelation(T source, T target) {
		super(source, target);
	}

}
