package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;

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
	public CPNFlowRelation clone(CPNPlace place, CPNTransition transition, boolean directionPT) {
		CPNFlowRelation result = null;
		try {
			result = new CPNFlowRelation(place, transition);
			result.setDirectionPT(directionPT);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}

}
