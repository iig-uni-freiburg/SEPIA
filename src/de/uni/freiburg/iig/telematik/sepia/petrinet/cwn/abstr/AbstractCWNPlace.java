package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;

public abstract class AbstractCWNPlace<E extends AbstractCWNFlowRelation<? extends AbstractCWNPlace<E>, 
																		 ? extends AbstractCWNTransition<E>>> 

										 extends AbstractCPNPlace<E> {
	
	private static final long serialVersionUID = -4780639560015316941L;

	protected AbstractCWNPlace(){
		super();
	}

	public AbstractCWNPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public AbstractCWNPlace(String name) throws ParameterException {
		super(name);
	}

}
