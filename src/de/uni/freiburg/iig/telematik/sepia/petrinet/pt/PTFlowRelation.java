package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;

public class PTFlowRelation extends AbstractPTFlowRelation<PTPlace, PTTransition> {
	
	public PTFlowRelation(PTPlace p, PTTransition t, int weight) {
		super(p, t);
		setWeight(weight);
	}

	public PTFlowRelation(PTPlace p, PTTransition t) {
		super(p, t);
	}

	public PTFlowRelation(PTTransition transition, PTPlace place, int weight) {
		super(transition, place);
		setWeight(weight);
	}

	public PTFlowRelation(PTTransition t, PTPlace p) {
		super(t, p);
	}

	@Override
	public PTFlowRelation clone(PTPlace place, PTTransition transition, boolean directionPT) {
		PTFlowRelation result = null;
		try {
			if (getDirectionPT())
				result = new PTFlowRelation(place, transition);
			else
				result = new PTFlowRelation(transition, place);
			result.setDirectionPT(getDirectionPT());
			result.setWeight(getWeight());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
