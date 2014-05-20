package de.uni.freiburg.iig.telematik.sepia.mg.cpn;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;

public abstract class AbstractCPNMarkingGraphRelation<M extends AbstractCPNMarking, T extends AbstractCPNMarkingGraphState<M>> extends AbstractMarkingGraphRelation<M, T, Multiset<String>> {

	public AbstractCPNMarkingGraphRelation(T source, T target, Event event) {
		super(source, target, event);
	}

	public AbstractCPNMarkingGraphRelation(T source, T target) {
		super(source, target);
	}

}
