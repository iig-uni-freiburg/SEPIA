package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNTransition;

public class CWNTransition extends AbstractCWNTransition<CWNFlowRelation> {
	
	protected CWNTransition(){
		super();
	}

	public CWNTransition(String name, boolean isEmpty) {
		super(name, isEmpty);
	}

	public CWNTransition(String name, String label, boolean isEmpty) {
		super(name, label, isEmpty);
	}

	public CWNTransition(String name, String label) { 
		super(name, label);
	}

	public CWNTransition(String name) {
		super(name);
	}

	@Override
	protected CWNTransition newInstance() {
		return new CWNTransition();
	}
	
	

}
