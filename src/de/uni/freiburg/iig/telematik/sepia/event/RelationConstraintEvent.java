package de.uni.freiburg.iig.telematik.sepia.event;

import java.io.Serializable;
import java.util.EventObject;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;


public class RelationConstraintEvent<E extends AbstractFlowRelation<?,?,?>> extends EventObject implements Serializable{
	
	private static final long serialVersionUID = 2802519130589510300L;

	public RelationConstraintEvent(E source) {
		super(source);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E getSource() {
		return (E) super.getSource();
	}

}
