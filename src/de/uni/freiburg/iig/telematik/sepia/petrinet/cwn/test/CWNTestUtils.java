package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.test;

import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;

public class CWNTestUtils {
	
    //Creates the standard cwn.
	//The standard cpn has four places and four transitions.
	//                	 -> t1
	//              	/       \
	//    p0 -> T0-> p1|         p2 -> t3 -> p3
	//              	\       /
	//               	 <- t2<-
	// 
	//p0: black
	static CWN createValidCWN() {

		CWN cwn = null;

		try {
			// Create places
			Set<String> places = new HashSet<String>();
			places.add("p0");
			places.add("p1");
			places.add("p2");
			places.add("p3");

			// create transitions
			Set<String> transitions = new HashSet<String>();
			transitions.add("t0");
			transitions.add("t1");
			transitions.add("t2");
			transitions.add("t3");

			// create the the token colors used in the initial marking
			Multiset<String> mset = new Multiset<String>();
			mset.add("black");
			CWNMarking marking = new CWNMarking();
			marking.set("p0", mset);

			// create the cwn with one black token in P0
			cwn = new CWN(places, transitions, marking);

			// Set bounds for all places
			// p0 contains only black
			cwn.getPlace("p0").setColorCapacity("black", 2);
			// p1 contains black and green
			cwn.getPlace("p1").setColorCapacity("black", 2);
			cwn.getPlace("p1").setColorCapacity("green", 2);
			// p2 contains black and red
			cwn.getPlace("p2").setColorCapacity("black", 2);
			cwn.getPlace("p2").setColorCapacity("red", 2);
			// p3 contains black
			cwn.getPlace("p3").setColorCapacity("black", 2);

			// Add the flow relation
			@SuppressWarnings("unused")
			CWNFlowRelation f1 = cwn.addFlowRelationPT("p0", "t0", true);
			CWNFlowRelation f2 = cwn.addFlowRelationTP("t0", "p1", true);
			CWNFlowRelation f3 = cwn.addFlowRelationPT("p1", "t1", true);
			CWNFlowRelation f4 = cwn.addFlowRelationTP("t1", "p2", true);
			CWNFlowRelation f5 = cwn.addFlowRelationPT("p2", "t2", true);
			CWNFlowRelation f6 = cwn.addFlowRelationTP("t2", "p1", true);
			CWNFlowRelation f7 = cwn.addFlowRelationPT("p2", "t3", true);
			@SuppressWarnings("unused")
			CWNFlowRelation f8 = cwn.addFlowRelationTP("t3", "p3", true);

			// configure flow reltions
			f2.addConstraint("green", 1);
			f3.addConstraint("green", 1);
			f4.addConstraint("red", 1);
			f5.addConstraint("red", 1);
			f6.addConstraint("green", 1);
			f7.addConstraint("red", 1);

		} catch (ParameterException e) {
			e.printStackTrace();
		}

		return cwn;
	}

	//Creates a cwn with two output places
	//by adding a place to the standard cwn
	//                	 -> t1
	//              	/       \
	//    p0 -> T0-> p1|         p2 -> t3 -> p3
	//              	\       /       \
	//              	 <- t2<-          ->p5
	// 
	//p0: black
	static void addSecondOutputPlaceP5(CWN origCWN) {

		try {
			// Add place
			origCWN.addPlace("p5");

			// Add flow relation
			@SuppressWarnings("unused")
			CWNFlowRelation f10 = origCWN.addFlowRelationTP("t3", "p5", true);

		} catch (ParameterException e) {
			e.printStackTrace();
		}

	}

    //Creates a cwn with two input places
	//by adding a place to the standard cwn
	//                	 -> t1
	//              	/       \
	//    p0 -> T0-> p1|         p2 -> t3 -> p3
	//        /     	\       /       
	//     p4       	 <- t2<-          
	// 
	//p0: black
	static void addSecondInputPlaceP4(CWN origCWN) {

		try {
			// Add place
			origCWN.addPlace("p4");

			// Add two flow relation
			@SuppressWarnings("unused")
			CWNFlowRelation f9 = origCWN.addFlowRelationPT("p4", "t0", true);

			// put a black token in p4
			Multiset<String> mp4 = new Multiset<String>();
			mp4.add("black");
			origCWN.getMarking().set("p4", mp4);

		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}

	/* Creates a petri net which does not create a token in the sink place */
	static void removeBlackFromRelationT3P3(CWN origCWN) throws ParameterException {

		for (CWNFlowRelation f : origCWN.getFlowRelations()) {
			if (f.getSource().getName().equals("t3") && f.getTarget().getName().equals("p3")) {
				Multiset<String> constraint = f.getConstraint();
				constraint.remove("black");
				constraint.add("pink");
				f.setConstraint(constraint);
			}
		}
	}

	/* Creates a petri net which contains a livelock. */
	static void addLiveLock(CWN origCWN) throws ParameterException {

		// add the places
		origCWN.addPlace("pl1");
		origCWN.addPlace("pl2");

		// add transitions
		origCWN.addTransition("tl1");
		origCWN.addTransition("tl2");
		origCWN.addTransition("tConncet");

		// add the flow relations connecting the
		// places and transitions
		origCWN.addFlowRelationPT("pl1", "tl2");
		origCWN.addFlowRelationPT("pl2", "tl1");
		origCWN.addFlowRelationTP("tl2", "pl2");
		origCWN.addFlowRelationTP("tl1", "pl1");

		// connect the livelock to the rest of the net
		CWNFlowRelation f1 = origCWN.addFlowRelationPT("p2", "tConncet", true);
		f1.addConstraint("red", 1);
		@SuppressWarnings("unused")
		CWNFlowRelation f2 = origCWN.addFlowRelationTP("tConncet", "pl2", true);
	}

	/* Adds a dead transition to the standard CWN */
	static void addDeadTransition(CWN origCWN) throws ParameterException {

		// Add the transition
		origCWN.addTransition("Tdead");

		// connect the transition to place p1 and p2 i.e.:
		// p1 --[black,pink]--> Tdead --[black,pink]--> p2
		CWNFlowRelation f1 = origCWN.addFlowRelationPT("p1", "Tdead", true);
		CWNFlowRelation f2 = origCWN.addFlowRelationTP("Tdead", "p2", true);

		// configure the flow relations
		f1.addConstraint("pink", 1);
		f2.addConstraint("pink", 1);
	}

	/* Removes place p0 from the standard cwn and conncts t0 to p1 (as input place). */
	static void removeInputPlace(CWN origCWN) throws ParameterException {
		// remove p0
		origCWN.removePlace("p0");

		// Connect p1 and t0
		origCWN.addFlowRelationPT("p1", "t0", true);
	}

	/* Removes place p3 from the standard cwn and conncts t3 to p2 (as input place). */
	static void removeOutputPlace(CWN origCWN) throws ParameterException {
		// remove p3
		origCWN.removePlace("p3");

		// Connect p1 and t0
		origCWN.addFlowRelationTP("t3", "p2", true);
	}

	/* Sets an invalid initial marking ==> no token in the input place */
	static void removeTokensFromInputPlace(CWN origCWN) throws ParameterException {
		// get the initial marking and clear p0
		CWNMarking m = origCWN.getInitialMarking();

		// remove p3
		Multiset<String> state = origCWN.getPlace("p0").getState();
		state.clear();
		m.set("p0", state);

		// set the new marking
		origCWN.setInitialMarking(m);
	}

	/* Sets an invalid initial marking ==> no black token in the input place but a colored tokens */
	static void addColoredTokensToInputPlace(CWN origCWN, int amount, String color) throws ParameterException {

		// add a colored token
		// get the initial marking
		CWNMarking m = origCWN.getInitialMarking();

		// remove p3
		Multiset<String> state = origCWN.getPlace("p0").getState();

		for (int i = 1; i <= amount; i++) {
			state.add(color);
		}
		m.set("p0", state);

		// set the new marking
		origCWN.setInitialMarking(m);
	}

	/* Adds an unconnected transition to the net i.e. a transition which is connected to new places */
	static void addCompletelyUnconectedTransition(CWN origCWN) throws ParameterException {
		// add the transition
		origCWN.addTransition("TUnconnceted");

		// add places
		origCWN.addPlace("pUnconncetedInput");
		origCWN.addPlace("pUnconncetedOutput");

		// add a token to the input place
		CWNMarking initialMarking = origCWN.getInitialMarking();
		initialMarking.set("pUnconncetedInput", new Multiset<String>("black"));

		// add flow relations
		origCWN.addFlowRelationPT("pUnconncetedInput", "TUnconnceted", true);
		origCWN.addFlowRelationTP("TUnconnceted", "pUnconncetedInput", true);
	}

	/*
	 * create a simple cwn with one inputplace, one output place and one transition<br/>
	 * pIn --black--> t0 --black-->pOut
	 */
	static CWN createSimpleCWN() throws ParameterException {
		// create the two places
		Set<String> places = new HashSet<String>();
		places.add("pIn");
		places.add("pOut");

		// create transition
		Set<String> transitions = new HashSet<String>();
		transitions.add("t0");

		// create the the token colors used in the initial marking
		Multiset<String> mset = new Multiset<String>();
		mset.add("black");
		CWNMarking marking = new CWNMarking();
		marking.set("pIn", mset);

		// create the cwn with one black token in P0
		CWN cwn = new CWN(places, transitions, marking);

		// Add the flow relation
		@SuppressWarnings("unused")
		CWNFlowRelation inRel = cwn.addFlowRelationPT("pIn", "t0", true);
		@SuppressWarnings("unused")
		CWNFlowRelation outRel = cwn.addFlowRelationTP("t0", "pOut", true);

		// Set bounds for all places
		// p0 contains only black
		cwn.getPlace("pIn").setColorCapacity("black", 2);
		cwn.getPlace("pOut").setColorCapacity("black", 2);

		return cwn;
	}
}
