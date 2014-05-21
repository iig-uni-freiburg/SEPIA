package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;

public abstract class AbstractPTMarking extends AbstractMarking<Integer> {
	
	public AbstractPTMarking() {
		super();
	}
	
	@Override
	protected void validateState(Integer state) {
		super.validateState(state);
		Validate.notNegative(state);
	}

	@Override
	public void set(String place, Integer state) {
		Validate.notNull(place);
		Validate.notNull(state);
		if(state <= 0){
			placeStates.remove(place);
		} else {
			super.set(place, state);
		}
	}

	@Override
	public String toString(){
		String placeFormat = "%s[%s] ";
		StringBuilder builder = new StringBuilder();
		for(String place: placeStates.keySet()){
			builder.append(String.format(placeFormat, place, placeStates.get(place)));
		}
		return builder.toString();
	}
}
