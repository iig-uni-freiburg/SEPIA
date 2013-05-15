package petrinet.pt;

import petrinet.AbstractPetriNet;
import traversal.RandomPNTraverser;
import validate.ParameterException;

public class RandomPTTraverser extends RandomPNTraverser<PTTransition> {

	public RandomPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net)
			throws ParameterException {
		super(net);
	}

}
