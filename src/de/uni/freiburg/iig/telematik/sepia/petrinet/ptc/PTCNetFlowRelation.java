package de.uni.freiburg.iig.telematik.sepia.petrinet.ptc;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;

public class PTCNetFlowRelation extends AbstractPTCNetFlowRelation<PTCNetPlace, AbstractPTCNetTransition<PTCNetFlowRelation>> {

	private static final long serialVersionUID = 8266335039396091886L;



	public PTCNetFlowRelation(AbstractPTCNetTransition<PTCNetFlowRelation> transition, PTCNetPlace place, Integer weight) {
		super(transition, place, weight);
	}

	public PTCNetFlowRelation(AbstractPTCNetTransition<PTCNetFlowRelation> transition, PTCNetPlace place) {
		super(transition, place);
	}

	public PTCNetFlowRelation(PTCNetPlace place, AbstractPTCNetTransition<PTCNetFlowRelation> transition, Integer weight) {
		super(place, transition, weight);
	}

	public PTCNetFlowRelation(PTCNetPlace place, AbstractPTCNetTransition<PTCNetFlowRelation> transition) {
		super(place, transition);
	}

	@Override
	public PTCNetFlowRelation clone(PTCNetPlace place, AbstractPTCNetTransition<PTCNetFlowRelation> transition, boolean directionPT) {
		PTCNetFlowRelation result = null;
		try {
			// Can't set direction afterwards. The default name could be wrong then when testing for equality.
			if (directionPT)
				result = new PTCNetFlowRelation(place, transition);
			else
				result = new PTCNetFlowRelation(transition, place);
			result.setWeight(getWeight());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}
}