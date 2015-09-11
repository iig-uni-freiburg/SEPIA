/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedFlowRelation<P extends AbstractTimedPlace<? extends AbstractTimedFlowRelation<P,T>>, 
											  T extends AbstractTimedTransition<? extends AbstractTimedFlowRelation<P,T>>> 
												extends AbstractPTFlowRelation<P, T> {

    public AbstractTimedFlowRelation(P place, T transition) {
        super(place, transition);
    }

    public AbstractTimedFlowRelation(T transition, P place) {
        super(transition, place);
    }

    public AbstractTimedFlowRelation(P place, T transition, int constraint) {
        super(place, transition, constraint);
    }

    public AbstractTimedFlowRelation(T transition, P place, int constraint) {
        super(transition, place, constraint);
    }
    
}
