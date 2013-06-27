package petrinet.cwn.abstr;

import de.invation.code.toval.validate.ParameterException;
import petrinet.cpn.abstr.AbstractCPNPlace;

public abstract class AbstractCWNPlace<E extends AbstractCWNFlowRelation<? extends AbstractCWNPlace<E>, 
																		 ? extends AbstractCWNTransition<E>>> 

										 extends AbstractCPNPlace<E> {
	
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
