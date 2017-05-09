package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTCNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;

public abstract class AbstractGraphicalPTCNet<P extends AbstractPTCNetPlace<F>, T extends AbstractPTCNetTransition<F>, F extends AbstractPTCNetFlowRelation<P, T>, M extends AbstractPTCNetMarking, N extends AbstractPTCNet<P, T, F, M>, G extends AbstractPTCNetGraphics<P, T, F, M>>
		extends AbstractGraphicalPN<P, T, F, M, Integer, N, G> {

	public AbstractGraphicalPTCNet(N petriNet, G petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
}
