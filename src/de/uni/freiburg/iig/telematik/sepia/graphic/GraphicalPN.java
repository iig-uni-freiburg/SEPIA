package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * <p>
 * The GraphicalPetriNet is a container class for the {@link AbstractPetriNet} and adds some graphical information to the net.
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
		setPetriNetGraphics(petriNetGraphics);
	}

	/**
	 * @return the {@link AbstractPetriNet}
	 */
	public AbstractPetriNet<P, T, F, M, S> getPetriNet() {
		return petriNet;
	}

	/**
	 * @param petriNet
	 *            the petriNet to set
	 */
	public void setPetriNet(AbstractPetriNet<P, T, F, M, S> petriNet) {
		this.petriNet = petriNet;
	}

	/**
	 * @return the {@link PNGraphics}
	 */
	public PNGraphics<P, T, F, M, S> getPetriNetGraphics() {
		return petriNetGraphics;
	}

	/**
	 * @param petriNetGraphics
	 *            the petriNetGraphics to set
	 */
	public void setPetriNetGraphics(PNGraphics<P, T, F, M, S> petriNetGraphics) {
		this.petriNetGraphics = petriNetGraphics;
	}
}
