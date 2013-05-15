package petrinet.pt;

import petrinet.AbstractPetriNet;
import traversal.StochasticPNTraverser;
import validate.ParameterException;

public class StochasticPTTraverser extends StochasticPNTraverser<PTTransition> {

	public StochasticPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net)
			throws ParameterException {
		super(net);
	}

	public StochasticPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net, int toleranceDenominator) throws ParameterException {
		super(net, toleranceDenominator);
	}
	
	

}
