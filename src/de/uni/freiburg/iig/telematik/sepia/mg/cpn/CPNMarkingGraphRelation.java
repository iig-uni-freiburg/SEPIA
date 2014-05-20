package de.uni.freiburg.iig.telematik.sepia.mg.cpn;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;

public class CPNMarkingGraphRelation extends AbstractCPNMarkingGraphRelation<CPNMarking, CPNMarkingGraphState>{

	public CPNMarkingGraphRelation(CPNMarkingGraphState source, CPNMarkingGraphState target, Event event) {
		super(source, target, event);
	}

	public CPNMarkingGraphRelation(CPNMarkingGraphState source, CPNMarkingGraphState target) {
		super(source, target);
	}
	
}
