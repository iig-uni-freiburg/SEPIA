package de.uni.freiburg.iig.telematik.sepia.util.mg.pt;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.util.mg.abstr.AbstractMarkingGraphRelation;

public class PTMarkingGraphRelation extends AbstractMarkingGraphRelation<PTMarking, PTMarkingGraphState, Integer>{

	public PTMarkingGraphRelation(PTMarkingGraphState source, PTMarkingGraphState target, Event event) {
		super(source, target, event);
	}

	public PTMarkingGraphRelation(PTMarkingGraphState source, PTMarkingGraphState target) {
		super(source, target);
	}
	
}
