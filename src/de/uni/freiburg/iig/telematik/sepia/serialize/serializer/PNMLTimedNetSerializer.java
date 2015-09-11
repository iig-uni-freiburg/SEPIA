package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import org.w3c.dom.Element;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractIFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractTimedNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

public class PNMLTimedNetSerializer<P extends AbstractTimedPlace<F>, 
T extends AbstractTimedTransition<F>, 
F extends AbstractTimedFlowRelation<P,T>, 
M extends AbstractTimedMarking,
N extends AbstractTimedNet<P, T, F, M>,
G extends AbstractTimedNetGraphics<P,T,F,M>> extends PNMLPTNetSerializer<P, T, F, M, N , G> {

	public PNMLTimedNetSerializer(AbstractGraphicalPTNet<P, T, F, M, N, G> petriNet) {
		super(petriNet);
		
	}
	
	public PNMLTimedNetSerializer(N net){
		super(net);
	}
	
	@Override
	public NetType acceptedNetType() {
		return NetType.TimedNet;
	}
	
	@Override
	protected void addHeader() {
		Element timeContext = getSupport().createElement("TimeContext");
		
	    Element tokenColorsElement = getSupport().createElement("tokencolors");
//	    for(String colorName: getPetriNet().getTokenColors()){
//	    	if(colorName.equals(getPetriNet().defaultTokenColor()))
//	    		continue;
//	    	tokenColorsElement.appendChild(createTokenColorElement(colorName));
//	    }
	    if(tokenColorsElement.getChildNodes().getLength() > 0)
	    	getSupport().getNetElement().appendChild(tokenColorsElement);
	}

}
