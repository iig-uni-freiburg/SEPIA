package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;

/**
 * @author boehr
 */
public class CPNFlowRelationTest {

	// A CPNTranstion which is used in several test cases
	CPNTransition cpnT = null;

	// A CPNPlace which is used in several test cases
	CPNPlace cpnP = null;

	@Before
	public void setUp() throws Exception {
		// Setup standard transition and place
		cpnT = new CPNTransition("t0");
		cpnP = new CPNPlace("p0");
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Test the constructors
	 */
	@Test
	public void testCPNFlowRelationConstructors() {

		// Constructor 1
		CPNFlowRelation f = new CPNFlowRelation(cpnP, cpnT);
		assertEquals(f.getSource().getName(), "p0");
		assertEquals(f.getTarget().getName(), "t0");
		assertTrue(f.getConstraint().equals(f.getDefaultConstraint()));

		// Constructor 2
		CPNFlowRelation f2 = new CPNFlowRelation(cpnT, cpnP);
		assertEquals(f2.getSource().getName(), "t0");
		assertEquals(f2.getTarget().getName(), "p0");
		assertTrue(f2.getConstraint().equals(f.getDefaultConstraint()));
	}

	/*
	 * Test methods dealing with constraints
	 */
	@Test
	public void testCPNFlowRelationConstraints() {

		// Create a cpn flow relation
		CPNFlowRelation f = new CPNFlowRelation(cpnP, cpnT);
		assertEquals(1, f.getConstraint("black"));
		assertEquals(0, f.getConstraint("pink"));
		assertTrue(f.hasConstraints());
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testCPNFlowRelationClone() {
		// Test PT transition
		CPNFlowRelation f1 = new CPNFlowRelation(cpnP, cpnT);
		assertEquals(f1.getSource().getName(), "p0");
		assertEquals(f1.getTarget().getName(), "t0");
		assertTrue(f1.getConstraint().equals(new Multiset<String>("black")));
		CPNFlowRelation f1clone = f1.clone((CPNPlace) f1.getSource(), (CPNTransition) f1.getTarget(), f1.getDirectionPT());
		assertEquals(f1, f1clone);
		assertNotSame(f1, f1clone);
		assertEquals(f1.getSource().getName(), f1clone.getSource().getName());
		assertEquals(f1.getTarget().getName(), f1clone.getTarget().getName());
		assertTrue(f1clone.getConstraint().equals(new Multiset<String>("black")));

		// Test TP transition
		CPNFlowRelation f2 = new CPNFlowRelation(cpnT, cpnP);
		assertEquals(f2.getSource().getName(), "t0");
		assertEquals(f2.getTarget().getName(), "p0");
		assertTrue(f2.getConstraint().equals(new Multiset<String>("black")));
		CPNFlowRelation f2clone = f2.clone((CPNPlace) f2.getTarget(), (CPNTransition) f2.getSource(), f2.getDirectionPT());
		assertEquals(f2, f2clone);
		assertNotSame(f2, f2clone);
		assertEquals(f2.getSource().getName(), f2clone.getSource().getName());
		assertEquals(f2.getTarget().getName(), f2clone.getTarget().getName());
		assertTrue(f2clone.getConstraint().equals(new Multiset<String>("black")));
	}
}
