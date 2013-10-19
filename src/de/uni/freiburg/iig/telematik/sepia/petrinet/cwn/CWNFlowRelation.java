package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;

public class CWNFlowRelation extends AbstractCWNFlowRelation<CWNPlace, CWNTransition> {

	public CWNFlowRelation(CWNPlace place, CWNTransition transition) throws ParameterException {
		super(place, transition);
	}

	public CWNFlowRelation(CWNTransition transition, CWNPlace place) throws ParameterException {
		super(transition, place);
	}
	
	@Override
	public CWNFlowRelation clone(CWNPlace place, CWNTransition transition, boolean directionPT) {
		CWNFlowRelation result = null;
		try {
			// Can't set direction afterwards. The default name could be wrong then when testing for equality.
			if (directionPT)
				result = new CWNFlowRelation(place, transition);
			else
				result = new CWNFlowRelation(transition, place);
			result.setConstraint(getConstraint().clone());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}
}
