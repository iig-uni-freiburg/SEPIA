package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;

public class FlowRelationListenerSupport<F extends AbstractFlowRelation<?,?,?>> {
	
	private Set<FlowRelationListener<F>> listeners = new HashSet<FlowRelationListener<F>>();
	
	public void addListener(FlowRelationListener<F> listener) throws ParameterException{
		Validate.notNull(listener);
		listeners.add(listener);
	}
	
	public void removeListener(FlowRelationListener<F> listener) throws ParameterException{
		Validate.notNull(listener);
		listeners.remove(listener);
	}
	
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
