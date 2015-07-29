/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

/**
 *
 * @author richard
 */
class TimedTransition extends AbstractTimedTransition<TimedFlowRelation> {

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
    protected boolean enoughTokensInInputPlaces() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean enoughSpaceInOutputPlaces() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected TimedTransition newInstance(String name) {
        return new TimedTransition(name);
    }
    
}
