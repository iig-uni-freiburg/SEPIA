package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;

public class CWNPlace extends AbstractCWNPlace<CWNFlowRelation> {
	
	protected CWNPlace(){
		super();
	}

	public CWNPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public CWNPlace(String name) throws ParameterException {
		super(name);
	}

	@Override
	protected CWNPlace newInstance() {
		return new CWNPlace();
	}

}
