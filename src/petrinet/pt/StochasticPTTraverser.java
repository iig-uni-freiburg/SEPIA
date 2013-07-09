package petrinet.pt;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.traversal.StochasticPNTraverser;
import petrinet.AbstractPetriNet;

public class StochasticPTTraverser extends StochasticPNTraverser<PTTransition> {

	public StochasticPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net)
			throws ParameterException {
		super(net);
	}

	public StochasticPTTraverser(AbstractPetriNet<?, PTTransition, ?, ?, ?> net, int toleranceDenominator) throws ParameterException {
		super(net, toleranceDenominator);
	}
	
	

}
