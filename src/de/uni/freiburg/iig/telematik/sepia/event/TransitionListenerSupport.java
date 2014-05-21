package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.HashSet;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;


public class TransitionListenerSupport<T extends AbstractTransition<?,?>> {
	
	protected HashSet<TransitionListener<T>> listeners = new HashSet<TransitionListener<T>>();
	
	public void addTransitionListener(TransitionListener<T> l) {
		Validate.notNull(l);
		listeners.add(l);
	}
	
	public void removeTransitionListener(TransitionListener<T> l) {
		Validate.notNull(l);
		listeners.remove(l);
	}
	public void notifyEnabling(TransitionEvent<? extends T> e){
		for(TransitionListener<T> l: listeners)
			l.transitionEnabled(e);
	}
	
	public void notifyDisabling(TransitionEvent<? extends T> e){
		for(TransitionListener<T> l: listeners)
			l.transitionDisabled(e);
	}
	
	public void notifyFiring(TransitionEvent<? extends T> e){
		for(TransitionListener<T> l: listeners)
			l.transitionFired(e);
	}
	
	public boolean requestNameChangePermission(T transition, String newName){
		boolean nameChangePermitted = true;
		for(TransitionListener<T> listener: listeners){
			if(!listener.nameChangeRequest(transition, newName)){
				nameChangePermitted = false;
			}
		}
		return nameChangePermitted;
	}

}
