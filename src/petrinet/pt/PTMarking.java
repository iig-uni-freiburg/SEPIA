package petrinet.pt;

import petrinet.pt.abstr.AbstractPTMarking;
import validate.ParameterException;

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
