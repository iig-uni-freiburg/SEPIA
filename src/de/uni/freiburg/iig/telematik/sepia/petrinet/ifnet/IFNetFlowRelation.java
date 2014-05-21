package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;

public class IFNetFlowRelation extends AbstractIFNetFlowRelation<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>> {

	public IFNetFlowRelation(IFNetPlace place, AbstractIFNetTransition<IFNetFlowRelation> transition) {
		super(place, transition);
	}

	public IFNetFlowRelation(AbstractIFNetTransition<IFNetFlowRelation> transition, IFNetPlace place) {
		super(transition, place);
	}

	@Override
	public IFNetFlowRelation clone(IFNetPlace place, AbstractIFNetTransition<IFNetFlowRelation> transition, boolean directionPT) {
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
