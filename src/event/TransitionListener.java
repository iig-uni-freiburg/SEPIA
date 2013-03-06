package event;

import petrinet.AbstractTransition;

public interface TransitionListener<T extends AbstractTransition<?,?>> {

	public void transitionFired(TransitionEvent<? extends T> e);
	
	public void transitionEnabled(TransitionEvent<? extends T> e);
	
	public void transitionDisabled(TransitionEvent<? extends T> e);
	
}