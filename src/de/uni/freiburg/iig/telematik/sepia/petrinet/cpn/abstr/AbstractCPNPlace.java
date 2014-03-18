package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.misc.SetUtils;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.CapacityEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TokenEvent;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;


public abstract class AbstractCPNPlace<E extends AbstractCPNFlowRelation<? extends AbstractCPNPlace<E>, ? extends AbstractCPNTransition<E>>> extends AbstractPlace<E, Multiset<String>>{
	
	private int numTokens = 0;
	/**
	 * Capacity for individual colors.
	 */
	private Map<String, Integer> colorCapacity = new HashMap<String, Integer>();
	
	protected AbstractCPNPlace(){
		super();
		state = new Multiset<String>();
	}
	
	public AbstractCPNPlace(String name) throws ParameterException {
		this(name, name);
	}
	
	public AbstractCPNPlace(String name, String label) throws ParameterException {
		super(name, label);
		state = new Multiset<String>();
	}

	
	@Override
	public Multiset<String> getState() {
		return state.clone();
	}
	
	public int getTokenCount(){
		return numTokens;
	}
	
	public void setColorCapacity(String color, int value) throws ParameterException{
		Validate.notNull(color);
		Validate.bigger(value, 0);
		
		// Check if place already contains more tokens of the given color
		// as the new capacity for this color.
		if(getTokens(color) > value)
			throw new ParameterException(ErrorCode.INCONSISTENCY, "Place already contains more tokens of color \""+color+"\" than the new capacity for this color.");

		int oldCapacity = capacity;
		if(colorCapacity.containsKey(color)){
			capacity -= colorCapacity.get(color);
		}
		colorCapacity.put(color, value);
		if(capacity == -1){
			capacity = 0;
		}
		capacity += value;
		if(capacity != oldCapacity)
			placeListenerSupport.notifyCapacityChanged(new CapacityEvent<AbstractPlace<E,Multiset<String>>>(this, capacity));
	}
	
	public void removeColorCapacity(String color) throws ParameterException{
		Validate.notNull(color);
		int oldCapacity = capacity;
		if(!colorCapacity.containsKey(color))
			return;
		capacity -= colorCapacity.get(color);
		colorCapacity.remove(color);
		if(capacity == 0){
			capacity = -1;
		}
		if(capacity != oldCapacity)
			placeListenerSupport.notifyCapacityChanged(new CapacityEvent<AbstractPlace<E,Multiset<String>>>(this, capacity));
	}
	
	public int getColorCapacity(String color) throws ParameterException{
		Validate.notNull(color);
		if(getCapacity() > 0){
			//There are color capacities.
			if(colorCapacity.containsKey(color)){
				return colorCapacity.get(color);
			} else {
				return 0;
			}
		} else {
			return -1;
		}
	}
	
	public Set<String> getColorsWithCapacityRestriction(){
		return colorCapacity.keySet();
	}
	
	public boolean hasCapacityRestriction(String color) throws ParameterException{
		return getColorCapacity(color) > -1;
	}
	
	/**
	 * A CPN-Place does not allow to set the capacity explicitly.<br>
	 * The overall capacity of a place is the sum of all color capacities.
	 */
	@Override
	public void setCapacity(int capacity) throws ParameterException {
		throw new UnsupportedOperationException("Use setColorCapacity() instead.");
	}

	/**
	 * A CPN-Place does not allow to remove the capacity explicitly.
	 */
	@Override
	public void removeCapacity() {
		throw new UnsupportedOperationException("Use removeColorCapacity() instead.");
	}

	@Override
	protected void addTokens(Multiset<String> tokens) throws ParameterException {
		Validate.notNull(tokens);
		for(String color: tokens.support()){
			addTokens(color, tokens.multiplicity(color));
		}
	}
	
	@Override
	protected void removeTokens(Multiset<String> tokens) throws ParameterException {
		Validate.notNull(tokens);
		for(String color: tokens.support()){
			removeTokens(color, tokens.multiplicity(color));
		}
	}
	
	protected void addTokens(String color, int number)  throws ParameterException{
		Validate.notNull(color);
		setTokens(color, state.multiplicity(color) + number);
	}
	
	protected void setTokens(String color, int number) throws ParameterException{
		Validate.notNull(color);
		Validate.notNegative(number);
		if(capacity > -1)
			Validate.smallerEqual(getTokenCountWithout(color)+number, capacity, "Place cannot hold more than "+capacity+" tokens");
		if(getColorCapacity(color) == 0)
			throw new ParameterException(ErrorCode.CONSTRAINT, "Place cannot hold tokens of color "+ color);
		if(getColorCapacity(color) > 0)
			Validate.smallerEqual(number, colorCapacity.get(color), "Place cannot hold more than "+colorCapacity.get(color)+" tokens of color "+color);
		
		
		int oldMultiplicity = state.multiplicity(color);
		state.setMultiplicity(color, number);
		initiateStateChecks();
		
		checkTokenDifference(color, oldMultiplicity, number);
	}
	
	private int getTokenCountWithout(String color) throws ParameterException{
		Validate.notNull(color);
		if(!state.contains(color))
			return numTokens;
		return numTokens - state.multiplicity(color);
	}
	
	protected void removeTokens(String color, int number) throws ParameterException {
		Validate.notNegative(number);
		Validate.notNull(color);
		if(!state.contains(color))
			return;
		if(number > state.multiplicity(color))
			throw new ParameterException(ErrorCode.INCONSISTENCY, "Cannot remove "+ number +" tokens, place only contains " + state.multiplicity(color)+ " tokens.");
		
		setTokens(color, state.multiplicity(color)-number);
	}
	
	protected void removeTokens(String color) throws ParameterException {
		Validate.notNull(color);
		if(!state.contains(color))
			return;
		setTokens(color, 0);
	}
	
	@Override
	public void setEmptyState() {
//		List<String> colors = new ArrayList<String>(state.support());
//		try {
//			for(String color: colors)
//				removeTokens(color);
//		} catch (ParameterException e) {}
		state.clear();
		initiateStateChecks();
	}
	
	@Override
	public boolean canConsume(Multiset<String> state) throws ParameterException {
		validateState(state);
		for(String color: state.support()){
			if(hasCapacityRestriction(color)){
				if(this.getState().multiplicity(color) + state.multiplicity(color) > getColorCapacity(color)){
					return false;
				}
			}
		}
		return true;
	}
	
	private void notifyTokensRemoved(String color, int removedTokens){
		TokenEvent<AbstractCPNPlace<E>> o = new TokenEvent<AbstractCPNPlace<E>>(this, removedTokens, color);
		tokenListenerSupport.notifyTokensRemoved(o);
	}
	
	private void notifyTokensAdded(String color, int addedTokens){
		TokenEvent<AbstractCPNPlace<E>> o = new TokenEvent<AbstractCPNPlace<E>>(this, addedTokens, color);
		tokenListenerSupport.notifyTokensAdded(o);
	}
	
	protected int getTokens(String color) throws ParameterException {
		Validate.notNull(color);
		return getState().multiplicity(color);
	}
	
	private void checkTokenDifference(String color, int oldValue, int newValue){
		if(newValue < oldValue){
			notifyTokensRemoved(color, oldValue - newValue);
			numTokens -= oldValue - newValue;
		} else if(newValue > oldValue){
			notifyTokensAdded(color, newValue - oldValue);
			numTokens += newValue - oldValue;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void stateChange(Multiset<String> oldState, Multiset<String> newState) {
		for(String color: SetUtils.union(oldState.support(), newState.support())){
			checkTokenDifference(color, oldState.multiplicity(color), newState.multiplicity(color));
		}
	}
	
	@Override
	public boolean hasEmptyState() {
		return state.isEmpty();
	}
	
	@Override
	protected void validateState(Multiset<String> state) throws ParameterException {
		super.validateState(state);
		if(capacity > -1)
			Validate.smallerEqual(state.size(), capacity, "Place cannot hold more than "+capacity+" tokens");
		for(String color: state.support()){
			if(colorCapacity.containsKey(color))
				Validate.smallerEqual(state.multiplicity(color), colorCapacity.get(color), "Place cannot hold more than "+capacity+" tokens of color "+color);
		}
	}
	
	//------- hashCode and equals --------------------------------------------------------------------
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((colorCapacity == null) ? 0 : colorCapacity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		AbstractCPNPlace<E> other = (AbstractCPNPlace<E>) obj;
		if (colorCapacity == null) {
			if (other.colorCapacity != null)
				return false;
		} else if (!colorCapacity.equals(other.colorCapacity))
			return false;
		return true;
	}
	
	
	//------- clone ----------------------------------------------------------------------------------
	
	@Override
	protected void cloneCapacity(AbstractPlace<E, Multiset<String>> clone) throws ParameterException {
		for(String color: colorCapacity.keySet()){
			((AbstractCPNPlace<E>) clone).setColorCapacity(color, colorCapacity.get(color));
		}
	}

	
	
	
	

}
