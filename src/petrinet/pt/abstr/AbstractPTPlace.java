package petrinet.pt.abstr;

import petrinet.AbstractPlace;
import validate.ParameterException;
import validate.Validate;
import event.TokenEvent;

/**
 * Abstract class that defines general properties for places of place/transition nets (P/T Nets).<br>
 * This class inherits basic Petri net place properties from {@link AbstractPlace}.<br>
 * It is parameterized with a specialized type of relations which can be added ({@link AbstractPTFlowRelation}).
 * <br>
 * P/T Nets consider only one token type.<br>
 * The state of a P/T Net place is determined by the number of tokens it holds,<br>
 * thus the state type is set to {@link Integer}.
 * 
 * @author Thomas Stocker
 *
 * @param <E> The type of flow relations connected to the place.
 */
public abstract class AbstractPTPlace<E extends AbstractPTFlowRelation<? extends AbstractPTPlace<E>, ? extends AbstractPTTransition<E>>> extends AbstractPlace<E, Integer>{
	/**
	 * The String format used for the PNML representation of the P/T Net place.
	 */
	protected final String pnmlFormat = "<place id=\"%s\">%n <name><text>%s</text></name>%n</place>%n";
	
	/**
	 * Constructs a new P/T Net place with the given name<br>
	 * and sets the number of tokens to 0.
	 * @param name The name for the new place.
	 * @throws ParameterException If the given name is <code>null</code>.
	 */
	public AbstractPTPlace(String name) throws ParameterException {
		super(name);
		state = 0;
	}
	
	/**
	 * Constructs a new P/T Net place with the given name and label<br>
	 * and sets the number of tokens to 0.
	 * @param name The name for the new place.
	 * @param label The label for the new place.
	 * @throws ParameterException If some parameters are <code>null</code>.
	 */
	public AbstractPTPlace(String name, String label) throws ParameterException {
		super(name, label);
		state = 0;
	}
	
	

	@Override
	protected void addTokens(Integer tokens) throws ParameterException {
		Validate.notNull(tokens);
		setState(state + tokens);
	}

	@Override
	protected void removeTokens(Integer tokens) throws ParameterException {
		Validate.notNull(tokens);
		setState(state - tokens);
	}

	@Override
	protected void stateChange(Integer oldState, Integer newState) {
		TokenEvent<AbstractPTPlace<E>> o = new TokenEvent<AbstractPTPlace<E>>(this, state);
		if(newState > oldState){
			tokenListenerSupport.notifyTokensAdded(o);
		} else {
			tokenListenerSupport.notifyTokensRemoved(o);
		}
	}

	@Override
	public boolean hasEmptyState() {
		return state == 0;
	}
	
	@Override
	public void setEmptyState() {
		try {
			setState(0);
		} catch (ParameterException e) {}
	}

	@Override
	public boolean canConsume(Integer state) throws ParameterException {
		validateState(state);
		return state + this.state <= capacity;
	}

	@Override
	protected void validateState(Integer state) throws ParameterException {
		super.validateState(state);
		Validate.notNegative(state);
		if(capacity > -1)
			Validate.smallerEqual(state, capacity, "Place cannot hold more than "+capacity+" tokens");
	}

	@Override
	public String toPNML() {
		return String.format(pnmlFormat, getName(), getLabel());
	}

}
