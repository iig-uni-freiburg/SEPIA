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
public class GraphicalPetriNet<P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object> {

	private AbstractPetriNet<P, T, F, M, S> abstractPetriNet = null;

	// TODO private HashMap<AbstractPage, PageGraphics> pageGraphics = new Vector<PageGraphics>();
	private HashMap<P, NodeGraphics> placeGraphics = new HashMap<P, NodeGraphics>();
	private HashMap<T, NodeGraphics> transitionGraphics = new HashMap<T, NodeGraphics>();
	private HashMap<F, EdgeGraphics> edgeGraphics = new HashMap<F, EdgeGraphics>();
	private HashMap<Object, AnnotationGraphics> annotationGraphics = new HashMap<Object, AnnotationGraphics>();

	/**
	 * Create new GraphicalPetriNet with the specified {@link AbstractPetriNet}.
	 */
	public GraphicalPetriNet(AbstractPetriNet<P, T, F, M, S> abstractPetriNet) {
		setAbstractPetriNet(abstractPetriNet);
	}

	/**
	 * @return the {@link AbstractPetriNet}
	 */
	public AbstractPetriNet<P, T, F, M, S> getAbstractPetriNet() {
		return abstractPetriNet;
	}

	/**
	 * @return the annotationGraphics
	 */
	public HashMap<Object, AnnotationGraphics> getAnnotationGraphics() {
		return annotationGraphics;
	}

	/**
	 * @return the edgeGraphics
	 */
	public HashMap<F, EdgeGraphics> getEdgeGraphics() {
		return edgeGraphics;
	}

	/**
	 * @return the placeGraphics
	 */
	public HashMap<P, NodeGraphics> getPlaceGraphics() {
		return placeGraphics;
	}

	/**
	 * @return the transitionGraphics
	 */
	public HashMap<T, NodeGraphics> getTransitionGraphics() {
		return transitionGraphics;
	}

	/**
	 * @param abstractPetriNet
	 *            the abstractPetriNet to set
	 */
	public void setAbstractPetriNet(AbstractPetriNet<P, T, F, M, S> abstractPetriNet) {
		this.abstractPetriNet = abstractPetriNet;
	}

	/**
	 * @param annotationGraphics
	 *            the annotationGraphics to set
	 */
	public void setAnnotationGraphics(HashMap<Object, AnnotationGraphics> annotationGraphics) {
		this.annotationGraphics = annotationGraphics;
	}

	/**
	 * @param edgeGraphics
	 *            the edgeGraphics to set
	 */
	public void setEdgeGraphics(HashMap<F, EdgeGraphics> edgeGraphics) {
		this.edgeGraphics = edgeGraphics;
	}

	/**
	 * @param placeGraphics
	 *            the placeGraphics to set
	 */
	public void setPlaceGraphics(HashMap<P, NodeGraphics> placeGraphics) {
		this.placeGraphics = placeGraphics;
	}

	/**
	 * @param transitionGraphics
	 *            the transitionGraphics to set
	 */
	public void setTransitionGraphics(HashMap<T, NodeGraphics> transitionGraphics) {
		this.transitionGraphics = transitionGraphics;
	}
}
