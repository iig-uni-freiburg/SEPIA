package de.uni.freiburg.iig.telematik.sepia.event;

import de.invation.code.toval.event.AbstractListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;

public class FlowRelationListenerSupport<F extends AbstractFlowRelation<?,?,?>> extends AbstractListenerSupport<FlowRelationListener<F>>{

	private static final long serialVersionUID = -2755094984147456490L;
	
	public boolean requestNameChangePermission(F relation, String newName){
		boolean nameChangePermitted = true;
		for(FlowRelationListener<F> listener: listeners){
			if(!listener.nameChangeRequest(relation, newName)){
				nameChangePermitted = false;
			}
		}
		return nameChangePermitted;
	}
	
	public void notifyCapacityChanged(RelationConstraintEvent<? extends F> event){
		for(FlowRelationListener<F> l: listeners)
			l.relationConstraintChanged(event);
	}
	
	public void notifyCapacityChanged(F relation){
		for(FlowRelationListener<F> l: listeners)
			l.relationConstraintChanged(new RelationConstraintEvent<F>(relation));
	}

}
