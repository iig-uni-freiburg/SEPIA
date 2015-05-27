package de.uni.freiburg.iig.telematik.sepia.event;

import java.io.Serializable;
import java.util.EventObject;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;


public class TransitionEvent<T extends AbstractTransition<?,?>> extends EventObject implements Serializable{

	private static final long serialVersionUID = 2806756866807041545L;

	public TransitionEvent(T t){
		super(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getSource() {
		return (T) super.getSource();
	}

}
