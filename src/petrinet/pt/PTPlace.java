package petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import petrinet.pt.abstr.AbstractPTPlace;

public class PTPlace extends AbstractPTPlace<PTFlowRelation>{
	
	protected PTPlace(){
		super();
	}
	
	public PTPlace(String name, String label) throws ParameterException {
		super(name, label);
	}

	public PTPlace(String name) throws ParameterException {
		super(name);
	}
	
	@Override
	protected PTPlace newInstance() {
		return new PTPlace();
	}
	
	@Override
	public PTPlace clone(){
		return (PTPlace) super.clone();
	}
	
}
