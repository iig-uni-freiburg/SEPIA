/**
 * 
 */
package petrinet.ifnet.test;

import java.util.Arrays;

import petrinet.ifnet.AccessMode;
import petrinet.ifnet.AnalysisContext;
import petrinet.ifnet.DeclassificationTransition;
import petrinet.ifnet.Labeling;
import petrinet.ifnet.RegularIFNetTransition;
import petrinet.ifnet.IFNet;
import petrinet.ifnet.IFNetFlowRelation;
import petrinet.ifnet.IFNetMarking;
import petrinet.ifnet.IFNetPlace;
import petrinet.ifnet.SecurityLevel;
import types.Multiset;
import validate.ParameterException;

/**
 * Methods often used during testing of SNets.
 * @author boehr
 *
 */
public class IFNetTestUtil {

	
	//Create a SNet with just three transition.
	//The transition t0 has two input and two output places.
	//The transition t0 consumes black from p0 and p1.
	//The transition t0 consumes green from p0.
	//The transition t0 consumes red from p1.
	//The transition t0 produces green in p2.
	//The transition t0 produces blue in p3.
	//
	//The transition tIn has one input and two output places.
	//The transition tIn consumes black from pIn.
	//The transition tIn produces green in p0.
	//The transition tIn produces red in p1.
	//The transition tIn produces black in p0 an p1.
	//
	//The transition tOut has two input and one output places.
	//The transition tOut consumes black from p2 and p3.
	//The transition tOut consumes green from p2.
	//The transition tOut consumes blue from p3.
	//The transition tOut produces black in pOut.		
	//
	//t0 ==>
	//processes green
	//deletes red
	//creates blue
	
	public static IFNet createSimpleSnet() throws ParameterException{
			
		
		//create the SNet
		IFNet sNet = new IFNet();				
		
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
		
		IFNetMarking sm = new IFNetMarking();
		sm.set("pIn", pInMarking);
		sNet.setInitialMarking(sm);
		
		
		
		//add the transitions
		sNet.addTransition("tIn");
		sNet.addTransition("t0");
		sNet.addTransition("tOut");
		
		//add flowrelations
		IFNetFlowRelation f1 = sNet.addFlowRelationPT("p0", "t0");
		IFNetFlowRelation f2 = sNet.addFlowRelationPT("p1", "t0");
		IFNetFlowRelation f3 = sNet.addFlowRelationTP("t0", "p2");
		IFNetFlowRelation f4 = sNet.addFlowRelationTP("t0", "p3");
		
		IFNetFlowRelation f5 = sNet.addFlowRelationPT("pIn", "tIn");
		IFNetFlowRelation f6 = sNet.addFlowRelationTP("tIn", "p0");
		IFNetFlowRelation f7 = sNet.addFlowRelationTP("tIn", "p1");
		
		IFNetFlowRelation f8 = sNet.addFlowRelationPT("p2", "tOut");
		IFNetFlowRelation f9 = sNet.addFlowRelationPT("p3", "tOut");
		IFNetFlowRelation f10 = sNet.addFlowRelationTP("tOut", "pOut");
		
	
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
		RegularIFNetTransition rst = (RegularIFNetTransition)sNet.getTransition("t0");
		rst.addAccessMode("green", AccessMode.READ);
		rst.addAccessMode("red", AccessMode.DELETE);
		rst.addAccessMode("blue", AccessMode.CREATE);
		
		
		//create labeling
		Labeling l = new Labeling(sNet, Arrays.asList("S1", "S2", "S3"));
		sNet.getAnalysisContext().setLabeling(l);
		
		//add subjcet descriptors
		sNet.getAnalysisContext().setSubjectDescriptor("tIn", "S1");
		sNet.getAnalysisContext().setSubjectDescriptor("t0", "S2");
		sNet.getAnalysisContext().setSubjectDescriptor("tOut", "S3");
		
		
		return sNet;
		
		
	}
	
	
	public static IFNet createSimpleSnetWithDeclassificationNoAC() throws ParameterException{
		return createSimpleSnetWithDeclassification(false);
	}

	//creates the same SNet as createSimpleSnet but with a declassification transition
	public static IFNet createSimpleSnetWithDeclassification() throws ParameterException{
		return createSimpleSnetWithDeclassification(true);
	}

		
	
	//creates the same SNet as createSimpleSnet but with a declassification transition
	public static IFNet createSimpleSnetWithDeclassification(boolean setAnalysisContext) throws ParameterException{
		
		IFNet simpleSNet = createSimpleSnet(); 
		
		
		//create additional transitions
		simpleSNet.addDeclassificationTransition("td");		
		simpleSNet.addTransition("t1");
		
		//create one additional place
		simpleSNet.addPlace("p4");
		simpleSNet.getPlace("p4").setColorCapacity("black", 1);
		simpleSNet.getPlace("p4").setColorCapacity("yellow", 1);
		
		//add flowrelations
		IFNetFlowRelation f11 = simpleSNet.addFlowRelationPT("p1", "td");
		IFNetFlowRelation f12 = simpleSNet.addFlowRelationTP("td", "p4");
		IFNetFlowRelation f13 = simpleSNet.addFlowRelationPT("p4", "t1");
		IFNetFlowRelation f14 = simpleSNet.addFlowRelationTP("t1", "pOut");
		
		//add constraints
		f11.addConstraint("red", 1); 
		f12.addConstraint("yellow", 1);
		f13.addConstraint("yellow", 1);
		//f14.addConstraint("black", 1);
		
		if(setAnalysisContext){
		
		//create labeling
		Labeling l = new Labeling(simpleSNet, Arrays.asList("sh0", "sh1", "sh2", "sh3", "sl0"));
		
		//Set subject clearence
		l.setSubjectClearance("sh0",SecurityLevel.HIGH);
		l.setSubjectClearance("sh1",SecurityLevel.HIGH);
		l.setSubjectClearance("sh2",SecurityLevel.HIGH);
		l.setSubjectClearance("sh3",SecurityLevel.HIGH);		
		l.setSubjectClearance("sl0",SecurityLevel.LOW);
		
		//set transition classification
		l.setActivityClassification("tIn", SecurityLevel.HIGH);
		l.setActivityClassification("t0", SecurityLevel.HIGH); 
		l.setActivityClassification("tOut", SecurityLevel.HIGH);
		l.setActivityClassification("td", SecurityLevel.HIGH);
		l.setActivityClassification("t1", SecurityLevel.LOW);
		
		//set token color classification
		l.setAttributeClassification("green", SecurityLevel.HIGH);
		l.setAttributeClassification("red", SecurityLevel.HIGH);
		l.setAttributeClassification("blue", SecurityLevel.HIGH);
		l.setAttributeClassification("yellow", SecurityLevel.LOW);
		
		//Create a new analysis context
		AnalysisContext ac = new AnalysisContext();
		ac.setLabeling(l);
		
		//Assign subjects to transitions
		ac.setSubjectDescriptor("tIn", "sh0");
		ac.setSubjectDescriptor("t0", "sh1");
		ac.setSubjectDescriptor("tOut", "sh2");
		ac.setSubjectDescriptor("td", "sh3");
		ac.setSubjectDescriptor("t1", "sl0");
				
		
		//set the labeling
		simpleSNet.setAnalysisContext(ac);
		}
		
		return simpleSNet;
		
	}
	
	
	
}
