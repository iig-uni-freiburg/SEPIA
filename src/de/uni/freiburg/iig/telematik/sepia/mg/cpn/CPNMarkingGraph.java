package de.uni.freiburg.iig.telematik.sepia.mg.cpn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;

public class CPNMarkingGraph extends AbstractCPNMarkingGraph<CPNMarking, CPNMarkingGraphState, CPNMarkingGraphRelation> {

	public CPNMarkingGraph() {
		super();
	}

	public CPNMarkingGraph(String name) throws ParameterException {
		super(name);
	}

	@Override
	protected Event createNewEvent(String name, String label) throws ParameterException {
		return new Event(name, label);
	}

	@Override
	public CPNMarkingGraph createNewInstance() {
		return new CPNMarkingGraph();
	}

	@Override
	protected CPNMarkingGraphState createNewState(String name, CPNMarking element) throws ParameterException {
		return new CPNMarkingGraphState(name, element);
	}

	@Override
	public CPNMarkingGraphRelation createNewTransitionRelation(CPNMarkingGraphState sourceState, CPNMarkingGraphState targetState) throws ParameterException {
		return new CPNMarkingGraphRelation(sourceState, targetState);
	}
	
	@Override
	protected CPNMarkingGraphRelation createNewTransitionRelation(String sourceStateName, String targetStateName, String eventName) throws Exception {
		validateVertex(sourceStateName);
		validateVertex(targetStateName);
		validateEvent(eventName);
		return new CPNMarkingGraphRelation(getState(sourceStateName), getState(targetStateName), getEvent(eventName));
	}

}
