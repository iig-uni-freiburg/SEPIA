package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public class TimedTransitionEvent<T extends AbstractTransition<?,?>> extends TransitionEvent<T> {

	private static final long serialVersionUID = -7370798435971705217L;
	
	private Long time = null;

	public TimedTransitionEvent(T t) {
		super(t);
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
}
