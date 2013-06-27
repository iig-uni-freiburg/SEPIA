package petrinet.cwn;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import petrinet.cwn.abstr.AbstractCWNPlace;

public class CWNPlace extends AbstractCWNPlace<CWNFlowRelation> {
	
	protected CWNPlace(){
		super();
	}

	public CWNPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public CWNPlace(String name) throws ParameterException {
		super(name);
	}

	@Override
	public String toPNML(Multiset<String> initialMarking) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CWNPlace newInstance() {
		return new CWNPlace();
	}

}
