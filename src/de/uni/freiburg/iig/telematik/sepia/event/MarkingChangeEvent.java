package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.EventObject;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;


public class MarkingChangeEvent<S,M extends AbstractMarking<S>> extends EventObject {
	
	private static final long serialVersionUID = 4688197676477186284L;
	
	public M marking = null;

	public MarkingChangeEvent(M marking) {
		super(marking);
		this.marking = marking;
	}

}
