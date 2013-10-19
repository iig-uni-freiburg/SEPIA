package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNTransition;

/**
 * @author boehr
 */
public class CWNFlowRelationTest {

	// A CWNTranstion which is used in several test cases
	CWNTransition cwnT = null;

	// A CWNPlace which is used in several test cases
	CWNPlace cwnP = null;

	@Before
	public void setUp() throws Exception {
		// Setup standard transition and place
		cwnT = new CWNTransition("t0");
		cwnP = new CWNPlace("p0");
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Test the constructors
	 */
	@Test
	public void testCWNFlowRelationConstructors() throws ParameterException {

		// Constructor 1
		CWNFlowRelation f = new CWNFlowRelation(cwnP, cwnT);
		assertEquals(f.getSource().getName(), "p0");
		assertEquals(f.getTarget().getName(), "t0");
		assertTrue(f.getConstraint().equals(new Multiset<String>("black")));

		// Constructor 2
		CWNFlowRelation f2 = new CWNFlowRelation(cwnT, cwnP);
		assertEquals(f2.getSource().getName(), "t0");
		assertEquals(f2.getTarget().getName(), "p0");
		assertTrue(f2.getConstraint().equals(new Multiset<String>("black")));
	}

	/*
	 * Test methods dealing with constraints
	 */
	@Test
	public void testCWNFlowRelationConstraints() throws ParameterException {

		// Create a cwn flow relation
		CWNFlowRelation f = new CWNFlowRelation(cwnP, cwnT);
		assertEquals(1, f.getConstraint("black"));
		assertEquals(0, f.getConstraint("pink"));
		assertTrue(f.hasConstraints());
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testCWNFlowRelationClone() throws ParameterException {
		// Test PT transition
		CWNFlowRelation f1 = new CWNFlowRelation(cwnP, cwnT);
		assertEquals(f1.getSource().getName(), "p0");
		assertEquals(f1.getTarget().getName(), "t0");
		assertTrue(f1.getConstraint().equals(new Multiset<String>("black")));
		CWNFlowRelation f1clone = f1.clone((CWNPlace) f1.getSource(), (CWNTransition) f1.getTarget(), f1.getDirectionPT());
		assertEquals(f1, f1clone);
		assertNotSame(f1, f1clone);
		assertEquals(f1.getSource().getName(), f1clone.getSource().getName());
		assertEquals(f1.getTarget().getName(), f1clone.getTarget().getName());
		assertTrue(f1clone.getConstraint().equals(new Multiset<String>("black")));

		// Test TP transition
		CWNFlowRelation f2 = new CWNFlowRelation(cwnT, cwnP);
		assertEquals(f2.getSource().getName(), "t0");
		assertEquals(f2.getTarget().getName(), "p0");
		assertTrue(f2.getConstraint().equals(new Multiset<String>("black")));
		CWNFlowRelation f2clone = f2.clone((CWNPlace) f2.getTarget(), (CWNTransition) f2.getSource(), f2.getDirectionPT());
		assertEquals(f2, f2clone);
		assertNotSame(f2, f2clone);
		assertEquals(f2.getSource().getName(), f2clone.getSource().getName());
		assertEquals(f2.getTarget().getName(), f2clone.getTarget().getName());
		assertTrue(f2clone.getConstraint().equals(new Multiset<String>("black")));
	}
}
