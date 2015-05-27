package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.traverse;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;
import de.uni.freiburg.iig.telematik.sepia.traversal.RandomPNTraverser;

public class RandomPTTraverser extends RandomPNTraverser<PTTransition> {

	public RandomPTTraverser(AbstractPetriNet<?,PTTransition,?,?,?> net) {
		super(net);
	}

}
