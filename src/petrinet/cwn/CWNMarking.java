package petrinet.cwn;

import de.invation.code.toval.validate.ParameterException;
import petrinet.cwn.abstr.AbstractCWNMarking;

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
