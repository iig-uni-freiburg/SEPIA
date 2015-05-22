package de.uni.freiburg.iig.telematik.sepia.traversal;

import java.util.HashMap;
import java.util.List;

import de.invation.code.toval.misc.valuegeneration.StochasticValueGenerator;
import de.invation.code.toval.misc.valuegeneration.ValueGenerationException;
import de.invation.code.toval.validate.InconsistencyException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * This flow control chooses the next transition to fire 
 * on the basis of predefined probabilities of occurrences of subsequent transition pairs.
 * 
 * @author Thomas Stocker
 *
 */
public class StochasticPNTraverser<T extends AbstractTransition<?,?>> extends RandomPNTraverser<T> {
	
	public static final int DEFAULT_TOLERANCE_DENOMINATOR = 1000;
	private HashMap<T, StochasticValueGenerator<T>> flowProbabilities = new HashMap<T, StochasticValueGenerator<T>>();
	private int toleranceDenominator;
	
	public StochasticPNTraverser(AbstractPetriNet<?,T,?,?,?> net) {
		this(net, DEFAULT_TOLERANCE_DENOMINATOR);
	}

	public StochasticPNTraverser(AbstractPetriNet<?,T,?,?,?> net, int toleranceDenominator) {
		super(net);
		Validate.biggerEqual(toleranceDenominator, 1, "Denominator must be >=1.");
		this.toleranceDenominator = toleranceDenominator;
	}
	
	public void addFlowProbability(String fromTransitionID, String toTransitionID, double probability) {
		addFlowProbability(net.getTransition(fromTransitionID), net.getTransition(toTransitionID), probability);
	}
	
	public void addFlowProbability(T fromTransition, T toTransition, double probability) {
		Validate.notNull(fromTransition);
		Validate.notNull(toTransition);
		Validate.inclusiveBetween(0.0, 1.0, probability);
		StochasticValueGenerator<T> chooser = flowProbabilities.get(fromTransition);
		if(chooser == null){
			chooser = new StochasticValueGenerator<T>(toleranceDenominator);
			flowProbabilities.put(fromTransition, chooser);
		}
		chooser.addProbability(toTransition, probability);
	}

	@Override
	public T chooseNextTransition(List<T> enabledTransitions) throws InconsistencyException {
		if(!flowProbabilities.containsKey(net.getLastFiredTransition()))
			return super.chooseNextTransition(enabledTransitions);
		if(!isValid())
			throw new InconsistencyException("At least one StochasticChooser is not valid.");
		Validate.notNull(enabledTransitions);
		Validate.noNullElements(enabledTransitions);
		
		if(enabledTransitions.isEmpty())
			return null;
		
		T nextTransition = null;
		try {
			nextTransition = flowProbabilities.get(net.getLastFiredTransition()).getNextValue();
		} catch (ValueGenerationException e) {
			// Cannot happen, since all choosers are valid.
			e.printStackTrace();
		}
		if(!net.getEnabledTransitions().contains(nextTransition))
			throw new InconsistencyException("Cannot fire transition \""+nextTransition+"\" since it is not enabled.");
		return nextTransition;
	}
	
	/**
	 * Checks, if all maintained stochastic choosers are valid.
	 * @return <code>true</code> if all choosers are valid,<br>
	 * <code>false</code> otherwise.
	 * @see StochasticValueGenerator#isValid()
	 */
	@Override
	public boolean isValid(){
		for(StochasticValueGenerator<T> chooser: flowProbabilities.values())
			if(!chooser.isValid())
				return false;
		return true;
	}
}
