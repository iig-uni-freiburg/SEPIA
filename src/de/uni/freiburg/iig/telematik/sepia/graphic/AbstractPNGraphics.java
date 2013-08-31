package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * <p>
 * Abstract petri net graphics super class. It contains all net type specific graphical information needed to draw the petri net.
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
public abstract class AbstractPNGraphics<P extends AbstractPlace<F, S>,
						T extends AbstractTransition<F, S>,
						F extends AbstractFlowRelation<P, T, S>,
						M extends AbstractMarking<S>,
						S extends Object> {

	private Map<P, NodeGraphics> placeGraphics = new HashMap<P, NodeGraphics>();
	private Map<P, Set<TokenGraphics>> tokenGraphics = new HashMap<P, Set<TokenGraphics>>();
	private Map<T, NodeGraphics> transitionGraphics = new HashMap<T, NodeGraphics>();
	private Map<F, EdgeGraphics> edgeGraphics = new HashMap<F, EdgeGraphics>();
	private Map<F, AnnotationGraphics> edgeAnnotationGraphics = new HashMap<F, AnnotationGraphics>();

	public Map<F, AnnotationGraphics> getEdgeAnnotationGraphics() {
		return edgeAnnotationGraphics;
	}

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

	public Map<P, Set<TokenGraphics>> getTokenGraphics() {
		return tokenGraphics;
	}

	/**
	 * @return the transitionGraphics
	 */
	public Map<T, NodeGraphics> getTransitionGraphics() {
		return transitionGraphics;
	}

	public void setEdgeAnnotationGraphics(Map<F, AnnotationGraphics> edgeAnnotationGraphics) {
		this.edgeAnnotationGraphics = edgeAnnotationGraphics;
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

	public void setTokenGraphics(Map<P, Set<TokenGraphics>> tokenGraphics) {
		this.tokenGraphics = tokenGraphics;
	}

	/**
	 * @param transitionGraphics
	 *            the transitionGraphics to set
	 */
	public void setTransitionGraphics(Map<T, NodeGraphics> transitionGraphics) {
		this.transitionGraphics = transitionGraphics;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Graphics:\n");
		str.append("          PlaceGraphics# " + placeGraphics.size());
		if (placeGraphics.size() > 0)
			str.append(":\n" + map2Str(placeGraphics) + "\n");
		str.append("     TransitionGraphics# " + transitionGraphics.size());
		if (transitionGraphics.size() > 0)
			str.append(":\n" + map2Str(transitionGraphics) + "\n");
		str.append("           EdgeGraphics# " + edgeGraphics.size());
		if (edgeGraphics.size() > 0)
			str.append(":\n" + map2Str(edgeGraphics) + "\n");
		str.append("          TokenGraphics# " + tokenGraphics.size());
		if (tokenGraphics.size() > 0)
			str.append(":\n" + map2Str(tokenGraphics) + "\n");
		str.append(" EdgeAnnotationGraphics# " + edgeAnnotationGraphics.size());
		if (edgeAnnotationGraphics.size() > 0)
			str.append(":\n" + map2Str(edgeAnnotationGraphics) + "\n");

		return str.toString();
	}

	protected static <A extends Object, B extends Object> String map2Str(Map<A, B> m) {
		boolean empty = true;
		StringBuilder str = new StringBuilder();
		for (Entry<A, B> pairs : m.entrySet()) {
			if (!empty)
				str.append("\n");
			str.append("                            " + pairs.getKey() + ": " + pairs.getValue());
			empty = false;
		}
		return str.toString();
	}
}
