package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

/**
 * Tests for the {@link PTTransition}.
 * 
 * @author Adrian Lange
 */
public class PTTransitionTest {

	/*
	 * Test the different constructors of the transition
	 */
	@Test
	public void testPTConstructors() {
		try {
			PTTransition t = new PTTransition("t0");
			assertEquals("t0", t.getName());
			assertEquals("t0", t.getLabel());
			assertFalse(t.isSilent());

			t = new PTTransition("t0_name", "t0_label");
			assertEquals("t0_name", t.getName());
			assertEquals("t0_label", t.getLabel());
			assertFalse(t.isSilent());

			t = new PTTransition("t0", true);
			assertEquals("t0", t.getName());
			assertEquals("t0", t.getLabel());
			assertTrue(t.isSilent());

			t = new PTTransition("t0_name", "t0_label", true);
			assertEquals("t0_name", t.getName());
			assertEquals("t0_label", t.getLabel());
			assertTrue(t.isSilent());
		} catch (ParameterException e) {
			fail("Testing PTTransition constructors failed: " + e.getMessage());
		}
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testPTTransitionClone() throws ParameterException {
		// PTTransition with non-default values
		PTTransition t1 = setUpStandadEnabledTransition();

		PTTransition t1clone = t1.clone();
		assertEquals(t1, t1clone);
		assertNotSame(t1, t1clone);
		assertEquals(t1.getName(), t1clone.getName());
		assertEquals(t1.getLabel(), t1clone.getLabel());
		assertEquals(t1.isSilent(), t1clone.isSilent());
		assertEquals(t1.isPlace(), t1clone.isPlace());
		assertEquals(t1.isDrain(), t1clone.isDrain());
		assertEquals(t1.isSource(), t1clone.isSource());
		assertEquals(t1.isTransition(), t1clone.isTransition());

		// PTTransition with default values
		PTTransition t2 = new PTTransition("name_trans1");
		assertFalse(t2.isSilent());

		PTTransition t2clone = t2.clone();
		assertEquals(t2, t2clone);
		assertNotSame(t2, t2clone);
		assertEquals(t2.getName(), t2clone.getName());
		assertEquals(t2.getLabel(), t2clone.getLabel());
		assertEquals(t2.isSilent(), t2clone.isSilent());
		assertEquals(t2.isPlace(), t2clone.isPlace());
		assertEquals(t2.isDrain(), t2clone.isDrain());
		assertEquals(t2.isSource(), t2clone.isSource());
		assertEquals(t2.isTransition(), t2clone.isTransition());
	}

	/*
	 * Creates a transition in a simple petri net
	 */
	protected PTTransition setUpStandadEnabledTransition() {
		// create a P/T-net
		PTNet ptnet = null;

		// Create places
		Set<String> places = new HashSet<String>();
		places.add("pre1");
		places.add("pre2");
		places.add("post1");
		places.add("post2");

		// create transitions
		Set<String> transitions = new HashSet<String>();
		transitions.add("t0");

		// create the initial marking
		PTMarking marking = new PTMarking();
		marking.set("pre1", 3);
		marking.set("pre2", 1);

		// create the P/T-net
		ptnet = new PTNet(places, transitions, marking);

		// Add the flow relation
		ptnet.addFlowRelationPT("pre1", "t0", 2);
		ptnet.addFlowRelationPT("pre2", "t0", 3);
		ptnet.addFlowRelationTP("t0", "post1", 4);
		ptnet.addFlowRelationTP("t0", "post2", 5);

		return ptnet.getTransition("t0");
	}
}