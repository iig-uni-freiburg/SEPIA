package traversal;

import java.util.List;

import petrinet.AbstractPetriNet;
import petrinet.AbstractTransition;
import validate.InconsistencyException;
import validate.ParameterException;
import validate.Validate;


public abstract class PNTraverser {
	
	protected AbstractPetriNet<?,?,?,?,?> net;
	
	public PNTraverser(AbstractPetriNet<?,?,?,?,?> net) throws ParameterException{
		Validate.notNull(net);
		this.net = net;
	}

	
	/**
	 * Checks if the traverser is in a valid state.<br>
	 * The traverser is in valid state, when the value chooser is in valid state.
	 * @return <code>true</code> if the flow control is valid<br>
	 * <code>false</code> otherwise.
	 */
	public abstract boolean isValid();

	/**
	 * Chooses among all enabled transitions of the Petri net, the next transition to fire.
	 * @return The next enabled transition to fire.
	 * @throws InconsistencyException If the flow control is not in valid state.
	 */
	public abstract AbstractTransition<?,?> chooseNextTransition(List<AbstractTransition<?,?>> enabledTransitions) throws InconsistencyException, ParameterException;
	
}
