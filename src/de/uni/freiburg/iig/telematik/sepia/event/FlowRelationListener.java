package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;


public interface FlowRelationListener<F extends AbstractFlowRelation<?,?,?>> {
	
	public boolean nameChangeRequest(F relation, String newName);
	
	public void relationConstraintChanged(RelationConstraintEvent<? extends F> e);

}
