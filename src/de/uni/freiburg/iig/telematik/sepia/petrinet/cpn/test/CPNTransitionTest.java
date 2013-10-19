package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;

/**
 * These are the test cases for the class CPNTransition and AbstractCPNTransition.
 * 
 * @author boehr
 */
public class CPNTransitionTest {

	/* Tests the constructors */
	@Test
	public void testConstructors() {

		try {
			// Create a transition
			CPNTransition cpnTrans = new CPNTransition("T0");

			// Check that the name is stored correctly
			assertEquals("T0", cpnTrans.getName());

			// Check that the name and label are stored correctly
			cpnTrans = new CPNTransition("T0_Name", "T0_Label");
			assertEquals("T0_Name", cpnTrans.getName());
			assertEquals("T0_Label", cpnTrans.getLabel());

			// Check that name label and silent are set correctly
			cpnTrans = new CPNTransition("T0__Name", "T0__Label", false);
			assertEquals("T0__Name", cpnTrans.getName());
			assertEquals("T0__Label", cpnTrans.getLabel());
			assertFalse(cpnTrans.isSilent());
			cpnTrans.setSilent(true);
			assertTrue(cpnTrans.isSilent());

			// Check that name and silent are set correctly
			cpnTrans = new CPNTransition("T0___Name", false);
			assertEquals("T0___Name", cpnTrans.getName());
			assertFalse(cpnTrans.isSilent());
			cpnTrans.setSilent(true);
			assertTrue(cpnTrans.isSilent());
		} catch (ParameterException e) {
			fail("Constructor throws exception!");
		}
	}

	/* Tests method processesColor */
	@Test
	public void testProcessesColor() {
		// Create a transition
		CPNTransition cpnTrans = setUpStandadEnabledTransition();

		// check whether the transition knows that it consumes a red token
		// but not a pink token
		assertTrue(cpnTrans.processesColor("red"));
		assertFalse(cpnTrans.processesColor("pink"));
	}

	/* Test method getConsumed Tokens */
	@Test
	public void testGetConsumedTokens() {
		// Create a transition
		CPNTransition cpnTrans = setUpStandadEnabledTransition();

		// The transition consumes two red, one black and one green token
		assertEquals(1, cpnTrans.getConsumedTokens("black"));
		assertEquals(2, cpnTrans.getConsumedTokens("red"));
		assertEquals(0, cpnTrans.getConsumedTokens("pink"));
		assertEquals(0, cpnTrans.getConsumedTokens(null));
	}

	/* Test method getConsumed Tokens */
	@Test
	public void testGetProducedTokens() {
		// Create a transition
		CPNTransition cpnTrans = setUpStandadEnabledTransition();

		// The transition produces a black and a yellow token
		assertEquals(1, cpnTrans.getProducedTokens("black"));
		assertEquals(1, cpnTrans.getProducedTokens("yellow"));
		assertEquals(0, cpnTrans.getProducedTokens("pink"));
		assertEquals(0, cpnTrans.getProducedTokens(null));
	}

	/* Test method ConsumesColor */
	@Test
	public void testConsumesColor() {
		// Create a transition
		CPNTransition cpnTrans = setUpStandadEnabledTransition();

		// The transition consumes two red, one black and one green token
		assertTrue(cpnTrans.consumesColor("black"));
		assertFalse(cpnTrans.consumesColor("pink"));
		assertFalse(cpnTrans.consumesColor(null));

	}

	/* Test method producesColor */
	@Test
	public void testProducesColor() {
		// Create a transition
		CPNTransition cpnTrans = setUpStandadEnabledTransition();

		// /The transition produces a black and a yellow token
		assertTrue(cpnTrans.producesColor("black"));
		assertTrue(cpnTrans.producesColor("yellow"));
		assertFalse(cpnTrans.producesColor("pink"));
		assertFalse(cpnTrans.producesColor(null));
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testCPNTransitionClone() throws ParameterException {
		// CPNTransition with non-default values
		CPNTransition t1 = setUpStandadEnabledTransition();
		t1.setSilent(true);
		assertTrue(t1.isSilent());
		assertTrue(t1.processesColor("red"));
		assertFalse(t1.processesColor("pink"));

		CPNTransition t1clone = (CPNTransition) t1.clone();
		assertEquals(t1, t1clone);
		assertNotSame(t1, t1clone);
		assertTrue(t1clone.isSilent());
		assertEquals(t1.getName(), t1clone.getName());
		assertEquals(t1.getLabel(), t1clone.getLabel());
		assertEquals(t1.isEnabled(), t1clone.isEnabled());
		assertEquals(t1.isPlace(), t1clone.isPlace());
		assertEquals(t1.isDrain(), t1clone.isDrain());
		assertEquals(t1.isSource(), t1clone.isSource());
		assertEquals(t1.isTransition(), t1clone.isTransition());
		// Processes color can't be tested
		// assertTrue(t1clone.processesColor("red"));
		// assertFalse(t1clone.processesColor("pink"));

		// CPNTransition with default values
		CPNTransition t2 = new CPNTransition("name_trans1");
		assertFalse(t2.isSilent());
		assertFalse(t2.processesColor("black"));

		CPNTransition t2clone = (CPNTransition) t2.clone();
		assertEquals(t2, t2clone);
		assertNotSame(t2, t2clone);
		assertFalse(t2clone.isSilent());
		assertEquals(t2.getName(), t2clone.getName());
		assertEquals(t2.getLabel(), t2clone.getLabel());
		assertEquals(t2.isPlace(), t2clone.isPlace());
		assertEquals(t2.isDrain(), t2clone.isDrain());
		assertEquals(t2.isSource(), t2clone.isSource());
		assertEquals(t2.isTransition(), t2clone.isTransition());
		// assertFalse(t2clone.processesColor("black"));
	}

	/*
	 * <p>
	 * Creates a transition in a simple petri net consisting of two input places connected to a single transition and two output places connected to the same transition.
	 * </p>
	 * <p>
	 * Initial Marking
	 * </p>
	 * <ul>
	 * <li>Pre1 = red,red , black</li>
	 * <li>Pre2 = green</li>
	 * </ul>
	 * 
	 * <p>
	 * Transition<br/>
	 * Consumes two red tokens and a black token from Pre1 and a green token from pre2 Produces a black token in Post1 and a yellow token in Post2
	 * </p>
	 */
	private CPNTransition setUpStandadEnabledTransition() {

		// create a cpn
		CPN cpn = null;

		try {
			// Create places
			Set<String> places = new HashSet<String>();
			places.add("pre1");
			places.add("pre2");
			places.add("post1");
			places.add("post2");

			// create transitions
			Set<String> transitions = new HashSet<String>();
			transitions.add("t0");

			// create the the token colors used in the initial marking
			Multiset<String> mset = new Multiset<String>();
			mset.add("red");
			mset.add("red");
			mset.add("black");
			CPNMarking marking = new CPNMarking();
			marking.set("pre1", mset);

			Multiset<String> mset2 = new Multiset<String>();
			mset2.add("green");
			marking.set("pre2", mset2);

			// create the cpn
			cpn = new CPN(places, transitions, marking);

			// Add the flow relation
			CPNFlowRelation pre1ToT0 = cpn.addFlowRelationPT("pre1", "t0", false);
			CPNFlowRelation pre2ToT0 = cpn.addFlowRelationPT("pre2", "t0", false);

			CPNFlowRelation t0ToPost1 = cpn.addFlowRelationTP("t0", "post1", false);
			CPNFlowRelation t0ToPost2 = cpn.addFlowRelationTP("t0", "post2", false);

			// configure flow relation
			pre1ToT0.addConstraint("red", 2);
			pre1ToT0.addConstraint("black", 1);
			pre2ToT0.addConstraint("green", 1);

			t0ToPost1.addConstraint("black", 1);
			t0ToPost2.addConstraint("yellow", 1);

		} catch (ParameterException e) {
			e.printStackTrace();
		}

		return cpn.getTransition("t0");
	}
}
