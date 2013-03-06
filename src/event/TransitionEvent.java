package event;

import java.util.EventObject;

import petrinet.AbstractTransition;

public class TransitionEvent<T extends AbstractTransition<?,?>> extends EventObject {

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
