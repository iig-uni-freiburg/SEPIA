package de.uni.freiburg.iig.telematik.sepia.mg.cwn;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;

public class CWNMarkingGraphRelation extends AbstractCWNMarkingGraphRelation<CWNMarking, CWNMarkingGraphState>{

	private static final long serialVersionUID = 8316177700987832268L;

	public CWNMarkingGraphRelation(CWNMarkingGraphState source, CWNMarkingGraphState target, Event event) {
		super(source, target, event);
	}

	public CWNMarkingGraphRelation(CWNMarkingGraphState source, CWNMarkingGraphState target) {
		super(source, target);
	}
	
}
