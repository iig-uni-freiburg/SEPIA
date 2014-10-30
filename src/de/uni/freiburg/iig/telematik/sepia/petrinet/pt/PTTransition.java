package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class PTTransition extends AbstractPTTransition<PTFlowRelation> {
	
	private static final long serialVersionUID = -3508062465237541851L;

//	protected PTTransition(){
//		super();
//	}
	
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

	@Override
	protected PTTransition newInstance(String name) {
		return new PTTransition(name);
	}
	
	@Override
	public PTTransition clone(){
		return (PTTransition) super.clone();
	}

}
