package petrinet.cwn;

import petrinet.cwn.abstr.AbstractCWNMarking;
import validate.ParameterException;

public class CWNMarking extends AbstractCWNMarking {
	
	public CWNMarking() {
		super();
	}

	@Override
	public CWNMarking clone() {
		CWNMarking newMarking = new CWNMarking();
		try{
			for(String placeName: placeStates.keySet()){
				newMarking.set(placeName, placeStates.get(placeName).clone());
			}
		}catch(ParameterException e){}
		return newMarking;
	}
	
	

}
