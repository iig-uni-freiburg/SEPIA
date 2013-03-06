package petrinet.cpn;

import petrinet.cpn.abstr.AbstractCPNMarking;
import validate.ParameterException;

public class CPNMarking extends AbstractCPNMarking {

	@Override
	public CPNMarking clone() {
		CPNMarking newMarking = new CPNMarking();
		try{
			for(String placeName: placeStates.keySet()){
				newMarking.set(placeName, placeStates.get(placeName).clone());
			}
		}catch(ParameterException e){}
		return newMarking;
	}

}
