package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
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
public abstract class AbstractGraphicalPN<P extends AbstractPlace<F, S>, 
										  T extends AbstractTransition<F, S>, 
										  F extends AbstractFlowRelation<P, T, S>, 
										  M extends AbstractMarking<S>, 
										  S extends Object,
										  X extends AbstractMarkingGraphState<M, S>,
										  Y extends AbstractMarkingGraphRelation<M, X, S>,
										  N extends AbstractPetriNet<P,T,F,M,S,X,Y>,
		   							  	  G extends AbstractPNGraphics<P,T,F,M,S>> {

	private N petriNet = null;
	private G petriNetGraphics = null;

	/**
	 * Create new GraphicalPetriNet with the specified {@link AbstractPetriNet}.
	 */
	public AbstractGraphicalPN(N petriNet, G petriNetGraphics) {
		setPetriNet(petriNet);
		setPetriNetGraphics(petriNetGraphics);
	}

	/**
	 * @return the {@link AbstractPetriNet}
	 */
	public N getPetriNet() {
		return petriNet;
	}

	/**
	 * @param petriNet
	 *            the petriNet to set
	 */
	public void setPetriNet(N petriNet) {
		this.petriNet = petriNet;
	}

	/**
	 * @return the {@link AbstractPNGraphics}
	 */
	public G getPetriNetGraphics() {
		return petriNetGraphics;
	}

	/**
	 * @param petriNetGraphics
	 *            the petriNetGraphics to set
	 */
	public void setPetriNetGraphics(G petriNetGraphics) {
		this.petriNetGraphics = petriNetGraphics;
	}
}
