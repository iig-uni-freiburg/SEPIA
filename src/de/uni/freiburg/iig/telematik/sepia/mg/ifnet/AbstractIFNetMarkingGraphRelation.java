package de.uni.freiburg.iig.telematik.sepia.mg.ifnet;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;

public abstract class AbstractIFNetMarkingGraphRelation<M extends AbstractIFNetMarking, T extends AbstractIFNetMarkingGraphState<M>> extends AbstractCPNMarkingGraphRelation<M, T> {

	private static final long serialVersionUID = -145868426288234199L;

	public AbstractIFNetMarkingGraphRelation(T source, T target, Event event) {
		super(source, target, event);
	}

	public AbstractIFNetMarkingGraphRelation(T source, T target) {
		super(source, target);
	}

}
