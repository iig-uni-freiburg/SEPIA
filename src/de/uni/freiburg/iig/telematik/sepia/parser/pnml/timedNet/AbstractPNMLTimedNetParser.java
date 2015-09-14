package de.uni.freiburg.iig.telematik.sepia.parser.pnml.timedNet;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractTimedNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt.AbstractPNMLPTNetParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLTimedNetSerializer;

public abstract class AbstractPNMLTimedNetParser <P extends AbstractTimedPlace<F>,
T extends AbstractTimedTransition<F>,
F extends AbstractTimedFlowRelation<P, T>,
M extends AbstractTimedMarking,
N extends AbstractTimedNet<P, T, F, M>,
G extends AbstractTimedNetGraphics<P, T, F, M>> extends AbstractPNMLPTNetParser<P, T, F, M, N, G>{

	 @Override
	    public void parseDocument(Document pnmlDocument) throws ParserException {
	
	        super.parseDocument(pnmlDocument);
	
	        // read positions of the classification annotations for token labels and clearances
	        NodeList processNode = pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.processContext);
	        net.setProcesContextName(processNode.item(0).getNodeValue());
	        
	        NodeList timeNode = pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.timeContext);
	        net.setTimeContextName(timeNode.item(0).getNodeValue());
	        
	        NodeList ressourceNode = pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.resourceContext);
	        net.setResourceContextName(ressourceNode.item(0).getNodeValue());
	        
	 }
	
	

}
