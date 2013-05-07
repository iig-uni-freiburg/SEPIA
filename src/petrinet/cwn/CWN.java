package petrinet.cwn;

import java.util.Set;

import petrinet.cwn.abstr.AbstractCWN;
import validate.ParameterException;

public class CWN extends AbstractCWN<CWNPlace, CWNTransition, CWNFlowRelation, CWNMarking> {

	public CWN() {
		super();
	}

	public CWN(Set<String> places, Set<String> transitions, CWNMarking initialMarking) 
			throws ParameterException {
		super(places, transitions, initialMarking);
	}	
	
	//------- Creation methods ----------------------------------------------------------------------
	
	@Override
	protected CWNMarking createNewMarking() {
		return new CWNMarking();
	}

	@Override
	protected CWNTransition createNewTransition(String name, String label, boolean isSilent) 
			throws ParameterException {
		return new CWNTransition(name, label, isSilent);
	}

	@Override
	protected CWNPlace createNewPlace(String name, String label) 
			throws ParameterException {
		return new CWNPlace(name, label);
	}

	@Override
	protected CWNFlowRelation createNewFlowRelation(CWNPlace place, CWNTransition transition) 
			throws ParameterException {
		return new CWNFlowRelation(place, transition);
	}

	@Override
	protected CWNFlowRelation createNewFlowRelation(CWNTransition transition, CWNPlace place) 
			throws ParameterException {
		return new CWNFlowRelation(transition, place);
	}
	
	//------- toString -----------------------------------------------------------------------------

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}
//	
//	public static void main(String[] args) throws Exception{
//		CWN cwn = new CWN();
//		cwn.addPlace("p1");
//		cwn.addPlace("p2");
//		cwn.addPlace("p3");
//		cwn.addPlace("p4");
//		cwn.addPlace("p5");
//		cwn.addPlace("p6");
//		cwn.addTransition("t1");
//		cwn.addTransition("t2");
//		cwn.addTransition("t3");
//		cwn.addTransition("t4");
//		cwn.addFlowRelationPT("p1", "t1");
//		cwn.addFlowRelationTP("t1", "p2");
//		cwn.addFlowRelationTP("t1", "p3");
//		cwn.addFlowRelationPT("p2", "t2");
//		cwn.addFlowRelationPT("p3", "t3");
//		cwn.addFlowRelationTP("t2", "p4");
//		cwn.addFlowRelationTP("t3", "p5");
//		cwn.addFlowRelationPT("p4", "t4");
//		cwn.addFlowRelationPT("p5", "t4");
//		cwn.addFlowRelationTP("t4", "p6");
//		CWNMarking initialMarking = new CWNMarking();
//		Multiset<String> markingInput = new Multiset<String>();
//		markingInput.add("black");
////		markingInput.put("green", 1);
//		initialMarking.set("p1", markingInput);
//		cwn.setInitialMarking(initialMarking);
//		
//		cwn.checkSoundness();
//	}

	@Override
	public CWN newInstance() {
		return new CWN();
	}

}
