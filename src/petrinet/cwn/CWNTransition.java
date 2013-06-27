package petrinet.cwn;

import de.invation.code.toval.validate.ParameterException;
import petrinet.cwn.abstr.AbstractCWNTransition;

public class CWNTransition extends AbstractCWNTransition<CWNFlowRelation> {
	
	protected CWNTransition(){
		super();
	}

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

	@Override
	protected CWNTransition newInstance() {
		return new CWNTransition();
	}
	
	

}
