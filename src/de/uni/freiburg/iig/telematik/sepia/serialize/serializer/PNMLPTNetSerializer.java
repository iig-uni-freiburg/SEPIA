package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import org.w3c.dom.Element;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerializer_PNML;

public class PNMLPTNetSerializer<P extends AbstractPTPlace<F>, 
								T extends AbstractPTTransition<F>, 
								F extends AbstractPTFlowRelation<P,T>, 
								M extends AbstractPTMarking,
								N extends AbstractPTNet<P,T,F,M>,
							  	G extends AbstractPTGraphics<P,T,F,M>> extends PNSerializer_PNML<P, T, F, M, Integer, N , G> {

	public PNMLPTNetSerializer(AbstractGraphicalPTNet<P,T,F,M,N,G> petriNet) {
		super(petriNet);
	}

	public PNMLPTNetSerializer(N petriNet) {
		super(petriNet);
	}
	
	@Override
	protected Element addInitialMarking(Element placeElement, Integer state){
		Element markingElement = getSupport().createElement("initialMarking");
		markingElement.appendChild(getSupport().createTextElement("text", state.toString()));
		placeElement.appendChild(markingElement);
		return markingElement;
	}
	
	@Override
	protected void addConstraint(Element arcElement, Integer constraint, AnnotationGraphics annotationGraphics) {
		Element inscriptionElement = getSupport().createElement("inscription");
		Element textElement = getSupport().createTextElement("text", constraint.toString());
		inscriptionElement.appendChild(textElement);
		
		if(annotationGraphics != null && annotationGraphics.hasContent()){
			Element graphicsElement = getSupport().createTextGraphicsElement(annotationGraphics);
			if(graphicsElement != null)
				inscriptionElement.appendChild(graphicsElement);
		}
		arcElement.appendChild(inscriptionElement);
	}

	@Override
	public NetType acceptedNetType() {
		return NetType.PTNet;
	}
}
