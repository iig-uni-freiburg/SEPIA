package petrinet.pt;

import petrinet.pt.abstr.AbstractPTPlace;
import validate.ParameterException;

public class PTPlace extends AbstractPTPlace<PTFlowRelation>{
	
	public PTPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public PTPlace(String name) throws ParameterException {
		super(name);
	}
	
}
