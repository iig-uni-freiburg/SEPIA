/* 
 * Copyright (c) 2015, IIG Telematics, Uni Freiburg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of IIG Telematics, Uni Freiburg nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
	
	public void notifyStructureChange(){
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.structureChanged();
	}
	
	public void notifyPlaceAdded(P place){
		notifyPlaceAdded(place, 0);
	}
	
	public void notifyPlaceAdded(P place, int affectedRelations){
		PlaceChangeEvent<P> event = new PlaceChangeEvent<>(place, affectedRelations);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.placeAdded(event);
		notifyStructureChange();
	}
	
	public void notifyPlaceRemoved(P place, int affectedRelations){
		PlaceChangeEvent<P> event = new PlaceChangeEvent<>(place, affectedRelations);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.placeRemoved(event);
		notifyStructureChange();
	}
	
	public void notifyTransitionAdded(T transition){
		notifyTransitionAdded(transition, 0);
	}
	
	public void notifyTransitionAdded(T transition, int affectedRelations){
		TransitionChangeEvent<T> event = new TransitionChangeEvent<>(transition, affectedRelations);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.transitionAdded(event);
		notifyStructureChange();
	}
	
	public void notifyTransitionRemoved(T transition, int affectedRelations){
		TransitionChangeEvent<T> event = new TransitionChangeEvent<>(transition, affectedRelations);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.transitionRemoved(event);
		notifyStructureChange();
	}
	
	public void notifyRelationAdded(F relation){
		RelationChangeEvent<F> event = new RelationChangeEvent<>(relation);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.relationAdded(event);
		notifyStructureChange();
	}

	public void notifyRelationRemoved(F relation){
		RelationChangeEvent<F> event = new RelationChangeEvent<>(relation);
		for(PNStructureListener<P,T,F,M,S> l: listeners)
			l.relationRemoved(event);
		notifyStructureChange();
	}
	

}
