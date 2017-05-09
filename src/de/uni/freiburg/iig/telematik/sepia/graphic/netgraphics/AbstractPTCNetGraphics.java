package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;

/**
 * {@link AbstractPNGraphics} implementation for the P/TCost nets.
 * 
 * @author Julius Holderer
 */
public class AbstractPTCNetGraphics<P extends AbstractPTCNetPlace<F>,
 T extends AbstractPTCNetTransition<F>, 
 F extends AbstractPTCNetFlowRelation<P,T>, 
 M extends AbstractPTCNetMarking> extends  AbstractPTGraphics<P,T,F,M> {}
