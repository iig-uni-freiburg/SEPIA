package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public interface TransitionListener<T extends AbstractTransition<?,?>>{
	
	public boolean nameChangeRequest(T transition, String newName);

	public void transitionFired(TransitionEvent<? extends T> e);
	
	public void transitionEnabled(TransitionEvent<? extends T> e);
	
	public void transitionDisabled(TransitionEvent<? extends T> e);
	
}