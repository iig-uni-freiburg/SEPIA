package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;

/**
 * @author boehr
 */
public class IFNetFlowRelationTest {

	// An AbstractIFNetTranstion which is used in several test cases
	AbstractIFNetTransition<IFNetFlowRelation> ifNetT = null;

	// A IFNetPlace which is used in several test cases
	IFNetPlace ifNetP = null;

	@Before
	public void setUp() throws Exception {
		// Setup standard transition and place
		ifNetT = new RegularIFNetTransition("t0");
		ifNetP = new IFNetPlace("p0");
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Test the constructors
	 */
	@Test
	public void testIFNetFlowRelationConstructors() {

		// Constructor 1
		IFNetFlowRelation f = new IFNetFlowRelation(ifNetP, ifNetT);
		assertEquals(f.getSource().getName(), "p0");
		assertEquals(f.getTarget().getName(), "t0");
		assertTrue(f.getConstraint().equals(new Multiset<String>("black")));

		// Constructor 2
		IFNetFlowRelation f2 = new IFNetFlowRelation(ifNetT, ifNetP);
		assertEquals(f2.getSource().getName(), "t0");
		assertEquals(f2.getTarget().getName(), "p0");
		assertTrue(f2.getConstraint().equals(new Multiset<String>("black")));
	}

	/*
	 * Test methods dealing with constraints
	 */
	@Test
	public void testIFNetFlowRelationConstraints() {

		// Create a IFNet flow relation
		IFNetFlowRelation f = new IFNetFlowRelation(ifNetP, ifNetT);
		assertEquals(1, f.getConstraint("black"));
		assertEquals(0, f.getConstraint("pink"));
		assertTrue(f.hasConstraints());
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testIFNetFlowRelationClone() {
		// Test PT transition
		IFNetFlowRelation f1 = new IFNetFlowRelation(ifNetP, ifNetT);
		assertEquals(f1.getSource().getName(), "p0");
		assertEquals(f1.getTarget().getName(), "t0");
		assertTrue(f1.getConstraint().equals(new Multiset<String>("black")));
		@SuppressWarnings("unchecked")
		IFNetFlowRelation f1clone = f1.clone((IFNetPlace) f1.getSource(), (AbstractIFNetTransition<IFNetFlowRelation>) f1.getTarget(), f1.getDirectionPT());
		assertEquals(f1, f1clone);
		assertNotSame(f1, f1clone);
		assertEquals(f1.getSource().getName(), f1clone.getSource().getName());
		assertEquals(f1.getTarget().getName(), f1clone.getTarget().getName());
		assertTrue(f1clone.getConstraint().equals(new Multiset<String>("black")));

		// Test TP transition
		IFNetFlowRelation f2 = new IFNetFlowRelation(ifNetT, ifNetP);
		assertEquals(f2.getSource().getName(), "t0");
		assertEquals(f2.getTarget().getName(), "p0");
		assertTrue(f2.getConstraint().equals(new Multiset<String>("black")));
		@SuppressWarnings("unchecked")
		IFNetFlowRelation f2clone = f2.clone((IFNetPlace) f2.getTarget(), (AbstractIFNetTransition<IFNetFlowRelation>) f2.getSource(), f2.getDirectionPT());
		assertEquals(f2, f2clone);
		assertNotSame(f2, f2clone);
		assertEquals(f2.getSource().getName(), f2clone.getSource().getName());
		assertEquals(f2.getTarget().getName(), f2clone.getTarget().getName());
		assertTrue(f2clone.getConstraint().equals(new Multiset<String>("black")));
	}
}
