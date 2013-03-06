package petrinet.pt;

import petrinet.pt.abstr.AbstractPTFlowRelation;
import validate.ParameterException;

public class PTFlowRelation extends AbstractPTFlowRelation<PTPlace, PTTransition> {
	
	public PTFlowRelation(PTPlace p, PTTransition t, int weight) throws ParameterException {
		super(p, t);
		setWeight(weight);
	}

	public PTFlowRelation(PTPlace p, PTTransition t) throws ParameterException {
		super(p, t);
	}

	public PTFlowRelation(PTTransition transition, PTPlace place, int weight) throws ParameterException {
		super(transition, place);
		setWeight(weight);
	}

	public PTFlowRelation(PTTransition t, PTPlace p) throws ParameterException {
		super(t, p);
	}
	
}
