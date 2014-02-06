package de.uni.freiburg.iig.telematik.sepia.generator;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.RandomPTTraverser;
import de.uni.freiburg.iig.telematik.sepia.traversal.PNTraversalUtils;
import de.uni.freiburg.iig.telematik.sepia.traversal.PNTraverser;


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
	
	public static PTNet producerConsumer(int producer, int consumer) throws ParameterException{
		Validate.bigger(producer, 0);
		Validate.bigger(consumer, 0);
		
		PTNet ptNet = new PTNet();
		ptNet.setName("ProducerConsumer("+producer+","+consumer+")");
		PTMarking initialMarking = new PTMarking();
		
		ptNet.addPlace("r1");
		ptNet.addPlace("r2");
		ptNet.addTransition("c");
		ptNet.addFlowRelationPT("r1", "c").setWeight(consumer);
		ptNet.addFlowRelationTP("c", "r2").setWeight(consumer);
		initialMarking.set("r2", consumer);
		
		for(int i=1; i<=producer; i++){
			ptNet.addTransition("use_"+i);
			ptNet.addTransition("produce_"+i);
			ptNet.addPlace("ready_"+i);
			ptNet.addPlace("produced_"+i);
			ptNet.addFlowRelationPT("ready_"+i, "produce_"+i);
			ptNet.addFlowRelationTP("produce_"+i, "produced_"+i);
			ptNet.addFlowRelationPT("produced_"+i, "use_"+i);
			ptNet.addFlowRelationTP("use_"+i, "ready_"+i);
			ptNet.addFlowRelationTP("use_"+i, "r1");
			ptNet.addFlowRelationPT("r2", "use_"+i);
			initialMarking.set("ready_"+i, 1);
		}
		
		ptNet.setInitialMarking(initialMarking);
		return ptNet;
	}
	
	public static PTNet boundedPipeline(int processes, int bound) throws ParameterException{
		Validate.bigger(processes, 1);
		Validate.bigger(bound, 0);
		
		PTNet ptNet = new PTNet();
		ptNet.setName("BoundedPipeline("+processes+","+bound+")");
		PTMarking initialMarking = new PTMarking();
		
		for(int i=1; i<=processes; i++){
			if(i==1){
				ptNet.addTransition("c_0");
			}
			ptNet.addTransition("c_"+i);
			ptNet.addPlace("p_"+i+"_1");
			ptNet.addPlace("p_"+i+"_2");
			ptNet.addFlowRelationTP("c_"+(i-1), "p_"+i+"_2").setWeight(bound);
			ptNet.addFlowRelationPT("p_"+i+"_1", "c_"+(i-1)).setWeight(bound);
			ptNet.addFlowRelationPT("p_"+i+"_2", "c_"+i);
			ptNet.addFlowRelationTP("c_"+i, "p_"+i+"_1");
			switch(i){
			case 1:
//				ptNet.addFlowRelationTP("c_"+i, "p_"+i+"_1").setWeight(bound);
//				ptNet.addFlowRelationPT("p_"+i+"_1", "c_"+(i-1));
				initialMarking.set("p_"+i+"_2", 1);
				break;
			default:
//				ptNet.addFlowRelationTP("c_"+i, "p_"+i+"_1");
//				ptNet.addFlowRelationPT("p_"+i+"_1", "c_"+(i-1)).setWeight(bound);
				initialMarking.set("p_"+i+"_1", bound);
			}
		}
		
		ptNet.setInitialMarking(initialMarking);
		return ptNet;
	}
	
	public static void main(String[] args) throws ParameterException, PNException {
		PTNet pc = producerConsumer(5, 2);
		PTNet pipeline = boundedPipeline(2, 2);
		System.out.println(pipeline);
		PNTraversalUtils.testTraces(pipeline, 1, 10, true, false);

	}
}
