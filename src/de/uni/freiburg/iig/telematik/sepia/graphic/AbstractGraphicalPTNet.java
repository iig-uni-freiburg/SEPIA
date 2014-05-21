package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.pt.AbstractPTMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

/**
 * Container class with a {@link PTNet} and its graphical information as {@link AbstractPTGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public abstract class AbstractGraphicalPTNet<P extends AbstractPTPlace<F>, 
											 T extends AbstractPTTransition<F>, 
											 F extends AbstractPTFlowRelation<P, T>, 
											 M extends AbstractPTMarking,
											 X extends AbstractPTMarkingGraphState<M>,
											 Y extends AbstractPTMarkingGraphRelation<M, X>,
											 N extends AbstractPTNet<P,T,F,M,X,Y>,
			   							  	 G extends AbstractPTGraphics<P,T,F,M>> extends AbstractGraphicalPN<P, T, F, M, Integer, X, Y, N, G> {

	public AbstractGraphicalPTNet(N petriNet, G petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
}
