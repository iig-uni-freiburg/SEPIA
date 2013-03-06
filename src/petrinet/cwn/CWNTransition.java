package petrinet.cwn;

import petrinet.cwn.abstr.AbstractCWNTransition;
import validate.ParameterException;

public class CWNTransition extends AbstractCWNTransition<CWNFlowRelation> {

	public CWNTransition(String name, boolean isEmpty) throws ParameterException {
		super(name, isEmpty);
	}

	public CWNTransition(String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
	}

	public CWNTransition(String name, String label) throws ParameterException { 
		super(name, label);
	}

	public CWNTransition(String name) throws ParameterException {
		super(name);
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
