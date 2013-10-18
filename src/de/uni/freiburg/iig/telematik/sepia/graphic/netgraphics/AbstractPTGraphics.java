package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

/**
 * {@link AbstractPNGraphics} implementation for the P/T nets.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public abstract class AbstractPTGraphics<P extends AbstractPTPlace<F>, T extends AbstractPTTransition<F>, F extends AbstractPTFlowRelation<P,T>, M extends AbstractPTMarking> extends AbstractPNGraphics<P, T, F, M, Integer>{}
