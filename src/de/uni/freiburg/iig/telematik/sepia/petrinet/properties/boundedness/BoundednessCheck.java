package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg.MGConstruction;

public class BoundednessCheck {
	
	/**
	 * Checks if the given net is capacity bounded.<br>
	 * A net is capacity bounded when all its places are bounded.
	 *  
	 * @param petriNet The input Petri net.
	 * @return The marking graph of the given Petri net.
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

	boolean checkCapacityBoundedness(AbstractPetriNet<P,T,F,M,S> petriNet) {
		for(P p: petriNet.getPlaces()){
			if(!p.isBounded())
				return false;
		}
		return true;
	}
	
	/**
	 * Checks if the Petri net is bounded.<br>
	 * In case the marking graph of the net cannot be constructed with the maximum number of elements (see {@link MGConstruction#MAX_RG_CALCULATION_STEPS}),<br>
	 * it is assumed to be unbounded; otherwise bounded.<br>
	 * @param petriNet The basic Petri net for operation.
	 * @throws BoundednessException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

	void initiateBoundednessCheck(AbstractPetriNet<P,T,F,M,S> petriNet, ExecutorListener listener) throws BoundednessException{
		initiateBoundednessCheck(new BoundednessCheckGenerator<P,T,F,M,S>(petriNet), listener);
	}

	/**
	 * Checks if the Petri net is bounded.<br>
	 * In case the marking graph of the net cannot be constructed with the maximum number of elements (see {@link MGConstruction#MAX_RG_CALCULATION_STEPS}),<br>
	 * it is assumed to be unbounded; otherwise bounded.<br>
	 * @param generator The boundedness check generator.
	 * @throws BoundednessException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

	void initiateBoundednessCheck(BoundednessCheckGenerator<P,T,F,M,S> generator, ExecutorListener listener) throws BoundednessException{
		
		ThreadedBoundednessChecker<P,T,F,M,S> checker = new ThreadedBoundednessChecker<P,T,F,M,S>(generator);
		checker.addExecutorListener(listener);
		
		checker.runCalculation();
	}
	
	/**
	 * Checks if the Petri net is bounded.<br>
	 * In case the marking graph of the net cannot be constructed with the maximum number of elements (see {@link MGConstruction#MAX_RG_CALCULATION_STEPS}),<br>
	 * it is assumed to be unbounded; otherwise bounded.<br>
	 * @param petriNet The basic Petri net for operation.
	 * @return The marking graph of the given Petri net.
	 * @throws BoundednessException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

	BoundednessCheckResult<P,T,F,M,S> getBoundedness(AbstractPetriNet<P,T,F,M,S> petriNet) throws BoundednessException{
		return getBoundedness(new BoundednessCheckGenerator<P,T,F,M,S>(petriNet));
	}
	
	/**
	 * Checks if the Petri net is bounded.<br>
	 * In case the marking graph of the net cannot be constructed with the maximum number of elements (see {@link MGConstruction#MAX_RG_CALCULATION_STEPS}),<br>
	 * it is assumed to be unbounded; otherwise bounded.<br>
	 * @param generator The boundedness check generator.
	 * @return The marking graph of the given Petri net.
	 * @throws BoundednessException
	 */
	public static <	P extends AbstractPlace<F,S>, 
					T extends AbstractTransition<F,S>, 
					F extends AbstractFlowRelation<P,T,S>, 
					M extends AbstractMarking<S>, 
					S extends Object>

	BoundednessCheckResult<P,T,F,M,S> getBoundedness(BoundednessCheckGenerator<P,T,F,M,S> generator) throws BoundednessException{
		
		ThreadedBoundednessChecker<P,T,F,M,S> checker = new ThreadedBoundednessChecker<P,T,F,M,S>(generator);
		checker.runCalculation();
		
		BoundednessCheckResult<P,T,F,M,S> boundednessCheckResult = null;
		try{
			boundednessCheckResult = checker.getBoundedness();
		} catch (BoundednessException e) {
			throw new BoundednessException("Exception during marking graph construction.\nReason: " + e.getMessage(), e);
		}
		return boundednessCheckResult;
	}

}
