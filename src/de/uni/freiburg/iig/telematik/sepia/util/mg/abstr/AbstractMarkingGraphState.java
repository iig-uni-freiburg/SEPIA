package de.uni.freiburg.iig.telematik.sepia.util.mg.abstr;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLTSState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;

public abstract class AbstractMarkingGraphState<M extends AbstractMarking<O>, O extends Object> extends AbstractLTSState<Event, M>{

	protected AbstractMarkingGraphState(String name) {
		super(name);
	}

	public AbstractMarkingGraphState(String name, M element) {
		super(name, element);
	}
	
}
