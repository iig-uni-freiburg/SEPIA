package de.uni.freiburg.iig.telematik.sepia.mg.pt;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;

public class PTMarkingGraph extends AbstractPTMarkingGraph<PTMarking, PTMarkingGraphState, PTMarkingGraphRelation> {

	public PTMarkingGraph() {
		super();
	}

	public PTMarkingGraph(String name) {
		super(name);
	}

	@Override
	protected Event createNewEvent(String name, String label) {
		return new Event(name, label);
	}

	@Override
	public PTMarkingGraph createNewInstance() {
		return new PTMarkingGraph();
	}

	@Override
	protected PTMarkingGraphState createNewState(String name, PTMarking element) {
		return new PTMarkingGraphState(name, element);
	}

	@Override
	public PTMarkingGraphRelation createNewTransitionRelation(PTMarkingGraphState sourceState, PTMarkingGraphState targetState) {
		return new PTMarkingGraphRelation(sourceState, targetState);
	}
	
	@Override
	protected PTMarkingGraphRelation createNewTransitionRelation(String sourceStateName, String targetStateName, String eventName) throws Exception {
		validateVertex(sourceStateName);
		validateVertex(targetStateName);
		validateEvent(eventName);
		return new PTMarkingGraphRelation(getState(sourceStateName), getState(targetStateName), getEvent(eventName));
	}

}
