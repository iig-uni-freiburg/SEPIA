package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;

public class CPNMarking extends AbstractCPNMarking {

	private static final long serialVersionUID = -4518590691648446632L;

	@Override
	public CPNMarking clone() {
		CPNMarking newMarking = new CPNMarking();
		for(String placeName: placeStates.keySet()){
			newMarking.set(placeName, placeStates.get(placeName).clone());
		}
		return newMarking;
	}

}
