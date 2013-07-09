package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.traversal.RandomPNTraverser;

public class RandomPTTraverser extends RandomPNTraverser<PTTransition> {

	public RandomPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net)
			throws ParameterException {
		super(net);
	}

}
