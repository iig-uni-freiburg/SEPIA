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
	private Map<F, ArcGraphics> arcGraphics = new HashMap<F, ArcGraphics>();
	private Map<F, AnnotationGraphics> arcAnnotationGraphics = new HashMap<F, AnnotationGraphics>();

	public Map<F, AnnotationGraphics> getArcAnnotationGraphics() {
		return arcAnnotationGraphics;
	}

	/**
	 * @return the arcGraphics
	 */
	public Map<F, ArcGraphics> getArcGraphics() {
		return arcGraphics;
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

	public void setArcAnnotationGraphics(Map<F, AnnotationGraphics> arcAnnotationGraphics) {
		this.arcAnnotationGraphics = arcAnnotationGraphics;
	}

	/**
	 * @param arcGraphics
	 *            the arcGraphics to set
	 */
	public void setArcGraphics(Map<F, ArcGraphics> arcGraphics) {
		this.arcGraphics = arcGraphics;
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
		str.append("          placeGraphics# " + placeGraphics.size());
		if (placeGraphics.size() > 0)
			str.append(":\n" + map2Str(placeGraphics) + "\n");
		str.append("     transitionGraphics# " + transitionGraphics.size());
		if (transitionGraphics.size() > 0)
			str.append(":\n" + map2Str(transitionGraphics) + "\n");
		str.append("            arcGraphics# " + arcGraphics.size());
		if (arcGraphics.size() > 0)
			str.append(":\n" + map2Str(arcGraphics) + "\n");
		str.append("          tokenGraphics# " + tokenGraphics.size());
		if (tokenGraphics.size() > 0)
			str.append(":\n" + map2Str(tokenGraphics) + "\n");
		str.append("  arcAnnotationGraphics# " + arcAnnotationGraphics.size());
		if (arcAnnotationGraphics.size() > 0)
			str.append(":\n" + map2Str(arcAnnotationGraphics) + "\n");

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
