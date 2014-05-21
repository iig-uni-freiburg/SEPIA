package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.cpn.AbstractCPNMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;

/**
 * Container class with a {@link CPN} and its graphical information as {@link AbstractCPNGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class AbstractGraphicalCPN<P extends AbstractCPNPlace<F>, 
								  T extends AbstractCPNTransition<F>, 
								  F extends AbstractCPNFlowRelation<P, T>, 
								  M extends AbstractCPNMarking,
								  X extends AbstractCPNMarkingGraphState<M>,
								  Y extends AbstractCPNMarkingGraphRelation<M, X>,
								  N extends AbstractCPN<P,T,F,M,X,Y>,
  							  	  G extends AbstractCPNGraphics<P,T,F,M>> extends AbstractGraphicalPN<P,T,F,M,Multiset<String>,X,Y,N,G> {

	public AbstractGraphicalCPN(N petriNet, G petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
}
