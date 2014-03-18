package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import java.awt.Color;

import org.w3c.dom.Element;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerializer_PNML;

public class PNMLCPNSerializer<P extends AbstractCPNPlace<F>, 
							   T extends AbstractCPNTransition<F>, 
							   F extends AbstractCPNFlowRelation<P,T>, 
							   M extends AbstractCPNMarking,
							   N extends AbstractCPN<P,T,F,M>,
							   G extends AbstractCPNGraphics<P,T,F,M>> extends PNSerializer_PNML<P, T, F, M, Multiset<String>, N, G> {

	public PNMLCPNSerializer(AbstractGraphicalCPN<P, T, F, M, N, G> petriNet) throws ParameterException {
		super(petriNet);
	}

	public PNMLCPNSerializer(N petriNet) throws ParameterException {
		super(petriNet);
	}

	@Override
	protected void addHeader() {
	    Element tokenColorsElement = getSupport().createElement("tokencolors");
	    for(String colorName: getPetriNet().getTokenColors()){
	    	if(colorName.equals(getPetriNet().defaultTokenColor()))
	    		continue;
	    	tokenColorsElement.appendChild(createTokenColorElement(colorName));
	    }
	    if(tokenColorsElement.getChildNodes().getLength() > 0)
	    	getSupport().getNetElement().appendChild(tokenColorsElement);
	}
	
	protected Element createTokenColorElement(String colorName){
		Element tokenColorElement = getSupport().createElement("tokencolor");
		Element colorElement = getSupport().createTextElement("color", colorName);
		tokenColorElement.appendChild(colorElement);
		
		if(hasGraphics()){
			Element rgbElement = getSupport().createElement("rgbcolor");
			tokenColorElement.appendChild(rgbElement);
			Color color = getGraphics().getColors().get(colorName);
			if (color != null) {
				rgbElement.appendChild(getSupport().createTextElement("r", (new Integer(color.getRed())).toString()));
				rgbElement.appendChild(getSupport().createTextElement("g", (new Integer(color.getGreen())).toString()));
				rgbElement.appendChild(getSupport().createTextElement("b", (new Integer(color.getBlue())).toString()));
			}
		}
		
		return tokenColorElement;
	}

	@Override
	protected void addCapacity(P place, Element placeElement) {
		if(place.getCapacity() >= 0){
			Element capacitiesElement = getSupport().createElement("capacities");
			for(String color: place.getColorsWithCapacityRestriction()){
				Element capacityElement = getSupport().createElement("colorcapacity");
				capacityElement.appendChild(getSupport().createTextElement("color", color));
				try {
					capacityElement.appendChild(getSupport().createTextElement("capacity", new Integer(place.getColorCapacity(color)).toString()));
				} catch (ParameterException e) {
					// Should not happen, since we know, that the place has a capacity for this color.
					e.printStackTrace();
				}
				capacitiesElement.appendChild(capacityElement);
			}
			
			placeElement.appendChild(capacitiesElement);
		}
	}

	@Override
	protected Element addInitialMarking(Element placeElement, Multiset<String> state) {
		Element markingElement = getSupport().createElement("initialMarking");
//		try {
			markingElement.appendChild(getSupport().createTextElement("text", (new Integer(state.multiplicity(getPetriNet().defaultTokenColor()))).toString()));
//		} catch (ParameterException e) {
//			// Should not happen, since default token color is not null
//			e.printStackTrace();
//		}
		
		Element colorsElement = createColorsElement(state);
		if(colorsElement.getChildNodes().getLength() > 0)
			markingElement.appendChild(colorsElement);
		
		placeElement.appendChild(markingElement);
		return markingElement;
	}

	protected Element createColorsElement(Multiset<String> state){
		Element colorsElement = getSupport().createElement("colors");
		for(String tokenColor: state.support()){
			if(tokenColor.equals(getPetriNet().defaultTokenColor()))
				continue;
			
//			try {
				for(int i=0; i<state.multiplicity(tokenColor); i++){
					colorsElement.appendChild(getSupport().createTextElement("color", tokenColor));
				}
//			} catch (ParameterException e) {
//				// Should not happen, since tokenColor is not null
//				e.printStackTrace();
//			}
		}
		return colorsElement;
	}

	@Override
	protected void addConstraint(Element arcElement, Multiset<String> constraint, AnnotationGraphics annotationGraphics) {
		Element inscriptionElement = getSupport().createElement("inscription");
		
		int defaultTokenColorTokens = 0;
		for(String tokenColor: constraint.support()){
			if(tokenColor.equals(getPetriNet().defaultTokenColor())){
//				try {
					defaultTokenColorTokens += constraint.multiplicity(tokenColor);
//				} catch (ParameterException e) {
//					// Should not happen, since tokenColor is not null
//					e.printStackTrace();
//				}
			}
		}
		Element textElement = getSupport().createTextElement("text", String.valueOf(defaultTokenColorTokens));
		inscriptionElement.appendChild(textElement);
		
		Element colorsElement = createColorsElement(constraint);
		if(colorsElement.getChildNodes().getLength() > 0)
			inscriptionElement.appendChild(colorsElement);
		
		if(annotationGraphics != null && annotationGraphics.hasContent()){
			Element graphicsElement = getSupport().createTextGraphicsElement(annotationGraphics);
			if(graphicsElement != null)
				inscriptionElement.appendChild(graphicsElement);
		}
		arcElement.appendChild(inscriptionElement);
	}
	
	
	@Override
	public NetType acceptedNetType() {
		return NetType.CPN;
	}
	

}
