/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

/**
 *
 * @author richard
 */
public class TimedTransition extends AbstractTimedTransition<TimedFlowRelation> {

	private static final long serialVersionUID = 2596908252141084555L;

	public TimedTransition(String name, String label) {
        super(name, label);
    }

    private TimedTransition(String name) {
        super(name);
    }

    
    public TimedTransition(String name,String  label, boolean isSilent){
        super(name, label, isSilent);
    }


    @Override
    protected TimedTransition newInstance(String name) {
        return new TimedTransition(name);
    }
    
}
