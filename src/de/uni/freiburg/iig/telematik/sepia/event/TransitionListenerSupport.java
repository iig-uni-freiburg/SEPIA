package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.HashSet;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;

import petrinet.AbstractTransition;

public class TransitionListenerSupport<T extends AbstractTransition<?,?>> {
	
	protected HashSet<TransitionListener<T>> transitionListeners = new HashSet<TransitionListener<T>>();
	
	public void addTransitionListener(TransitionListener<T> l) throws ParameterException {
		Validate.notNull(l);
		transitionListeners.add(l);
	}
	
	public void removeTransitionListener(TransitionListener<T> l) throws ParameterException {
		Validate.notNull(l);
		transitionListeners.remove(l);
	}
	public void notifyEnabling(TransitionEvent<? extends T> e){
		for(TransitionListener<T> l: transitionListeners)
			l.transitionEnabled(e);
	}
	
	public void notifyDisabling(TransitionEvent<? extends T> e){
		for(TransitionListener<T> l: transitionListeners)
			l.transitionDisabled(e);
	}
	
	public void notifyFiring(TransitionEvent<? extends T> e){
		for(TransitionListener<T> l: transitionListeners)
			l.transitionFired(e);
	}
	


}
