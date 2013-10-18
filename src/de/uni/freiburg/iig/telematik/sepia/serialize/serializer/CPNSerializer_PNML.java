package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import java.awt.Color;

import org.w3c.dom.Element;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerializer_PNML;

public class CPNSerializer_PNML<P extends AbstractCPNPlace<F>, 
								T extends AbstractCPNTransition<F>, 
								F extends AbstractCPNFlowRelation<P,T>, 
								M extends AbstractCPNMarking> extends PNSerializer_PNML<P, T, F, M, Multiset<String>> {

	public CPNSerializer_PNML(AbstractGraphicalPN<P, T, F, M, Multiset<String>> petriNet) throws ParameterException {
		super(petriNet);
	}

	public CPNSerializer_PNML(AbstractPetriNet<P, T, F, M, Multiset<String>> petriNet) throws ParameterException {
		super(petriNet);
	}

	@Override
	protected void addHeader() {
	    Element tokenColorsElement = createElement("tokencolors");
	    for(String colorName: getPetriNet().getTokenColors()){
	    	if(colorName.equals(getPetriNet().defaultTokenColor()))
	    		continue;
	    	tokenColorsElement.appendChild(createTokenColorElement(colorName));
	    }
	    if(tokenColorsElement.getChildNodes().getLength() > 0)
	    	netElement.appendChild(tokenColorsElement);
	}
	
	protected Element createTokenColorElement(String colorName){
		Element tokenColorElement = createElement("tokencolor");
		Element colorElement = createTextElement("color", colorName);
		tokenColorElement.appendChild(colorElement);
		Element rgbElement = createElement("rgbcolor");
		tokenColorElement.appendChild(rgbElement);
		Color color = getGraphics().getColors().get(colorName);
		if(color != null){
			rgbElement.appendChild(createTextElement("r", (new Integer(color.getRed())).toString()));
			rgbElement.appendChild(createTextElement("g", (new Integer(color.getGreen())).toString()));
			rgbElement.appendChild(createTextElement("b", (new Integer(color.getBlue())).toString()));
		}
		
		return tokenColorElement;
	}

	@Override
	protected Element addInitialMarking(Element placeElement, Multiset<String> state) {
		Element markingElement = createElement("initialMarking");
		try {
			markingElement.appendChild(createTextElement("text", (new Integer(state.multiplicity(getPetriNet().defaultTokenColor()))).toString()));
		} catch (ParameterException e) {
			// Should not happen, since default token color is not null
			e.printStackTrace();
		}
		
		Element colorsElement = createColorsElement(state);
		if(colorsElement.getChildNodes().getLength() > 0)
			markingElement.appendChild(colorsElement);
		
		placeElement.appendChild(markingElement);
		return markingElement;
	}
	
	protected Element createColorsElement(Multiset<String> state){
		Element colorsElement = createElement("colors");
		for(String tokenColor: state.support()){
			if(tokenColor.equals(getPetriNet().defaultTokenColor()))
				continue;
			
			try {
				for(int i=0; i<state.multiplicity(tokenColor); i++){
					colorsElement.appendChild(createTextElement("color", tokenColor));
				}
			} catch (ParameterException e) {
				// Should not happen, since tokenColor is not null
				e.printStackTrace();
			}
		}
		return colorsElement;
	}

	@Override
	protected void addConstraint(Element arcElement, Multiset<String> constraint, AnnotationGraphics annotationGraphics) {
		Element inscriptionElement = createElement("inscription");
		
		Element colorsElement = createColorsElement(constraint);
		if(colorsElement.getChildNodes().getLength() > 0)
			inscriptionElement.appendChild(colorsElement);
		
		if(annotationGraphics != null && annotationGraphics.hasContent()){
			Element graphicsElement = getTextGraphics(annotationGraphics);
			if(graphicsElement != null)
				inscriptionElement.appendChild(graphicsElement);
		}
		arcElement.appendChild(inscriptionElement);
	}
	
	
	@Override
	public NetType acceptedNetType() {
		return NetType.CPN;
	}

	@Override
	public AbstractCPN<P, T, F, M> getPetriNet() {
		return (AbstractCPN<P,T,F,M>) super.getPetriNet();
	}
	
	@Override
	public AbstractCPNGraphics<P, T, F, M> getGraphics() {
		return (AbstractCPNGraphics<P, T, F, M>) super.getGraphics();
	}
	

}
