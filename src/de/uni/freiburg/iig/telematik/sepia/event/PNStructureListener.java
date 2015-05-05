package de.uni.freiburg.iig.telematik.sepia.event;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

public interface PNStructureListener<P extends AbstractPlace<F,S>, 
								   T extends AbstractTransition<F,S>, 
								   F extends AbstractFlowRelation<P,T,S>,
								   M extends AbstractMarking<S>,
								   S extends Object> {

	public void structureChanged();
	
	public void placeAdded(PlaceChangeEvent<P> event);
	
	public void placeRemoved(PlaceChangeEvent<P> event);
	
	public void transitionAdded(TransitionChangeEvent<T> event);
	
	public void transitionRemoved(TransitionChangeEvent<T> event);
	
	public void relationAdded(RelationChangeEvent<F> event);
	
	public void relationRemoved(RelationChangeEvent<F> event);

}
