package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.EventObject;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;


public class RelationChangeEvent<F extends AbstractFlowRelation<?,?,?>> extends EventObject {
	
	private static final long serialVersionUID = 3290980580774384615L;
	
	public F relation = null;

	public RelationChangeEvent(F relation) {
		super(relation);
		this.relation = relation;
	}

}
