package event;

import java.util.HashSet;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;

import petrinet.AbstractFlowRelation;

public class RelationConstraintListenerSupport<E extends AbstractFlowRelation<?,?,?>> {
	
	protected HashSet<RelationConstraintListener<E>> capacityListeners = new HashSet<RelationConstraintListener<E>>();
	
	public void addCapacityListener(RelationConstraintListener<E> l) throws ParameterException {
		Validate.notNull(l);
		capacityListeners.add(l);
	}
	
	public void removeCapacityListener(RelationConstraintListener<E> l) throws ParameterException {
		Validate.notNull(l);
		capacityListeners.remove(l);
	}
	
	public void notifyCapacityChanged(RelationConstraintEvent<? extends E> event){
		for(RelationConstraintListener<E> l: capacityListeners)
			l.relationConstraintChanged(event);
	}
	
	public void notifyCapacityChanged(E relation){
		for(RelationConstraintListener<E> l: capacityListeners)
			l.relationConstraintChanged(new RelationConstraintEvent<E>(relation));
	}

}
