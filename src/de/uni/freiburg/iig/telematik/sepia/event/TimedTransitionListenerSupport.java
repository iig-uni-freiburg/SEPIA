package de.uni.freiburg.iig.telematik.sepia.event;

import de.invation.code.toval.event.AbstractListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class TimedTransitionListenerSupport<T extends AbstractTransition<?,?>> extends AbstractListenerSupport<TimedTransitionListener<T>>{
	
	private static final long serialVersionUID = -6155890470158946909L;

	
	public void notifyFiring(TimedTransitionEvent<? extends T> e){
		for(TimedTransitionListener<T> l: listeners)
			l.transitionFired(e);
	}

}
