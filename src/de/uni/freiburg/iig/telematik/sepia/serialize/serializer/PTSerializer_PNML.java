package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import org.w3c.dom.Element;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerializer_PNML;

public class PTSerializer_PNML<P extends AbstractPTPlace<F>, 
								T extends AbstractPTTransition<F>, 
								F extends AbstractPTFlowRelation<P,T>, 
								M extends AbstractPTMarking> extends PNSerializer_PNML<P, T, F, M, Integer> {

	public PTSerializer_PNML(AbstractGraphicalPN<P, T, F, M, Integer> petriNet) throws ParameterException {
		super(petriNet);
	}

	public PTSerializer_PNML(AbstractPetriNet<P, T, F, M, Integer> petriNet) throws ParameterException {
		super(petriNet);
	}
	
	@Override
	protected Element addInitialMarking(Element placeElement, Integer state){
		Element markingElement = createElement("initialMarking");
		markingElement.appendChild(createTextElement("text", state.toString()));
		placeElement.appendChild(markingElement);
		return markingElement;
	}
	
	@Override
	protected void addConstraint(Element arcElement, Integer constraint, AnnotationGraphics annotationGraphics) {
		Element inscriptionElement = createElement("inscription");
		Element textElement = createTextElement("text", constraint.toString());
		inscriptionElement.appendChild(textElement);
		
		if(annotationGraphics != null && annotationGraphics.hasContent()){
			Element graphicsElement = createTextGraphicsElement(annotationGraphics);
			if(graphicsElement != null)
				inscriptionElement.appendChild(graphicsElement);
		}
		arcElement.appendChild(inscriptionElement);
	}

	@Override
	public NetType acceptedNetType() {
		return NetType.PTNet;
	}

	@Override
	public AbstractPTNet<P, T, F, M> getPetriNet() {
		return (AbstractPTNet<P,T,F,M>) super.getPetriNet();
	}

	@Override
	public AbstractPTGraphics<P, T, F, M> getGraphics() {
		return (AbstractPTGraphics<P, T, F, M>) super.getGraphics();
	}

}
