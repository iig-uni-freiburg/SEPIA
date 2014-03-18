package de.uni.freiburg.iig.telematik.sepia.util.mg.cwn;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.cpn.AbstractCPNMarkingGraphRelation;

public abstract class AbstractCWNMarkingGraphRelation<M extends AbstractCWNMarking, T extends AbstractCWNMarkingGraphState<M>> extends AbstractCPNMarkingGraphRelation<M, T> {

	public AbstractCWNMarkingGraphRelation(T source, T target, Event event) {
		super(source, target, event);
	}

	public AbstractCWNMarkingGraphRelation(T source, T target) {
		super(source, target);
	}

}
