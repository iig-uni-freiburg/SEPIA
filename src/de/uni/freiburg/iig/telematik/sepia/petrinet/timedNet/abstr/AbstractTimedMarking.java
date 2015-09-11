/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedMarking extends AbstractPTMarking {

    public AbstractTimedMarking() {
        super();
    }

    /**
     * Sets the state of the given place in the marking.
     *
     * @param place The place whose state is set.
     * @param tokens The number of tokens in the place.
     */
    public void set(String place, int tokens) {
        validatePlace(place);
        validateState(tokens);
        placeStates.put(place, tokens);
    }

}
