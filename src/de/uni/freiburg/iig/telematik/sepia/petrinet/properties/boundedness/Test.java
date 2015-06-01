package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness;

import de.invation.code.toval.thread.ExecutorListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

public class Test implements ExecutorListener<BoundednessCheckResult<PTPlace,PTTransition,PTFlowRelation,PTMarking,Integer>>{

	@Override
	public void executorStarted() {
		System.out.println("started");
	}

	@Override
	public void executorStopped() {
		System.out.println("stopped");
	}

	@Override
	public void executorFinished(BoundednessCheckResult result) {
		System.out.println(result.getBoundedness());
	}

	@Override
	public void executorException(Exception exception) {
		System.out.println(exception.getStackTrace());
	}

	@Override
	public void progress(double progress) {}
	
	public static void main(String[] args) throws BoundednessException {
		PTNet net = new PTNet();
		net.addPlace("p0");
		net.addTransition("t0");
		net.addFlowRelationPT("p0", "t0");
		net.addFlowRelationTP("t0", "p0", 1);
		PTMarking marking = new PTMarking();
		marking.set("p0", 100);
		net.setInitialMarking(marking);
		
		BoundednessCheck.initiateBoundednessCheck(net, new Test());
	}

}
