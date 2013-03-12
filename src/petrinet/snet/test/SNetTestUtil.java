/**
 * 
 */
package petrinet.snet.test;

import java.util.Arrays;

import petrinet.snet.AccessMode;
import petrinet.snet.Labeling;
import petrinet.snet.RegularSNetTransition;
import petrinet.snet.SNet;
import petrinet.snet.SNetFlowRelation;
import petrinet.snet.SNetMarking;
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
		
		//add places with marking and capacity
		sNet.addPlace("pIn");
		sNet.getPlace("pIn").setColorCapacity("black", 1);
		
		sNet.addPlace("p0");
		sNet.getPlace("p0").setColorCapacity("black", 1);
		sNet.getPlace("p0").setColorCapacity("green", 1);
		
		sNet.addPlace("p1");
		sNet.getPlace("p1").setColorCapacity("black", 1);
		sNet.getPlace("p1").setColorCapacity("red", 1);
				
		sNet.addPlace("p2");
		sNet.getPlace("p2").setColorCapacity("black", 1);
		sNet.getPlace("p2").setColorCapacity("green", 1);
		
		sNet.addPlace("p3");
		sNet.getPlace("p3").setColorCapacity("black", 1);
		sNet.getPlace("p3").setColorCapacity("blue", 1);
		
		sNet.addPlace("pOut");
		sNet.getPlace("pOut").setColorCapacity("black", 1);
		
		
		Multiset<String> pInMarking = new Multiset<String>();		
		pInMarking.add("black");				
		
		SNetMarking sm = new SNetMarking();
		sm.set("pIn", pInMarking);
		sNet.setInitialMarking(sm);
		
		
		
		//add the transitions
		sNet.addTransition("tIn");
		sNet.addTransition("t0");
		sNet.addTransition("tOut");
		
		//add flowrelations
		SNetFlowRelation f1 = sNet.addFlowRelationPT("p0", "t0");
		SNetFlowRelation f2 = sNet.addFlowRelationPT("p1", "t0");
		SNetFlowRelation f3 = sNet.addFlowRelationTP("t0", "p2");
		SNetFlowRelation f4 = sNet.addFlowRelationTP("t0", "p3");
		
		SNetFlowRelation f5 = sNet.addFlowRelationPT("pIn", "tIn");
		SNetFlowRelation f6 = sNet.addFlowRelationTP("tIn", "p0");
		SNetFlowRelation f7 = sNet.addFlowRelationTP("tIn", "p1");
		
		SNetFlowRelation f8 = sNet.addFlowRelationPT("p2", "tOut");
		SNetFlowRelation f9 = sNet.addFlowRelationPT("p3", "tOut");
		SNetFlowRelation f10 = sNet.addFlowRelationTP("tOut", "pOut");
		
	
		//configure flow relations
		f1.addConstraint("green", 1);
		f2.addConstraint("red", 1);
		f3.addConstraint("green", 1);
		f4.addConstraint("blue", 1);
		
		//f5.addConstraint("black", 1);
		f6.addConstraint("green", 1);
		f7.addConstraint("red", 1);
		
		f8.addConstraint("green", 1);
		f9.addConstraint("blue", 1);
		//f10.addConstraint("black", 1);
		
		
		//configure read write
		RegularSNetTransition rst = (RegularSNetTransition)sNet.getTransition("t0");
		rst.addAccessMode("green", AccessMode.READ);
		rst.addAccessMode("red", AccessMode.DELETE);
		rst.addAccessMode("blue", AccessMode.CREATE);
		
		
		//create labeling
		Labeling l = new Labeling(sNet, Arrays.asList("Karl", "Heinz", "Becker"));
		sNet.getAnalysisContext().setLabeling(l);
		
		//add subjcet descriptors
		sNet.getAnalysisContext().setSubjectDescriptor("tIn", "Karl");
		sNet.getAnalysisContext().setSubjectDescriptor("t0", "Heinz");
		sNet.getAnalysisContext().setSubjectDescriptor("tOut", "Becker");
		
		
		return sNet;
		
		
	}
	
}
