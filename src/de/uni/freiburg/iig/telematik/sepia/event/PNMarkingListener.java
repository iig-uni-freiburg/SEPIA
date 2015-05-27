package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;

public interface PNMarkingListener<S, M extends AbstractMarking<S>>{

	public void markingChanged(MarkingChangeEvent<S,M> markingEvent);
	
	public void initialMarkingChanged(MarkingChangeEvent<S,M> markingEvent);

}
