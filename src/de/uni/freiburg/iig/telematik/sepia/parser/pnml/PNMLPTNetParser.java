package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import org.w3c.dom.Document;

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

/**
 * TODO
 * 
 * @author Adrian Lange
 */
public class PNMLPTNetParser {

	/**
	 * TODO
	 * 
	 * @param pnmlDocument
	 * @return
	 */
	public static <P extends AbstractPlace<F,S>, 
				   T extends AbstractTransition<F,S>, 
				   F extends AbstractFlowRelation<P,T,S>, 
				   M extends AbstractMarking<S>, 
				   S extends Object> 

					GraphicalPTNet
	
				  parse(Document pnmlDocument) {

		PTNet net = new PTNet();
		PTGraphics graphics = new PTGraphics();

		return new GraphicalPTNet(net, graphics);
	}
}
