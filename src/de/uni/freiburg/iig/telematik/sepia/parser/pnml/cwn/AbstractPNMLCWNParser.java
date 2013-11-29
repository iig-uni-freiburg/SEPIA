package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cwn;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCWNGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLCPNParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNTransition;

/**
 * <p>
 * Abstract parser for abstract CWNs.
 * </p>
 * 
 * @author Adrian Lange
 */
public abstract class AbstractPNMLCWNParser<P extends AbstractCWNPlace<F>,
											T extends AbstractCWNTransition<F>,
											F extends AbstractCWNFlowRelation<P, T>,
											M extends AbstractCWNMarking,
											N extends AbstractCWN<P, T, F, M>,
											G extends AbstractCWNGraphics<P, T, F, M>>

	extends AbstractPNMLCPNParser<P, T, F, M, N, G> {
}
