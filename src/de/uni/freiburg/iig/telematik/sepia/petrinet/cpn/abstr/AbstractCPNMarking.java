package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;

public abstract class AbstractCPNMarking extends AbstractMarking<Multiset<String>> {

	public AbstractCPNMarking() {
		super();
	}	
	
	@Override
	public void set(String place, Multiset<String> state) {
		Validate.notNull(place);
		Validate.notNull(state);
		if(state.isEmpty()){
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
