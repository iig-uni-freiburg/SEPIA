package de.uni.freiburg.iig.telematik.sepia.petrinet.timedPTnet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.TimeRessourceContext;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLTimedNetSerializer;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.sewol.context.process.ProcessContext;

public class AllTimedNetTests {
	
	public static void main (String args[]) throws IOException, ParserException{
		TimedNet net = getRTPNet();
		
		PNMLTimedNetSerializer serializer = new PNMLTimedNetSerializer<>(net);
		serializer.serialize("/tmp/", "test");
		PNMLParser parser = new PNMLParser<>();
		AbstractGraphicalTimedNet graphicalNet = (AbstractGraphicalTimedNet) parser.parse(new File("/tmp/test.pnml"),true,false);
		TimedNet timedNet = (TimedNet) graphicalNet.getPetriNet();
		System.out.println(timedNet.toString());
	}
	
	public static TimedNet getRTPNet(){
		TimedNet net = new TimedNet();
		net.addTransition("test");
		net.addTransition("test2");
		net.addPlace("start");
		net.addPlace("place2");
		net.addPlace("end");
		net.addFlowRelationPT("start", "test");
		net.addFlowRelationTP("test", "place2");
		net.addFlowRelationPT("place2", "test2");
		net.addFlowRelationTP("test2", "end");
		net.setResourceContext(new TestRessourceContest());
		net.setAccessControl(getProcessContext());
		net.setTimeContextName("Test-TimeContext");
		//net.setTimeRessourceContext(new TestTimedResourceContext());
		return net;
	}
	
	private static ProcessContext getProcessContext(){
		ProcessContext bla = new ProcessContext("Test Process-Context");
		bla.setACModel(new RBACModel("RBAC"));
		return bla;
	}
	


}

class TestRessourceContest implements IResourceContext{

	@Override
	public String getName() {
		return "Test-ResourceContext";
	}


	@Override
	public void blockResources(List<String> resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unBlockResources(List<String> resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAllowedResourcesFor(String activity) {
		return null;
	}

	@Override
	public List<String> getRandomAvailableResourceSetFor(String activity, boolean blockResources) {
		LinkedList<String> result = new LinkedList<>();
		result.add("Gerd");
		result.add("Hans");
		return result;
	}

	@Override
	public boolean isAvailable(String resourceName) {
		return true;
	}

	@Override
	public IResource getResourceObject(String resourceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsBlockedResources() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

        @Override
        public void setName(String name) {
                throw new UnsupportedOperationException("Not supported yet."); // TODO
        }


		@Override
		public void renameResource(String oldName, String newName) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public boolean needsResources(String name) {
			return true;
		}
}

class TestTimedResourceContext implements TimeRessourceContext{

	@Override
	public boolean isAvailable(String ressourceName) {
		return false;
	}

	@Override
	public String getName() {
		return "Test TimedResource-Context";
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ITimeBehaviour getTimeFor(String activity, List resources) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITimeBehaviour getTimeFor(String activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTimeBehaviourFor(String activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTimeBehaviourFor(String activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addResource(String activity, List resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void blockResources(List resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List getAllowedResourcesFor(String activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getRandomAllowedResourcesFor(String activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean behaviorIsKnown(String activity, List resources) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeResourceUsage(String activity, List resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTimeBehaviourFor(String activity, List resources, ITimeBehaviour behaviour) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTimeBehaviourFor(String activity, List resource) {
		// TODO Auto-generated method stub
		
	}
	
}
