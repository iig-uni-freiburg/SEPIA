package de.uni.freiburg.iig.telematik.sepia.event;

import de.invation.code.toval.event.AbstractListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;


public class PNStructureListenerSupport<P extends AbstractPlace<F,S>, 
									  T extends AbstractTransition<F,S>, 
									  F extends AbstractFlowRelation<P,T,S>,
									  M extends AbstractMarking<S>,
									  S extends Object> extends AbstractListenerSupport<PNStructureListener<P,T,F,M,S>>{
	
	private static final long serialVersionUID = 2706896428707837624L;
	
	private void notifyStructureChange(){
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.structureChanged();
	}
	
	public void notifyPlaceAdded(P place){
		notifyPlaceAdded(place, 0);
	}
	
	public void notifyPlaceAdded(P place, int affectedRelations){
		PlaceChangeEvent<P> event = new PlaceChangeEvent<P>(place, affectedRelations);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.placeAdded(event);
		notifyStructureChange();
	}
	
	public void notifyPlaceRemoved(P place, int affectedRelations){
		PlaceChangeEvent<P> event = new PlaceChangeEvent<P>(place, affectedRelations);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.placeRemoved(event);
		notifyStructureChange();
	}
	
	public void notifyTransitionAdded(T transition){
		notifyTransitionAdded(transition, 0);
	}
	
	public void notifyTransitionAdded(T transition, int affectedRelations){
		TransitionChangeEvent<T> event = new TransitionChangeEvent<T>(transition, affectedRelations);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.transitionAdded(event);
		notifyStructureChange();
	}
	
	public void notifyTransitionRemoved(T transition, int affectedRelations){
		TransitionChangeEvent<T> event = new TransitionChangeEvent<T>(transition, affectedRelations);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.transitionRemoved(event);
		notifyStructureChange();
	}
	
	public void notifyRelationAdded(F relation){
		RelationChangeEvent<F> event = new RelationChangeEvent<F>(relation);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.relationAdded(event);
		notifyStructureChange();
	}

	public void notifyRelationRemoved(F relation){
		RelationChangeEvent<F> event = new RelationChangeEvent<F>(relation);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.relationRemoved(event);
		notifyStructureChange();
	}
	

}
