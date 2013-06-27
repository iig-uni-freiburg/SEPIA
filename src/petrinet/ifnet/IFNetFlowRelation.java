package petrinet.ifnet;

import de.invation.code.toval.validate.ParameterException;
import petrinet.cwn.abstr.AbstractCWNFlowRelation;

public class IFNetFlowRelation extends AbstractCWNFlowRelation<IFNetPlace, AbstractIFNetTransition> {

	public IFNetFlowRelation(IFNetPlace place, AbstractIFNetTransition transition) throws ParameterException {
		super(place, transition);
	}

	public IFNetFlowRelation(AbstractIFNetTransition transition, IFNetPlace place) throws ParameterException {
		super(transition, place);
	}

	@Override
	public String toPNML(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFNetFlowRelation clone(IFNetPlace place, AbstractIFNetTransition transition, boolean directionPT) {
		IFNetFlowRelation result = null;
		try{
			result = new IFNetFlowRelation(place, transition);
			result.setDirectionPT(directionPT);
		} catch(ParameterException e){
			e.printStackTrace();
		}
		return result;
	}
	
}
