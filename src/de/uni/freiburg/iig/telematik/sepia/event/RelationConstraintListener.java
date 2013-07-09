package de.uni.freiburg.iig.telematik.sepia.event;

import petrinet.AbstractFlowRelation;

public interface RelationConstraintListener<E extends AbstractFlowRelation<?,?,?>> {
	
	public void relationConstraintChanged(RelationConstraintEvent<? extends E> e);

}