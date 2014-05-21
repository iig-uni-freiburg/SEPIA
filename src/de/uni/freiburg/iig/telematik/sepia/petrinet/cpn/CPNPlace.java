package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;

public class CPNPlace extends AbstractCPNPlace<CPNFlowRelation> {
	
	protected CPNPlace(){
		super();
	}

	public CPNPlace(String name) {
		super(name);
	}

	public CPNPlace(String name, String label) {
		super(name, label);
	}

	@Override
	protected CPNPlace newInstance() {
		return new CPNPlace();
	}
	
	@Override
	public CPNPlace clone(){
		return (CPNPlace) super.clone();
	}
	
}
