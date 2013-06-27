package petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import petrinet.AbstractPetriNet;
import traversal.RandomPNTraverser;

public class RandomPTTraverser extends RandomPNTraverser<PTTransition> {

	public RandomPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net)
			throws ParameterException {
		super(net);
	}

}
