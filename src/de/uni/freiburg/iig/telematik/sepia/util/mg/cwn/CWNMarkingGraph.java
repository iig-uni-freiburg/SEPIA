 package de.uni.freiburg.iig.telematik.sepia.util.mg.cwn;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;

public class CWNMarkingGraph extends AbstractCWNMarkingGraph<CWNMarking, CWNMarkingGraphState, CWNMarkingGraphRelation> {

	public CWNMarkingGraph() {
		super();
	}

	public CWNMarkingGraph(String name) throws ParameterException {
		super(name);
	}

	@Override
	protected Event createNewEvent(String name, String label) throws ParameterException {
		return new Event(name, label);
	}

	@Override
	public CWNMarkingGraph createNewInstance() {
		return new CWNMarkingGraph();
	}

	@Override
	protected CWNMarkingGraphState createNewState(String name, CWNMarking element) throws ParameterException {
		return new CWNMarkingGraphState(name, element);
	}

	@Override
	public CWNMarkingGraphRelation createNewTransitionRelation(CWNMarkingGraphState sourceState, CWNMarkingGraphState targetState) throws ParameterException {
		return new CWNMarkingGraphRelation(sourceState, targetState);
	}
	
	@Override
	protected CWNMarkingGraphRelation createNewTransitionRelation(String sourceStateName, String targetStateName, String eventName) throws Exception {
		validateVertex(sourceStateName);
		validateVertex(targetStateName);
		validateEvent(eventName);
		return new CWNMarkingGraphRelation(getState(sourceStateName), getState(targetStateName), getEvent(eventName));
	}

}
