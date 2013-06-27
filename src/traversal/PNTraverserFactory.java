package traversal;

import de.invation.code.toval.validate.ParameterException;
import petrinet.AbstractPetriNet;

public class PNTraverserFactory {
	
	public static RandomPNTraverser createDefaultTraverser(AbstractPetriNet<?,?,?,?,?> net) 
			throws ParameterException{
		return new RandomPNTraverser(net);
	}
	
	public static StochasticPNTraverser createStochasticTraverser(AbstractPetriNet<?,?,?,?,?> net) 
			throws ParameterException{
		return new StochasticPNTraverser(net);
	}
	

}
