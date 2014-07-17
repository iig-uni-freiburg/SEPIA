package de.uni.freiburg.iig.telematik.sepia.mg.ifnet;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;

public class IFNetMarkingGraph extends AbstractIFNetMarkingGraph<IFNetMarking, IFNetMarkingGraphState, IFNetMarkingGraphRelation> {

	private static final long serialVersionUID = 3438921241428581537L;

	public IFNetMarkingGraph() {
		super();
	}

	public IFNetMarkingGraph(String name) {
		super(name);
	}

	@Override
	protected Event createNewEvent(String name, String label) {
		return new Event(name, label);
	}

	@Override
	public IFNetMarkingGraph createNewInstance() {
		return new IFNetMarkingGraph();
	}

	@Override
	protected IFNetMarkingGraphState createNewState(String name, IFNetMarking element) {
		return new IFNetMarkingGraphState(name, element);
	}

	@Override
	public IFNetMarkingGraphRelation createNewTransitionRelation(IFNetMarkingGraphState sourceState, IFNetMarkingGraphState targetState) {
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
