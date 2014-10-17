package de.uni.freiburg.iig.telematik.sepia.event;

import de.invation.code.toval.event.AbstractListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class TransitionListenerSupport<T extends AbstractTransition<?,?>> extends AbstractListenerSupport<TransitionListener<T>>{
	
	private static final long serialVersionUID = -6155890470158946909L;
	
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
