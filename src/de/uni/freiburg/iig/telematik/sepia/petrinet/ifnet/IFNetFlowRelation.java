package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;

public class IFNetFlowRelation extends AbstractCWNFlowRelation<IFNetPlace, AbstractIFNetTransition> {

	public IFNetFlowRelation(IFNetPlace place, AbstractIFNetTransition transition) throws ParameterException {
		super(place, transition);
	}

	public IFNetFlowRelation(AbstractIFNetTransition transition, IFNetPlace place) throws ParameterException {
		super(transition, place);
	}

	@Override
	public IFNetFlowRelation clone(IFNetPlace place, AbstractIFNetTransition transition, boolean directionPT) {
		IFNetFlowRelation result = null;
		try {
			// Can't set direction afterwards. The default name could be wrong then when testing for equality.
			if (directionPT)
				result = new IFNetFlowRelation(place, transition);
			else
				result = new IFNetFlowRelation(transition, place);
			result.setConstraint(getConstraint().clone());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}
}
