package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public interface TimedTransitionListener<T extends AbstractTransition<?,?>>{
	
	public boolean nameChangeRequest(T transition, String newName);

	public void transitionFired(TimedTransitionEvent<? extends T> e);
	
	public void transitionEnabled(TimedTransitionEvent<? extends T> e);
	
	public void transitionDisabled(TimedTransitionEvent<? extends T> e);

}
