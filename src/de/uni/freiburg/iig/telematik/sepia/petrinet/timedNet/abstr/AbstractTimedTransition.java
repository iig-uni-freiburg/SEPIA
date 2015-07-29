/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.AccessContextException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.AccessContext;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author richard
 */
public abstract class AbstractTimedTransition<E extends AbstractTimedFlowRelation<? extends AbstractTimedPlace<E>, ? extends AbstractTimedTransition<E>>> extends AbstractTransition<E, Integer> {
    private AccessContext accessContext;

    public AbstractTimedTransition(String name, String label) {
        super(name, label);
    }

    @Override
    public synchronized void checkValidity() throws PNValidationException {
        try {
            super.checkValidity();
            String subject = accessContext.getSubjectFor(getLabel());
        } catch (AccessContextException ex) {
            throw new PNValidationException("No available Subjekt for "+getLabel()+" found");
        } 
    }

    public AbstractTimedTransition(String name) {
        super(name);
    }


    public AbstractTimedTransition(String name, boolean isSilent) {
        super(name, isSilent);
    }

    public AbstractTimedTransition(String name, String label, boolean isSilent) {
        super(name, label, isSilent);
    }
    
    public void setAccessContext(AccessContext accessContext){
        this.accessContext=accessContext;
    }
    
    public AccessContext getAccessContext(){
        return accessContext;
    }
    
}
