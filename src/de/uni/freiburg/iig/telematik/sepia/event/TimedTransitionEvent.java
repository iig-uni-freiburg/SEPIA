package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public class TimedTransitionEvent<T extends AbstractTransition<?,?>> extends TransitionEvent<T> {

	private static final long serialVersionUID = -7370798435971705217L;
	
	private Double time = null;

	public TimedTransitionEvent(T t) {
		super(t);
	}

	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}
}
