package petrinet.cwn.abstr;

import petrinet.cpn.abstr.AbstractCPN;
import petrinet.cpn.abstr.AbstractCPNTransition;
import validate.ParameterException;
import exception.PNValidationException;

public abstract class AbstractCWNTransition<E extends AbstractCWNFlowRelation<? extends AbstractCWNPlace<E>, 
																			  ? extends AbstractCWNTransition<E>>> 

											  extends AbstractCPNTransition<E> {

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
		if(!getConsumedColors().contains(AbstractCPN.CONTROL_FLOW_TOKEN_COLOR))
			throw new PNValidationException("Transition must consume at least one control flow token: " + this);
		if(!getProducedColors().contains(AbstractCPN.CONTROL_FLOW_TOKEN_COLOR))
			throw new PNValidationException("Transition must produce at least one control flow token: " + this);
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
