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
			result = new CWNFlowRelation(place, transition);
			result.setDirectionPT(directionPT);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
