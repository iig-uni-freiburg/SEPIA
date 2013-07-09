package de.uni.freiburg.iig.telematik.sepia.parser;

import java.io.File;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import de.invation.code.toval.validate.InconsistencyException;
import de.invation.code.toval.validate.ParameterException;

import petrinet.pt.PTMarking;
import petrinet.pt.PTNet;


public class PNMLParser {
	private static PTNet petriNet;
	private static NodeType nodeType;
	private static String lastPlace;
	private static String lastTransition;
	private static String nodeID;
	
	public static PTNet parsePNML(String path, boolean replaceSpacesInTransitionLabels) throws XMLStreamException, ParameterException {
		PTMarking marking = new PTMarking();
		petriNet = new PTNet();
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLStreamReader stax;
		boolean nodeTagFound = false;
		boolean nameTagFound = false;
		boolean textTagFound = false;
		boolean markingTagFound = false;
		stax = inputFactory.createXMLStreamReader(new StreamSource(new File(path)));
		while (stax.hasNext()) {
			int type = stax.next();
			switch (type) {
			case XMLStreamConstants.START_ELEMENT:
				if (stax.getLocalName().equals("net")) {
					petriNet.setName(stax.getAttributeValue(null, "id"));
				} else if (stax.getLocalName().equals("place")) {
					nodeType = NodeType.PLACE;
					nodeID = getID(stax);
					nodeTagFound = true;
					nameTagFound = false;
					textTagFound = false;
					markingTagFound = false;
					lastPlace = nodeID;
					petriNet.addPlace(lastPlace);
//					System.out.println("add place: "+nodeID);
				} else if (stax.getLocalName().equals("transition")) {
					nodeType = NodeType.TRANSITION;
					nodeID = getID(stax);
					nodeTagFound = true;
					nameTagFound = false;
					textTagFound = false;
					markingTagFound = false;
					lastTransition = nodeID;
					petriNet.addTransition(lastTransition);
//					System.out.println("add transition: "+nodeID);
				} else if (stax.getLocalName().equals("initialMarking")) {
					if (nodeTagFound)
						markingTagFound = true;
				} else if (stax.getLocalName().equals("arc")) {
					addArc(stax);
					nodeTagFound = true;
					nameTagFound = false;
					textTagFound = false;
					markingTagFound = false;
				} else if (stax.getLocalName().equals("name")) {
					if (nodeTagFound)
						nameTagFound = true;
				} else if (stax.getLocalName().equals("text")) {
					if (nameTagFound || markingTagFound)
						textTagFound = true;
				}
				break;
			case XMLStreamConstants.CDATA:
			case XMLStreamConstants.CHARACTERS:
				if (!stax.isWhiteSpace() && textTagFound) {
					switch (nodeType) {
					case PLACE:
						if(markingTagFound){
							marking.set(lastPlace, new Integer(stax.getText()));
						} else {
							petriNet.getPlace(lastPlace).setLabel(stax.getText());
						}
						break;
					case TRANSITION:
						petriNet.getTransition(lastTransition).setLabel(stax.getText().replace(' ', '_'));
						petriNet.getTransition(lastTransition).setSilent(stax.getText().startsWith("_"));
						break;
					}
				}
				break;
			}
		}
		petriNet.setInitialMarking(marking);
		return petriNet;
	}
	
	private static String getID(XMLStreamReader reader){
		if(reader.getAttributeCount() > 0){
			for(int i=0; i<reader.getAttributeCount(); i++)
				if(reader.getAttributeLocalName(i).equals("id")){
					return reader.getAttributeValue(i); 
				}
		}
		return null;
	}
	
	private static void addArc(XMLStreamReader reader) throws InconsistencyException, ParameterException {
		String id = null, source = null, target = null;
		if(reader.getAttributeCount() > 2){
			for(int i=0; i<reader.getAttributeCount(); i++){
				if(reader.getAttributeLocalName(i).equals("id")){
					id = reader.getAttributeValue(i); 
				} else if (reader.getAttributeLocalName(i).equals("source")){
					source = reader.getAttributeValue(i); 
				} else if (reader.getAttributeLocalName(i).equals("target")){
					target = reader.getAttributeValue(i); 
				}
			}
		}
		if(id!=null && source!=null && target!=null){
			if(petriNet.containsPlace(source) && petriNet.containsTransition(target)){
				petriNet.addFlowRelationPT(source, target);
			} else if(petriNet.containsPlace(target) && petriNet.containsTransition(source)){
				petriNet.addFlowRelationTP(source, target);
			} else throw new InconsistencyException("Inconsistency in parsed PNML:\n Place or transition of arc "+getID(reader)+" does not exist in Petri net.");
		}
	}
	
	private enum NodeType {PLACE, TRANSITION, ARC};

}