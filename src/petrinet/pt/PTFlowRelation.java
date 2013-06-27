package petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import petrinet.pt.abstr.AbstractPTFlowRelation;

public class PTFlowRelation extends AbstractPTFlowRelation<PTPlace, PTTransition> {
	
	public PTFlowRelation(PTPlace p, PTTransition t, int weight) throws ParameterException {
		super(p, t);
		setWeight(weight);
	}

	public PTFlowRelation(PTPlace p, PTTransition t) throws ParameterException {
		super(p, t);
	}

	public PTFlowRelation(PTTransition transition, PTPlace place, int weight) throws ParameterException {
		super(transition, place);
		setWeight(weight);
	}

	public PTFlowRelation(PTTransition t, PTPlace p) throws ParameterException {
		super(t, p);
	}

	@Override
	public PTFlowRelation clone(PTPlace place, PTTransition transition, boolean directionPT) {
		PTFlowRelation result = null;
		try{
			result = new PTFlowRelation(place, transition);
			result.setDirectionPT(directionPT);
		} catch(ParameterException e){
			e.printStackTrace();
		}
		return result;
	}
	
}
