package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;

public class IFNetPlace extends AbstractIFNetPlace<IFNetFlowRelation> {
	
	protected IFNetPlace(){
		super();
	}

	public IFNetPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public IFNetPlace(String name) throws ParameterException {
		super(name);
	}

	@Override
	protected IFNetPlace newInstance() {
		return new IFNetPlace();
	}

}
