package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import org.w3c.dom.Element;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractTimedNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

public class PNMLTimedNetSerializer<P extends AbstractTimedPlace<F>, 
T extends AbstractTimedTransition<F>, 
F extends AbstractTimedFlowRelation<P,T>, 
M extends AbstractTimedMarking,
N extends AbstractTimedNet<P, T, F, M>,
G extends AbstractTimedNetGraphics<P,T,F,M>> extends PNMLPTNetSerializer<P, T, F, M, N , G> {
	
	public static final String resourceContext = "resourcecontext";
	public static final String timeContext = "timecontext";
	public static final String processContext = "processcontext";
	public static final String recurringString = "isRecurring";
	public static final String costPerTimeUnitString = "costPerTimeUnit";
	public static final String costPerTimeUnitAfterDeadlineString = "costPerTimeUnitAfterDeadline";

	public PNMLTimedNetSerializer(AbstractGraphicalTimedNet<P, T, F, M, N, G> petriNet) {
		super(petriNet);
		
	}
	
	public PNMLTimedNetSerializer(N net){
		super(net);
	}
	
	@Override
	public NetType acceptedNetType() {
		return NetType.RTPTnet;
	}
	
	@Override
	protected void addHeader() {
		// Add resource context name
		Element resourceContextElement = getSupport().createElement(resourceContext);
		String resourceContextName = petriNet.getResourceContextName();
		if (resourceContextName==null|| resourceContextName.isEmpty())
			resourceContextName="nullResourceContext";
		resourceContextElement.setAttribute("id", resourceContextName);
		getSupport().getNetElement().appendChild(resourceContextElement);
		
		// Add time context name
		Element timeContextElement = getSupport().createElement(timeContext);
		String timeContextName = petriNet.getTimeContextName();
		if (timeContextName==null|| timeContextName.isEmpty())
			timeContextName="nullTimeContext";
		//timeContextElement.setTextContent(petriNet.getTimeContextName());
		timeContextElement.setAttribute("id", timeContextName);
		getSupport().getNetElement().appendChild(timeContextElement);
		
		// Add processContext (AccessContext)
		Element processContextElement = getSupport().createElement(processContext);
		String processContextName = petriNet.getAccessContextName();
		if(processContextName==null||processContextName.isEmpty())
			processContextName="nullProcessContext";
		//processContextElement.setTextContent(petriNet.getAccessContextName());
		processContextElement.setAttribute("id", processContextName);
		getSupport().getNetElement().appendChild(processContextElement);
		
		// Add recurring information
		Element recurringContext = getSupport().createTextElement(recurringString, Boolean.toString(petriNet.isRecurring()));
		getSupport().getNetElement().appendChild(recurringContext);
		
		// Add cost per time unit
		Element costElement = getSupport().createTextElement(costPerTimeUnitString, Double.toString(petriNet.getCostPerTimeUnit()));
		getSupport().getNetElement().appendChild(costElement);
		
		//Add cost per time unit after deadline was missed
		Element costDeadlineElement = getSupport().createTextElement(costPerTimeUnitAfterDeadlineString, Double.toString(petriNet.getCostPerTimeUnitAfterDeadline()));
		getSupport().getNetElement().appendChild(costDeadlineElement);
		
	}

}
