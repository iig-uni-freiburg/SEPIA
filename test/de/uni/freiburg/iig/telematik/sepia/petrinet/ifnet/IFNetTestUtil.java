package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import java.util.Arrays;

import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;

/**
 * Methods often used during testing of SNets.
 * 
 * @author boehr
 */
public class IFNetTestUtil {

	// Create a SNet with just three transition.
	// The transition t0 has two input and two output places.
	// The transition t0 consumes black from p0 and p1.
	// The transition t0 consumes green from p0.
	// The transition t0 consumes red from p1.
	// The transition t0 produces green in p2.
	// The transition t0 produces blue in p3.
	//
	// The transition tIn has one input and two output places.
	// The transition tIn consumes black from pIn.
	// The transition tIn produces green in p0.
	// The transition tIn produces red in p1.
	// The transition tIn produces black in p0 an p1.
	//
	// The transition tOut has two input and one output places.
	// The transition tOut consumes black from p2 and p3.
	// The transition tOut consumes green from p2.
	// The transition tOut consumes blue from p3.
	// The transition tOut produces black in pOut.
	//
	// t0 ==>
	// processes green
	// deletes red
	// creates blue
	
	public static IFNet createSimpleIFNet() {
		return createSimpleIFNet(true);
	}

	public static IFNet createSimpleIFNet(boolean setAnalysisContext) {

		// create the SNet
		IFNet ifNet = new IFNet();

		// add places with marking and capacity
		ifNet.addPlace("pIn");
		ifNet.getPlace("pIn").setColorCapacity("black", 1);

		ifNet.addPlace("p0");
		ifNet.getPlace("p0").setColorCapacity("black", 1);
		ifNet.getPlace("p0").setColorCapacity("green", 1);

		ifNet.addPlace("p1");
		ifNet.getPlace("p1").setColorCapacity("black", 1);
		ifNet.getPlace("p1").setColorCapacity("red", 1);

		ifNet.addPlace("p2");
		ifNet.getPlace("p2").setColorCapacity("black", 1);
		ifNet.getPlace("p2").setColorCapacity("green", 1);

		ifNet.addPlace("p3");
		ifNet.getPlace("p3").setColorCapacity("black", 1);
		ifNet.getPlace("p3").setColorCapacity("blue", 1);

		ifNet.addPlace("pOut");
		ifNet.getPlace("pOut").setColorCapacity("black", 1);

		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");

		IFNetMarking sm = new IFNetMarking();
		sm.set("pIn", pInMarking);
		ifNet.setInitialMarking(sm);

		// add the transitions
		ifNet.addTransition("tIn");
		ifNet.addTransition("t0");
		ifNet.addTransition("tOut");

		// add flowrelations
		IFNetFlowRelation f1 = ifNet.addFlowRelationPT("p0", "t0");
		IFNetFlowRelation f2 = ifNet.addFlowRelationPT("p1", "t0");
		IFNetFlowRelation f3 = ifNet.addFlowRelationTP("t0", "p2");
		IFNetFlowRelation f4 = ifNet.addFlowRelationTP("t0", "p3");

		@SuppressWarnings("unused")
		IFNetFlowRelation f5 = ifNet.addFlowRelationPT("pIn", "tIn");
		IFNetFlowRelation f6 = ifNet.addFlowRelationTP("tIn", "p0");
		IFNetFlowRelation f7 = ifNet.addFlowRelationTP("tIn", "p1");

		IFNetFlowRelation f8 = ifNet.addFlowRelationPT("p2", "tOut");
		IFNetFlowRelation f9 = ifNet.addFlowRelationPT("p3", "tOut");
		@SuppressWarnings("unused")
		IFNetFlowRelation f10 = ifNet.addFlowRelationTP("tOut", "pOut");

		// configure flow relations
		f1.addConstraint("green", 1);
		f2.addConstraint("red", 1);
		f3.addConstraint("green", 1);
		f4.addConstraint("blue", 1);

		// f5.addConstraint("black", 1);
		f6.addConstraint("green", 1);
		f7.addConstraint("red", 1);

		f8.addConstraint("green", 1);
		f9.addConstraint("blue", 1);
		// f10.addConstraint("black", 1);

		// configure read write
		RegularIFNetTransition rst = (RegularIFNetTransition) ifNet.getTransition("t0");
		rst.addAccessMode("green", AccessMode.READ);
		rst.addAccessMode("red", AccessMode.DELETE);
		rst.addAccessMode("blue", AccessMode.CREATE);

		if (setAnalysisContext) {
			// create labeling
			SOABase context = new SOABase("");
			context.setSubjects(Arrays.asList("S1", "S2", "S3"));
			context.setActivities(Arrays.asList("tIn", "t0", "tOut"));
			context.setObjects(Arrays.asList("green", "red", "blue", "black"));
			ifNet.setAnalysisContext(new AnalysisContext(new Labeling(context)));
	
			// add subject descriptors
			ifNet.getAnalysisContext().setSubjectDescriptor("tIn", "S1");
			ifNet.getAnalysisContext().setSubjectDescriptor("t0", "S2");
			ifNet.getAnalysisContext().setSubjectDescriptor("tOut", "S3");
		}

		return ifNet;
	}

	public static IFNet createSimpleIFNetWithDeclassificationNoAC() {
		return createSimpleIFNetWithDeclassification(false);
	}

	// creates the same SNet as createSimpleSnet but with a declassification transition
	public static IFNet createSimpleIFNetWithDeclassification() {
		return createSimpleIFNetWithDeclassification(true);
	}

	// creates the same SNet as createSimpleSnet but with a declassification transition
	@SuppressWarnings("unused")
	public static IFNet createSimpleIFNetWithDeclassification(boolean setAnalysisContext) {

		IFNet simpleSNet = createSimpleIFNet(setAnalysisContext);

		if (setAnalysisContext) {
			// create labeling
			SOABase context = new SOABase("");
			context.setSubjects(Arrays.asList("sh0", "sh1", "sh2", "sh3", "sl0"));
			context.setObjects(Arrays.asList("green", "red", "blue", "yellow", "black"));
			context.setActivities(Arrays.asList("tIn", "t0", "tOut", "td", "td2", "t1", "connector"));
			Labeling l = new Labeling(context);

			// Set subject clearance
			l.setSubjectClearance("sh0", SecurityLevel.HIGH);
			l.setSubjectClearance("sh1", SecurityLevel.HIGH);
			l.setSubjectClearance("sh2", SecurityLevel.HIGH);
			l.setSubjectClearance("sh3", SecurityLevel.HIGH);
			l.setSubjectClearance("sl0", SecurityLevel.LOW);

			// set transition classification
			l.setActivityClassification("tIn", SecurityLevel.HIGH);
			l.setActivityClassification("t0", SecurityLevel.HIGH);
			l.setActivityClassification("tOut", SecurityLevel.HIGH);
			l.setActivityClassification("td", SecurityLevel.HIGH);
			l.setActivityClassification("t1", SecurityLevel.LOW);

			// set token color classification
			l.setAttributeClassification("green", SecurityLevel.HIGH);
			l.setAttributeClassification("red", SecurityLevel.HIGH);
			l.setAttributeClassification("blue", SecurityLevel.HIGH);
			l.setAttributeClassification("yellow", SecurityLevel.LOW);

			// Create a new analysis context
			AnalysisContext ac = new AnalysisContext(l);

			// Assign subjects to transitions
			ac.setSubjectDescriptor("tIn", "sh0");
			ac.setSubjectDescriptor("t0", "sh1");
			ac.setSubjectDescriptor("tOut", "sh2");
			ac.setSubjectDescriptor("td", "sh3");
			ac.setSubjectDescriptor("t1", "sl0");

			// set the labeling
			simpleSNet.setAnalysisContext(ac);
		}

		// create additional transitions
		simpleSNet.addDeclassificationTransition("td");
		simpleSNet.addTransition("t1");

		// create one additional place
		simpleSNet.addPlace("p4");
		simpleSNet.getPlace("p4").setColorCapacity("black", 1);
		simpleSNet.getPlace("p4").setColorCapacity("yellow", 1);

		// add flowrelations
		IFNetFlowRelation f11 = simpleSNet.addFlowRelationPT("p1", "td");
		IFNetFlowRelation f12 = simpleSNet.addFlowRelationTP("td", "p4");
		IFNetFlowRelation f13 = simpleSNet.addFlowRelationPT("p4", "t1");
		IFNetFlowRelation f14 = simpleSNet.addFlowRelationTP("t1", "pOut");

		// add constraints
		f11.addConstraint("red", 1);
		f12.addConstraint("yellow", 1);
		f13.addConstraint("yellow", 1);
		// f14.addConstraint("black", 1);

		return simpleSNet;
	}
}
