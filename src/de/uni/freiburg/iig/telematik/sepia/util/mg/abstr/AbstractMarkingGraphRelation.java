package de.uni.freiburg.iig.telematik.sepia.util.mg.abstr;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;

public abstract class AbstractMarkingGraphRelation<	M extends AbstractMarking<O>,
													S extends AbstractMarkingGraphState<M,O>, 
													O extends Object> 

													extends AbstractLabeledTransitionRelation<S, Event, M>{

	public AbstractMarkingGraphRelation(S source, S target, Event event) {
		super(source, target, event);
	}

	public AbstractMarkingGraphRelation(S source, S target) {
		super(source, target);
	}

	
	
}
