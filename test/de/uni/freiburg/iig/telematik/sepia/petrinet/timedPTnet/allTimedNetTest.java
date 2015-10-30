package de.uni.freiburg.iig.telematik.sepia.petrinet.timedPTnet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.AccessContextException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.TimeRessourceContext;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLTimedNetSerializer;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.sewol.context.process.ProcessContext;

public class allTimedNetTest {
	
	public static void main (String args[]) throws IOException, ParserException{
		TimedNet net = getRTPNet();
		
		PNMLTimedNetSerializer serializer = new PNMLTimedNetSerializer<>(net);
		serializer.serialize("/tmp/", "test");
		PNMLParser parser = new PNMLParser<>();
		AbstractGraphicalPN graphicalNet = parser.parse(new File("/tmp/test.pnml"),true,false);
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
		net.setTimeRessourceContext(new TestTimedResourceContext());
		return net;
	}
	
	private static ProcessContext getProcessContext(){
		ProcessContext bla = new ProcessContext("Test Process-Context");
		bla.setACModel(new RBACModel("RBAC"));
		return bla;
	}
	


}

class TestRessourceContest implements ResourceContext{

	@Override
	public ResourceContext getInstance() {
		return this;
	}

	@Override
	public ResourceContext getInstance(String contextName) {
		return this;
	}

	@Override
	public boolean mayAcces(String subject, String transition) {
		return true;
	}

	@Override
	public List<String> getSubjectsFor(String transition) throws AccessContextException {
		LinkedList<String> result = new LinkedList<>();
		result.add("Bernd");
		return result;
	}

	@Override
	public String getName() {
		return "idTestRessourceContext";
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}
	
}

class TestTimedResourceContext implements TimeRessourceContext{

	@Override
	public boolean isAvailable(String ressourceName) {
		return false;
	}

	@Override
	public boolean behaviorIsKnown(String activity, String... ressource) {
		return false;
	}

	@Override
	public void addRessource(String activity, String... ressources) {
		
	}

	@Override
	public List getAllowedRessourcesFor(String activity) {
		return null;
	}

	@Override
	public void removeRessourceUsage(String activity, String... ressources) {
		// TODO Auto-generated method stub
		
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
	public List getRandomAllowedRessourcesFor(String activity, boolean blockRessources) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
