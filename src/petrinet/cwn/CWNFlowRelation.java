package petrinet.cwn;

import petrinet.cwn.abstr.AbstractCWNFlowRelation;
import validate.ParameterException;

public class CWNFlowRelation extends AbstractCWNFlowRelation<CWNPlace, CWNTransition> {

	public CWNFlowRelation(CWNPlace place, CWNTransition transition) throws ParameterException {
		super(place, transition);
	}

	public CWNFlowRelation(CWNTransition transition, CWNPlace place) throws ParameterException {
		super(transition, place);
	}

	@Override
	public String toPNML(int count) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
