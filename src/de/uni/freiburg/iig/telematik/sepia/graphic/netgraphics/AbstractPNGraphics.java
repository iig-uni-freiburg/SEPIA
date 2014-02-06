package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

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

	private Map<String, NodeGraphics> placeGraphics = new HashMap<String, NodeGraphics>();
	private Map<String, Set<TokenGraphics>> tokenGraphics = new HashMap<String, Set<TokenGraphics>>();
	private Map<String, NodeGraphics> transitionGraphics = new HashMap<String, NodeGraphics>();
	private Map<String, ArcGraphics> arcGraphics = new HashMap<String, ArcGraphics>();
	private Map<String, AnnotationGraphics> arcAnnotationGraphics = new HashMap<String, AnnotationGraphics>();
	private Map<String, AnnotationGraphics> placeLabelAnnotationGraphics = new HashMap<String, AnnotationGraphics>();
	private Map<String, AnnotationGraphics> transitionLabelAnnotationGraphics = new HashMap<String, AnnotationGraphics>();

	/**
	 * @return the arcGraphics
	 */
	public Map<String, ArcGraphics> getArcGraphics() {
		return arcGraphics;
	}
	
	public ArcGraphics getArcGraphics(String arcName) {
		return arcGraphics.get(arcName);
	}
	
	public boolean containsArcGraphics(String arcName){
		return arcGraphics.containsKey(arcName);
	}

	public Map<String, AnnotationGraphics> getArcAnnotationGraphics() {
		return arcAnnotationGraphics;
	}
	
	public boolean containsArcAnnotationGraphics(String arcName){
		return arcAnnotationGraphics.containsKey(arcName);
	}
	
	public AnnotationGraphics getArcAnnotationGraphics(String arcName) {
		return arcAnnotationGraphics.get(arcName);
	}

	/**
	 * @return the placeGraphics
	 */
	public Map<String, NodeGraphics> getPlaceGraphics() {
		return placeGraphics;
	}
	
	public NodeGraphics getPlaceGraphics(String placeName) {
		return placeGraphics.get(placeName);
	}
	
	public boolean containsPlaceGraphics(String placeName){
		return placeGraphics.containsKey(placeName);
	}

	/**
	 * @return the placeLabelAnnotationGraphics
	 */
	public Map<String, AnnotationGraphics> getPlaceLabelAnnotationGraphics() {
		return placeLabelAnnotationGraphics;
	}
	
	public AnnotationGraphics getPlaceLabelAnnotationGraphics(String placeName) {
		return placeLabelAnnotationGraphics.get(placeName);
	}
	
	public boolean containsPlaceLabelAnnotationGraphics(String placeName){
		return placeLabelAnnotationGraphics.containsKey(placeName);
	}

	public Map<String, Set<TokenGraphics>> getTokenGraphics() {
		return tokenGraphics;
	}
	
	public Set<TokenGraphics> getTokenGraphics(String placeName) {
		return tokenGraphics.get(placeName);
	}
	
	public boolean containsTokenGraphics(String placeName){
		return tokenGraphics.containsKey(placeName);
	}

	/**
	 * @return the transitionGraphics
	 */
	public Map<String, NodeGraphics> getTransitionGraphics() {
		return transitionGraphics;
	}
	
	public NodeGraphics getTransitionGraphics(String transitionName) {
		return transitionGraphics.get(transitionName);
	}
	
	public boolean containsTransitionGraphics(String transitionName){
		return transitionGraphics.containsKey(transitionName);
	}

	/**
	 * @return the transitionLabelAnnotationGraphics
	 */
	public Map<String, AnnotationGraphics> getTransitionLabelAnnotationGraphics() {
		return transitionLabelAnnotationGraphics;
	}
	
	
	public AnnotationGraphics getTransitionLabelAnnotationGraphics(String transitionName) {
		return transitionLabelAnnotationGraphics.get(transitionName);
	}
	
	public boolean containsTransitionLabelAnnotationGraphics(String transitionName){
		return transitionLabelAnnotationGraphics.containsKey(transitionName);
	}

	public void setArcAnnotationGraphics(Map<String, AnnotationGraphics> arcAnnotationGraphics) {
		this.arcAnnotationGraphics = arcAnnotationGraphics;
	}

	/**
	 * @param arcGraphics
	 *            the arcGraphics to set
	 */
	public void setArcGraphics(Map<String, ArcGraphics> arcGraphics) {
		this.arcGraphics = arcGraphics;
	}

	/**
	 * @param placeLabelAnnotationGraphics
	 *            the placeLabelAnnotationGraphics to set
	 */
	public void setPlaceLabelAnnotationGraphics(Map<String, AnnotationGraphics> placeLabelAnnotationGraphics) {
		this.placeLabelAnnotationGraphics = placeLabelAnnotationGraphics;
	}

	/**
	 * @param placeGraphics
	 *            the placeGraphics to set
	 */
	public void setPlaceGraphics(Map<String, NodeGraphics> placeGraphics) {
		this.placeGraphics = placeGraphics;
	}

	public void setTokenGraphics(Map<String, Set<TokenGraphics>> tokenGraphics) {
		this.tokenGraphics = tokenGraphics;
	}

	/**
	 * @param transitionLabelAnnotationGraphics
	 *            the transitionLabelAnnotationGraphics to set
	 */
	public void setTransitionLabelAnnotationGraphics(Map<String, AnnotationGraphics> transitionLabelAnnotationGraphics) {
		this.transitionLabelAnnotationGraphics = transitionLabelAnnotationGraphics;
	}

	/**
	 * @param transitionGraphics
	 *            the transitionGraphics to set
	 */
	public void setTransitionGraphics(Map<String, NodeGraphics> transitionGraphics) {
		this.transitionGraphics = transitionGraphics;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Graphics:\n");
		str.append("                    placeGraphics# " + placeGraphics.size());
		if (placeGraphics.size() > 0)
			str.append(":\n" + map2Str(placeGraphics) + "\n");
		else
			str.append("\n");
		str.append("               transitionGraphics# " + transitionGraphics.size());
		if (transitionGraphics.size() > 0)
			str.append(":\n" + map2Str(transitionGraphics) + "\n");
		else
			str.append("\n");
		str.append("                      arcGraphics# " + arcGraphics.size());
		if (arcGraphics.size() > 0)
			str.append(":\n" + map2Str(arcGraphics) + "\n");
		else
			str.append("\n");
		str.append("                    tokenGraphics# " + tokenGraphics.size());
		if (tokenGraphics.size() > 0)
			str.append(":\n" + map2Str(tokenGraphics) + "\n");
		else
			str.append("\n");
		str.append("            arcAnnotationGraphics# " + arcAnnotationGraphics.size());
		if (arcAnnotationGraphics.size() > 0)
			str.append(":\n" + map2Str(arcAnnotationGraphics) + "\n");
		else
			str.append("\n");
		str.append("     placeLabelAnnotationGraphics# " + placeLabelAnnotationGraphics.size());
		if (placeLabelAnnotationGraphics.size() > 0)
			str.append(":\n" + map2Str(placeLabelAnnotationGraphics) + "\n");
		else
			str.append("\n");
		str.append("transitionLabelAnnotationGraphics# " + transitionLabelAnnotationGraphics.size());
		if (transitionLabelAnnotationGraphics.size() > 0)
			str.append(":\n" + map2Str(transitionLabelAnnotationGraphics) + "\n");
		else
			str.append("\n");

		return str.toString();
	}

	protected static <A extends Object, B extends Object> String map2Str(Map<A, B> m) {
		boolean empty = true;
		StringBuilder str = new StringBuilder();
		for (Entry<A, B> pairs : m.entrySet()) {
			if (!empty)
				str.append("\n");
			str.append("                                      " + pairs.getKey() + ": " + pairs.getValue());
			empty = false;
		}
		return str.toString();
	}
}
