package de.uni.freiburg.iig.telematik.sepia.mg.ifnet;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;

public class IFNetMarkingGraph extends AbstractIFNetMarkingGraph<IFNetMarking, IFNetMarkingGraphState, IFNetMarkingGraphRelation> {

	public IFNetMarkingGraph() {
		super();
	}

	public IFNetMarkingGraph(String name) throws ParameterException {
		super(name);
	}

	@Override
	protected Event createNewEvent(String name, String label) throws ParameterException {
		return new Event(name, label);
	}

	@Override
	public IFNetMarkingGraph createNewInstance() {
		return new IFNetMarkingGraph();
	}

	@Override
	protected IFNetMarkingGraphState createNewState(String name, IFNetMarking element) throws ParameterException {
		return new IFNetMarkingGraphState(name, element);
	}

	@Override
	public IFNetMarkingGraphRelation createNewTransitionRelation(IFNetMarkingGraphState sourceState, IFNetMarkingGraphState targetState) throws ParameterException {
		return new IFNetMarkingGraphRelation(sourceState, targetState);
	}
	
	@Override
	protected IFNetMarkingGraphRelation createNewTransitionRelation(String sourceStateName, String targetStateName, String eventName) throws Exception {
		validateVertex(sourceStateName);
		validateVertex(targetStateName);
		validateEvent(eventName);
		return new IFNetMarkingGraphRelation(getState(sourceStateName), getState(targetStateName), getEvent(eventName));
	}

}
