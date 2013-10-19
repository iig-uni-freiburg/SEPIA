package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.FiringRule;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNTransition;

/**
 * @author boehr
 */
public class CWNTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/* Check whether a net with an unconnected transition is valid */
	@Test
	public void testCheckValidityUnconnectedTransition() throws ParameterException {

		// Create the standard cwn
		CWN cwn = createCWN();

		// Create a transition which does not consumes or produce a token
		try {
			cwn.addTransition("t2");
		} catch (ParameterException e) {
			fail("Cannot add transition to CWN!");
			e.printStackTrace();
		}

		// //The net should not be valid now!
		// try {
		// //cwn.addFlowRelationPT("p0", "t2");
		// cwn.checkValidity();
		// fail("The Net is not valid!");
		// } catch (PNValidationException e) {}
	}

	/* Check whether a net with a transition which is only connected via one arc is valid */
	@Test
	public void testCheckValidityOneArcTransition() throws ParameterException {

		// Create the standard cwn
		CWN cwn = createCWN();

		// Create a transition which does not consumes or produce a token
		try {
			cwn.addTransition("t2");
		} catch (ParameterException e) {
			fail("Cannot add transition to CWN!");
			e.printStackTrace();
		}

		// The net should not be valid now!
		try {
			cwn.addFlowRelationPT("p0", "t2");
			cwn.checkValidity();
			fail("The Net is not valid!");
		} catch (PNValidationException e) {
		}
	}

	/* Find out the colors in the CWN */
	@Test
	public void testGetTokenColors() throws ParameterException {

		// Create the standard cwn
		CWN cwn = createCWN();
		Set<String> returnedColors = cwn.getTokenColors();

		// The net contains three colors red, black and green
		Set<String> realColors = new HashSet<String>();
		realColors.add("red");
		realColors.add("black");
		realColors.add("green");

		// Check whether the correct colors got returned
		assertEquals(realColors, returnedColors);

	}

	/* Test adding flow relations */
	@Test
	public void testAddFlowRelation() throws ParameterException {
		// Create the standard cwn
		CWN cwn = createCWN();

		// PT
		// Adding this flow relation should raise an exception since the place does not exist
		try {
			cwn.addFlowRelationPT("p_false", "t0", true);
			fail("A flow relation from a non existing place got added!");
		} catch (ParameterException e) {
		}

		// The following flow relation is added twice.
		// It should change nothing when the relation is added a second time
		cwn.addFlowRelationPT("p0", "t0", true);
		HashSet<CWNFlowRelation> relationsBeforeSecondAdd = new HashSet<CWNFlowRelation>(cwn.getFlowRelations());
		cwn.addFlowRelationPT("p0", "t0", true);
		HashSet<CWNFlowRelation> relationsAfterSecondAdd = new HashSet<CWNFlowRelation>(cwn.getFlowRelations());
		assertEquals(relationsBeforeSecondAdd, relationsAfterSecondAdd);

		// TP
		// Adding this flow relation should raise an exception since the place does not exist
		try {
			cwn.addFlowRelationTP("t_false", "p0", true);
			fail("A flow relation from a non existing transition got added!");
		} catch (ParameterException e) {
		}

		// The following flow relation is added twice.
		// It should change nothing when the relation is added a second time
		cwn.addFlowRelationTP("t0", "p0", true);
		relationsBeforeSecondAdd = new HashSet<CWNFlowRelation>(cwn.getFlowRelations());
		cwn.addFlowRelationTP("t0", "p0", true);
		relationsAfterSecondAdd = new HashSet<CWNFlowRelation>(cwn.getFlowRelations());
		assertEquals(relationsBeforeSecondAdd, relationsAfterSecondAdd);

		// Add a flow relation without any constraints
		cwn.addFlowRelationTP("t0", "p0");
		try {
			cwn.checkValidity();
			fail("A cpn with a flow relation which does not produces or consume any token is considered valid!");
		} catch (PNValidationException e) {
		}

	}

	/* Test setting firing rules via transitions and not via flow relations */
	@Test
	public void testaddFiringRule() throws ParameterException {

		// Create the standard cwn
		CWN cwn = createCWN();

		// Add a firing rule to transition t0
		// which requires more tokens in input places
		FiringRule fr = new FiringRule();
		fr.addProduction("p1", "blue", 2);
		cwn.addFiringRule("t0", fr);

		// check whether the flow relation got updated
		for (CWNFlowRelation f : cwn.getFlowRelations()) {
			String source = f.getSource().getName();
			String dest = f.getTarget().getName();

			// This is the flow relation which got changed by addFiringrule
			if ((source == "t0") && (dest == "p1")) {
				assertEquals(1, f.getConstraint("black"));
				assertEquals(1, f.getConstraint("red"));
				assertEquals(2, f.getConstraint("blue"));
			}
		}
		// ///////////////////////////

		// Add a firing rule to transition t0
		// which creates more tokens in outputplaces
		FiringRule fr2 = new FiringRule();
		fr2.addRequirement("p0", "blue", 3);
		cwn.addFiringRule("t0", fr2);

		// check whether the flow relation got updated
		for (CWNFlowRelation f : cwn.getFlowRelations()) {
			String source = f.getSource().getName();
			String dest = f.getTarget().getName();

			// This is the flow relation which got changed by addFiringrule
			if ((source == "p0") && (dest == "t1")) {
				assertEquals(1, f.getConstraint("black"));
				assertEquals(1, f.getConstraint("red"));
				assertEquals(3, f.getConstraint("blue"));
			}
		}

	}

	/*
	 * Test the method for checking validity<br /> Focus on the input output place related part of validity
	 */
	@Test
	public void testCWNValidityInputOutPutPlaces() throws ParameterException {

		// Create the standard cwn which is valid
		CWN validCwn = CWNTestUtils.createValidCWN();
		try {
			validCwn.checkValidity();
		} catch (PNValidationException e) {
			fail("A valid CWN is detected as invalid!");
		}

		// create a cwn with to many input places
		CWN invalidCwn1 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addSecondInputPlaceP4(invalidCwn1);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with two inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a cwn with to many output places
		CWN invalidCwn2 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addSecondOutputPlaceP5(invalidCwn2);
		try {
			invalidCwn2.checkValidity();
			fail("An ivalid CWN (with two inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a cwn with to many input and output places
		CWN invalidCwn3 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addSecondInputPlaceP4(invalidCwn3);
		CWNTestUtils.addSecondOutputPlaceP5(invalidCwn3);
		try {
			invalidCwn3.checkValidity();
			fail("An ivalid CWN (with two inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a CWN without an input place
		CWN invalidCwn4 = CWNTestUtils.createValidCWN();
		CWNTestUtils.removeInputPlace(invalidCwn4);

		try {
			invalidCwn4.checkValidity();
			fail("An ivalid CWN (without any inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a CWN without an output place
		CWN invalidCwn5 = CWNTestUtils.createValidCWN();
		CWNTestUtils.removeOutputPlace(invalidCwn5);
		try {
			invalidCwn5.checkValidity();
			fail("An ivalid CWN (without any outputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test the method for checking validity<br/> Focus on the marking related part of validity
	 */
	@Test
	public void testCWNValidityMarking() throws ParameterException {

		// Create the standard cwn which is valid
		CWN invalidCwn1 = CWNTestUtils.createValidCWN();

		// setup some markings used later on
		// black black in p0
		Multiset<String> placeStatebb = new Multiset<String>("black", "black");
		CWNMarking p0bb = new CWNMarking();
		p0bb.set("p0", placeStatebb);

		// black all empty in p0
		Multiset<String> placeStateEmpty = new Multiset<String>();
		CWNMarking empty = new CWNMarking();
		empty.set("p0", placeStateEmpty);

		// black in p0 and p3
		Multiset<String> placeStateP0P3 = new Multiset<String>("black");
		CWNMarking p0p3 = new CWNMarking();
		p0p3.set("p0", placeStateP0P3);
		p0p3.set("p3", placeStateP0P3);

		// black token in a place which is not the input place
		Multiset<String> placeStateb = new Multiset<String>("black");
		CWNMarking p3b = new CWNMarking();
		p3b.set("p3", placeStateb);

		// green token in an inputplace
		Multiset<String> placeStateGreen = new Multiset<String>("green");
		CWNMarking p0g = new CWNMarking();
		p0g.set("p0", placeStateGreen);

		// green and red token in an inputplace
		Multiset<String> placeStateGreenRed = new Multiset<String>("green", "red");
		CWNMarking p0gr = new CWNMarking();
		p0gr.set("p0", placeStateGreenRed);

		// All needed markings are set up here

		// Set two tokens to place p0
		invalidCwn1.setInitialMarking(p0bb);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with two black tokens in the input place) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// Set zero tokens to place p0
		invalidCwn1.setInitialMarking(empty);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with no tokens in the input place) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// Set one black token to p0 and one black token to p3
		invalidCwn1.setInitialMarking(p0p3);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with two black tokens in different places) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// Set one black token to p3
		invalidCwn1.setInitialMarking(p3b);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with one black token in a non input place) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// Set one green token to p0
		invalidCwn1.setInitialMarking(p0g);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with one green token in the input place) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// Set one green and one red token to p0
		invalidCwn1.setInitialMarking(p0gr);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with one green and one red token in the input place) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// change the petriNet to not produce black tokens in the output place
	}

	/*
	 * Test the method for checking validity<br/> Focus on the connectedness related part of validity
	 */
	@Test
	public void testCWNValidityConnectedness() throws ParameterException {

		// Create the standard cwn which is valid
		CWN invalidCwn1 = CWNTestUtils.createValidCWN();

		// Add a unconnceted transition
		CWNTestUtils.addCompletelyUnconectedTransition(invalidCwn1);

		// Check whether the cwn is valid
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (which is not strongly connected) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// Add a livelock
		CWN invalidCwn2 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addLiveLock(invalidCwn2);

		// Check whether the cwn is valid
		try {
			invalidCwn2.checkValidity();
			fail("An ivalid CWN (which is not strongly connected) is detected as valid!");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test the method for checking validity<br/> Focus on the boundedness related part of validity
	 */
	@Test
	public void testCWNValidityBoundedness() throws ParameterException {

		// Create the standard cwn which is valid
		CWN invalidCwn1 = CWNTestUtils.createValidCWN();

		// Make all places unbounded
		for (CWNPlace p : invalidCwn1.getPlaces()) {
			p.removeColorCapacity("black");
			p.removeColorCapacity("green");
			p.removeColorCapacity("red");

		}

		// Check whether the cwn is valid
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with all places beeing unbounded) is detected as valid!");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test the method for checking soundness<br/> Focus on the "option to complete" related part of soundness
	 */
	@Test(timeout = 1000)
	// i.e. 1 second
	public void testCWNSoundnessOptionToComplete() throws ParameterException, PNException {

		// Create the standard cwn which is sound
		CWN soundCwn1 = CWNTestUtils.createValidCWN();

		try {
			soundCwn1.checkSoundness();
		} catch (PNSoundnessException e) {
			fail("A sound CWN was reported to not be sound (PNSoundnessException)");
		} catch (PNValidationException e) {
			fail("A sound CWN was reported to not be sound (PNValidationException)");
		}

		// Create a cwn which does not create a black token in the sink place
		CWN unSoundCwn1 = CWNTestUtils.createValidCWN();
		CWNTestUtils.removeBlackFromRelationT3P3(unSoundCwn1);

		try {

			unSoundCwn1.checkSoundness();
			fail("A unsound CWN was not detected as unsound");
		} catch (PNSoundnessException e) {
		} catch (PNValidationException e) {
		}

		// Create a cwn which has a livelock and thus not the option to complete
		CWN unSoundCwn2 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addLiveLock(unSoundCwn2);

		try {
			unSoundCwn2.checkSoundness();
			fail("A unsound CWN was not detected as unsound");
		} catch (PNSoundnessException e) {
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test the method for checking soundness<br/> Focus on the "No Dead Transition" related part of soundness
	 */
	@Test(timeout = 1000)
	// i.e. 1 second
	public void testCWNSoundnessNoDeadTransition() throws ParameterException, PNException {

		// Create the standard cwn which is sound
		CWN unSoundCwn1 = CWNTestUtils.createValidCWN();

		// add a dead transition
		CWNTestUtils.addDeadTransition(unSoundCwn1);

		try {
			unSoundCwn1.checkSoundness();
			fail("A unsound CWN was not detected as unsound");
		} catch (PNSoundnessException e) {
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testCWNClone() {
		/*
		 * Test equal CWNs
		 */
		CWN cwn1 = CWNTestUtils.createValidCWN();
		CWN cwn1clone = (CWN) cwn1.clone();
		assertNotSame(cwn1, cwn1clone);
		// Can't just test equality because of the different order of same lists.
		// assertEquals(cpn1, cpn1clone);
		// Check equality for places
		assertEquals(cwn1.getPlaces().size(), cwn1clone.getPlaces().size());
		for (CWNPlace p : cwn1.getPlaces()) {
			assertTrue(cwn1clone.getPlace(p.getName()) != null);
			assertEquals(p, cwn1clone.getPlace(p.getName()));
			assertNotSame(p, cwn1clone.getPlace(p.getName()));
		}
		// Check equality for transitions
		assertEquals(cwn1.getTransitions().size(), cwn1clone.getTransitions().size());
		for (CWNTransition t : cwn1.getTransitions()) {
			assertTrue(cwn1clone.getTransition(t.getName()) != null);
			assertEquals(t, cwn1clone.getTransition(t.getName()));
			assertNotSame(t, cwn1clone.getTransition(t.getName()));
		}
		// Check equality for flow relations
		assertEquals(cwn1.getFlowRelations().size(), cwn1clone.getFlowRelations().size());
		for (CWNFlowRelation f : cwn1.getFlowRelations()) {
			CWNFlowRelation flowRelationClone = null;
			for (CWNFlowRelation fc : cwn1clone.getFlowRelations()) {
				if (fc.getSource().equals(f.getSource()) && fc.getTarget().equals(f.getTarget()) && fc.getDirectionPT() == f.getDirectionPT() && fc.getConstraint().equals(f.getConstraint())) {
					flowRelationClone = fc;
				}
			}
			assertFalse(flowRelationClone == null);
			assertEquals(f, flowRelationClone);
			assertNotSame(f, flowRelationClone);
		}
		// Check equality for the initial marking
		assertEquals(cwn1.getInitialMarking(), cwn1clone.getInitialMarking());
		assertNotSame(cwn1.getInitialMarking(), cwn1clone.getInitialMarking());
	}

	/*
	 * <p> Creates the standard cwn. </p> <p> The standard cwn has four places and two transitions.<br/> -> p1<br/> / \<br/> p0 -> T0-| |-> T1 -> p3<br/> \ /<br/> <- p1<-</p> </p> <ul> <li>p0: red, black</li> <li>p2: green</li> </ul>
	 */
	private CWN createCWN() {

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

			// create the the token colors used in the initial marking
			Multiset<String> mset = new Multiset<String>();
			mset.add("red");
			mset.add("black");
			CWNMarking marking = new CWNMarking();
			marking.set("p0", mset);

			Multiset<String> mset2 = new Multiset<String>();
			mset2.add("green");
			marking.set("p2", mset2);

			// create the cwn with all tokens in P0
			cwn = new CWN(places, transitions, marking);

			// Add the flow relation
			CWNFlowRelation f1 = cwn.addFlowRelationPT("p0", "t0", true);
			CWNFlowRelation f2 = cwn.addFlowRelationTP("t0", "p1", true);
			CWNFlowRelation f3 = cwn.addFlowRelationPT("p1", "t1", true);
			@SuppressWarnings("unused")
			CWNFlowRelation f4 = cwn.addFlowRelationTP("t1", "p3", true);
			CWNFlowRelation f5 = cwn.addFlowRelationTP("t1", "p2", true);
			CWNFlowRelation f6 = cwn.addFlowRelationPT("p2", "t0", true);

			// configure flow relation
			f1.addConstraint("red", 1);
			f2.addConstraint("red", 1);
			f3.addConstraint("red", 1);
			Multiset<String> s1 = new Multiset<String>();
			s1.add("green");
			f5.setConstraint(s1);
			Multiset<String> s2 = new Multiset<String>();
			s2.add("green");
			f6.setConstraint(s2);

		} catch (ParameterException e) {
			e.printStackTrace();
		}

		return cwn;
	}
}
