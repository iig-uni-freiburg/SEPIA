package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;


public class IFNet extends AbstractIFNet<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, RegularIFNetTransition, DeclassificationTransition> {

	
	
	public IFNet() {
		super();
	}

	public IFNet(Set<String> places, Set<String> transitions, IFNetMarking initialMarking) throws ParameterException {
		super(places, transitions, initialMarking);
	}

	@Override
	public IFNet newInstance() {
		return new IFNet();
	}
	
	@Override
	protected IFNetMarking createNewMarking() {
		return new IFNetMarking();
	}	

	@Override
	protected IFNetPlace createNewPlace(String name, String label) 
			throws ParameterException {
		return new IFNetPlace(name, label);
	}
	
	@Override
	protected RegularIFNetTransition createNewRegularTransition(String name, String label, boolean isSilent) 
			throws ParameterException {
		return new RegularIFNetTransition(name, label, isSilent);
	}

	@Override
	protected DeclassificationTransition createNewDeclassificationTransition(String name, String label, boolean isSilent) 
			throws ParameterException {
		return new DeclassificationTransition(name, label, isSilent);
	}

	@Override
	protected AbstractIFNetTransition<IFNetFlowRelation> createNewTransition(String name, String label, boolean isSilent) 
			throws ParameterException {
		return createNewRegularTransition(name, label, isSilent);
	}


	@Override
	protected IFNetFlowRelation createNewFlowRelation(IFNetPlace place, AbstractIFNetTransition<IFNetFlowRelation> transition) 
			throws ParameterException {
		return new IFNetFlowRelation(place, transition);
	}

	@Override
	protected IFNetFlowRelation createNewFlowRelation(AbstractIFNetTransition<IFNetFlowRelation> transition, IFNetPlace place) 
			throws ParameterException {
		return new IFNetFlowRelation(transition, place);
	}
	
}
