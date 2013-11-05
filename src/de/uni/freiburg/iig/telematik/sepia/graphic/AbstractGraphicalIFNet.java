package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractIFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;

/**
 * Container class with a {@link IFNet} and its graphical information as {@link IFNetGraphics}.
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class AbstractGraphicalIFNet<P extends AbstractIFNetPlace<F>, 
									T extends AbstractIFNetTransition<F>, 
									F extends AbstractIFNetFlowRelation<P, T>, 
									M extends AbstractIFNetMarking,
									R extends AbstractRegularIFNetTransition<F>,
									D extends AbstractDeclassificationTransition<F>,
									N extends AbstractIFNet<P,T,F,M,R,D>,
	   							  	G extends AbstractIFNetGraphics<P,T,F,M>> extends AbstractGraphicalCWN<P,T,F,M,N,G> {

	public AbstractGraphicalIFNet(N petriNet, G petriNetGraphics) {
		super(petriNet, petriNetGraphics);
	}
}
