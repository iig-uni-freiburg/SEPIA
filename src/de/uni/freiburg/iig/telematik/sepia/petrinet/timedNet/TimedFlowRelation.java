/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

/**
 *
 * @author richard
 */
public class TimedFlowRelation extends AbstractTimedFlowRelation<TimedNetPlace,TimedTransition>{

    public TimedFlowRelation(TimedNetPlace place, TimedTransition transition) {
        super(place, transition);
    }

    TimedFlowRelation(TimedNetPlace place, TimedTransition transition, Integer constraint) {
        super(place,transition, constraint);
    }

    TimedFlowRelation(TimedTransition transition, TimedNetPlace place, Integer constraint) {
        super(transition,place,constraint);
    }

    TimedFlowRelation(TimedTransition transition, TimedNetPlace place) {
        super(transition,place);
    }
    

    @Override
    protected Integer getDefaultConstraint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void validateConstraint(Integer constraint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFlowRelation<TimedNetPlace, TimedTransition, Integer> clone(TimedNetPlace place, TimedTransition transition, boolean directionPT) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
