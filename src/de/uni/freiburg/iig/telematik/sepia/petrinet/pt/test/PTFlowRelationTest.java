package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

/**
 * Tests for the {@link PTFlowRelation}.
 * 
 * @author Adrian Lange
 */
public class PTFlowRelationTest {

	// A PTTransition which is used in several test cases
	PTTransition ptT = null;

	// A PTPlace which is used in several test cases
	PTPlace ptP = null;

	@Before
	public void setUp() throws Exception {
		// Setup standard transition and place
		ptT = new PTTransition("t0");
		ptP = new PTPlace("p0");
	}

	/*
	 * Test the constructors of the PTFlowRelation
	 */
	@Test
	public void testPTFlowRelationConstructors() throws ParameterException {
		// Test constructor 1 for a PT relation
		PTFlowRelation f = new PTFlowRelation(ptP, ptT);
		assertEquals("p0", f.getSource().getName());
		assertEquals("t0", f.getTarget().getName());
		assertEquals(1, f.getWeight());
		assertEquals(true, f.getDirectionPT());
		assertEquals("arcPT_p0t0", f.getName());

		// Test constructor 2 for a TP relation
		f = new PTFlowRelation(ptT, ptP);
		assertEquals("t0", f.getSource().getName());
		assertEquals("p0", f.getTarget().getName());
		assertEquals(1, f.getWeight());
		assertEquals(false, f.getDirectionPT());
		assertEquals("arcTP_t0p0", f.getName());

		// Test constructor 3 for a PT relation
		f = new PTFlowRelation(ptP, ptT, 4);
		assertEquals("p0", f.getSource().getName());
		assertEquals("t0", f.getTarget().getName());
		assertEquals(4, f.getWeight());
		assertEquals(true, f.getDirectionPT());
		assertEquals("arcPT_p0t0", f.getName());

		// Test constructor 4 for a PT relation
		f = new PTFlowRelation(ptT, ptP, 17);
		assertEquals("t0", f.getSource().getName());
		assertEquals("p0", f.getTarget().getName());
		assertEquals(17, f.getWeight());
		assertEquals(false, f.getDirectionPT());
		assertEquals("arcTP_t0p0", f.getName());
	}

	/*
	 * Test the assigning of a weight to the PTFlowRelation
	 */
	@Test
	public void testPTFlowRelationWeight() throws ParameterException {
		PTFlowRelation f = new PTFlowRelation(ptP, ptT);
		assertEquals(1, f.getWeight());
		f.setWeight(4);
		assertEquals(4, f.getWeight());
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testPTFlowRelationClone() throws ParameterException {
		// Test PT transition
		PTFlowRelation f1 = new PTFlowRelation(ptP, ptT);
		f1.setWeight(3);
		assertEquals(f1.getSource().getName(), "p0");
		assertEquals(f1.getTarget().getName(), "t0");
		PTFlowRelation f1clone = f1.clone((PTPlace) f1.getSource(), (PTTransition) f1.getTarget(), f1.getDirectionPT());
		assertEquals(f1, f1clone);
		assertNotSame(f1, f1clone);
		assertEquals(f1.getSource().getName(), f1clone.getSource().getName());
		assertEquals(f1.getTarget().getName(), f1clone.getTarget().getName());
		assertEquals(f1.getDirectionPT(), f1clone.getDirectionPT());
		assertEquals(f1.getWeight(), f1clone.getWeight());

		// Test TP transition
		PTFlowRelation f2 = new PTFlowRelation(ptT, ptP, 12);
		assertEquals(f2.getSource().getName(), "t0");
		assertEquals(f2.getTarget().getName(), "p0");
		PTFlowRelation f2clone = f2.clone((PTPlace) f2.getTarget(), (PTTransition) f2.getSource(), f2.getDirectionPT());
		assertEquals(f2, f2clone);
		assertNotSame(f2, f2clone);
		assertEquals(f2.getSource().getName(), f2clone.getSource().getName());
		assertEquals(f2.getTarget().getName(), f2clone.getTarget().getName());
		assertEquals(f1.getWeight(), f1clone.getWeight());
	}
}