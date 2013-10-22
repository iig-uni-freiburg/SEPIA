package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNTransition;

/**
 * @author boehr
 */
public class CWNTransitionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/* Test the constructors */
	@Test
	public void cwnTransitionConstructorTest() throws ParameterException {

		// Creates a non silent transition where label and name are the same
		CWNTransition t1 = new CWNTransition("SameNameAndLabel");
		assertEquals("SameNameAndLabel", t1.getName());
		assertEquals("SameNameAndLabel", t1.getLabel());
		assertEquals(t1.getName(), t1.getName());
		assertFalse(t1.isSilent());

		// Creates a transition where label and name are the same but isEmpty is set explicitly
		CWNTransition t2 = new CWNTransition("SameNameAndLabel", false);
		assertEquals("SameNameAndLabel", t2.getName());
		assertEquals("SameNameAndLabel", t2.getLabel());
		assertEquals(t2.getName(), t2.getName());
		assertFalse(t2.isSilent());
		CWNTransition t3 = new CWNTransition("SameNameAndLabel", true);
		assertTrue(t3.isSilent());

		// Creates a transition where label and name are different (silent is set implicitly to false)
		CWNTransition t4 = new CWNTransition("transitionName", "transitionLabel");
		assertEquals("transitionName", t4.getName());
		assertEquals("transitionLabel", t4.getLabel());
		assertFalse(t4.isSilent());

		// Creates a transition where label and name are different and isempty gets set explicitly
		CWNTransition t5 = new CWNTransition("transitionName", "transitionLabel", false);
		assertEquals("transitionName", t5.getName());
		assertEquals("transitionLabel", t5.getLabel());
		assertFalse(t5.isSilent());
		CWNTransition t6 = new CWNTransition("transitionName", "transitionLabel", true);
		assertEquals("transitionName", t6.getName());
		assertEquals("transitionLabel", t6.getLabel());
		assertTrue(t6.isSilent());
	}

	/*
	 * Test the checkValidity method.<br/>
	 * ==> black token must be consumed and produced
	 */
	@Test
	public void cwnTransitionCheckValidityTest() throws ParameterException {
		// create a simple net
		// pIn --black--> to --black-->pOut

		// Create places
		Set<String> places = new HashSet<String>();
		places.add("pIn");
		places.add("pOut");

		// create transitions
		Set<String> transitions = new HashSet<String>();
		transitions.add("t0");

		// create the the token colors used in the initial marking
		Multiset<String> mset = new Multiset<String>();
		mset.add("black");
		CWNMarking marking = new CWNMarking();
		marking.set("pIn", mset);

		// create the cwn with one black token in P0
		CWN simpleCwn = new CWN(places, transitions, marking);

		// Add the flow relation
		CWNFlowRelation inRelation = simpleCwn.addFlowRelationPT("pIn", "t0", true);
		CWNFlowRelation outRelation = simpleCwn.addFlowRelationTP("t0", "pOut", true);

		// Transition t0 in simple cwn should be valid
		CWNTransition t0 = simpleCwn.getTransition("t0");
		try {
			t0.checkValidity();
		} catch (PNValidationException e) {
			fail("A valid CWNTransition was reported as unvalid!");
			e.printStackTrace();
		}

		// remove black from the inrelation
		Multiset<String> pinkMSet = new Multiset<String>();
		pinkMSet.add("pink");
		inRelation.setConstraint(pinkMSet);

		// t0 should be invalid now
		try {
			t0.checkValidity();
			fail("An invalid CWNTransition was reported as valid!");
		} catch (PNValidationException e) {
		}

		// Add reset black at the inrelation
		// remove black from the outrelation
		Multiset<String> blackMSet = new Multiset<String>();
		blackMSet.add("black");
		inRelation.setConstraint(blackMSet);
		outRelation.setConstraint(pinkMSet);

		// t0 should be invalid now
		try {
			t0.checkValidity();
			fail("An invalid CWNTransition was reported as valid!");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testCWNTransitionClone() throws ParameterException {
		CWNTransition t1 = new CWNTransition("name_trans1", "label_trans1");
		t1.setSilent(true);

		CWNTransition t1clone = (CWNTransition) t1.clone();
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

		CWNTransition t2 = new CWNTransition("name_trans2");
		assertFalse(t2.isSilent());
		assertFalse(t2.processesColor("black"));

		CWNTransition t2clone = (CWNTransition) t2.clone();
		assertEquals(t2, t2clone);
		assertNotSame(t2, t2clone);
		assertFalse(t2clone.isSilent());
		assertEquals(t2.getName(), t2clone.getName());
		assertEquals(t2.getLabel(), t2clone.getLabel());
		assertEquals(t2.isPlace(), t2clone.isPlace());
		assertEquals(t2.isDrain(), t2clone.isDrain());
		assertEquals(t2.isSource(), t2clone.isSource());
		assertEquals(t2.isTransition(), t2clone.isTransition());
	}
}
