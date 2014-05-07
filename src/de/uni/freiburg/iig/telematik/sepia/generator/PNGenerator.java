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
		ptNet.setName("SharedResource_"+processes+"_"+ressources);
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
		ptNet.setName("ProducerConsumer_"+producer+"_"+consumer);
		PTMarking initialMarking = new PTMarking();
		
		ptNet.addPlace("r1");
		ptNet.getPlace("r1").setCapacity(producer);
		ptNet.addPlace("r2");
		ptNet.getPlace("r2").setCapacity(producer);
		ptNet.addTransition("c");
		ptNet.addFlowRelationPT("r1", "c").setWeight(consumer);
		ptNet.addFlowRelationTP("c", "r2").setWeight(consumer);
		initialMarking.set("r2", consumer);
		
		for(int i=1; i<=producer; i++){
			ptNet.addTransition("use_"+i);
			ptNet.addTransition("produce_"+i);
			ptNet.addPlace("ready_"+i);
			ptNet.getPlace("ready_"+i).setCapacity(producer);
			ptNet.addPlace("produced_"+i);
			ptNet.getPlace("produced_"+i).setCapacity(producer);
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
		ptNet.setName("BoundedPipeline_"+processes+"_"+bound);
		PTMarking initialMarking = new PTMarking();
		
		for(int i=1; i<=processes; i++){
			if(i==1){
				ptNet.addTransition("a0");
			}
			ptNet.addTransition("a"+i);
			ptNet.addPlace("p"+i+"1");
			ptNet.addPlace("p"+i+"2");
			ptNet.addFlowRelationTP("a"+(i-1), "p"+i+"2").setWeight(bound);
			ptNet.addFlowRelationPT("p"+i+"1", "a"+(i-1)).setWeight(bound);
			ptNet.addFlowRelationPT("p"+i+"2", "a"+i);
			ptNet.addFlowRelationTP("a"+i, "p"+i+"1");
			switch(i){
			case 1:
//				ptNet.addFlowRelationTP("c_"+i, "p_"+i+"_1").setWeight(bound);
//				ptNet.addFlowRelationPT("p_"+i+"_1", "c_"+(i-1));
				initialMarking.set("p"+i+"2", bound);
				break;
			default:
//				ptNet.addFlowRelationTP("c_"+i, "p_"+i+"_1");
//				ptNet.addFlowRelationPT("p_"+i+"_1", "c_"+(i-1)).setWeight(bound);
				initialMarking.set("p"+i+"1", bound);
			}
		}
		
		ptNet.setInitialMarking(initialMarking);
		return ptNet;
	}
	
}
