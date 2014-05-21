package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.event.CapacityEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.PTMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.PTMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.PTMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;


public class PTNet extends AbstractPTNet<PTPlace, PTTransition, PTFlowRelation, PTMarking, PTMarkingGraphState, PTMarkingGraphRelation>{
	
	public PTNet() {
		super();
	}

	public PTNet(Set<String> places, Set<String> transitions, PTMarking initialMarking)
			throws ParameterException {
		super(places, transitions, initialMarking);
	}
	
	@Override
	public PTMarking fireCheck(String transitionName) throws ParameterException, PNException {
		PTMarking newMarking = cloneMarking();
		PTTransition transition = getTransition(transitionName);
		for(PTFlowRelation relation: transition.getIncomingRelations()){
			String inputPlaceName = relation.getPlace().getName();
			newMarking.set(inputPlaceName, newMarking.get(inputPlaceName) - relation.getWeight());
		}
		for(PTFlowRelation relation: transition.getOutgoingRelations()){
			String outputPlaceName = relation.getPlace().getName();
			Integer oldState = (newMarking.get(outputPlaceName) == null ? 0 : newMarking.get(outputPlaceName));
			newMarking.set(outputPlaceName, oldState + relation.getWeight());
		}
		return newMarking;
	}

	@Override
	protected PTMarking createNewMarking() {
		return new PTMarking();
	}

	@Override
	protected PTTransition createNewTransition(String name, String label, boolean isSilent) throws ParameterException{
		return new PTTransition(name, label, isSilent);
	}

	@Override
	protected PTPlace createNewPlace(String name, String label) throws ParameterException{
		return new PTPlace(name, label);
	}

	@Override
	protected PTFlowRelation createNewFlowRelation(PTPlace place, PTTransition transition) throws ParameterException{
		return new PTFlowRelation(place, transition);
	}

	@Override
	protected PTFlowRelation createNewFlowRelation(PTTransition transition, PTPlace place) throws ParameterException{
		return new PTFlowRelation(transition, place);
	}
	
	@Override
	public PTMarkingGraph createNewMarkingGraph() throws ParameterException {
		return new PTMarkingGraph();
	}

	@Override
	public PTNet newInstance() {
		return new PTNet();
	}

	@Override
	public void capacityChanged(CapacityEvent<? extends AbstractPlace<PTFlowRelation, Integer>> o) {}
	
	@Override
	public PTMarkingGraph getMarkingGraph() throws PNException{
		return (PTMarkingGraph) super.getMarkingGraph();
	}
	
	@Override
	public PTMarkingGraph buildMarkingGraph() throws PNException{
		return (PTMarkingGraph) super.buildMarkingGraph();
	}
	
}
