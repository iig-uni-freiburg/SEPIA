package de.uni.freiburg.iig.telematik.sepia.parser.pnml.timedNet;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractTimedNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt.AbstractPNMLPTNetParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

public abstract class AbstractPNMLTimedNetParser <P extends AbstractTimedPlace<F>,
T extends AbstractTimedTransition<F>,
F extends AbstractTimedFlowRelation<P, T>,
M extends AbstractTimedMarking,
N extends AbstractTimedNet<P, T, F, M>,
G extends AbstractTimedNetGraphics<P, T, F, M>> extends AbstractPNMLPTNetParser<P, T, F, M, N, G>{

}
