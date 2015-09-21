package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TimedNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedTransition;

public class GraphicalTimedNet extends AbstractGraphicalTimedNet<TimedNetPlace, TimedTransition, TimedFlowRelation, TimedMarking, TimedNet, TimedNetGraphics>{

	public GraphicalTimedNet(TimedNet petriNet, TimedNetGraphics petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
	
	public GraphicalTimedNet(){
		this(new TimedNet(),new TimedNetGraphics()); 
	}

}
