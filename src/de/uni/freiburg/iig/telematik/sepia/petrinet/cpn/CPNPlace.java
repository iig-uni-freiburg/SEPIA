package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;

public class CPNPlace extends AbstractCPNPlace<CPNFlowRelation> {
	
	protected CPNPlace(){
		super();
	}

	public CPNPlace(String name) throws ParameterException {
		super(name);
	}

	public CPNPlace(String name, String label) throws ParameterException {
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
