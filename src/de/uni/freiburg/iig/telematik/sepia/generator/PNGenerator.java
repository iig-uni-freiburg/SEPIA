package de.uni.freiburg.iig.telematik.sepia.generator;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;


public class PNGenerator {
	
	public static PTNet sharedResource(int processes, int ressources) throws ParameterException{
		Validate.bigger(processes, 1);
		Validate.bigger(ressources, 0);
		
		PTNet ptNet = new PTNet();
		ptNet.setName("SharedResource("+processes+","+ressources+")");
		PTMarking initialMarking = new PTMarking();
		
		ptNet.addPlace("r");
		ptNet.getPlace("r").setCapacity(ressources);
		initialMarking.set("r", ressources);
		
		for(int i=1; i<=processes; i++){
			ptNet.addTransition("t"+i+"1");
			ptNet.addTransition("t"+i+"2");
			ptNet.addTransition("t"+i+"3");
			ptNet.addTransition("t"+i+"4");
			ptNet.addPlace("p"+i+"1");
			ptNet.addPlace("p"+i+"2");
			ptNet.addPlace("p"+i+"3");
			ptNet.addPlace("p"+i+"4");
			ptNet.addFlowRelationTP("t"+i+"1", "p"+i+"1");
			ptNet.addFlowRelationTP("t"+i+"2", "p"+i+"2");
			ptNet.addFlowRelationTP("t"+i+"3", "p"+i+"3");
			ptNet.addFlowRelationTP("t"+i+"4", "p"+i+"4");
			ptNet.addFlowRelationPT("p"+i+"1", "t"+i+"2");
			ptNet.addFlowRelationPT("p"+i+"2", "t"+i+"3");
			ptNet.addFlowRelationPT("p"+i+"3", "t"+i+"4");
			ptNet.addFlowRelationPT("p"+i+"4", "t"+i+"1");
			
			ptNet.addFlowRelationPT("r", "t"+i+"1");
			ptNet.addFlowRelationTP("t"+i+"2", "r");
			
			initialMarking.set("p"+i+"4", 1);
		}
		ptNet.setInitialMarking(initialMarking);
		
		return ptNet;
	}
}
