package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.traversal.StochasticPNTraverser;

public class StochasticPTTraverser extends StochasticPNTraverser<PTTransition> {

	public StochasticPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?, ?, ?> net)
			throws ParameterException {
		super(net);
	}

	public StochasticPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?, ?, ?> net, int toleranceDenominator) throws ParameterException {
		super(net, toleranceDenominator);
	}
	
	

}
