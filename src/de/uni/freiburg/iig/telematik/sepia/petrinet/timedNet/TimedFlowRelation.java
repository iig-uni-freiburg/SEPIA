/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
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
    

//    @Override
//    protected Integer getDefaultConstraint() {
//        return 1;
//    }

//    @Override
//    protected void validateConstraint(Integer constraint) {
//        Validate.notNegative(constraint);
//        Validate.notNull(constraint);
//    }

    @Override
    public TimedFlowRelation clone(TimedNetPlace place, TimedTransition transition, boolean directionPT) {
    	TimedFlowRelation result = null;
		try {
			if (getDirectionPT())
				result = new TimedFlowRelation(place, transition);
			else
				result = new TimedFlowRelation(transition, place);
			result.setDirectionPT(getDirectionPT());
			result.setWeight(getWeight());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
    }
    
}
