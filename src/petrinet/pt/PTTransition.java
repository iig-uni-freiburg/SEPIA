package petrinet.pt;

import petrinet.pt.abstr.AbstractPTTransition;
import validate.ParameterException;

public class PTTransition extends AbstractPTTransition<PTFlowRelation> {
	
	public PTTransition(String name) throws ParameterException {
		super(name);
	}
	
	public PTTransition(String name, String label) throws ParameterException {
		super(name, label);
	}
	
	public PTTransition(String name, boolean isSilent) throws ParameterException{
		super(name, isSilent);
	}
	
	public PTTransition(String name, String label, boolean isSilent) throws ParameterException{
		super(name, label, isSilent);
	}

}
