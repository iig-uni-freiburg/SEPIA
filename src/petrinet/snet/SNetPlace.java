package petrinet.snet;

import petrinet.cwn.abstr.AbstractCWNPlace;
import validate.ParameterException;

public class SNetPlace extends AbstractCWNPlace<SNetFlowRelation> {

	public SNetPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public SNetPlace(String name) throws ParameterException {
		super(name);
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}

}
