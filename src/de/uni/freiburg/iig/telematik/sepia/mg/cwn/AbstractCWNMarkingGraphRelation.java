package de.uni.freiburg.iig.telematik.sepia.mg.cwn;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;

public abstract class AbstractCWNMarkingGraphRelation<M extends AbstractCWNMarking, T extends AbstractCWNMarkingGraphState<M>> extends AbstractCPNMarkingGraphRelation<M, T> {

	private static final long serialVersionUID = -1288065488534835917L;

	public AbstractCWNMarkingGraphRelation(T source, T target, Event event) {
		super(source, target, event);
	}

	public AbstractCWNMarkingGraphRelation(T source, T target) {
		super(source, target);
	}

}
