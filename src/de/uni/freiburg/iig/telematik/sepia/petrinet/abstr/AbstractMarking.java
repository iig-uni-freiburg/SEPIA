package de.uni.freiburg.iig.telematik.sepia.petrinet.abstr;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.Validate;


/**
 * This class defines a Petri net state in form of a marking.<br>
 * Markings hold the state (number and kind of tokens) for all net places.<br>
 * Each place state is defined with a token representation of type M.
 * 
 * @author Thomas Stocker
 *
 * @param <S> Token representation type for places.
 */
public abstract class AbstractMarking<S extends Object> implements Serializable{
	
	private static final long serialVersionUID = -8258625737221734181L;
	/**
	 * Map for place states, indexed with place-names.<br>
	 * Each place state is defined with a token representation of type M.
	 */
	protected Map<String,S> placeStates = new HashMap<String,S>(); 
	
	
	//------- Basic properties -----------------------------------------------------------------------
	
	/**
	 * Returns all places for which the marking contains a state.
	 * @return The set of places with states in the marking.
	 */
	public Set<String> places(){
		return placeStates.keySet();
	}
	
	/**
	 * Checks if the marking contains a state for the given place.
	 * @param place The place of interest.
	 * @return <code>true</code> if the marking contains a states for the given place;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean contains(String place) {
		return placeStates.keySet().contains(place);
	}
	
	/**
	 * Removes the state for the given place from the marking if it contains a state for the place.
	 * @param place The place whose state has to be removed.
	 */
	public void remove(String place){
		placeStates.remove(place);
	}
	
	/**
	 * Indicates if the marking is empty, i.e. contains no place states.
	 * @return <code>true</code> if the marking is empty;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isEmpty(){
		return placeStates.isEmpty();
	}
	
	/**
	 * Clears the marking, i.e. removes all states for all places.
	 */
	public void clear(){
		placeStates.clear();
	}
	
	
	//------- State methods --------------------------------------------------------------------------
	
	/**
	 * Returns the state of the given place within the marking.
	 * @param place The place whose state is requested.
	 * @return The state of the given place in the marking.
	 */
	public S get(String place){
		validatePlace(place);
		return placeStates.get(place);
	}
	
	/**
	 * Sets the state of the given place in the marking.
	 * @param place The place whose state is set.
	 * @param state The state for the given place.
	 */
	public void set(String place, S state){
		validatePlace(place);
		validateState(state);
		placeStates.put(place, state);
	}
	
	
	//------- Validation methods ---------------------------------------------------------------------
	
	/**
	 * Validates a place for which a state has to be set.
	 * @param place The place whose state is set.
	 */
	protected void validatePlace(String place){
		Validate.notNull(place);
	}
	
	/**
	 * Validates a state which has to be set for a place.
	 * @param state The state which has to be set.
	 */
	protected void validateState(S state){
		Validate.notNull(state);
	}
	
	
	//------- clone, hashCode and equals ---------------------------------------------------------------------
	
	/**
	 * Returns a copy of the marking.<br>
	 * Depending on M, this may require different routines, which have to be implemented by subclasses.
	 */
	@Override
	public abstract AbstractMarking<S> clone();
	
	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		result = prime * result + ((placeStates == null) ? 0 : placeStates.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		AbstractMarking<S> other = (AbstractMarking<S>) obj;
		if (placeStates == null) {
			if (other.placeStates != null)
				return false;
		} else if (!placeStates.equals(other.placeStates))
			return false;
		return true;
	}
	
}
