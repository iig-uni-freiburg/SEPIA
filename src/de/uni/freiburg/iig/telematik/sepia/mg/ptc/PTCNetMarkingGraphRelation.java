package de.uni.freiburg.iig.telematik.sepia.mg.ptc;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetMarking;

public class PTCNetMarkingGraphRelation
		extends AbstractPTCNetMarkingGraphRelation<PTCNetMarking, PTCNetMarkingGraphState> {

	private static final long serialVersionUID = -2317692961792324586L;

	public PTCNetMarkingGraphRelation(PTCNetMarkingGraphState source, PTCNetMarkingGraphState target, Event event) {
		super(source, target, event);
	}

	public PTCNetMarkingGraphRelation(PTCNetMarkingGraphState source, PTCNetMarkingGraphState target) {
		super(source, target);
	}

}
