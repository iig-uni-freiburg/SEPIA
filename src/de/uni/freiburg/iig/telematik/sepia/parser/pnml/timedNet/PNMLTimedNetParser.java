package de.uni.freiburg.iig.telematik.sepia.parser.pnml.timedNet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TimedNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedTransition;

public class PNMLTimedNetParser extends AbstractPNMLTimedNetParser<TimedNetPlace, TimedTransition, TimedFlowRelation, TimedMarking, TimedNet, TimedNetGraphics> {

	@Override
	public GraphicalTimedNet parse(Document pnmlDocument) throws ParserException {
		net=new TimedNet();
		graphics = new TimedNetGraphics();
		parseDocument(pnmlDocument);
		//GraphicalTimedNet result = new (net,graphics);
		return new GraphicalTimedNet(net,graphics);
	}
	

}
