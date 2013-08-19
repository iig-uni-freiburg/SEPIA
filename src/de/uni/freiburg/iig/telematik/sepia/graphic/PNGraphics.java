package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * <p>
 * TODO
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

		str.append("         PlaceGraphics# " + placeGraphics.size() + ": [" + map2Str(placeGraphics)+ "]\n");
		str.append("    TransitionGraphics# " + transitionGraphics.size() + ": [" + map2Str(transitionGraphics)+ "]\n");
		str.append("          EdgeGraphics# " + edgeGraphics.size() + ": [" + map2Str(edgeGraphics)+ "]\n");
		str.append("         TokenGraphics# " + tokenGraphics.size() + ": [" + map2Str(tokenGraphics)+ "]\n");
		str.append("EdgeAnnotationGraphics# " + edgeAnnotationGraphics.size() + ": [" + map2Str(edgeAnnotationGraphics)+ "]\n");

		return str.toString();
	}

	@SuppressWarnings("rawtypes")
	private static String map2Str(Map m) {
		boolean empty = true;
		StringBuilder str = new StringBuilder();
		Iterator it = m.entrySet().iterator();
		while (it.hasNext()) {
			if (!empty)
				str.append(",");
			Map.Entry pairs = (Map.Entry) it.next();
			str.append(pairs.getKey() + "=" + pairs.getValue());
			it.remove();
			empty = false;
		}
		return str.toString();
	}
}
