package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCWNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNTransition;

/**
 * Container class with a {@link CWN} and its graphical information as {@link AbstractCWNGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class AbstractGraphicalCWN<P extends AbstractCWNPlace<F>, 
								  T extends AbstractCWNTransition<F>, 
								  F extends AbstractCWNFlowRelation<P, T>, 
								  M extends AbstractCWNMarking,
								  N extends AbstractCWN<P,T,F,M>,
  							  	  G extends AbstractCWNGraphics<P,T,F,M>> extends AbstractGraphicalCPN<P,T,F,M,N,G> {

	public AbstractGraphicalCWN(N petriNet, G petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}

//	@Override
//	public AbstractCWN<P,T,F,M> getPetriNet() {
//		return (AbstractCWN<P,T,F,M>) super.getPetriNet();
//	}
//
//	@Override
//	public AbstractCWNGraphics<P,T,F,M> getPetriNetGraphics() {
//		return (AbstractCWNGraphics<P,T,F,M>) super.getPetriNetGraphics();
//	}
}
