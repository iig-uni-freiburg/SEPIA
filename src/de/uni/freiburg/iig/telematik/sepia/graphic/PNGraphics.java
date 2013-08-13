package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
public class PNGraphics<P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object> {

	private Map<P, NodeGraphics> placeGraphics = new HashMap<P, NodeGraphics>();
	private Map<P, Set<TokenGraphics>> tokenGraphics = new HashMap<P, Set<TokenGraphics>>();
	private Map<T, NodeGraphics> transitionGraphics = new HashMap<T, NodeGraphics>();
	private Map<F, EdgeGraphics> edgeGraphics = new HashMap<F, EdgeGraphics>();

	/**
	 * @return the edgeGraphics
	 */
	public Map<F, EdgeGraphics> getEdgeGraphics() {
		return edgeGraphics;
	}

	/**
	 * @return the placeGraphics
	 */
	public Map<P, NodeGraphics> getPlaceGraphics() {
		return placeGraphics;
	}

	/**
	 * @return the transitionGraphics
	 */
	public Map<T, NodeGraphics> getTransitionGraphics() {
		return transitionGraphics;
	}

	public Map<P, Set<TokenGraphics>> getMarkingGraphics() {
		return tokenGraphics;
	}

	/**
	 * @param edgeGraphics
	 *            the edgeGraphics to set
	 */
	public void setEdgeGraphics(Map<F, EdgeGraphics> edgeGraphics) {
		this.edgeGraphics = edgeGraphics;
	}

	/**
	 * @param placeGraphics
	 *            the placeGraphics to set
	 */
	public void setPlaceGraphics(Map<P, NodeGraphics> placeGraphics) {
		this.placeGraphics = placeGraphics;
	}

	/**
	 * @param transitionGraphics
	 *            the transitionGraphics to set
	 */
	public void setTransitionGraphics(Map<T, NodeGraphics> transitionGraphics) {
		this.transitionGraphics = transitionGraphics;
	}
	
	public void setMarkingGraphics(Map<P, Set<TokenGraphics>> markingGraphics) {
		this.tokenGraphics = markingGraphics;
	}
}
