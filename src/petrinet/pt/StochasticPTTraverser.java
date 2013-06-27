package petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import petrinet.AbstractPetriNet;
import traversal.StochasticPNTraverser;

public class StochasticPTTraverser extends StochasticPNTraverser<PTTransition> {

	public StochasticPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net)
			throws ParameterException {
		super(net);
	}

	public StochasticPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net, int toleranceDenominator) throws ParameterException {
		super(net, toleranceDenominator);
	}
	
	

}
