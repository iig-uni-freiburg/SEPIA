package petrinet.cpn.abstr;

import petrinet.AbstractMarking;
import types.Multiset;
import validate.ParameterException;
import validate.Validate;

public abstract class AbstractCPNMarking extends AbstractMarking<Multiset<String>> {

	public AbstractCPNMarking() {
		super();
	}	
	
	@Override
	public void set(String place, Multiset<String> state) throws ParameterException {
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
