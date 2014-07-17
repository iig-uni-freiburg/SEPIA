package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;

public abstract class AbstractCWNTransition<E extends AbstractCWNFlowRelation<? extends AbstractCWNPlace<E>, 
																			  ? extends AbstractCWNTransition<E>>> 

											  extends AbstractCPNTransition<E> {

	private static final long serialVersionUID = -7588362779618386287L;

	protected AbstractCWNTransition(){
		super();
	}
	
	public AbstractCWNTransition(String name, boolean isEmpty) throws ParameterException {
		super(name, isEmpty);
	}

	public AbstractCWNTransition(String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
	}

	public AbstractCWNTransition(String name, String label) throws ParameterException { 
		super(name, label);
	}

	public AbstractCWNTransition(String name) throws ParameterException {
		super(name);
	}

	@Override
	public void checkValidity() throws PNValidationException {
		super.checkValidity();
		// Check control flow dependency.
		// -> At least one control flow token must be consumed and produced.
		if(!getConsumedColors().contains(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR))
			throw new PNValidationException("Transition must consume at least one control flow token: " + this);
		if(!getProducedColors().contains(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR))
			throw new PNValidationException("Transition must produce at least one control flow token: " + this);
	}

	@Override
	public AbstractCWNTransition<E> clone() {
		return (AbstractCWNTransition<E>) super.clone();
	}
	
	
	
	
	
}
