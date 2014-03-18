package de.uni.freiburg.iig.telematik.sepia.util.mg.cpn;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.abstr.AbstractMarkingGraphRelation;

public abstract class AbstractCPNMarkingGraphRelation<M extends AbstractCPNMarking, T extends AbstractCPNMarkingGraphState<M>> extends AbstractMarkingGraphRelation<M, T, Multiset<String>> {

	public AbstractCPNMarkingGraphRelation(T source, T target, Event event) {
		super(source, target, event);
	}

	public AbstractCPNMarkingGraphRelation(T source, T target) {
		super(source, target);
	}

}
