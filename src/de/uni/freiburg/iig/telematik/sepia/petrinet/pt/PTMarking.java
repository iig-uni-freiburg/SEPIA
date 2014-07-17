package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;

public class PTMarking extends AbstractPTMarking {

	private static final long serialVersionUID = 3301981336784904647L;

	@Override
	public PTMarking clone() {
		PTMarking newMarking = new PTMarking();
		for(String placeName: placeStates.keySet()){
			newMarking.set(placeName, (int) placeStates.get(placeName));
		}
		return newMarking;
	}

}
