package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNTransition;

/**
 * {@link AbstractPNGraphics} implementation for the CWNs.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public abstract class AbstractCWNGraphics<P extends AbstractCWNPlace<F>,
						 		  T extends AbstractCWNTransition<F>, 
						 		  F extends AbstractCWNFlowRelation<P,T>, 
						 		  M extends AbstractCWNMarking> extends AbstractCPNGraphics<P, T, F, M> {
}
