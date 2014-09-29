package de.uni.freiburg.iig.telematik.sepia.event;

import java.io.Serializable;
import java.util.HashSet;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;


public class TimedTransitionListenerSupport<T extends AbstractTransition<?,?>> implements Serializable{
	
	private static final long serialVersionUID = -6155890470158946909L;
	
	protected HashSet<TimedTransitionListener<T>> listeners = new HashSet<TimedTransitionListener<T>>();
	
	public void addTimedTransitionListener(TimedTransitionListener<T> l) {
		Validate.notNull(l);
		listeners.add(l);
	}
	
	public void removeTimedTransitionListener(TimedTransitionListener<T> l) {
		Validate.notNull(l);
		listeners.remove(l);
	}
	
	public void notifyFiring(TimedTransitionEvent<? extends T> e){
		for(TimedTransitionListener<T> l: listeners)
			l.transitionFired(e);
	}

}
