package petrinet.cpn;

import petrinet.cpn.abstr.AbstractCPNFlowRelation;
import validate.ParameterException;

public class CPNFlowRelation extends AbstractCPNFlowRelation<CPNPlace, CPNTransition> {

	public CPNFlowRelation(CPNPlace p, CPNTransition t, boolean addDefaultConstraint) throws ParameterException {
		super(p, t, addDefaultConstraint);
	}

	public CPNFlowRelation(CPNPlace p, CPNTransition t) throws ParameterException {
		super(p, t);
	}

	public CPNFlowRelation(CPNTransition t, CPNPlace p, boolean addDefaultConstraint) throws ParameterException {
		super(t, p, addDefaultConstraint);
	}

	public CPNFlowRelation(CPNTransition t, CPNPlace p) throws ParameterException {
		super(t, p);
	}

	@Override
	public String toPNML(int count) {
		// TODO Auto-generated method stub
		return null;
	}

}
