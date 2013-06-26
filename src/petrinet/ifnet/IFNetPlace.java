package petrinet.ifnet;

import petrinet.cwn.abstr.AbstractCWNPlace;
import types.Multiset;
import validate.ParameterException;

public class IFNetPlace extends AbstractCWNPlace<IFNetFlowRelation> {
	
	protected IFNetPlace(){
		super();
	}

	public IFNetPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public IFNetPlace(String name) throws ParameterException {
		super(name);
	}

	@Override
	public String toPNML(Multiset<String> initialMarking) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IFNetPlace newInstance() {
		return new IFNetPlace();
	}

}
