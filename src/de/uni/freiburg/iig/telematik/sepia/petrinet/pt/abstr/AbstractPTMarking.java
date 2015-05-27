package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;

public abstract class AbstractPTMarking extends AbstractMarking<Integer> {
	
	private static final long serialVersionUID = 6385115305691528197L;

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
		List<String> placeNamesSorted = new ArrayList<String>(placeStates.keySet());
		Collections.sort(placeNamesSorted);
		for(String place: placeNamesSorted){
			builder.append(String.format(placeFormat, place, placeStates.get(place)));
		}
		return builder.toString();
	}
}
