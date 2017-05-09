package de.uni.freiburg.iig.telematik.sepia.petrinet.ptc;

import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.mg.ptc.PTCNetMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;

public class PTCNet extends
		AbstractPTCNet<PTCNetPlace, AbstractPTCNetTransition<PTCNetFlowRelation>, PTCNetFlowRelation, PTCNetMarking> {

	private static final long serialVersionUID = 6414068104829281889L;

	public PTCNet() {
		super();
	}

	public PTCNet(Set<String> places, Set<String> transitions, PTCNetMarking initialMarking) {
		super(places, transitions, initialMarking);
	}

	@Override
	public PTCNet newInstance() {
		return new PTCNet();
	}

	@Override
	protected AbstractPTCNetTransition<PTCNetFlowRelation> createNewTransition(String name, String label,
			boolean isSilent) {
		return new PTCNetTransition(name, label, isSilent);
	}

	@Override
	protected PTCNetPlace createNewPlace(String name, String label) {
		return new PTCNetPlace(name, label);
	}

	@Override
	protected PTCNetFlowRelation createNewFlowRelation(PTCNetPlace place,
			AbstractPTCNetTransition<PTCNetFlowRelation> transition, Integer weight) {
		return new PTCNetFlowRelation(place, transition, weight);
	}

	@Override
	protected PTCNetFlowRelation createNewFlowRelation(AbstractPTCNetTransition<PTCNetFlowRelation> transition,
			PTCNetPlace place, Integer weight) {
		return new PTCNetFlowRelation(transition, place, weight);
	}

	@Override
	protected PTCNetFlowRelation createNewFlowRelation(PTCNetPlace place,
			AbstractPTCNetTransition<PTCNetFlowRelation> transition) {
		return new PTCNetFlowRelation(place, transition);
	}

	@Override
	protected PTCNetFlowRelation createNewFlowRelation(AbstractPTCNetTransition<PTCNetFlowRelation> transition,
			PTCNetPlace place) {
		return new PTCNetFlowRelation(transition, place);
	}

	@Override
	public PTCNetMarking createNewMarking() {
		return new PTCNetMarking();
	}

	@Override
	public PTCNetMarking fireCheck(String transitionName) throws PNException {
		PTCNetMarking newMarking = cloneMarking();
		PTCNetTransition transition = (PTCNetTransition) getTransition(transitionName);
		for (PTCNetFlowRelation relation : transition.getIncomingRelations()) {
			String inputPlaceName = relation.getPlace().getName();
			newMarking.set(inputPlaceName, newMarking.get(inputPlaceName) - relation.getWeight());
		}
		for (PTCNetFlowRelation relation : transition.getOutgoingRelations()) {
			String outputPlaceName = relation.getPlace().getName();
			Integer oldState = (newMarking.get(outputPlaceName) == null ? 0 : newMarking.get(outputPlaceName));
			newMarking.set(outputPlaceName, oldState + relation.getWeight());
		}
		return newMarking;
	}

	@Override
	public Class<?> getMarkingGraphClass() {
		return PTCNetMarkingGraph.class;
	}

	public static void main(String[] args) {
		PTCNet net = new PTCNet();
		net.addPlace("p1");
		net.addTransition("t1");
		net.addFlowRelationPT("p1", "t1", 12);
		System.out.println(net);
		PTCNetFlowRelation rel = net.addFlowRelationPT("p1", "t1", 2);
		System.out.println(rel);
		System.out.println(net);
		System.out.println(net.getPlace("p1").getOutgoingRelations().size());

	}

}