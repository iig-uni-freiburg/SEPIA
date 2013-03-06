package petrinet.snet;

import petrinet.cwn.abstr.AbstractCWNTransition;
import validate.ParameterException;

public abstract class AbstractSNetTransition extends AbstractCWNTransition<SNetFlowRelation> {

	public AbstractSNetTransition(String name, boolean isEmpty) throws ParameterException {
		super(name, isEmpty);
	}

	public AbstractSNetTransition(String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
	}

	public AbstractSNetTransition(String name, String label) throws ParameterException {
		super(name, label);
	}

	public AbstractSNetTransition(String name) throws ParameterException {
		super(name);
	}

	public abstract boolean isDeclassificator();
	
}
