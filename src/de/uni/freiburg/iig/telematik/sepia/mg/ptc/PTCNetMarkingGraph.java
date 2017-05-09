package de.uni.freiburg.iig.telematik.sepia.mg.ptc;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetMarking;

public class PTCNetMarkingGraph
		extends AbstractPTCNetMarkingGraph<PTCNetMarking, PTCNetMarkingGraphState, PTCNetMarkingGraphRelation> {

	private static final long serialVersionUID = -863349359704189513L;

	public PTCNetMarkingGraph() {
		super();
	}

	public PTCNetMarkingGraph(String name) {
		super(name);
	}

	@Override
	protected Event createNewEvent(String name, String label) {
		return new Event(name, label);
	}

	@Override
	public PTCNetMarkingGraph createNewInstance() {
		return new PTCNetMarkingGraph();
	}

	@Override
	protected PTCNetMarkingGraphState createNewState(String name, PTCNetMarking element) {
		return new PTCNetMarkingGraphState(name, element);
	}

	@Override
	public PTCNetMarkingGraphRelation createNewTransitionRelation(PTCNetMarkingGraphState sourceState,
			PTCNetMarkingGraphState targetState) {
		return new PTCNetMarkingGraphRelation(sourceState, targetState);
	}

	@Override
	protected PTCNetMarkingGraphRelation createNewTransitionRelation(String sourceStateName, String targetStateName,
			String eventName) throws Exception {
		validateVertex(sourceStateName);
		validateVertex(targetStateName);
		validateEvent(eventName);
		return new PTCNetMarkingGraphRelation(getState(sourceStateName), getState(targetStateName),
				getEvent(eventName));
	}

}
