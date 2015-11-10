/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet;

import java.util.List;
import java.util.Map.Entry;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedMarking;

/**
 *
 * @author richard
 */
public class TimedMarking extends AbstractTimedMarking {

	@Override
	public TimedMarking clone() {
		// do not clone. instead give direct reference
		// return this;
		TimedMarking newMarking = new TimedMarking();
		for (String placeName : placeStates.keySet()) {
			newMarking.set(placeName, (int) placeStates.get(placeName));
		}
		for (Entry<Double, List<String>> keyValue : pendingActions.entrySet()) {
			for (String s : keyValue.getValue()) {
				newMarking.addPendingAction(s, keyValue.getKey());
			}
		}
		return newMarking;
	}

}
