package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;

public class CWNMarking extends AbstractCWNMarking {
	
	private static final long serialVersionUID = 2187568438614474283L;

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
