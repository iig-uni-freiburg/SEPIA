/**
 * 
 */
package petrinet.snet.test;

import petrinet.snet.AccessMode;
import petrinet.snet.RegularSNetTransition;
import petrinet.snet.SNet;
import petrinet.snet.SNetFlowRelation;
import petrinet.snet.SNetPlace;
import types.Multiset;
import validate.ParameterException;

/**
 * Methods often used during testing of SNets.
 * @author boehr
 *
 */
public class SNetTestUtil {

	
	//Create a SNet with just one transition.
	//The transition has two input and two output places.
	//The transition consumes black from p0 and p1.
	//The transition consumes green from p0.
	//The transition consumes red from p1.
	//The transition produces green from p2.
	//The transition produces blue from p3.
	//
	//==>
	//processes green
	//deletes red
	//creates blue
	
	public static SNet createSimpleSnet() throws ParameterException{
		
	
		
		//create the SNet
		SNet sNet = new SNet();
		
		System.out.println("Reg1: " + sNet.getRegularTransitions());
		
		//add places with marking
		sNet.addPlace("p0");
		sNet.addPlace("p1");
		sNet.addPlace("p2");
		sNet.addPlace("p3");
		Multiset<String> p0Marking = new Multiset<String>();
		Multiset<String> p1Marking = new Multiset<String>();		
		p0Marking.add("black");
		p0Marking.add("green");
		p1Marking.add("black");
		p1Marking.add("red");
		sNet.getPlace("p0").setState(p0Marking);
		sNet.getPlace("p1").setState(p1Marking);
				
		
		//add the transition
		sNet.addTransition("t0");
		
		//add flowrelations
		SNetFlowRelation f1 = sNet.addFlowRelationPT("p0", "t0");
		SNetFlowRelation f2 = sNet.addFlowRelationPT("p1", "t0");
		SNetFlowRelation f3 = sNet.addFlowRelationTP("t0", "p2");
		SNetFlowRelation f4 = sNet.addFlowRelationTP("t0", "p3");
	
		//configure flow relations
		f1.addConstraint("green", 1);
		f2.addConstraint("red", 1);
		f3.addConstraint("green", 1);
		f4.addConstraint("blue", 1);
		
		//configure read write
		RegularSNetTransition rst = (RegularSNetTransition)sNet.getTransition("t0");
		rst.addAccessMode("green", AccessMode.READ);
		rst.addAccessMode("red", AccessMode.DELETE);
		rst.addAccessMode("blue", AccessMode.CREATE);
		
		return sNet;
		
		
	}
	
}
