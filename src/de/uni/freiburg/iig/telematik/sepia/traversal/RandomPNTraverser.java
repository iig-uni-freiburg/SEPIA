package de.uni.freiburg.iig.telematik.sepia.traversal;

import java.util.List;

import de.invation.code.toval.misc.valuegeneration.RandomChooser;
import de.invation.code.toval.validate.InconsistencyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;


/**
 * This flow control chooses the next transition to fire 
 * randomly out of the set of enabled Transitions.
 * 
 * @author Thomas Stocker
 */
public class RandomPNTraverser<T extends AbstractTransition<?,?>> extends PNTraverser<T> {
	
	RandomChooser<T> randomChooser = new RandomChooser<T>();

	public RandomPNTraverser(AbstractPetriNet<?,T,?,?,?,?,?> net) {
		super(net);
	}

	@Override
	public T chooseNextTransition(List<T> enabledTransitions) throws InconsistencyException {
		if(!isValid())
			throw new ParameterException(ErrorCode.INCONSISTENCY, "Cannot provide transitions in invalid state.");
		Validate.notNull(enabledTransitions);
		Validate.noNullElements(enabledTransitions);
		
		if(enabledTransitions.isEmpty())
			return null;
		
		if(!net.getEnabledTransitions().containsAll(enabledTransitions))
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Some of the given transitions re not enabled.");
		
		return randomChooser.chooseValue(enabledTransitions);
	}

	@Override
	public boolean isValid() {
		return randomChooser.isValid();
	}
}
