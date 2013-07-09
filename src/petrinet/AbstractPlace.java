package petrinet;

import java.util.List;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.CapacityEvent;
import de.uni.freiburg.iig.telematik.sepia.event.CapacityListener;
import de.uni.freiburg.iig.telematik.sepia.event.CapacityListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.event.TokenListener;
import de.uni.freiburg.iig.telematik.sepia.event.TokenListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;


/**
 * Abstract class that defines general properties for Petri net places.<br>
 * This class inherits basic Petri net node properties from {@link AbstractPNNode}<br>
 * and defines additional properties:<br>
 * <ul>
 * <li>Capacity: The maximum number of tokens that can reside in the place.<br>
 * A capacity of -1 is interpreted as unboundedness, i.e. the number and kind of tokens is not restricted.</li>
 * </ul>
 * <br>
 * A place maintains a state of type S, which defines the number and kind of tokens in the place.<br>
 * The type S is used for the internal state of the place and for adding and removing tokens from the place.
 * A place allows {@link TokenListener}s to register so that they can be notified on state changes,<br>
 * i.e. added/removed tokens.
 * 
 * @author Thomas Stocker
 *
 * @param <E> The type of flow relations connected to the place.
 * @param <S> Type for token number and type.
 */
public abstract class AbstractPlace<E extends AbstractFlowRelation<? extends AbstractPlace<E,S>, ? extends AbstractTransition<E,S>, S>, 
								    S extends Object> extends AbstractPNNode<E>{
	
	/**
	 * Support class for {@link TokenListener} handling.
	 */
	protected TokenListenerSupport<AbstractPlace<E,S>> tokenListenerSupport = new TokenListenerSupport<AbstractPlace<E,S>>();
	/**
	 * Support class for {@link CapacityListener} handling.
	 */
	protected CapacityListenerSupport<AbstractPlace<E,S>> capacityListenerSupport = new CapacityListenerSupport<AbstractPlace<E,S>>();
	/**
	 * Capacity of the place, i.e. the maximum number of tokens the place can hold.<br>
	 * A capacity of -1 is interpreted as unboundedness, i.e. the number and kind of tokens is not restricted.
	 */
	protected int capacity = -1;
	/**
	 * The state of the place, i.e. the number and kind of tokens that reside in the place.
	 */
	protected S state = null;
	
	
	//------- Constructors --------------------------------------------------------------------------
	
	protected AbstractPlace(){
		super();
	}
	
	/**
	 * Creates a new place with the given name.
	 * @param name The name for the new Place.
	 * @throws ParameterException If the given name is <code>null</code>.
	 */
	public AbstractPlace(String name) throws ParameterException{
		super(name);
	}
	
	/**
	 * Creates a new place with the given name and label.
	 * @param name The name for the new place.
	 * @param label The label for the new place.
	 * @throws ParameterException If some parameters are <code>null</code>.
	 */
	public AbstractPlace(String name, String label) throws ParameterException {
		super(name, label);
	}
	
	//------- Basic properties -----------------------------------------------------------------------
	
	/**
	 * Returns the capacity of the place, i.e.<br>
	 * the maximum number of tokens the place can hold.
	 * @return The capacity of the place.
	 */
	public int getCapacity(){
		return capacity;
	}
	
	/**
	 * Sets the capacity of the place, i.e.<br>
	 * the maximum number of tokens the place can hold.<br>
	 * The capacity must be > 0.
	 * @param capacity The desired capacity.
	 * @throws ParameterException If the capacity is smaller or equal 0.
	 */
	public void setCapacity(int capacity) throws ParameterException{
		Validate.bigger(capacity, 0);
		int oldCapacity = this.capacity;
		this.capacity = capacity;
		if(oldCapacity != capacity)
			capacityListenerSupport.notifyCapacityChanged(new CapacityEvent<AbstractPlace<E,S>>(this, capacity));
	}
	
	/**
	 * Checks, if the place is bounded.<br>
	 * Bounded places restrict the number and kind of tokens that can reside in the place.<br>
	 * A capacity of -1 is interpreted as unboundedness.
	 * @return <code>true</code> if the place is bounded,<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isBounded(){
		return getCapacity() > -1;
	}
	
	/**
	 * checks if this place and the given place have the same relations.
	 * @param otherPlace The place in question.
	 * @return <code>true</code> if the two places have the same relations;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the other place is <code>null</code>.
	 */
	public boolean hasEqualRelations(AbstractPlace<E,S> otherPlace) throws ParameterException{
		Validate.notNull(otherPlace);
		List<E> incomingRelations = otherPlace.getIncomingRelations();
		List<E> outgoingRelations = otherPlace.getOutgoingRelations();
		if((this.incomingRelations.size()!=incomingRelations.size()) || (this.outgoingRelations.size()!=outgoingRelations.size())){
			return false;
		}
		for(AbstractFlowRelation<? extends AbstractPlace<E,S>,? extends AbstractTransition<E,S>, S> incoming: incomingRelations){
			if(!this.containsRelationFrom(incoming.getTransition()))
				return false;
		}
		for(AbstractFlowRelation<? extends AbstractPlace<E,S>,? extends AbstractTransition<E,S>, S> outgoing: outgoingRelations){
			if(!this.containsRelationTo(outgoing.getTransition()))
				return false;
		}
		return true;
	}
	
	
	//------- State manipulation -----------------------------------------------------------------------------
	
	/**
	 * Returns the state of the place in terms of number and kind of tokens.<br>
	 * The returned state should NOT be used to manually add or remove tokens from a place.<br>
	 * Token adding/removal is done by Petri net transitions.
	 * @return The actual state of the place.
	 * @see AbstractPlace#state
	 */
	public abstract S getState();
	
	/**
	 * Sets the state of the place in terms of number and kind of tokens if the place is not already in this state.<br>
	 * Before the state is set, its validity is checked with the help of {@link #validateState(Object)}.<br>
	 * On state changes, the place tells connected transitions to update their state<br>
	 * and calls the method {@link #stateChange(Object, Object)} which can be <br>
	 * overridden in subclasses to take further actions on state changes.
	 * 
	 * @param state The desired state of the place.
	 * @return <code>true</code> if the actual place state was changed to the given state;<br>
	 * <code>false</code> if the place is already in the given state.
	 * @throws ParameterException If the given state is <code>null</code>.
	 * @see #initiateStateChecks()
	 */
	public boolean setState(S state) throws ParameterException {
		validateState(state);
		if(this.state.equals(state))
			return false;
		
		S oldState = this.state;
		this.state = state;
		// Tell outgoing transitions to update their state.
		initiateStateChecks();
		// Call state-change method
		stateChange(oldState, state);
		return true;
	}

	/**
	 * Adds all given tokens to the place.<br>
	 * The number and type of tokens to add are defined by type S.
	 * @param tokens The number and type of tokens to add.
	 * @throws ParameterException If the given parameter is <code>null</code> or invalid,<br>
	 * or if the tokens cannot be added to the place, e.g. due to capacity restrictions.
	 */
	protected abstract void addTokens(S tokens) throws ParameterException;
	
	/**
	 * Removes all given tokens from the place.<br>
	 * The number and type of tokens to remove are defined by type S.
	 * @param tokens The number and type of tokens to remove.
	 * @throws ParameterException If the given parameter is <code>null</code> or invalid,<br>
	 * or if the tokens cannot be removed from the place.
	 */
	protected abstract void removeTokens(S tokens) throws ParameterException;
	
	/**
	 * Checks, if the place can consume the given state, i.e.<br>
	 * merging the actual state and the given state does not exceed the places' capacity.<br>
	 * This method does NOT actually "add" the given state to the places' actual state.
	 * @param state The state to merge.
	 * @return <code>true</code> if the place can consume the state;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException if the given state is <code>null</code>.
	 */
	public abstract boolean canConsume(S state) throws ParameterException;
	
	/**
	 * Checks if the place state is empty, i.e. the place contains no tokens.<br>
	 * Depending on the state representation this may require different check routines,<br>
	 * which have to be implemented by subclasses.
	 * @return <code>true</code> if the place contains not tokens;<br>
	 * <code>false</code> otherwise.
	 */
	public abstract boolean hasEmptyState();
	
	/**
	 * Sets the state of the place in empty condition, i.e. removes all tokens from the place.<br>
	 * Depending on the state representation this may require different check routines,<br>
	 * which have to be implemented by subclasses.
	 */
	public abstract void setEmptyState();
	
	/**
	 * This method is called by {@link #setState(Object)} in case of state changes.<br>
	 * It can be overridden in subclasses to take further actions on state changes.
	 * @param oldState Old place state before change.
	 * @param newState New place state after change.
	 */
	protected abstract void stateChange(S oldState, S newState);
	
	
	/**
	 * Tells outgoing transitions to update their state.<br>
	 * Depending on token changes outgoing transitions may get enabled/disabled.
	 */
	protected void initiateStateChecks() {
		
		
		for(AbstractFlowRelation<? extends AbstractPlace<E,S>, ? extends AbstractTransition<E,S>, S> r: outgoingRelations) {
			
		
			r.getTransition().checkState();
		}
		for(AbstractFlowRelation<? extends AbstractPlace<E,S>, ? extends AbstractTransition<E,S>, S> r: incomingRelations) {
			
		
			r.getTransition().checkState();
		}
	}
	
	
	//------- Validity -------------------------------------------------------------------------------
	
	/**
	 * Checks if the Petri net place is valid.<br>
	 * Subclasses may define the validity of a net place e.g. in terms of specific constraints
	 * and must throw PNValidationExceptions in case any constraint is violated.
	 * 
	 * @throws PNValidationException
	 */
	public void checkValidity() throws PNValidationException{}
	
	
	//------- Validation methods ----------------------------------------------------------------------
	
	
	/**
	 * Checks if the given state is valid.<br>
	 * The exact definition of validity can be defined in subclasses by overriding this method.<br>
	 * Default behavior is to check <code>null</code>-pointers.
	 * @param state The state to validate.
	 * @throws ParameterException If the given state is <code>null</code>.
	 */
	protected void validateState(S state) throws ParameterException{
		Validate.notNull(state);
	}
	
	
	//------- Listener support -----------------------------------------------------------------------
	
	/**
	 * Adds a token listener.
	 * @param listener The token listener to add.
	 * @throws ParameterException If the listener reference is <code>null</code>.
	 */
	public void addTokenListener(TokenListener<AbstractPlace<E,S>> l) throws ParameterException {
		tokenListenerSupport.addTokenListener(l);
	}
	
	/**
	 * Removes a token listener.
	 * @param listener The token listener to remove.
	 * @throws ParameterException If the listener reference is <code>null</code>.
	 */
	public void removeTokenListener(TokenListener<AbstractPlace<E,S>> l) throws ParameterException {
		tokenListenerSupport.removeTokenListener(l);
	}
	
	/**
	 * Adds a capacity listener.
	 * @param listener The capacity listener to add.
	 * @throws ParameterException If the listener reference is <code>null</code>.
	 */
	public void addCapacityListener(CapacityListener<AbstractPlace<E,S>> l) throws ParameterException {
		capacityListenerSupport.addCapacityListener(l);
	}
	
	/**
	 * Removes a capacity listener.
	 * @param listener The capacity listener to remove.
	 * @throws ParameterException If the listener reference is <code>null</code>.
	 */
	public void removeCapacityListener(CapacityListener<AbstractPlace<E,S>> l) throws ParameterException {
		capacityListenerSupport.removeCapacityListener(l);
	}
	
	//------- clone ----------------------------------------------------------------------------------
	
	@Override
	protected abstract AbstractPlace<E,S> newInstance();
	
	@Override
	public AbstractPlace<E,S> clone() {
		AbstractPlace<E,S> result = (AbstractPlace<E,S>) newInstance();
		try {
			result.setCapacity(getCapacity());
			result.setState(getState());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//------- toString --------------------------------------------------------------------------------
	
	/**
	 * Retrieves the PNML-representation of the Petri net place.<br>
	 * This method is abstract and requires subclasses to implement specific representations.
	 * @param initialMarking The number of tokens in this place according to the initial marking
	 * @return The PNML-representation of the transition in String format.
	 * @return
	 */
	public abstract String toPNML(S initialMarking);
}
