package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractIFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.TransitionType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

public class PNMLIFNetSerializer<P extends AbstractIFNetPlace<F>, 
								  T extends AbstractIFNetTransition<F>, 
								  F extends AbstractIFNetFlowRelation<P,T>, 
								  M extends AbstractIFNetMarking,
								  R extends AbstractRegularIFNetTransition<F>,
								  D extends AbstractDeclassificationTransition<F>,
								  N extends AbstractIFNet<P,T,F,M,R,D>,
  							  	  G extends AbstractIFNetGraphics<P,T,F,M>> extends PNMLCPNSerializer<P, T, F, M, N, G> {

	public PNMLIFNetSerializer(AbstractGraphicalIFNet<P, T, F, M, R, D, N, G> ifNet) throws ParameterException {
		super(ifNet);
	}

	public PNMLIFNetSerializer(N ifNet) throws ParameterException {
		super(ifNet);
	}
	
	@Override
	protected void addHeader() {
		super.addHeader();
		
		// Add classification position information
		if(hasGraphics()){
			Element classificationPositionsElement = getSupport().createElement("classificationpositions");
			// Add clearance position information
			Position clearancePosition = getGraphics().getClearancesPosition();
			if(clearancePosition != null && clearancePosition.hasContent()){
				Element clearancesElement = getSupport().createElement("clearances");
				Element positionElement = getSupport().createPositionElement(clearancePosition);
				if(positionElement != null){
					clearancesElement.appendChild(positionElement);
				}
				classificationPositionsElement.appendChild(clearancesElement);
			}
			
			// Add token label position information
			Position tokenLabelPosition = getGraphics().getTokenLabelsPosition();
			if(tokenLabelPosition != null && tokenLabelPosition.hasContent()){
				Element tokenLabelsElement = getSupport().createElement("tokenlabels");
				Element positionElement = getSupport().createPositionElement(tokenLabelPosition);
				if(positionElement != null){
					tokenLabelsElement.appendChild(positionElement);
				}
				classificationPositionsElement.appendChild(tokenLabelsElement);
			}
			if(classificationPositionsElement.getChildNodes().getLength() > 0)
				getSupport().getNetElement().appendChild(classificationPositionsElement);
		}
	}

	@Override
	protected void appendTransitionInformation(AbstractIFNetTransition transition, Element transitionElement) {
		// Add transition type information
		Element transitionTypeElement = getSupport().createTextElement("transitiontype", transition.getType().toString().toLowerCase());
		transitionElement.appendChild(transitionTypeElement);
		
//		<subject>
//        <text>subjectA</text>
//        <graphics>
//          <offset x="0" y="5"/>
//        </graphics>
//      </subject>
		
		
		if(transition.getType() == TransitionType.REGULAR){
			RegularIFNetTransition regularTransition = (RegularIFNetTransition) transition;
			
			// Add subject graphics
			if(hasGraphics()){
				AnnotationGraphics subjectGraphics = getGraphics().getSubjectGraphics().get(transition.getName());
				if(subjectGraphics != null && subjectGraphics.hasContent()){
					Element subjectGraphicsRootElement = getSupport().createElement("subjectgraphics");
					Element subjectGraphicsElement = getSupport().createTextGraphicsElement(subjectGraphics);
					if(subjectGraphicsElement != null){
						subjectGraphicsRootElement.appendChild(subjectGraphicsElement);
						transitionElement.appendChild(subjectGraphicsRootElement);
					}
				}
			}
			
			// Add data access information
			Map<String, Set<AccessMode>> accessModes = regularTransition.getAccessModes();
			if(!accessModes.isEmpty()){
				Element accessFunctionsElement = getSupport().createElement("accessfunctions");
				for(String color: accessModes.keySet()){
					if(accessModes.get(color).isEmpty())
						continue;
					
					Element accessfunctionElement = getSupport().createElement("accessfunction");
					accessfunctionElement.appendChild(getSupport().createTextElement("color", color));
					Element accessModesElement = getSupport().createElement("accessmodes");
					for(AccessMode mode: AccessMode.values()){
						if(accessModes.get(color).contains(mode)){
							accessModesElement.appendChild(getSupport().createTextElement(mode.toString().toLowerCase(), "true"));
						} else {
							accessModesElement.appendChild(getSupport().createTextElement(mode.toString().toLowerCase(), "false"));
						}
					}
					accessfunctionElement.appendChild(accessModesElement);
					accessFunctionsElement.appendChild(accessfunctionElement);
				}
				
				// Add access modes graphics
				if(hasGraphics()){
					AnnotationGraphics functionGraphics = getGraphics().getAccessFunctionGraphics().get(transition.getName());
					if(functionGraphics != null && functionGraphics.hasContent()){
						Element graphicsElement = getSupport().createTextGraphicsElement(functionGraphics);
						if(graphicsElement != null){
							accessFunctionsElement.appendChild(graphicsElement);
						}
					}
				}
				
				transitionElement.appendChild(accessFunctionsElement);
			}
		}
		
	}
	

}
