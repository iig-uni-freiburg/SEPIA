package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;

public class PTMarking extends AbstractPTMarking {

	@Override
	public PTMarking clone() {
		PTMarking newMarking = new PTMarking();
		try{
			for(String placeName: placeStates.keySet()){
				newMarking.set(placeName, placeStates.get(placeName));
			}
		}catch(ParameterException e){}
		return newMarking;
	}

}
