package traversal;

import java.util.List;

import misc.valuegeneration.RandomChooser;
import petrinet.AbstractPetriNet;
import petrinet.AbstractTransition;
import validate.InconsistencyException;
import validate.ParameterException;
import validate.ParameterException.ErrorCode;
import validate.Validate;

/**
 * This flow control chooses the next transition to fire 
 * randomly out of the set of enabled Transitions.
 * 
 * @author Thomas Stocker
 */
public class RandomPNTraverser extends PNTraverser {
	
	RandomChooser<AbstractTransition<?,?>> randomChooser = new RandomChooser<AbstractTransition<?,?>>();

	public RandomPNTraverser(AbstractPetriNet<?,?,?,?,?> net) throws ParameterException {
		super(net);
	}

	@Override
	public AbstractTransition<?,?> chooseNextTransition(List<AbstractTransition<?,?>> enabledTransitions) throws InconsistencyException, ParameterException{
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
