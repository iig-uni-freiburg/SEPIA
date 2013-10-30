package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.event.RelationConstraintEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.FiringRule;


/**
 * Class for defining Colored Petri nets (CPNs), which are a special kind of Petri nets
 * that consider distinguishable tokens (with different colors).<br>
 * <br>
 * The class defines specific markings that respect CPN properties.<br>
 * Markings contain for each place the number of tokens it contains for different colors.
 * By providing appropriate methods, the class allows to add relations having constraints<br>
 * (which define the number of tokens transferred over the relation for different colors).
 * 
 * @author Thomas Stocker
 */
public abstract class AbstractCPN<P extends AbstractCPNPlace<F>,
								  T extends AbstractCPNTransition<F>, 
								  F extends AbstractCPNFlowRelation<P,T>, 
								  M extends AbstractCPNMarking> 
									extends AbstractPetriNet<P,T,F,M, Multiset<String>>{
	
	public static final String DEFAULT_TOKEN_COLOR = "black";
	/**
	 * String format for plain output.
	 * @see #toString()
	 */
	private static final String toStringFormat = "Petri-Net: %n          places: %s %n     transitions: %s %n   flow-relation: %n%s %n initial marking: %s";

	
	/**
	 * Creates a new Colored Petri net.
	 */
	public AbstractCPN(){
		super();
	}
	
	/**
	 * Creates a new Colored Petri net.
	 * @param places Names of Petri net places to add.
	 * @param transitions Names of Petri net transition to add.
	 * @param initialMarking The initial marking of the Petri net.
	 * @throws ParameterException If some parameters are <code>null</code> or contain <code>null</code>-values.
	 */
	public AbstractCPN(Set<String> places, Set<String> transitions, M initialMarking) 
			   throws ParameterException {
		super(places, transitions);
		setInitialMarking(initialMarking);
	}
	
	
	@Override
	public NetType getNetType() {
		return NetType.CPN;
	}
	
	
	public Set<String> getTokenColors(){
		Set<String> colors = new HashSet<String>();
		for(T transition: transitions.values()){
			colors.addAll(transition.getProcessedColors());
		}
		
		for(P place : places.values()){
			colors.addAll(place.getState().support());
		}
		return colors;
	}
	
	public String defaultTokenColor(){
		return DEFAULT_TOKEN_COLOR;
	}
	
	//------- Markings
	
	
//	/**
//	 * Sets the initial marking of the Petri net.<br>
//	 * The initial marking defines the initial state of a Petri net in the sense of tokens contained in places.
//	 * When a Petri net is reset ({@link #reset()}), it should reach its initial marking.<br>
//	 * The initial marking does not have to contain all net places. For places not contained in the given initial marking,
//	 * the net assumes 0 tokens in the initial net state.<br>
//	 * This method uses {@link #setInitialMarking()} to actually transform the net state to the initial state.
//	 * @param marking The marking used as initial marking (state) of the net.
//	 * @throws ParameterException If parameters are <code>null</code>, contain <code>null</code>-values<br>
//	 * or the net does not contain some of the marking places.
//	 */
//	@Override
//	public void setInitialMarking(M marking) throws InconsistencyException, ParameterException{
//		validateMarking(marking);
//		
//		if(initialMarking != null)
//			initialMarking.clear();
//		for(String placeName: marking.places()){
//			Multiset<String> colorMap = marking.get(placeName);
//			Multiset<String> markingColors = new Multiset<String>();
//			for(String color: colorMap.support()){
//				markingColors.setMultiplicity(color, colorMap.multiplicity(color));
//			}
//			initialMarking.set(placeName, markingColors);
//		}
//		setMarking(initialMarking);
//	}
	
//	@Override
//	public void setMarking(M marking) throws ParameterException{
//		validateMarking(marking);
//		try {
//			for(String netPlaceName: places.keySet()){
//				if(!marking.contains(netPlaceName)){
//					places.get(netPlaceName).removeTokens();
//					this.marking.remove(netPlaceName);
//				} else {
//					places.get(netPlaceName).setState(marking.get(netPlaceName));
//					this.marking.set(netPlaceName, marking.get(netPlaceName));
//				}
//			}
//		}catch(ParameterException e){
//			// Cannot happen, since the initial marking is always valid.
//			e.printStackTrace();
//		}
//	}
	
	@Override
	protected void updateMarking(P p) throws ParameterException {
		Validate.notNull(p);
		if(!places.containsKey(p.getName()))
			return;
		if(!p.getState().isEmpty()) {
			marking.set(p.getName(), p.getState());
		} else {
			marking.remove(p.getName());
		}
	}
	
	
	//------- Flow Relations
	

//	/**
//	 * Adds a flow relation to the Petri net leading from a place to a transition.<br>
//	 * When relations are added using this method, they contain no constraints,<br>
//	 * i.e. no information about how many tokens of which color are transferred over the relation.<br>
//	 * Use either {@link #addFlowRelationPT(String, String, String, boolean)} to generate a default constraint,
//	 * or add constraints via {@link #addFiringRule(String, FiringRule)}.
//	 * @see super{@link #addFlowRelationPT(String, String, String)}
//	 */
//	@Override
//	public F addFlowRelationPT(String name, String placeName, String transitionName) throws ParameterException {
//		return super.addFlowRelationPT(name, placeName, transitionName);
//	}

	/**
	 * Adds a flow relation to the Petri net leading from a place to a transition.<br>
	 * When relations are added using this method, they contain no constraints,<br>
	 * i.e. no information about how many tokens of which color are transferred over the relation.<br>
	 * Use either {@link #addFlowRelationPT(String, String, boolean)} to generate a default constraint,
	 * or add constraints via {@link #addFiringRule(String, FiringRule)}.
	 * @see super{@link #addFlowRelationPT(String, String)}
	 */
	@Override
	public F addFlowRelationPT(String placeName, String transitionName) throws ParameterException {
		return super.addFlowRelationPT(placeName, transitionName);
	}
	
	/**
	 * Adds a flow relation to the Petri net leading from a place to a transition.<br>
	 * This method allows to specify if a default constraint is generated for the relation.<br>
	 * A default constraint adds one control flow token to the relation (indicated with color "black").
	 * The relations' name is set automatically, based on the names of the given place/transition.
	 * @param placeName The name of the place where the relation starts.
	 * @param transitionName The name of the transition where the relation ends.
	 * @param addDefaultConstraint Indicates if a default constraint is added to the relation.
	 * @return The new relation that was added to the Petri net<br>
	 * or <code>null</code> if the net already contains the relation.
	 * @throws ParameterException If some parameters are <code>null</code> or the net does not contain the given places/transitions.
	 */
	public F addFlowRelationPT(String placeName, String transitionName, boolean addDefaultConstraint) throws ParameterException {
		F newRelation = super.addFlowRelationPT(placeName, transitionName);
		if(addDefaultConstraint && (newRelation != null))
			newRelation.addConstraint("black", 1);
		return newRelation;
	}
	
//	/**
//	 * Adds a flow relation to the Petri net leading from a place to a transition.<br>
//	 * This method allows to specify if a default constraint is generated for the relation.<br>
//	 * A default constraint adds one control flow token to the relation (indicated with color "black").
//	 * @param name The name for the relation.
//	 * @param placeName The name of the place where the relation starts.
//	 * @param transitionName The name of the transition where the relation ends.
//	 * @param addDefaultConstraint Indicates if a default constraint is added to the relation.
//	 * @return The new relation that was added to the Petri net<br>
//	 * or <code>null</code> if the net already contains the relation.
//	 * @throws ParameterException If some parameters are <code>null</code> or the net does not contain the given places/transitions.
//	 */
//	public F addFlowRelationPT(String name, String placeName, String transitionName, boolean addDefaultConstraint) throws ParameterException {
//		F newRelation = super.addFlowRelationPT(name, placeName, transitionName);
//		if(addDefaultConstraint && (newRelation != null))
//			newRelation.addConstraint("black", 1);
//		return newRelation;
//	}
	
//	/**
//	 * Adds a flow relation to the Petri net leading from a transition to a place.<br>
//	 * When relations are added using this method, they contain no constraints,<br>
//	 * i.e. no information about how many tokens of which color are transferred over the relation.<br>
//	 * Use either {@link #addFlowRelationPT(String, String, String, boolean)} to generate a default constraint,
//	 * or add constraints via {@link #addFiringRule(String, FiringRule)}.
//	 * @see super{@link #addFlowRelationPT(String, String, String)}
//	 */
//	@Override
//	public F addFlowRelationTP(String name, String transitionName, String placeName) throws ParameterException {
//		return super.addFlowRelationTP(name, transitionName, placeName);
//	}

	/**
	 * Adds a flow relation to the Petri net leading from a transition to a place.<br>
	 * When relations are added using this method, they contain no constraints,<br>
	 * i.e. no information about how many tokens of which color are transferred over the relation.<br>
	 * Use either {@link #addFlowRelationPT(String, String, boolean)} to generate a default constraint,
	 * or add constraints via {@link #addFiringRule(String, FiringRule)}.
	 * @see super{@link #addFlowRelationPT(String, String)}
	 */
	@Override
	public F addFlowRelationTP(String transitionName, String placeName) throws ParameterException {
		return super.addFlowRelationTP(transitionName, placeName);
	}

	/**
	 * Adds a flow relation to the Petri net leading from a transition to a place.<br>
	 * This method allows to specify if a default constraint is generated for the relation.<br>
	 * A default constraint adds one control flow token to the relation (indicated with color "black").
	 * The relations' name is set automatically, based on the names of the given place/transition.
	 * @param transitionName The name of the transition where the relation starts.
	 * @param placeName The name of the place where the relation ends.
	 * @param addDefaultConstraint Indicates if a default constraint is added to the relation.
	 * @return The new relation that was added to the Petri net<br>
	 * or <code>null</code> if the net already contains the relation.
	 * @throws ParameterException If some parameters are <code>null</code> or the net does not contain the given places/transitions.
	 */
	public F addFlowRelationTP(String transitionName, String placeName, boolean addDefaultConstraint) throws ParameterException {
		F newRelation = super.addFlowRelationTP(transitionName, placeName);
		if(addDefaultConstraint && (newRelation != null))
			newRelation.addConstraint("black", 1);
		return newRelation;
	}
	
	
	
	//------- Interface methods -------------------------------------------------------------
	
	@Override
	public void relationConstraintChanged(RelationConstraintEvent<? extends AbstractFlowRelation<P, T, Multiset<String>>> e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void transitionFired(TransitionEvent<? extends AbstractTransition<F, Multiset<String>>> e) {
		// TODO Auto-generated method stub
	}
	
	
	//------- Soundness ---------------------------------------------------------------------
	
	/**
	 * CPNs require relation effectiveness.<br>
	 * Each relation must move at least one token from a place to a transition or vice versa.
	 * 
	 * @see AbstractPetriNet#checkValidity()
	 * @throws PNValidationException 
	 * @see AbstractCPNFlowRelation#hasConstraints()
	 */
	@Override 
	public void checkValidity() throws PNValidationException {
		super.checkValidity();
		for(AbstractCPNFlowRelation<?,?> relation: relations.values()){
			if(!relation.hasConstraints()){
				throw new PNValidationException("Inoperative relation: " + relation.toString());
			} 
		}
	} 
	
	//------- Firing Rules

	/**
	 * Adds a firing rule for a transition to the Petri net.<br>
	 * A firing rule of a transition defines the tokens needed to fire (to get enabled)
	 * for places leading to the transitions and the tokens that are produced and added 
	 * to places connected with outgoing relations.<br>
	 * This method translates the given firing rule into constraints for incoming and outgoing relations 
	 * of the given transition and sets them accordingly.
	 * @param transitionName The name of the transition for which the firing rule is added.
	 * @param rule The firing rule.
	 * @throws ParameterException If some parameters are <code>null</code>, the transition is unknown<br>
	 * or the firing rule contains unknown places.
	 */
	public void addFiringRule(String transitionName, FiringRule rule) throws ParameterException{
		Validate.notNull(transitionName);
		if(!containsTransition(transitionName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unknown transition: "+transitionName);
		Validate.notNull(rule);
		
		T transition = transitions.get(transitionName);
		
		// Add relation constraints for requirements (incoming places)
		for(String reqPlaceName: rule.getRequirements().keySet()){
			if(!containsPlace(reqPlaceName))
				throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unknown place: "+reqPlaceName);
			P place = places.get(reqPlaceName);
			F relationFromPlace = (F) transition.getRelationFrom(place);			
			if(relationFromPlace == null)
				throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net contains no transition from place "+reqPlaceName+" to transition "+transitionName);
			Map<String, Integer> colorRequirement = rule.getRequirements().get(reqPlaceName);
			for(String color: colorRequirement.keySet()){
				relationFromPlace.addConstraint(color, colorRequirement.get(color));
			}
		}
		// Add relation constraints for productions (outgoing places)
		for(String prodPlaceName: rule.getProductions().keySet()){
			if(!containsPlace(prodPlaceName))
				throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unknown place: "+prodPlaceName);
			P place = places.get(prodPlaceName);
			F relationToPlace = (F) transition.getRelationTo(place);
			if(relationToPlace == null)
				throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net contains no transition from transition "+transitionName+" to place "+prodPlaceName);
			Map<String, Integer> colorRequirement = rule.getProductions().get(prodPlaceName);
			for(String color: colorRequirement.keySet()){
				relationToPlace.addConstraint(color, colorRequirement.get(color));
			}
		}
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
		return String.format(toStringFormat, places.values(), transitions.values(), relationBuilder.toString(), initialMarking);
	}

}
