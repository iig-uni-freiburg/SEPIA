/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.AccessContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.TimeRessourceContext;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedNet<P extends AbstractTimedPlace<F>, T extends AbstractTimedTransition<F>, F extends AbstractTimedFlowRelation<P, T>, M extends AbstractTimedMarking>
        extends AbstractPetriNet<P, T, F, M, Integer> {

    protected double clock = 0;

    private AccessContext accessContext;

    private TimeRessourceContext timeRessourceContext;

    public TimeRessourceContext getTimeRessourceContext() {
        return timeRessourceContext;
    }

    public void setTimeRessourceContext(TimeRessourceContext timeRessourceContext) {
        this.timeRessourceContext = timeRessourceContext;
    }

    public void setAccessContext(AccessContext accessContext) {
        this.accessContext = accessContext;
        for (T transition : getTransitions()) {
            transition.setAccessContext(accessContext);
        }
    }

    public AccessContext getAccessContext() {
        return accessContext;
    }

    public void AccessContext(AccessContext accessContext) {
        this.accessContext = accessContext;
    }

    @Override
    public boolean addTransition(String transitionName) {
        boolean result = super.addTransition(transitionName); //To change body of generated methods, choose Tools | Templates.
        getTransition(transitionName).setAccessContext(accessContext);
        return result;
    }

}
