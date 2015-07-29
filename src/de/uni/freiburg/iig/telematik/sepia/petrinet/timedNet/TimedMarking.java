/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedMarking;

/**
 *
 * @author richard
 */
class TimedMarking extends AbstractTimedMarking {
    
    	@Override
	public TimedMarking clone() {
		TimedMarking newMarking = new TimedMarking();
		for(String placeName: placeStates.keySet()){
			newMarking.set(placeName, (int) placeStates.get(placeName));
		}
		return newMarking;
	}
    
}
