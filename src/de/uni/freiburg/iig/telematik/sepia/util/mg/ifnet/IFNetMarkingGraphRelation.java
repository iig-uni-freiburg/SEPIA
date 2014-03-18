package de.uni.freiburg.iig.telematik.sepia.util.mg.ifnet;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;

public class IFNetMarkingGraphRelation extends AbstractIFNetMarkingGraphRelation<IFNetMarking, IFNetMarkingGraphState>{

	public IFNetMarkingGraphRelation(IFNetMarkingGraphState source, IFNetMarkingGraphState target, Event event) {
		super(source, target, event);
	}

	public IFNetMarkingGraphRelation(IFNetMarkingGraphState source, IFNetMarkingGraphState target) {
		super(source, target);
	}
	
}
