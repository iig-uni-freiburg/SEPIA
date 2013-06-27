package petrinet;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.invation.code.toval.validate.ParameterException.ErrorCode;

import traverse.Traversable;
import event.RelationConstraintListener;
import event.TokenEvent;
import event.TokenListener;
import event.TransitionEvent;
import event.TransitionListener;
import exception.PNException;
import exception.PNSoundnessException;
import exception.PNValidationException;

/**
 * Abstract class for defining Petri nets and their properties.<br>
 * <br>
 * It contains major properties like the places, transitions and relations of a Petri net
 * and provides methods for adding/removing them.<br>
 * <br>
 * It keeps track of the nets' state by remembering the marking of the net 
 * which describes the number and kind of tokens contained in net places.
 * The net state changes when transitions fire.
 * Using listeners, the net reacts on changes of transitions and places that belong to the net.
 * <br>
 * It also keeps track of enabled transitions, transition and place sources/drains.
 * A transition is enabled if all its precondition hold.<br>
 * Preconditions are defined in the sense of constraints on the number and kind<br>
 * of tokens contained in places that have outgoing relations to the transition.<br>
 * In case an enabled transition fires, it removes tokens from incoming places and puts tokens into outgoing places.
 * <br>
 * This class allows to fire an enabled transition.<br>
 * In case there is more than one transition, 
 * the FlowControl determines the transition to fire.
 * 
 * @author Thomas Stocker
 *
 * @param <P> Type of Petri net places
 * @param <T> Type of Petri net transitions
 * @param <F> Type of Petri net relations
 * @param <M> Type of Petri net markings
 * @param <S> Type of Petri net place states
 */
public abstract class AbstractPetriNet<P extends AbstractPlace<F,S>, 
									   T extends AbstractTransition<F,S>, 
									   F extends AbstractFlowRelation<P,T,S>,
									   M extends AbstractMarking<S>,
									   S extends Object>

			    implements 	TransitionListener<AbstractTransition<F,S>>, 
			    			TokenListener<AbstractPlace<F,S>>,
			    			RelationConstraintListener<AbstractFlowRelation<P,T,S>>,
							Traversable<AbstractPNNode<F>>{
	
	/**
	 * Name of the Petri net.
	 */
	protected String name = "";
	/**
	 * Map that contains Petri net places indexed with their names.
	 */
	protected Map<String, P> places = new HashMap<String, P>();
	/**
	 * Map that contains Petri net places without incoming relations.
	 */
	protected Map<String, P> sourcePlaces = new HashMap<String, P>();
	/**
	 * Map that contains Petri net places without outgoing relations.
	 */
	protected Map<String, P> drainPlaces = new HashMap<String, P>();
	/**
	 * Map that contains Petri net transitions indexed with their names.
	 */
	protected Map<String, T> transitions = new HashMap<String, T>();
	/**
	 * Map that contains Petri net transitions without incoming relations.
	 */
	protected Map<String, T> sourceTransitions = new HashMap<String, T>();
	/**
	 * Map that contains Petri net transitions without outgoing relations.
	 */
	protected Map<String, T> drainTransitions = new HashMap<String, T>();
	/**
	 * Map that contains Petri net relations indexed with their names.
	 */
	protected Map<String, F> relations = new HashMap<String, F>();
	
	/**
	 * A list that contains all enabled transitions.<br>
	 */
	protected List<T> enabledTransitions = new ArrayList<T>();
	/**
	 * The last fired transition.
	 */
	protected T lastFiredTransition = null;
	/**
	 * The initial marking of the Petri net.<br>
	 * This marking defines the initial state of a Petri net, before the firing of any transitions.
	 */
	protected M initialMarking = null;
	/**
	 * The actual marking of the Petri net.<br>
	 * This marking changes whenever transitions fire and tokens are added to or removed from net places.
	 */
	protected M marking = null;
	
	
	protected Boundedness boundedness = Boundedness.UNKNOWN;
	
//	/**
//	 * The flow control of the Petri net.<br>
//	 * It determines the next enabled transition to fire.
//	 */
//	protected FlowControl<P,T,F> flowControl;
	
	
	//------- Constructors --------------------------------------------------------------------------
	
	/**
	 * Creates a new Petri net, using a DefaultFlow as flow control.<br>
	 * The next enabled transition is chosen randomly.
	 */
	public AbstractPetriNet(){

		
		initialize();
		initialMarking = createNewMarking();
		marking = createNewMarking();
	}
	
	/**
	 * Creates a new Petri net using a DefaultFlow as flow control.<br>
	 * The next enabled transition is chosen randomly.
	 * @param places Names of Petri net places to add.
	 * @param transitions Names of Petri net transition to add.
	 * @throws ParameterException If some parameters are <code>null</code> or contain <code>null</code>-values.
	 */
	public AbstractPetriNet(Set<String> places, Set<String> transitions) throws ParameterException {
		this();			
		addTransitions(transitions);
		addPlaces(places);
	}
	
	
	/**
	 * Initialization method for basic properties and fields. <br>
	 * Called by default constructor.
	 */
	protected void initialize(){};
	
	
	
	//------- Basic properties ----------------------------------------------------------------------
	
	/**
	 * Returns the name of the Petri net.
	 * @return The name of the Petri net.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the name of the Petri net to the given name.
	 * @param name New name for the Petri net.
	 * @throws ParameterException If the given name is <code>null</code>.
	 */
	public void setName(String name) throws ParameterException{
		Validate.notNull(name);
		this.name = name;
	}
	
//	/**
//	 * Sets the flow control for the Petri net.<br>
//	 * The flow control decides which transition to fire, if there is more than one enabled transition.
//	 * @param flowControl The flow control to use.
//	 * @throws ParameterException If the flow control parameter is <code>null</code>.
//	 * @throws InconsistencyException If the given flow control is in invalid state.
//	 */
//	public void setFlowControl(FlowControl<P,T,F> flowControl) throws ParameterException, InconsistencyException{
//		Validate.notNull(flowControl);
//		if(!flowControl.isValid())
//			throw new InconsistencyException("FlowControl is not valid");
//		this.flowControl = flowControl;
//	}
	
	/**
	 * Checks if the Petri net has enabled transitions that are ready to fire.
	 * @return <code>true</code> if the net has enabled transitions;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean hasEnabledTransitions(){
		return enabledTransitions.size()>0;
	}
	
	/**
	 * Returns a list of all enabled transitions.
	 * @return A list of enabled transitions.
	 */
	public List<T> getEnabledTransitions(){
		return Collections.unmodifiableList(enabledTransitions);
	}
	
	/**
	 * Returns the last fired transition.<br>
	 * @return The last fired transition or <code>null</code> if no transition was fired so far.
	 */
	public T getLastFiredTransition(){
		return lastFiredTransition;
	}
	
	//------- Transitions ------------------------------------------------------------------------
	
	/**
	 * Returns the Petri net transitions.
	 * @return A collection of Petri net transitions.
	 */
	public Collection<T> getTransitions(){
		return Collections.unmodifiableCollection(transitions.values());
	}
	
	/**
	 * Returns the Petri net source transitions.
	 * @return A collection of Petri net transitions.
	 */
	public Collection<T> getSourceTransitions(){
		return Collections.unmodifiableCollection(sourceTransitions.values());
	}
	
	/**
	 * Returns the Petri net drain transitions.
	 * @return A collection of Petri net transitions.
	 */
	public Collection<T> getDrainTransitions(){
		return Collections.unmodifiableCollection(drainTransitions.values());
	}
	
	/**
	 * Returns the transition with the given name.
	 * @param transitionName The name of the desired transition.
	 * @return The Petri net transition with the given name<br>
	 * or <code>null</code> if the net does not contain a transition with the given name.
	 */
	public T getTransition(String transitionName){
		return transitions.get(transitionName);
	}
	
	/**
	 * Returns all transitions with the given label.
	 * @param label The label of all desired transitions.
	 * @return A set of all transitions with the given label.
	 */
	public Set<T> getTransitions(String label){
		Set<T> result = new HashSet<T>();
		for(T transition: transitions.values()){
			if(transition.getLabel().equals(label)){
				result.add(transition);
			}
		}
		return result;
	}
	
	/**
	 * Adds transitions with the given names to the Petri net.<br>
	 * Transitions names have to be unique. In case the net already contains transitions with
	 * given names, less transitions than the given number of arguments may be added to the Petri net.<br>
	 * This method calls {@link #addTransition(String)} for each transition name.
	 * @param transitionNames Names for the Petri net transitions.
	 * @return <code>true</code> if at least one transition was successfully added;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the set of transition names is <code>null</code>
	 * or some transition names are <code>null</code>.
	 * @see #addTransition(String)
	 */
	public boolean addTransitions(Collection<String> transitionNames) throws ParameterException {
		Validate.notNull(transitionNames);
		boolean updated = false;
		for(String transitionName: transitionNames){
			if(addTransition(transitionName)){
				updated = true;
			}
		}
		return updated;
	}
	
	/**
	 * Adds a transition with the given name to the Petri net.<br>
	 * Transitions names have to be unique. In case the net already contains a transition with
	 * the given name, no transition is added to the net.
	 * @param transitionName The name for the Petri net transition.
	 * @return <code>true</code> if the transition was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the transition name is <code>null</code>.
	 */
	public boolean addTransition(String transitionName) throws ParameterException{
		return addTransition(transitionName, transitionName);
	}
	
	/**
	 * Adds a transition with the given name and label to the Petri net.<br>
	 * Transitions names have to be unique. In case the net already contains a transition with
	 * the given name, no transition is added to the net.
	 * @param transitionName The name for the Petri net transition.
	 * @param transitionLabel the label for the Petri net transition.
	 * @return <code>true</code> if the transition was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the transition name is <code>null</code>.
	 */
	public boolean addTransition(String transitionName, String transitionLabel) throws ParameterException{
		return addTransition(transitionName, transitionLabel, false);
	}
	
	/**
	 * Adds a transition with the given name and silent-state to the Petri net.<br>
	 * Transitions names have to be unique. In case the net already contains a transition with
	 * the given name, no transition is added to the net.
	 * @param transitionName The name for the Petri net transition.
	 * @param isSilent The silent state of the transition.
	 * @return <code>true</code> if the transition was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the transition name is <code>null</code>.
	 */
	public boolean addTransition(String transitionName, boolean isSilent) throws ParameterException{
		return addTransition(transitionName, transitionName, isSilent);
	}
	
	/**
	 * Adds a transition with the given name, label and silent-state to the Petri net.<br>
	 * Transitions names have to be unique. In case the net already contains a transition with
	 * the given name, no transition is added to the net.
	 * @param transitionName The name for the Petri net transition.
	 * @param transitionLabel the label for the Petri net transition.
	 * @param isSilent The silent state of the transition.
	 * @return <code>true</code> if the transition was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the transition name is <code>null</code>.
	 */
	public boolean addTransition(String transitionName, String transitionLabel, boolean isSilent) throws ParameterException{
		if(containsTransition(transitionName))
			return false;
		addTransition(createNewTransition(transitionName, transitionLabel, isSilent));
		return true;
	}
	
	/**
	 * Protected method for adding a transition of type <code>T<code> to the transition map.<br>
	 * This method is called from {@link #addTransition(String)} which checks if there already exists
	 * a transition with equal name before making the call.
	 * @param transition The Petri net transition to add.
	 * @throws ParameterException If the given transition is <code>null</code>.
	 */
	protected void addTransition(T transition) throws ParameterException {
		
		
		Validate.notNull(transition);
		transitions.put(transition.getName(), transition);
				
		try {
			transition.addTransitionListener(this);									
			enabledTransitions.add(transition);
		} catch (ParameterException e) {
			// Cannot happen, since "this" is not null
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the Petri net contains the given transition.
	 * @param transition The transition to check.
	 * @return <code>true</code> if the net contains the transition;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given transition is <code>null</code>.
	 */
	protected boolean containsTransition(T transition) throws ParameterException{
		return containsTransition(transition.getName());
	}
	
	/**
	 * Checks if the Petri net contains a transition with the given name.
	 * @param transitionName The name of the transition to check.
	 * @return <code>true</code> if the net contains a transition with the given name;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the transition name is <code>null</code>.
	 */
	public boolean containsTransition(String transitionName) throws ParameterException{
		Validate.notNull(transitionName);
		return transitions.keySet().contains(transitionName);
	}
	
	/**
	 * Removes the transition with the given name from the Petri net.<br>
	 * On removing a transition, all corresponding relations are removed as well.
	 * @param transitionName The name of the place to remove
	 * @return <code>true</code> if the transition was successfully removed from the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given transition name is <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public boolean removeTransition(String transitionName) throws ParameterException{
		if(!containsTransition(transitionName))
			return false;
		for(AbstractFlowRelation<? extends AbstractPlace<F,S>,? extends AbstractTransition<F,S>, S> relation: transitions.get(transitionName).getIncomingRelations()){
			removeFlowRelation((F) relation);
		}
		for(AbstractFlowRelation<? extends AbstractPlace<F,S>,? extends AbstractTransition<F,S>, S> relation: transitions.get(transitionName).getOutgoingRelations()){
			removeFlowRelation((F) relation);
		}
		enabledTransitions.remove(getTransition(transitionName));
		transitions.remove(transitionName);
		sourceTransitions.remove(transitionName);
		drainTransitions.remove(transitionName);
		return true;
	}
	
	/**
	 * Creates a new transition of type <code>T</code> with the given name.<br>
	 * This method is abstract because only subclasses know the type <code>T</code> of their transitions.
	 * @param name The name for the new transition.
	 * @return A new transition of type <code>T</code> with the given name.
	 */
	protected abstract T createNewTransition(String name, String label, boolean isSilent) throws ParameterException;
	
	
	//------- Places ---------------------------------------------------------------------------------


	/**
	 * Returns all Petri net places.
	 * @return A collection of all Petri net places.
	 */
	public Collection<P> getPlaces(){
		return Collections.unmodifiableCollection(places.values());
	}
	
	/**
	 * Returns all Petri net source places.
	 * @return A collection of Petri net places.
	 */
	public Collection<P> getSourcePlaces(){
		return Collections.unmodifiableCollection(sourcePlaces.values());
	}
	
	/**
	 * Returns all Petri net drain places.
	 * @return A collection of Petri net places.
	 */
	public Collection<P> getDrainPlaces(){
		return Collections.unmodifiableCollection(drainPlaces.values());
	}
	
	/**
	 * Returns the net place with the given name.
	 * @param placeName The name of the desired place.
	 * @return The Petri net place with the given name<br>
	 * or <code>null</code> if the net does not contain a place with the given name.
	 */
	public P getPlace(String placeName){
		return places.get(placeName);
	}
	
	/**
	 * Adds places with the given names to the Petri net.<br>
	 * Place names have to be unique. In case the net already contains places with
	 * given names, less places than the given number of arguments may be added to the Petri net.<br>
	 * This method calls {@link #addPlace(String)} for each place name.
	 * @param placeNames Names for the Petri net places to add.
	 * @return <code>true</code> if at least one place was successfully added;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the set of place names is <code>null</code>
	 * or some place names are <code>null</code>.
	 * @see #addPlace(String)
	 */
	public boolean addPlaces(Set<String> placeNames) throws ParameterException {
		Validate.notNull(placeNames);
		boolean updated = false;
		for(String placeName: placeNames){
			if(addPlace(placeName)){
				updated = true;
			}
		}
		return updated;
	}
	
	/**
	 * Adds a place with the given name to the Petri net.<br>
	 * Place names have to be unique. In case the net already contains a place with
	 * the given name, no place is added to the net.
	 * @param placeName The name for the Petri net place.
	 * @return <code>true</code> if the place was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the place name is <code>null</code>.
	 */
	public boolean addPlace(String placeName) throws ParameterException{
		return addPlace(placeName, placeName);
	}
	
	/**
	 * Adds a place with the given name to the Petri net.<br>
	 * Place names have to be unique. In case the net already contains a place with
	 * the given name, no place is added to the net.
	 * Place labels do not have to be unique.
	 * @param placeName The name for the Petri net place.
	 * @param placeLabel The label for the Petri net place.
	 * @return <code>true</code> if the place was successfully added to the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the place name is <code>null</code>.
	 */
	public boolean addPlace(String placeName, String placeLabel) throws ParameterException{
		if(containsPlace(placeName))
			return false;
		addPlace(createNewPlace(placeName, placeLabel));
		return true;
	}
	
	/**
	 * Protected method for adding a place of type <code>P<code> to the place map.<br>
	 * This method is called from {@link #addPlace(String)} which checks if there already exists
	 * a place with equal name before making the call.
	 * @param place The Petri net place to add.
	 * @throws ParameterException If the given place is <code>null</code>.
	 */
	protected void addPlace(P place) throws ParameterException {
		Validate.notNull(place);
		places.put(place.getName(), place);
		try {
			place.addTokenListener(this);
		} catch (ParameterException e) {
			// Cannot happen, since "this" is not null
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the Petri net contains the given place.
	 * @param place The place to check.
	 * @return <code>true</code> if the net contains the transition;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given place is <code>null</code>.
	 */
	protected boolean containsPlace(P place) throws ParameterException{
		return containsTransition(place.getName());
	}
	
	/**
	 * Checks if the Petri net contains a place with the given name.
	 * @param placeName The name of the place to check.
	 * @return <code>true</code> if the net contains a place with the given name;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the place name is <code>null</code>.
	 */
	public boolean containsPlace(String placeName) throws ParameterException{
		Validate.notNull(placeName);
		return places.keySet().contains(placeName);
	}
	
	/**
	 * Creates a new place of type <code>P</code> with the given name.<br>
	 * This method is abstract because only subclasses know the type <code>P</code> of their places.
	 * @param name The name for the new place.
	 * @return A new place of type <code>P</code> with the given name.
	 */
	protected abstract P createNewPlace(String name, String label) throws ParameterException;
	
	
	/**
	 * Removes the place with the given name from the Petri net.<br>
	 * On removing a place, all corresponding relations are removed as well.
	 * @param placeName The name of the place to remove
	 * @return <code>true</code> if the place was successfully removed from the net;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given place name is <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public boolean removePlace(String placeName) throws ParameterException{
		if(!containsPlace(placeName))
			return false;
		for(AbstractFlowRelation<? extends AbstractPlace<F,S>,? extends AbstractTransition<F,S>, S> relation: places.get(placeName).getIncomingRelations()){
			removeFlowRelation((F) relation);
		}
		for(AbstractFlowRelation<? extends AbstractPlace<F,S>,? extends AbstractTransition<F,S>, S> relation: places.get(placeName).getOutgoingRelations()){
			removeFlowRelation((F) relation);
		}
		places.remove(placeName);
		sourcePlaces.remove(placeName);
		drainPlaces.remove(placeName);
		initialMarking.remove(placeName);
		marking.remove(placeName);
		return true;
	}
	
	
	//------- Flow Relations ------------------------------------------------------------------------
	
	/**
	 * Returns the flow relations of the Petri net.
	 * @return A collection of flow relations.
	 */
	public Collection<F> getFlowRelations(){
		return Collections.unmodifiableCollection(relations.values());
	}
	
	/**
	 * Adds a flow relation starting at a place and ending at a transition.
	 * @param placeName The name of the place where toe relation starts.
	 * @param transitionName The name of the transition where the relation ends.
	 * @return <code>true</code> if the flow relation was successfully added;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If parameters are <code>null</code><br>
	 * or the net does not contain a place/transition with given names.
	 */
	public F addFlowRelationPT(String placeName, String transitionName) throws ParameterException{
		if(!containsPlace(placeName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a place with name \""+placeName+"\"");
		if(!containsTransition(transitionName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a transition with name \""+transitionName+"\"");
		F newFlowRelation = createNewFlowRelation(places.get(placeName), transitions.get(transitionName));
		if(addFlowRelation(newFlowRelation)){
			return newFlowRelation;
		}
		return null;
	}
	
	/**
	 * Adds a flow relation starting at a transition and ending at a place.
	 * @param transitionName The name of the transition where the relation ends.
	 * @param placeName The name of the place where toe relation starts.
	 * @return <code>true</code> if the flow relation was successfully added;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If parameters are <code>null</code><br>
	 * or the net does not contain a place/transition with given names.
	 */
	public F addFlowRelationTP(String transitionName, String placeName) throws ParameterException{
		if(!containsPlace(placeName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a place with name \""+placeName+"\"");
		if(!containsTransition(transitionName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a transition with name \""+transitionName+"\"");
		F newFlowRelation = createNewFlowRelation(transitions.get(transitionName), places.get(placeName));
		if(addFlowRelation(newFlowRelation)){
			return newFlowRelation;
		}
		return null;
	}
	
	/**
	 * Protected method for adding a relation of type <code>F<code> to the relation map.<br>
	 * This method is called from {@link #addFlowRelationPT(String)} and {@link #addFlowRelationTP(String, String)}
	 * which both check if there already exists a relation with equal name before making the call.<br>
	 * They also ensure that the place/transition of the relation are places/transitions of the net.
	 * @param relation The Petri net relation to add.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 * @see #checkSD(AbstractPlace)
	 * @see #checkSD(AbstractTransition)
	 */
	protected boolean addFlowRelation(F relation) throws ParameterException {
		Validate.notNull(relation);
		if(containsRelation(relation))
			return false;
		if(relation.getDirectionPT()){
			relation.getPlace().addOutgoingRelation(relation);
			relation.getTransition().addIncomingRelation(relation);
		} else {
			relation.getTransition().addOutgoingRelation(relation);
			relation.getPlace().addIncomingRelation(relation);
		}
		relations.put(relation.getName(), relation);
		checkSD(relation.getPlace());
		checkSD(relation.getTransition());
		try{
			relation.addRelationConstraintListener(this);
		} catch(ParameterException e){}
		return true;
	}
	
	/**
	 * Checks the source and drain property of the given place<br>
	 * and adds/removes it from the list of source/drain places.
	 * @param place The Petri net place to check.
	 */
	private void checkSD(P place){
		if(place.isSource()){
			sourcePlaces.put(place.getName(), place);
		} else {
			sourcePlaces.remove(place.getName());
		}
		if (place.isDrain()){
			drainPlaces.put(place.getName(), place);
		} else {
			drainPlaces.remove(place.getName());
		}
	}
	
	/**
	 * Checks the source and drain property of the given transition<br>
	 * and adds/removes it from the list of source/drain transitions.
	 * @param place The Petri net transition to check.
	 */
	private void checkSD(T transition){
		if(transition.isSource()){
			sourceTransitions.put(transition.getName(), transition);
		} else {
			sourceTransitions.remove(transition.getName());
		}	
		if (transition.isDrain()){
			drainTransitions.put(transition.getName(), transition);
		} else {
			drainTransitions.remove(transition.getName());
		}
	}
	
	/**
	 * Checks if the Petri net contains the given relation.
	 * @param place The relation to check.
	 * @return <code>true</code> if the net contains the relation;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 */
	protected boolean containsRelation(F relation) throws ParameterException{
		return relations.values().contains(relation);
	}
	
	/**
	 * Creates a new relation of type <code>F</code> from the given place to the given transition.
	 * This method is abstract because only subclasses know the type <code>F</code> of their relations.
	 * @param place The place where the relation starts.
	 * @param transition The transition where the relation ends.
	 * @return A new relation of type <code>F</code>.
	 */
	protected abstract F createNewFlowRelation(P place, T transition) throws ParameterException;
	
	/**
	 * Creates a new relation of type <code>F</code> from the given  transition to the given place.
	 * This method is abstract because only subclasses know the type <code>F</code> of their relations.
	 * @param transition The transition where the relation starts
	 * @param place The place where the relation ends.
	 * @return A new relation of type <code>F</code>.
	 */
	protected abstract F createNewFlowRelation(T transition, P place) throws ParameterException;
	
	/**
	 * Removes the given relation from the net.<br>
	 * This method is used by {@link #removePlace(String)} and should not be accessed in a public way.<br>
	 * The caller is responsible for parameter validity.
	 * @param relation The relation to remove.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 */
	protected void removeFlowRelation(F relation) throws ParameterException {
		relation.getPlace().removeRelation(relation);
		relation.getTransition().removeRelation(relation);
		relations.remove(relation.getName());
		checkSD(relation.getPlace());
		checkSD(relation.getTransition());
	}
	
	//------- Markings -------------------------------------------------------------------------------
	
	protected abstract M createNewMarking();
	
	/**
	 * Sets the initial marking of the Petri net.<br>
	 * The initial marking defines the initial state of a Petri net in the sense of tokens contained in places.
	 * When a Petri net is reset ({@link #reset()}), it should reach its initial marking.<br>
	 * The initial marking does not have to contain all net places. For places not contained in the given initial marking,
	 * the net assumes 0 tokens in the initial net state.<br>
	 * This method uses {@link #setInitialMarking()} to actually transform the net state to the initial state.
	 * @param marking The marking used as initial marking (state) of the net.
	 * @throws ParameterException If parameters are <code>null</code>, contain <code>null</code>-values<br>
	 * or the net does not contain some of the marking places.
	 */
	@SuppressWarnings("unchecked")
	public void setInitialMarking(M marking) throws ParameterException{
		validateMarking(marking);
		if(initialMarking != null)
			initialMarking.clear();
		for(String placeName: marking.places()){
			initialMarking.set(placeName, marking.get(placeName));
		}
		setMarking((M) initialMarking.clone());
	}
	
	/**
	 * Returns the initial marking of the Petri net.<br>
	 * The initial marking defines the initial state of a Petri net in the sense of tokens contained in places.
	 * @return The initial marking of the net.
	 */
	@SuppressWarnings("unchecked")
	public M getInitialMarking(){
		return (M) initialMarking.clone();
	}
	
	/**
	 * Sets the marking of the Petri net.<br>
	 * The net marking defines states for Petri net classes.<br>
	 * In case the given marking does not define a state for a net place,<br>
	 * its state is set to empty, i.e. all tokens are removed.
	 * @param marking The desired net marking.
	 * @throws ParameterException If the given marking is <code>null</code>.
	 */
	public void setMarking(M marking) throws ParameterException {
		validateMarking(marking);
		if(this.marking != null)
			this.marking.clear();
		for(String placeName: places.keySet()){
			if(marking.contains(placeName)){
				this.marking.set(placeName, marking.get(placeName));
				getPlace(placeName).setState(marking.get(placeName));
			} else {
				getPlace(placeName).setEmptyState();
			}
		}
		lastFiredTransition = null;
	}
	
	/**
	 * Returns the actual marking (state) of the Petri net which is defined by the number of
	 * tokens contained in net places.
	 * @return The actual marking (state) of the net.
	 */
	@SuppressWarnings("unchecked")
	public M getMarking(){
		return (M) marking.clone();
	}
	
	/**
	 * Updates the marking (state) of the Petri net for the given place.<br>
	 * When the number of tokens in places change, the marking of the net which describes its actual state
	 * in the sense of tokens in net places has to be updated.
	 * @param place Place whose token number has changed.
	 * @throws ParameterException If the given place is <code>null</code>.
	 */
	protected void updateMarking(P place) throws ParameterException {
		Validate.notNull(place);
		if(!places.containsKey(place.getName()))
			return;
		if(!place.hasEmptyState()) {
			marking.set(place.getName(), place.getState());
		} else {
			marking.remove(place.getName());
		}
	}
	
	//------- Functionality ------------------------------------------------------------------------

	
	/**
	 * Fires the net transition with the given name.
	 * @param transitionName The name of the transition to fire.
	 * @return <code>true</code> if the transition could be fired;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given transition name is <code>null</code><br>
	 * or the net does not contain a transition with the given name.
	 * @throws PNException If the net transition with the given name is not enabled.
	 */
	public T fire(String transitionName) throws ParameterException, PNException {
		validateFireTransition(transitionName);
		T transition = getTransition(transitionName);
		transition.fire();
		lastFiredTransition = transition;
		return transition;
	}
	
	/**
	 * Returns the marking which is the result of firing the transition with the given name.<br>
	 * The transition is not actually fired, but its firing impact with respect to the actual net state<br>
	 * simulated and returned in form of a resulting marking.
	 * @param transitionName The name of the transition to fire.
	 * @return <code>true</code> if the transition could be fired;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given transition name is <code>null</code><br>
	 * or the net does not contain a transition with the given name.
	 * @throws PNException If the net transition with the given name is not enabled.
	 */
	public abstract M fireCheck(String transitionName) throws ParameterException, PNException;
	
	/**
	 * Returns a copy of the actual net state (marking).
	 * @return A copy of the nets' actual marking.
	 */
	@SuppressWarnings("unchecked")
	public M cloneMarking(){
		return (M) getMarking().clone();
	}
	
	/**
	 * Resets the net to the initial state.<br>
	 * This includes setting the actual marking to the actual marking
	 * and setting the last fired transition to <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public void reset() {
		try {
			setMarking((M) initialMarking.clone());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isCapacityBounded(){
		for(P p: getPlaces()){
			if(!p.isBounded())
				return false;
		}
		return true;
	}
	
	public Boundedness isBounded(){
		if(isCapacityBounded()){
			boundedness = Boundedness.BOUNDED;
		}
		return boundedness;
	}
	
	public void setBoundedness(Boundedness boundedness){
		this.boundedness = boundedness;
	}
	
	//------- Validation methods --------------------------------------------------------------------
	
	/**
	 * This method can be used to validate transition names.<br>
	 * It checks if it is <code>null</code> and if the Petri net contains a transition with this name.
	 * @param transitionName The name of the transition in question.
	 * @throws ParameterException If the transition name is <code>null</code><br>
	 * or the net does not contain a transition with this name.
	 */
	protected void validateTransition(String transitionName) throws ParameterException{
		Validate.notNull(transitionName);
		if(!transitions.containsKey(transitionName))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Unknown transition: " + transitionName);
	}
	
	/**
	 * This method can be used to validate the name of a transition to fire.<br>
	 * It checks if the given name is <code>null</code>, if the Petri net contains a transition with this name<br>
	 * and if this transition is enabled.
	 * @param transitionName The name of the transition in question.
	 * @throws ParameterException If the transition name is <code>null</code><br>
	 * or the net does not contain a transition with this name.
	 * @throws PNException If the net transition with the given name is not enabled.
	 */
	protected void validateFireTransition(String transitionName) throws ParameterException, PNException{
		validateTransition(transitionName);
		T transition = getTransition(transitionName);
		if(!enabledTransitions.contains(transition))
			throw new PNException("Cannot fire disabled transition " + transitionName);
	}
	
	/**
	 * This method can be used to evaluate the compatibility markings to be assigned to the Petri net.<br>
	 * It checks if the Petri net contains all places for which the marking holds states.
	 * @param marking The marking in question.
	 * @throws ParameterException If the given marking is <code>null</code><br>
	 * or if it is not compatible with the net.
	 */
	protected void validateMarking(M marking) throws ParameterException{
		Validate.notNull(marking);
		if(!places.keySet().containsAll(marking.places()))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Places in initial marking must be places of the net.");
	}
	
	
	//------- validity and soundness -----------------------------------------------------------------
	
	/**
	 * Checks if the Petri net is valid.<br>
	 * Validity of a net relates to basic structural properties
	 * and requires all net places and transitions to be valid.<br>
	 * Subclasses may define the validity of a net e.g. in terms of specific constraints<br>
	 * and must throw PNValidationExceptions in case any constraint is violated.
	 * 
	 * @see AbstractPlace#checkValidity()
	 * @see AbstractTransition#checkValidity()
	 * @throws PNValidationException
	 */
	public void checkValidity() throws PNValidationException{
		for(P place: getPlaces()){
			place.checkValidity();
		}
		for(T transition: getTransitions()){
			transition.checkValidity();
		}
	}
	
	/**
	 * Checks if the Petri net is soundness.<br>
	 * In contrast to validity, Soundness of a net relates to more complex net properties,
	 * such as reachability properties or specific structural restrictions.
	 * Subclasses may define the soundness of a net e.g. in terms of specific constraints
	 * and must throw PNSoundnessExceptions in case any constraint is violated.<br>
	 * <br>
	 * Soundness implies validity.<br>
	 * Before soundness is checked, validity is checked first.
	 * 
	 * @throws PNValidationException
	 */
	public void checkSoundness() throws PNValidationException,PNSoundnessException{
		checkSoundness(true);
	}
	
	/**
	 * Checks if the Petri net is soundness.<br>
	 * In contrast to validity, Soundness of a net relates to more complex net properties,
	 * such as reachability properties or specific structural restrictions.
	 * Subclasses may define the soundness of a net e.g. in terms of specific constraints
	 * and must throw PNSoundnessExceptions in case any constraint is violated.<br>
	 * <br>
	 * Soundness implies validity.<br>
	 * Before soundness is checked, validity is checked first.
	 * @param checkValidity Indicates, if the method also checks the validity of the net as a precondition.
	 * @throws PNValidationException
	 */
	public void checkSoundness(boolean checkValidity) throws PNValidationException,PNSoundnessException{
		if(checkValidity)
			checkValidity();
	}
	
	//------- Interface Methods ----------------------------------------------------------------------
	
	@Override
	public Set<AbstractPNNode<F>> getParents(AbstractPNNode<F> node) {
		return node.getParents();
	}

	@Override
	public Set<AbstractPNNode<F>> getChildren(AbstractPNNode<F> node) {
		return node.getChildren();
	}
	
	@Override
	public int nodeCount() {
		return transitions.size() + places.size();
	}
	
	@Override
	public Set<AbstractPNNode<F>> getNodes() {
		Set<AbstractPNNode<F>> result = new HashSet<AbstractPNNode<F>>();
		result.addAll(getTransitions());
		result.addAll(getPlaces());
		return result;
	}
	
	
	//------ Listener methods ------------------------------------------------------------------------

	/**
	 * When a transition changes its state to enabled,<br>
	 * the net adds it to the set of enabled transitions.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void transitionEnabled(TransitionEvent<? extends AbstractTransition<F, S>> e) {
		
		enabledTransitions.add((T) e.getSource());
	}

	/**
	 * When a transition changes its state to disabled,<br>
	 * the net removes it from the set of enabled transitions.
	 */
	@Override
	public void transitionDisabled(TransitionEvent<? extends AbstractTransition<F, S>> e) {
		enabledTransitions.remove((T) e.getSource());
	}

	/**
	 * When tokens are added to net places,<br>
	 * the Petri net updates its state accordingly by adding tokens to its marking.
	 * @see #updateMarking(AbstractPlace)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void tokensAdded(TokenEvent<? extends AbstractPlace<F,S>> o) {
		try {
			updateMarking((P) o.getSource());
		} catch (ParameterException e) {
			// Cannot happen
			// TokenEvent ensures that source is of right type and not null 
			e.printStackTrace();
		}
	}

	/**
	 * When tokens are removed from net places,<br>
	 * the Petri net updates its state accordingly by removing tokens from its marking.
	 * @see #updateMarking(AbstractPlace)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void tokensRemoved(TokenEvent<? extends AbstractPlace<F,S>> o) {
		try {
			updateMarking((P) o.getSource());
		} catch (ParameterException e) {
			// Cannot happen
		    // TokenEvent ensures that source is of right type and not null 
			e.printStackTrace();
		}
	}
	
	//------- clone ----------------------------------------------------------------------------------
	
	public AbstractPetriNet<P,T,F,M,S> clone(){
		AbstractPetriNet<P,T,F,M,S> result = newInstance();
		Map<T,T> clonedTransitions = new HashMap<T,T>();
		Map<P,P> clonedPlaces = new HashMap<P,P>();
		try{
			for(T ownTransition: getTransitions()){
				clonedTransitions.put(ownTransition, (T) ownTransition.clone());
				result.addTransition(clonedTransitions.get(ownTransition));
			}
			for(P ownPlace: getPlaces()){
				clonedPlaces.put(ownPlace, (P) ownPlace.clone());
				result.addPlace(clonedPlaces.get(ownPlace));
			}
			for(F ownRelation: getFlowRelations()){
				result.addFlowRelation((F) ownRelation.clone(clonedPlaces.get(ownRelation.getPlace()),
														     clonedTransitions.get(ownRelation.getTransition()), 
														     ownRelation.getDirectionPT()));
			}
			result.setInitialMarking((M) getInitialMarking().clone());
		}catch(ParameterException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public abstract AbstractPetriNet<P,T,F,M,S> newInstance();
	
	//------- toString -------------------------------------------------------------------------------

	/**
	 * Returns a PNML description of the Petri net.
	 * @return A string describing the net in the PNML format.
	 */
	public abstract String toPNML();
	
	public enum Boundedness {BOUNDED, UNBOUNDED, UNKNOWN}
	
}
