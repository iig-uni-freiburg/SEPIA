package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;

public class IFNetMarking extends AbstractCWNMarking {

	public IFNetMarking() {
		super();
	}

	@Override
	public IFNetMarking clone() {
		IFNetMarking newMarking = new IFNetMarking();
		try{
			for(String placeName: placeStates.keySet()){
				newMarking.set(placeName, placeStates.get(placeName).clone());
			}
		}catch(ParameterException e){}
		return newMarking;
	}
	
	

}
