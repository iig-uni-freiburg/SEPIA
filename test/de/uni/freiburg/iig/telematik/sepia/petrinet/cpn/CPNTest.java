package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.FiringRule;

/**
 * @author boehr
 */
public class CPNTest {

	/* Tests whether the right transitions are enabled */
	@Test
	public void testEnablednessCPN() {

		// Create the standard cpn
		CPN cpn = createCPN();

		// check whether the right transition (t0) is enabled
		for (CPNTransition t : cpn.getEnabledTransitions()) {
			assertEquals("t0", t.getName());
		}

		// fire t0 and see whether the right transition T1 gets enabled afterwards
		try {
			cpn.fire("t0");
		} catch (ParameterException e) {
			fail("t0 cannot be fired!");
			e.printStackTrace();
		} catch (PNException e) {
			fail("t0 cannot be fired!");
			e.printStackTrace();
		}

		for (CPNTransition t : cpn.getEnabledTransitions()) {
			assertEquals("t1", t.getName());
		}
	}

	/* Tests whether firecheck return the same marking as firing */
	@Test
	public void testFireCheckCPN() {

		// Create the standard cpn
		CPN cpn = createCPN();

		// Retrieve a marking via firecheck
		CPNMarking fireCheckMarking = null;
		try {
			fireCheckMarking = cpn.fireCheck("t0");

		} catch (ParameterException e1) {
			fail("fireCheck raised an exception!");
			e1.printStackTrace();
		} catch (PNException e1) {
			fail("fireCheck raised an exception!");
			e1.printStackTrace();
		}

		// fire t0 and see which marking is reached

		try {
			cpn.fire("t0");
		} catch (ParameterException e) {
			fail("fireing raised an exception!");
			e.printStackTrace();
		} catch (PNException e) {
			fail("fireing raised an exception!");
			e.printStackTrace();
		}
		CPNMarking fireMarking = cpn.getMarking();

		// check whether the markings are the same
		assertEquals(fireMarking, fireCheckMarking);
	}

	/* Check whether a valid net is reported as valid */
	@Test
	public void testCheckValidityValidNet() throws ParameterException {

		// Create the standard cpn
		CPN cpn = createCPN();

		// The standard net is valid
		try {
			cpn.checkValidity();
		} catch (PNValidationException e) {
			fail("The standart Net is valid!");
			e.printStackTrace();
		}
	}

	/* Check whether a net with an unconnected transition is valid */
	@Test
	public void testCheckValidityUnconnectedTransition() throws ParameterException {

		// Create the standard cpn
		CPN cpn = createCPN();

		// Create a transition which does not consumes or produce a token
		try {
			cpn.addTransition("t2");
		} catch (ParameterException e) {
			fail("Cannot add transition to CPN!");
			e.printStackTrace();
		}

		// //The net should not be valid now!
		// try {
		// //cpn.addFlowRelationPT("p0", "t2");
		// cpn.checkValidity();
		// fail("The Net is not valid!");
		// } catch (PNValidationException e) {}
	}

	/* Check whether a net with a transition which is only connected via one arc is valid */
	@Test
	public void testCheckValidityOneArcTransition() throws ParameterException {

		// Create the standard cpn
		CPN cpn = createCPN();

		// Create a transition which does not consumes or produce a token
		try {
			cpn.addTransition("t2");
		} catch (ParameterException e) {
			fail("Cannot add transition to CPN!");
			e.printStackTrace();
		}

		// The net should not be valid now!
		try {
			cpn.addFlowRelationPT("p0", "t2");
			cpn.checkValidity();
			fail("The Net is not valid!");
		} catch (PNValidationException e) {
		}
	}

	/* Check whether the standard net is sound */
	@Test
	public void testCheckSoundnessNet() throws ParameterException {

		// Create the standard cpn
		CPN cpn = createCPN();

		try {
			cpn.checkSoundness();
		} catch (PNValidationException e) {
			fail("The standart net is sound!");
			e.printStackTrace();
		} catch (PNSoundnessException e) {
			fail("The standart net is sound!");
			e.printStackTrace();
		}
	}

	/* Find out the colors in the CPN */
	@Test
	public void testGetTokenColors() throws ParameterException {

		// Create the standard cpn
		CPN cpn = createCPN();
		Set<String> returnedColors = cpn.getTokenColors();

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
		// Create the standard cpn
		CPN cpn = createCPN();

		// PT
		// Adding this flow relation should raise an exception since the place does not exist
		try {
			cpn.addFlowRelationPT("p_false", "t0", true);
			fail("A flow relation from a non existing place got added!");
		} catch (ParameterException e) {
		}

		// The following flow relation is added twice.
		// It should change nothing when the relation is added a second time
		cpn.addFlowRelationPT("p0", "t0", true);
		HashSet<CPNFlowRelation> relationsBeforeSecondAdd = new HashSet<CPNFlowRelation>(cpn.getFlowRelations());
		cpn.addFlowRelationPT("p0", "t0", true);
		HashSet<CPNFlowRelation> relationsAfterSecondAdd = new HashSet<CPNFlowRelation>(cpn.getFlowRelations());
		assertEquals(relationsBeforeSecondAdd, relationsAfterSecondAdd);

		// TP
		// Adding this flow relation should raise an exception since the place does not exist
		try {
			cpn.addFlowRelationTP("t_false", "p0", true);
			fail("A flow relation from a non existing transition got added!");
		} catch (ParameterException e) {
		}

		// The following flow relation is added twice.
		// It should change nothing when the relation is added a second time
		cpn.addFlowRelationTP("t0", "p0", true);
		relationsBeforeSecondAdd = new HashSet<CPNFlowRelation>(cpn.getFlowRelations());
		cpn.addFlowRelationTP("t0", "p0", true);
		relationsAfterSecondAdd = new HashSet<CPNFlowRelation>(cpn.getFlowRelations());
		assertEquals(relationsBeforeSecondAdd, relationsAfterSecondAdd);

		// Add a flow relation without any constraints
		cpn.addFlowRelationTP("t0", "p0");
		try {
			cpn.checkValidity();
			fail("A cpn with a flow relation which does not produces or consume any token is considered valid!");
		} catch (PNValidationException e) {
		}
	}

	/* Test setting firing rules via transitions and not via flow relations */
	@Test
	public void testAddFiringRule() throws ParameterException {

		// Create the standard cpn
		CPN cpn = createCPN();

		// Add a firing rule to transition t0
		// which requires more tokens in input places
		FiringRule fr = new FiringRule();
		fr.addProduction("p1", "blue", 2);
		cpn.addFiringRule("t0", fr);

		// check whether the flow relation got updated
		for (CPNFlowRelation f : cpn.getFlowRelations()) {
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
		cpn.addFiringRule("t0", fr2);

		// check whether the flow relation got updated
		for (CPNFlowRelation f : cpn.getFlowRelations()) {
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

	/* Test whether a non effective relation is detected */
	@Test
	public void testRelationEffectiveness() throws ParameterException {
		// Create the standard cpn
		CPN cpn = createCPN();

		@SuppressWarnings("unused")
		CPNFlowRelation f = cpn.addFlowRelationPT("p0", "t1");

		try {
			cpn.checkValidity();
			fail("A non effective relation was not detected.");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testCPNClone() {
		/*
		 * Test equal CPNs
		 */
		CPN cpn1 = createCPN();
		CPN cpn1clone = (CPN) cpn1.clone();
		assertNotSame(cpn1, cpn1clone);
		// Can't just test equality because of the different order of same lists.
		// assertEquals(cpn1, cpn1clone);
		// Check equality for places
		assertEquals(cpn1.getPlaces().size(), cpn1clone.getPlaces().size());
		for (CPNPlace p : cpn1.getPlaces()) {
			assertTrue(cpn1clone.getPlace(p.getName()) != null);
			assertEquals(p, cpn1clone.getPlace(p.getName()));
			assertNotSame(p, cpn1clone.getPlace(p.getName()));
		}
		// Check equality for transitions
		assertEquals(cpn1.getTransitions().size(), cpn1clone.getTransitions().size());
		for (CPNTransition t : cpn1.getTransitions()) {
			assertTrue(cpn1clone.getTransition(t.getName()) != null);
			assertEquals(t, cpn1clone.getTransition(t.getName()));
			assertNotSame(t, cpn1clone.getTransition(t.getName()));
		}
		// Check equality for flow relations
		assertEquals(cpn1.getFlowRelations().size(), cpn1clone.getFlowRelations().size());
		for (CPNFlowRelation f : cpn1.getFlowRelations()) {
			CPNFlowRelation flowRelationClone = null;
			for (CPNFlowRelation fc : cpn1clone.getFlowRelations()) {
				if (fc.getSource().equals(f.getSource()) && fc.getTarget().equals(f.getTarget()) && fc.getDirectionPT() == f.getDirectionPT() && fc.getConstraint().equals(f.getConstraint())) {
					flowRelationClone = fc;
				}
			}
			assertFalse(flowRelationClone == null);
			assertEquals(f, flowRelationClone);
			assertNotSame(f, flowRelationClone);
		}
		// Check equality for the initial marking
		assertEquals(cpn1.getInitialMarking(), cpn1clone.getInitialMarking());
		assertNotSame(cpn1.getInitialMarking(), cpn1clone.getInitialMarking());
	}

	/*
	 * <p>
	 * Creates the standard cpn.
	 * </p>
	 * <p>
	 * The standard cpn has four places and two transitions.<br/>
	 * 	              -> p1<br/>
	 *              /       \<br/>
	 *    p0 -> T0-|         |-> T1 -> p3<br/>
	 *              \       /<br/>
	 *               <- p1<-</p>
	 * </p>
	 * <ul>
	 * <li>p0: red, black</li>
	 * <li>p2: green</li>
	 * </ul>
	 */
	private CPN createCPN() {

		CPN cpn = null;

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
			CPNMarking marking = new CPNMarking();
			marking.set("p0", mset);

			Multiset<String> mset2 = new Multiset<String>();
			mset2.add("green");
			marking.set("p2", mset2);

			// create the cpn with all tokens in P0
			cpn = new CPN(places, transitions, marking);

			// Add the flow relation
			CPNFlowRelation f1 = cpn.addFlowRelationPT("p0", "t0", true);
			CPNFlowRelation f2 = cpn.addFlowRelationTP("t0", "p1", true);
			CPNFlowRelation f3 = cpn.addFlowRelationPT("p1", "t1", true);
			@SuppressWarnings("unused")
			CPNFlowRelation f4 = cpn.addFlowRelationTP("t1", "p3", true);
			CPNFlowRelation f5 = cpn.addFlowRelationTP("t1", "p2", true);
			CPNFlowRelation f6 = cpn.addFlowRelationPT("p2", "t0", true);

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

		return cpn;
	}
}
