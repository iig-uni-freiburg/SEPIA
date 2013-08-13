package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr;

import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.RelationConstraintEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;


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
public abstract class AbstractPTNet<P extends AbstractPTPlace<F>, T extends AbstractPTTransition<F>, F extends AbstractPTFlowRelation<P,T>, M extends AbstractPTMarking> extends AbstractPetriNet<P, T, F, M, Integer> {

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
	
	//------- Markings
	
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

	/**
	 * Adds a flow relation to the Petri net leading from a place to a transition.<br>
	 * This method allows to specify an arc weight which defines the number of tokens transferred along the relation.<br>
	 * The relations' name is set automatically, based on the names of the given place/transition.
	 * @param placeName The name of the place where the relation starts.
	 * @param transitionName The name of the transition where the relation ends.
	 * @param weight The weight of the relation.
	 * @return The new relation that was added to the Petri net<br>
	 * or <code>null</code> if the net already contains the relation.
	 * @throws ParameterException If some parameters are <code>null</code> or the net does not contain the given places/transitions.
	 */
	public F addFlowRelationPT(String placeName, String transitionName, int weight) throws ParameterException {
		F newRelation = super.addFlowRelationPT(placeName, transitionName);
		if(newRelation != null){
			newRelation.setWeight(weight);
		}
		return newRelation;
	}
	
	/**
	 * Adds a flow relation to the Petri net leading from a transition to a place.<br>
	 * This method allows to specify an arc weight which defines the number of tokens transferred along the relation.<br>
	 * The relations' name is set automatically, based on the names of the given place/transition.
	 * @param name The name of the relation.
	 * @param transitionName The name of the transition where the relation starts.
	 * @param placeName The name of the place where the relation ends.
	 * @param weight The weight of the relation.
	 * @return The new relation that was added to the Petri net<br>
	 * or <code>null</code> if the net already contains the relation.
	 * @throws ParameterException If some parameters are <code>null</code> or the net does not contain the given places/transitions.
	 */
	public F addFlowRelationTP(String transitionName, String placeName, int weight) throws ParameterException {
		F newRelation = super.addFlowRelationTP(transitionName, placeName);
		if(newRelation != null){
			newRelation.setWeight(weight);
		}
		return newRelation;
	}
	
	@Override
	public void transitionFired(TransitionEvent<? extends AbstractTransition<F, Integer>> e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void relationConstraintChanged(RelationConstraintEvent<? extends AbstractFlowRelation<P, T, Integer>> e) {
		// TODO Auto-generated method stub
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
