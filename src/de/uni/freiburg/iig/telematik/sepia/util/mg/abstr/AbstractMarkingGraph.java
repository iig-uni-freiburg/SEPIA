package de.uni.freiburg.iig.telematik.sepia.util.mg.abstr;

import java.util.Collection;
import java.util.HashSet;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jagal.ts.Event;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionRelation;
import de.uni.freiburg.iig.telematik.jagal.ts.labeled.abstr.AbstractLabeledTransitionSystem;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;



public abstract class AbstractMarkingGraph<M extends AbstractMarking<S>, S extends Object, T extends AbstractMarkingGraphState<M,S>> extends AbstractLabeledTransitionSystem<Event,T, M> {

	private T initialState = null;
	
	public AbstractMarkingGraph() {
		super();
	}
	
	public AbstractMarkingGraph(String name) throws ParameterException{
		super(name);
	}
	
	public AbstractMarkingGraph(Collection<String> states) throws ParameterException{
		super(states);
	}
	
	public AbstractMarkingGraph(String name, Collection<String> states) throws ParameterException{
		super(name, states);
	}
	
	public AbstractMarkingGraph(Collection<String> states, Collection<String> events) throws ParameterException{
		super(states, events);
	}
	
	public AbstractMarkingGraph(String name, Collection<String> states, Collection<String> events) throws ParameterException{
		super(name, states, events);
	}
	
	public T getInitialState() {
		return initialState;
	}

	public void setInitialState(T initialState) {
		this.initialState = initialState;
	}
	
	@Override
	public String toString(){
		StringBuilder relations = new StringBuilder();
		boolean firstEntry = true;
		for(AbstractLabeledTransitionRelation<T,Event,M> relation: getRelations()){
			if(!firstEntry){
				relations.append("            ");
			}
			relations.append(relation.toString());
			relations.append('\n');
			firstEntry = false;
		}
		return String.format(toStringFormat, getVertices(), startStates.keySet(), endStates.keySet(), events.keySet(), relations.toString());
	}
	
	private Collection<M> getElements(Collection<T> vertices){
		Collection<M> result = new HashSet<M>();
		for(T vertex: vertices){
			result.add(vertex.getElement());
		}
		return result;
	}

	@Override
	protected abstract AbstractMarkingGraphRelation<M,T,S> createNewTransitionRelation(String sourceStateName, String targetStateName, String eventName) throws Exception;


}
