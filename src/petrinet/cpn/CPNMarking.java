package petrinet.cpn;

import de.invation.code.toval.validate.ParameterException;
import petrinet.cpn.abstr.AbstractCPNMarking;

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
