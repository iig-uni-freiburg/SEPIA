package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;

public interface RelationConstraintListener<E extends AbstractFlowRelation<?,?,?>> {
	
	public void relationConstraintChanged(RelationConstraintEvent<? extends E> e);

}