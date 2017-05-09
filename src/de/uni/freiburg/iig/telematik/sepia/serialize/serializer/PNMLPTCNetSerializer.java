package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import org.w3c.dom.Element;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTCNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPTCNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ptc.abstr.AbstractPTCNetTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerializer_PNML;

public class PNMLPTCNetSerializer<P extends AbstractPTCNetPlace<F>, T extends AbstractPTCNetTransition<F>, F extends AbstractPTCNetFlowRelation<P, T>, M extends AbstractPTCNetMarking, N extends AbstractPTCNet<P, T, F, M>, G extends AbstractPTCNetGraphics<P, T, F, M>>
		extends PNSerializer_PNML<P, T, F, M, Integer, N, G> {

	public PNMLPTCNetSerializer(AbstractGraphicalPTCNet<P, T, F, M, N, G> petriNet) {
		super(petriNet);
	}

	public PNMLPTCNetSerializer(N petriNet) {
		super(petriNet);
	}

	@Override
	protected Element addInitialMarking(Element placeElement, Integer state) {
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

		if (annotationGraphics != null && annotationGraphics.hasContent()) {
			Element graphicsElement = getSupport().createTextGraphicsElement(annotationGraphics);
			if (graphicsElement != null)
				inscriptionElement.appendChild(graphicsElement);
		}
		arcElement.appendChild(inscriptionElement);
	}

	@Override
	public NetType acceptedNetType() {
		return NetType.PTCNet;
	}

	@Override
	protected void appendPlaceInformation(P place, Element placeElement) {
		super.appendPlaceInformation(place, placeElement);
		Element costElement = getSupport().createElement("cost");
		costElement.appendChild(getSupport().createTextElement("text", place.getCost().toString()));
		placeElement.appendChild(costElement);

	}

	@Override
	protected void appendTransitionInformation(T transition, Element transitionElement) {
		super.appendTransitionInformation(transition, transitionElement);
		Element costElement = getSupport().createElement("cost");
		costElement.appendChild(getSupport().createTextElement("text", transition.getCost().toString()));
		transitionElement.appendChild(costElement);
	}
}
