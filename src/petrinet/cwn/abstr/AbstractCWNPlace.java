package petrinet.cwn.abstr;

import petrinet.cpn.abstr.AbstractCPNPlace;
import validate.ParameterException;

public abstract class AbstractCWNPlace<E extends AbstractCWNFlowRelation<? extends AbstractCWNPlace<E>, 
																		 ? extends AbstractCWNTransition<E>>> 

										 extends AbstractCPNPlace<E> {

	public AbstractCWNPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public AbstractCWNPlace(String name) throws ParameterException {
		super(name);
	}

}
