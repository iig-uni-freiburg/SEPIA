package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.traversal.StochasticPNTraverser;

public class StochasticPTTraverser extends StochasticPNTraverser<PTTransition> {

	public StochasticPTTraverser(AbstractPetriNet<?,PTTransition,?,?,?> net) {
		super(net);
	}

	public StochasticPTTraverser(AbstractPetriNet<?,PTTransition,?,?,?> net, int toleranceDenominator) {
		super(net, toleranceDenominator);
	}
	
	

}
