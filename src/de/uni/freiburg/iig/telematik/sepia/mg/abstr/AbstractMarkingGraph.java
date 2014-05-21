package de.uni.freiburg.iig.telematik.sepia.mg.abstr;

import java.util.Collection;

import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;



public abstract class AbstractMarkingGraph<M extends AbstractMarking<O>, O extends Object, S extends AbstractMarkingGraphState<M,O>, R extends AbstractLabeledTransitionRelation<S,Event,M>> extends AbstractLabeledTransitionSystem<Event,S,R,M> {

	private S initialState = null;
	
	public AbstractMarkingGraph() {
		super();
	}
	
	public AbstractMarkingGraph(String name) {
		super(name);
	}
	
	public AbstractMarkingGraph(Collection<String> states) {
		super(states);
	}
	
	public AbstractMarkingGraph(String name, Collection<String> states) {
		super(name, states);
	}
	
	public AbstractMarkingGraph(Collection<String> states, Collection<String> events) {
		super(states, events);
	}
	
	public AbstractMarkingGraph(String name, Collection<String> states, Collection<String> events) {
		super(name, states, events);
	}
	
	public S getInitialState() {
		return initialState;
	}

	public void setInitialState(S initialState) {
		this.initialState = initialState;
	}
	
	@Override
	public String toString(){
		StringBuilder relations = new StringBuilder();
		boolean firstEntry = true;
		for(R relation: getRelations()){
			if(!firstEntry){
				relations.append("            ");
			}
			relations.append(relation.toString());
			relations.append('\n');
			firstEntry = false;
		}
		return String.format(toStringFormat, getVertices(), startStates.keySet(), endStates.keySet(), events.keySet(), relations.toString());
	}
	
	@Override
	protected abstract R createNewTransitionRelation(String sourceStateName, String targetStateName, String eventName) throws Exception;


}
