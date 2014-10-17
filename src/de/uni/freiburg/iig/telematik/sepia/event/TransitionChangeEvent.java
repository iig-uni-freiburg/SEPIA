package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.EventObject;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;


public class TransitionChangeEvent<T extends AbstractTransition<?,?>> extends EventObject {
	
	private static final long serialVersionUID = -5415894461740664843L;
	
	public T transition = null;
	public int affectedRelations = 0;

	public TransitionChangeEvent(T transition) {
		super(transition);
		this.transition = transition;
	}
	
	public TransitionChangeEvent(T transition, int affectedRelations) {
		this(transition);
		this.affectedRelations = affectedRelations;
	}

}
