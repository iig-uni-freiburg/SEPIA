package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.event.PNStructureListener;
import de.uni.freiburg.iig.telematik.sepia.event.PlaceChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.RelationChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

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
										  N extends AbstractPetriNet<P,T,F,M,S>,
		   							  	  G extends AbstractPNGraphics<P,T,F,M,S>> implements PNStructureListener<P,T,F,M,S> {

	private N petriNet = null;
	private G petriNetGraphics = null;

	/**
	 * Create new GraphicalPetriNet with the specified {@link AbstractPetriNet}.
	 */
	public AbstractGraphicalPN(N petriNet, G petriNetGraphics) {
		setPetriNet(petriNet);
		setPetriNetGraphics(petriNetGraphics);
	}
	
	protected AbstractGraphicalPN() {
		super();
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
		if(this.petriNet != null){
			this.petriNet.removeStructureListener(this);
		}
		this.petriNet = petriNet;
		this.petriNet.addStructureListener(this);
		this.petriNetGraphics = null;
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

	@Override
	public void structureChanged() {}

	@Override
	public void placeAdded(PlaceChangeEvent<P> event) {
		if(petriNetGraphics != null){
			petriNetGraphics.getPlaceGraphics().put(event.place.getName(), new NodeGraphics());
			petriNetGraphics.getPlaceLabelAnnotationGraphics().put(event.place.getName(), new AnnotationGraphics());
		}
	}

	@Override
	public void placeRemoved(PlaceChangeEvent<P> event) {
		if(petriNetGraphics != null){
			petriNetGraphics.getPlaceGraphics().remove(event.place.getName());
			petriNetGraphics.getPlaceLabelAnnotationGraphics().remove(event.place.getName());
		}
	}

	@Override
	public void transitionAdded(TransitionChangeEvent<T> event) {
		if(petriNetGraphics != null){
			petriNetGraphics.getTransitionGraphics().put(event.transition.getName(), new NodeGraphics());
			petriNetGraphics.getTransitionLabelAnnotationGraphics().put(event.transition.getName(), new AnnotationGraphics());
		}
	}

	@Override
	public void transitionRemoved(TransitionChangeEvent<T> event) {
		if(petriNetGraphics != null){
			petriNetGraphics.getTransitionGraphics().remove(event.transition.getName());
			petriNetGraphics.getTransitionLabelAnnotationGraphics().remove(event.transition.getName());
		}
	}

	@Override
	public void relationAdded(RelationChangeEvent<F> event) {
		if(petriNetGraphics != null){
			petriNetGraphics.getArcGraphics().put(event.relation.getName(), new ArcGraphics());
			petriNetGraphics.getArcAnnotationGraphics().put(event.relation.getName(), new AnnotationGraphics());
		}
	}

	@Override
	public void relationRemoved(RelationChangeEvent<F> event) {
		if(petriNetGraphics != null){
			petriNetGraphics.getArcGraphics().remove(event.relation.getName());
			petriNetGraphics.getArcAnnotationGraphics().remove(event.relation.getName());
		}
	}
	
}
