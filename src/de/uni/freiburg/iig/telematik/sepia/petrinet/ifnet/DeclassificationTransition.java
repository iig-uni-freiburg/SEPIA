package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;


public class DeclassificationTransition extends AbstractDeclassificationTransition<IFNetFlowRelation>{
	
	protected DeclassificationTransition(){
		super();
	}
	
	public DeclassificationTransition(String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
	}

	public DeclassificationTransition(String name, boolean isEmpty) throws ParameterException {
		this(name, name, isEmpty);
	}

	public DeclassificationTransition(String name, String label) throws ParameterException {
		this(name, label, false);
	}

	public DeclassificationTransition(String name) throws ParameterException {
		this(name, name, false);
	}

	@Override
	protected DeclassificationTransition newInstance() {
		return new DeclassificationTransition();
	}

	@Override
	public DeclassificationTransition clone() {
		return (DeclassificationTransition) super.clone();
	}

	
	
}
