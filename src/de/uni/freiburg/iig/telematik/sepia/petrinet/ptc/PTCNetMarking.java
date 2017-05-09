package de.uni.freiburg.iig.telematik.sepia.petrinet.ptc;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetMarking;

public class PTCNetMarking extends AbstractPTCNetMarking {

	private static final long serialVersionUID = -4486322213252190071L;

	public PTCNetMarking() {
		super();
	}

	@Override
	public PTCNetMarking clone() {
		PTCNetMarking newMarking = new PTCNetMarking();
		for (String placeName : placeStates.keySet()) {
			newMarking.set(placeName, (int) placeStates.get(placeName));
		}
		return newMarking;
	}
}
