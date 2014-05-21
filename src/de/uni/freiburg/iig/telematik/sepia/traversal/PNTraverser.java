package de.uni.freiburg.iig.telematik.sepia.traversal;

import java.util.List;

import de.invation.code.toval.validate.InconsistencyException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;



public abstract class PNTraverser<T extends AbstractTransition<?,?>> {
	
	protected AbstractPetriNet<?,T,?,?,?,?,?> net;
	
	public PNTraverser(AbstractPetriNet<?,T,?,?,?,?,?> net) {
		Validate.notNull(net);
		this.net = net;
	}
	
	public AbstractPetriNet<?,T,?,?,?,?,?> getPetriNet(){
		return net;
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
	public abstract T chooseNextTransition(List<T> enabledTransitions) throws InconsistencyException;
	
}
