package petrinet.cpn;

import petrinet.cpn.abstr.AbstractCPNPlace;
import validate.ParameterException;

public class CPNPlace extends AbstractCPNPlace<CPNFlowRelation> {

	public CPNPlace(String name) throws ParameterException {
		super(name);
	}

	public CPNPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
