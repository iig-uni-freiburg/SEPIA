package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;


import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

public abstract class AbstractTimedNetGraphics<P extends AbstractTimedPlace<F>,
T extends AbstractTimedTransition<F>, 
F extends AbstractTimedFlowRelation<P,T>, 
M extends AbstractTimedMarking> extends AbstractPNGraphics<P,T,F,M,Integer>{
	
	public AbstractTimedNetGraphics(){
		super();
	}

}
