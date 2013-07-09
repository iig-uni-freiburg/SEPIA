package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;

public class CPNTransition extends AbstractCPNTransition<CPNFlowRelation> {
	
	protected CPNTransition(){
		super();
	}

	public CPNTransition(String name, boolean isEmpty) throws ParameterException {
		super(name, isEmpty);
	}

	public CPNTransition(String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
	}

	public CPNTransition(String name, String label) throws ParameterException {
		super(name, label);
	}

	public CPNTransition(String name) throws ParameterException {
		super(name);
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CPNTransition newInstance() {
		return new CPNTransition();
	}
	
}
