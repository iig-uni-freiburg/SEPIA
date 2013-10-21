package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;

public abstract class AbstractIFNetPlace<E extends AbstractIFNetFlowRelation<? extends AbstractIFNetPlace<E>, 
																		 	 ? extends AbstractIFNetTransition<E>>> 

										   extends AbstractCWNPlace<E> {
	
	protected AbstractIFNetPlace(){
		super();
	}

	public AbstractIFNetPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public AbstractIFNetPlace(String name) throws ParameterException {
		super(name);
	}

}
