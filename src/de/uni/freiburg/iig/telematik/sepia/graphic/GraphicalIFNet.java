package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;

/**
 * Container class with a {@link IFNet} and its graphical information as {@link IFNetGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class GraphicalIFNet extends AbstractGraphicalPN<IFNetPlace, AbstractIFNetTransition, IFNetFlowRelation, IFNetMarking, Multiset<String>> {

	public GraphicalIFNet(AbstractPetriNet<IFNetPlace, AbstractIFNetTransition, IFNetFlowRelation, IFNetMarking, Multiset<String>> petriNet, AbstractPNGraphics<IFNetPlace, AbstractIFNetTransition, IFNetFlowRelation, IFNetMarking, Multiset<String>> petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}

	@Override
	public IFNet getPetriNet() {
		return (IFNet) super.getPetriNet();
	}

	@Override
	public IFNetGraphics getPetriNetGraphics() {
		return (IFNetGraphics) super.getPetriNetGraphics();
	}
}
