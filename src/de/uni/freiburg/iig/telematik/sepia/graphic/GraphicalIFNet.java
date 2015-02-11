package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.awt.Color;
import java.util.Random;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.IFNetMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.IFNetMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;

/**
 * Container class with a {@link IFNet} and its graphical information as {@link IFNetGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class GraphicalIFNet extends AbstractGraphicalIFNet<	IFNetPlace, 
															AbstractIFNetTransition<IFNetFlowRelation>, 
															IFNetFlowRelation, 
															IFNetMarking, 
															RegularIFNetTransition, 
															DeclassificationTransition, 
															IFNetMarkingGraphState, 
															IFNetMarkingGraphRelation, 
															IFNet, 
															IFNetGraphics> {

	public GraphicalIFNet(IFNet petriNet) {
		super();
		setPetriNet(petriNet);
		IFNetGraphics graphics = new IFNetGraphics();
		Random rand = new Random();
		for(String attribute: petriNet.getTokenColors()){
			if(attribute.equals(petriNet.defaultTokenColor()))
				continue;
			graphics.getColors().put(attribute, new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
		}
		setPetriNetGraphics(graphics);
	}
	
	public GraphicalIFNet(IFNet petriNet, IFNetGraphics petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
}
