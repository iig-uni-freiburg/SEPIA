package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.RelationConstraintEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;


/**
 * Abstract class for defining P/T-Nets, which are a special kind of Petri nets
 * that only consider one token type (tokens are indistinguishable).<br>
 * <br>
 * The class defines specific markings that respect P/T-Net properties.<br>
 * Markings contain for each place the number of tokens it contains.
 * By providing appropriate methods, the class allows to add relations having weights<br>
 * (which define the number of tokens transferred over the relation).
 * 
 * @author Thomas Stocker
 */									
public abstract class AbstractPTNet<P extends AbstractPTPlace<F>, 
									T extends AbstractPTTransition<F>, 
									F extends AbstractPTFlowRelation<P,T>, 
									M extends AbstractPTMarking,
									X extends AbstractPTMarkingGraphState<M>,
									Y extends AbstractPTMarkingGraphRelation<M,X>> 

									  extends AbstractPetriNet<P,T,F,M,Integer,X,Y> {

	private static final long serialVersionUID = -1544464197896650398L;
	/**
	 * String format for plain output.
	 * @see #toString()
	 */
	private static final String toStringFormat = "Petri-Net: %s%n          places: %s %n     transitions: %s %n   flow-relation: %n%s %n initial marking: %s %n  actual marking: %s %n";
		
	/**
	 * Creates a new P/T-Net.
	 */
	public AbstractPTNet() {
		super();
	}
	
	/**
	 * Creates a new P/T-Net.
	 * @param places Names of Petri net places to add.
	 * @param transitions Names of Petri net transition to add.
	 * @param initialMarking The initial marking of the Petri net.
	 * @throws ParameterException If some parameters are <code>null</code> or contain <code>null</code>-values.
	 */
	public AbstractPTNet(Set<String> places,
				 Set<String> transitions,
				 M initialMarking) 
				 throws ParameterException {
		super(places, transitions);
		setInitialMarking(initialMarking);
	}
	
	@Override
	public NetType getNetType() {
		return NetType.PTNet;
	}
	
	
	//------- Markings
	
	@Override
	public M getInitialMarking(){
		return super.getInitialMarking();
	}
	
	@Override
	public M getMarking(){
		return super.getMarking();
	}

	@Override
	protected void updateMarking(P p) throws ParameterException {
		Validate.notNull(p);
		if(!places.containsKey(p.getName()))
			return;
		if(p.getState() > 0) {
			marking.set(p.getName(), p.getState());
		} else {
			marking.remove(p.getName());
		}
	}
	
	//------- Flow Relations
	
	@Override
	protected void validateMarking(M marking) throws ParameterException {
		super.validateMarking(marking);
		for(P place: getPlaces()){
			if(marking.get(place.getName()) != null && place.isBounded()){
				if(place.getCapacity() < marking.get(place.getName()))
					throw new ParameterException("Place \""+place.getName()+"\" cannot contain more than " + place.getCapacity() + " tokens");
			}
		}
	}

	@Override
	public void transitionFired(TransitionEvent<? extends AbstractTransition<F, Integer>> e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void relationConstraintChanged(RelationConstraintEvent<? extends AbstractFlowRelation<P, T, Integer>> e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public AbstractPTMarkingGraph<M,X,Y> getMarkingGraph() throws PNException{
		return (AbstractPTMarkingGraph<M, X, Y>) super.getMarkingGraph();
	}
	
	//------- ToString

	@Override
	public String toString() {
		StringBuilder relationBuilder = new StringBuilder();
		for(F relation: relations.values()){
			relationBuilder.append("                  ");
			relationBuilder.append(relation);
			relationBuilder.append('\n');
		}
		return String.format(toStringFormat, name, places.values(), transitions.values(), relationBuilder.toString(), initialMarking, marking);
	}
	
}
