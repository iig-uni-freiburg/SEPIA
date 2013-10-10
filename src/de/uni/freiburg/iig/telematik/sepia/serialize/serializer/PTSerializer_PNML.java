package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerializer_PNML;
import de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException;

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
	protected void addTransitionInformation() {
		// TODO Auto-generated method stub
		for(T transition: getPetriNet().getTransitions()){
			
		}
	}

	@Override
	protected void addArcInformation() {
		// TODO Auto-generated method stub
		for(F relation: getPetriNet().getFlowRelations()){
			
		}
	}
	
	@Override
	protected Element addInitialMarking(Element placeElement, Integer state){
		Element markingElement = createElement("initialMarking");
		markingElement.appendChild(createTextElement("text", state.toString()));
		placeElement.appendChild(markingElement);
		return markingElement;
	}
	
	@Override
	public String serialize() throws SerializationException {
		String result = super.serialize();
		toFile("/Users/stocker/Desktop/out.xml");
		return result;
	}

	

	@Override
	public NetType acceptedNetType() {
		return NetType.PTNet;
	}

	@Override
	public AbstractPTNet<P, T, F, M> getPetriNet() {
		return (AbstractPTNet<P,T,F,M>) super.getPetriNet();
	}

}
