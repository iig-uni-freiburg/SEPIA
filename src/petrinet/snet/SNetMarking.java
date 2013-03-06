package petrinet.snet;

import petrinet.cwn.abstr.AbstractCWNMarking;
import validate.ParameterException;

public class SNetMarking extends AbstractCWNMarking {

	public SNetMarking() {
		super();
	}

	@Override
	public SNetMarking clone() {
		SNetMarking newMarking = new SNetMarking();
		try{
			for(String placeName: placeStates.keySet()){
				newMarking.set(placeName, placeStates.get(placeName).clone());
			}
		}catch(ParameterException e){}
		return newMarking;
	}
	
	

}
