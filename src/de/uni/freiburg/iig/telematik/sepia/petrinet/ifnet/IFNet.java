package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.event.CapacityEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.IFNetMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.IFNetMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.IFNetMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;


public class IFNet extends AbstractIFNet<IFNetPlace, 
										 AbstractIFNetTransition<IFNetFlowRelation>, 
										 IFNetFlowRelation, 
										 IFNetMarking, 
										 RegularIFNetTransition, 
										 DeclassificationTransition, 
										 IFNetMarkingGraphState, 
										 IFNetMarkingGraphRelation> {

	public IFNet() {
		super();
	}

	public IFNet(Set<String> places, Set<String> transitions, IFNetMarking initialMarking) {
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
	public IFNetMarkingGraph createNewMarkingGraph() {
		return new IFNetMarkingGraph();
	}

	@Override
	protected IFNetPlace createNewPlace(String name, String label) {
		return new IFNetPlace(name, label);
	}
	
	@Override
	protected RegularIFNetTransition createNewRegularTransition(String name, String label, boolean isSilent) {
		return new RegularIFNetTransition(name, label, isSilent);
	}

	@Override
	protected DeclassificationTransition createNewDeclassificationTransition(String name, String label, boolean isSilent) {
		return new DeclassificationTransition(name, label, isSilent);
	}

	@Override
	protected AbstractIFNetTransition<IFNetFlowRelation> createNewTransition(String name, String label, boolean isSilent) {
		return createNewRegularTransition(name, label, isSilent);
	}


	@Override
	protected IFNetFlowRelation createNewFlowRelation(IFNetPlace place, AbstractIFNetTransition<IFNetFlowRelation> transition) {
		return new IFNetFlowRelation(place, transition);
	}

	@Override
	protected IFNetFlowRelation createNewFlowRelation(AbstractIFNetTransition<IFNetFlowRelation> transition, IFNetPlace place) {
		return new IFNetFlowRelation(transition, place);
	}

	@Override
	public void capacityChanged(CapacityEvent<? extends AbstractPlace<IFNetFlowRelation, Multiset<String>>> o) {}
	
	@Override
	public IFNetMarkingGraph getMarkingGraph() throws PNException{
		return (IFNetMarkingGraph) super.getMarkingGraph();
	}
	
	@Override
	public IFNetMarkingGraph buildMarkingGraph() throws PNException{
		return (IFNetMarkingGraph) super.buildMarkingGraph();
	}
}
