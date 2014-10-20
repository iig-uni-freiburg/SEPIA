package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;

public class CPNFlowRelation extends AbstractCPNFlowRelation<CPNPlace, CPNTransition> {


	private static final long serialVersionUID = -8743990268471831530L;

//	public CPNFlowRelation(CPNPlace p, CPNTransition t, boolean addDefaultConstraint) {
//		super(p, t, addDefaultConstraint);
//	}
//	
//	public CPNFlowRelation(CPNTransition t, CPNPlace p, boolean addDefaultConstraint) {
//		super(t, p, addDefaultConstraint);
//	}

	public CPNFlowRelation(CPNPlace place, CPNTransition transition, Multiset<String> constraint) {
		super(place, transition, constraint);
	}

	public CPNFlowRelation(CPNPlace place, CPNTransition transition) {
		super(place, transition);
	}

	public CPNFlowRelation(CPNTransition transition, CPNPlace place, Multiset<String> constraint) {
		super(transition, place, constraint);
	}

	public CPNFlowRelation(CPNTransition transition, CPNPlace place) {
		super(transition, place);
	}

	@Override
	public CPNFlowRelation clone(CPNPlace place, CPNTransition transition, boolean directionPT) {
		CPNFlowRelation result = null;
		try {
			// Can't set direction afterwards. The default name could be wrong then when testing for equality.
			if (directionPT)
				result = new CPNFlowRelation(place, transition);
			else
				result = new CPNFlowRelation(transition, place);
			result.setConstraint(getConstraint().clone());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}

}
