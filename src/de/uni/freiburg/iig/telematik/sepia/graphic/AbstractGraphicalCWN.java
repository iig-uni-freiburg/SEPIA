package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCWNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.cwn.AbstractCWNMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.cwn.AbstractCWNMarkingGraphState;
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
								  X extends AbstractCWNMarkingGraphState<M>,
								  Y extends AbstractCWNMarkingGraphRelation<M, X>,
								  N extends AbstractCWN<P,T,F,M,X,Y>,
  							  	  G extends AbstractCWNGraphics<P,T,F,M>> extends AbstractGraphicalCPN<P,T,F,M,X,Y,N,G> {

	public AbstractGraphicalCWN(N petriNet, G petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
}
