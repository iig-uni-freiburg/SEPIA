package petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.traversal.RandomPNTraverser;
import petrinet.AbstractPetriNet;

public class RandomPTTraverser extends RandomPNTraverser<PTTransition> {

	public RandomPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net)
			throws ParameterException {
		super(net);
	}

}
