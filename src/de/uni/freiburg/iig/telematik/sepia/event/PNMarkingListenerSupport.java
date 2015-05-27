package de.uni.freiburg.iig.telematik.sepia.event;

import de.invation.code.toval.event.AbstractListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;

public class PNMarkingListenerSupport<S,M extends AbstractMarking<S>> extends AbstractListenerSupport<PNMarkingListener<S,M>>{

	private static final long serialVersionUID = -9115760450580421633L;
	
	public void notifyMarkingChanged(MarkingChangeEvent<S,M> event){
		for(PNMarkingListener<S,M> l: listeners)
			l.markingChanged(event);
	}
	
	public void notifyInitialMarkingChanged(MarkingChangeEvent<S,M> event){
		for(PNMarkingListener<S,M> l: listeners)
			l.initialMarkingChanged(event);
	}

}
