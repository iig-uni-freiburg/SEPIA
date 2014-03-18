package de.uni.freiburg.iig.telematik.sepia.util.mg.cwn;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;

public class CWNMarkingGraphRelation extends AbstractCWNMarkingGraphRelation<CWNMarking, CWNMarkingGraphState>{

	public CWNMarkingGraphRelation(CWNMarkingGraphState source, CWNMarkingGraphState target, Event event) {
		super(source, target, event);
	}

	public CWNMarkingGraphRelation(CWNMarkingGraphState source, CWNMarkingGraphState target) {
		super(source, target);
	}
	
}
