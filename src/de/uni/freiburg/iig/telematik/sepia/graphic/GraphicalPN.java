package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.util.HashMap;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * <p>
 * The GraphicalPetriNet is a container class for the {@link AbstractPetriNet} and adds some graphical information to the net. The components of the net are
 * mapped in {@link HashMap}s to their graphical information.
 * </p>
 * 
 * @author Adrian Lange
 * 
 * @param <P>
 *            Type of Petri net places
 * @param <T>
 *            Type of Petri net transitions
 * @param <F>
 *            Type of Petri net relations
 * @param <M>
 *            Type of Petri net markings
 * @param <S>
 *            Type of Petri net place states
 */
public abstract class GraphicalPN<P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object> {

	private AbstractPetriNet<P, T, F, M, S> petriNet = null;
	private PNGraphics<P, T, F, M, S> petriNetGraphics = null;

	/**
	 * Create new GraphicalPetriNet with the specified {@link AbstractPetriNet}.
	 */
	public GraphicalPN(AbstractPetriNet<P, T, F, M, S> petriNet, PNGraphics<P, T, F, M, S> petriNetGraphics) {
		setPetriNet(petriNet);
		
	}

	/**
	 * @return the {@link AbstractPetriNet}
	 */
	public AbstractPetriNet<P, T, F, M, S> getPetriNet() {
		return petriNet;
	}
	
	/**
	 * @param petriNet the abstractPetriNet to set
	 */
	public void setPetriNet(AbstractPetriNet<P, T, F, M, S> petriNet) {
		this.petriNet = petriNet;
	}

	public PNGraphics<P, T, F, M, S> getPetriNetGraphics() {
		return petriNetGraphics;
	}

	public void setPetriNetGraphics(PNGraphics<P, T, F, M, S> petriNetGraphics) {
		this.petriNetGraphics = petriNetGraphics;
	}

}
