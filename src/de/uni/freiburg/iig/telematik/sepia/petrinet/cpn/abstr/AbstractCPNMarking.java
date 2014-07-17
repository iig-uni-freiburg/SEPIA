package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;

public abstract class AbstractCPNMarking extends AbstractMarking<Multiset<String>> {

	private static final long serialVersionUID = -2688813879312465383L;

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
		List<String> placeNamesSorted = new ArrayList<String>(placeStates.keySet());
		Collections.sort(placeNamesSorted);
		for(String place: placeNamesSorted){
			builder.append(String.format(placeFormat, place, placeStates.get(place)));
		}
		return builder.toString();
	}
}
