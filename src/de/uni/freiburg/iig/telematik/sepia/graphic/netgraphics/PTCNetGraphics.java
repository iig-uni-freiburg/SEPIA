package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.PTCNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;

/**
 * {@link AbstractPNGraphics} implementation for the P/T nets.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class PTCNetGraphics extends AbstractPTCNetGraphics<PTCNetPlace, AbstractPTCNetTransition<PTCNetFlowRelation>, PTCNetFlowRelation, PTCNetMarking>{}
