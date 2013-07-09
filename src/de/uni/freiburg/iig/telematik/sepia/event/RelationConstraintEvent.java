package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.EventObject;

import petrinet.AbstractFlowRelation;

@SuppressWarnings("serial")
public class RelationConstraintEvent<E extends AbstractFlowRelation<?,?,?>> extends EventObject {
	
	public RelationConstraintEvent(E source) {
		super(source);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E getSource() {
		return (E) super.getSource();
	}

}
