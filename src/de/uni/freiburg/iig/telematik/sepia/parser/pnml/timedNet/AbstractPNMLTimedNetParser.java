package de.uni.freiburg.iig.telematik.sepia.parser.pnml.timedNet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
	        
	        //read context names
	        Element processContextElement = (Element) pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.processContext).item(0);
	        String processContextName = processContextElement.getAttribute("id");
	        net.setProcesContextName(processContextName);

	        Element timeContextElement = (Element) pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.timeContext).item(0);
	        String timeContextName = timeContextElement.getAttribute("id");
	        net.setTimeContextName(timeContextName);
	        
	        Element resourceContextElement = (Element) pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.resourceContext).item(0);
	        String resourceContextName = resourceContextElement.getAttribute("id");
	        net.setResourceContextName(resourceContextName);
	        
	        //read recurring attribute
	        try {
	        	Element recurringContext = (Element) pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.recurringString).item(0);
	        	boolean recurring = Boolean.parseBoolean(recurringContext.getTextContent());
	        	net.setRecurring(recurring);
	        } catch (Exception e) {
	        	net.setRecurring(false);
	        }
	        
	        //read cost per time unit
	        try{
	        	Element costPerTimeUnit = (Element) pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.costPerTimeUnitString).item(0);
	        	double cost = Double.parseDouble(costPerTimeUnit.getTextContent());
	        	net.setCostPerTimeUnit(cost);
	        	
	        } catch (Exception e) {
	        	net.setCostPerTimeUnit(0);
	        }
	        
	      //read cost per time unit after deadline was missed
	        try{
	        	Element costPerTimeUnitDeadline = (Element) pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.costPerTimeUnitAfterDeadlineString).item(0);
	        	double costDeadline = Double.parseDouble(costPerTimeUnitDeadline.getTextContent());
	        	net.setCostPerTimeUnitAfterDeadline(costDeadline);
	        	
	        } catch (Exception e) {
	        	net.setCostPerTimeUnitAfterDeadline(0);
	        }
	        
	        //read net weight
	        try{
	        	Element netWeight = (Element) pnmlDocument.getElementsByTagName(PNMLTimedNetSerializer.netWeight).item(0);
	        	double weight= Double.parseDouble(netWeight.getTextContent());
	        	net.setNetWeight(weight);
	        } catch (Exception e) {
	        	net.setNetWeight(1);
	        }
	        
	        
	        for(T transition:net.getTransitions()){
	        	transition.setNet(net);
	        }
	        
	 }
	 
	 protected String getNodeContent(String tagName, Document pnmlDocument){
		 try{
			 NodeList nodes = pnmlDocument.getElementsByTagName(tagName);
			 Node node = nodes.item(0);
			 return node.getChildNodes().item(0).getNodeValue();
		 } catch (Exception e){
			 return "";
		 }
	 }
	
	

}
