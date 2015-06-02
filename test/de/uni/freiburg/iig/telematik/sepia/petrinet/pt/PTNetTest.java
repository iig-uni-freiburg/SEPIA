package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.soundness.PTNetSoundness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.properties.validity.PTNetValidity;

/**
 * Tests for the {@link PTNet}.
 * 
 * @author Adrian Lange
 */
public class PTNetTest {

	/*
	 * Check for enabled transitions
	 */
	@Test
	public void testPTEnabledTransitions() throws ParameterException, PNException {
		PTNet ptnet = createPTNet();

		// check whether the right transition (t0) is enabled
		for (PTTransition t : ptnet.getEnabledTransitions()) {
			assertEquals("t0", t.getName());
		}

		ptnet.fire("t0");

		// check whether the right transition (t1) is enabled
		for (PTTransition t : ptnet.getEnabledTransitions()) {
			assertEquals("t1", t.getName());
		}
	}

	/* Tests whether firecheck return the same marking as firing */
	@Test
	public void testFireCheckPT() throws ParameterException {
		// Create the standard P/T-net
		PTNet ptnet = createPTNet();

		// Retrieve a marking via firecheck
		PTMarking fireCheckMarking = null;
		try {
			fireCheckMarking = ptnet.fireCheck("t0");
		} catch (ParameterException e1) {
			fail("fireCheck raised an exception!");
			e1.printStackTrace();
		} catch (PNException e1) {
			fail("fireCheck raised an exception!");
			e1.printStackTrace();
		}

		// fire t0 and see which marking is reached
		try {
			ptnet.fire("t0");
		} catch (ParameterException e) {
			fail("fireing raised an exception!");
			e.printStackTrace();
		} catch (PNException e) {
			fail("fireing raised an exception!");
			e.printStackTrace();
		}
		PTMarking fireMarking = ptnet.getMarking();

		// check whether the markings are the same
		assertEquals(fireMarking, fireCheckMarking);
	}

	/*
	 * Check whether a valid net is reported as valid
	 */
	@Test
	public void testCheckValidityValidNet() throws ParameterException {
		// Create the standard P/T-net
		PTNet ptnet = createPTNet();

		// The standard net is valid
		try {
			PTNetValidity.checkValidity(ptnet);
		} catch (PNValidationException e) {
			fail("The standart Net is valid!");
			e.printStackTrace();
		}
	}

	/* Check whether a net with an unconnected transition is valid */
	@Test
	public void testCheckValidityUnconnectedTransition() throws ParameterException {
		// Create the standard P/T-net
		PTNet ptnet = createPTNet();

		// Create a transition which does not consumes or produce a token
		try {
			ptnet.addTransition("t2");
		} catch (ParameterException e) {
			fail("Cannot add transition to P/T-net!");
			e.printStackTrace();
		}
	}


	/*
	 * Check whether the standard net is sound
	 */
	@Test
	public void testCheckSoundnessNet() throws ParameterException {
		// Create the standard P/T-net
		PTNet ptnet = createPTNet();

		try {
			PTNetSoundness.checkSoundness(ptnet, true);
		} catch (PNSoundnessException e) {
			fail("The standard net is sound!");
			e.printStackTrace();
//		} catch (PNSoundnessException e) {
//			fail("The standard net is sound!");
//			e.printStackTrace();
		}
	}

	/*
	 * Test adding flow relations
	 */
	@Test
	public void testAddFlowRelation() throws ParameterException {
		// Create the standard P/T-net
		PTNet ptnet = createPTNet();

		// PT
		// Adding this flow relation should raise an exception since the place does not exist
		try {
			ptnet.addFlowRelationPT("p_false", "t0");
			fail("A flow relation from a non existing place got added!");
		} catch (ParameterException e) {
		}

		// The following flow relation is added twice.
		// It should change nothing when the relation is added a second time
		ptnet.addFlowRelationPT("p0", "t0");
		HashSet<PTFlowRelation> relationsBeforeSecondAdd = new HashSet<PTFlowRelation>(ptnet.getFlowRelations());
		ptnet.addFlowRelationPT("p0", "t0");
		HashSet<PTFlowRelation> relationsAfterSecondAdd = new HashSet<PTFlowRelation>(ptnet.getFlowRelations());
		assertEquals(relationsBeforeSecondAdd, relationsAfterSecondAdd);

		// TP
		// Adding this flow relation should raise an exception since the place does not exist
		try {
			ptnet.addFlowRelationTP("t_false", "p0");
			fail("A flow relation from a non existing transition got added!");
		} catch (ParameterException e) {
		}

		// The following flow relation is added twice.
		// It should change nothing when the relation is added a second time
		ptnet.addFlowRelationTP("t0", "p0");
		relationsBeforeSecondAdd = new HashSet<PTFlowRelation>(ptnet.getFlowRelations());
		ptnet.addFlowRelationTP("t0", "p0");
		relationsAfterSecondAdd = new HashSet<PTFlowRelation>(ptnet.getFlowRelations());
		assertEquals(relationsBeforeSecondAdd, relationsAfterSecondAdd);
	}
	
	/*
	 * Tests setting an initial marking for a place with a too small capacity
	 */
	@Test(expected = ParameterException.class)
	public void testInitialMarking() throws ParameterException {
		PTNet ptnet = new PTNet();

		ptnet.addPlace("p0");
		ptnet.getPlace("p0").setCapacity(1);

		// create the the token colors used in the initial marking
		PTMarking marking = new PTMarking();
		marking.set("p0", 5);

		// create the P/T-net with all tokens in P0
		ptnet.setInitialMarking(marking);
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testPTNetClone() throws ParameterException {
		/*
		 * Test equal P/T-nets
		 */
		PTNet ptnet1 = createPTNet();
		PTNet ptnet1clone = (PTNet) ptnet1.clone();
		assertNotSame(ptnet1, ptnet1clone);
		// Can't just test equality because of the different order of same lists.
		// assertEquals(ptnet1, ptnet1clone);
		// Check equality for places
		assertEquals(ptnet1.getPlaces().size(), ptnet1clone.getPlaces().size());
		for (PTPlace p : ptnet1.getPlaces()) {
			assertTrue(ptnet1clone.getPlace(p.getName()) != null);
			assertEquals(p, ptnet1clone.getPlace(p.getName()));
			assertNotSame(p, ptnet1clone.getPlace(p.getName()));
		}
		// Check equality for transitions
		assertEquals(ptnet1.getTransitions().size(), ptnet1clone.getTransitions().size());
		for (PTTransition t : ptnet1.getTransitions()) {
			assertTrue(ptnet1clone.getTransition(t.getName()) != null);
			assertEquals(t, ptnet1clone.getTransition(t.getName()));
			assertNotSame(t, ptnet1clone.getTransition(t.getName()));
		}
		// Check equality for flow relations
		assertEquals(ptnet1.getFlowRelations().size(), ptnet1clone.getFlowRelations().size());
		for (PTFlowRelation f : ptnet1.getFlowRelations()) {
			PTFlowRelation flowRelationClone = null;
			for (PTFlowRelation fc : ptnet1clone.getFlowRelations()) {
				if (fc.getSource().equals(f.getSource()) && fc.getTarget().equals(f.getTarget()) && fc.getDirectionPT() == f.getDirectionPT() && fc.getConstraint().equals(f.getConstraint())) {
					flowRelationClone = fc;
				}
			}
			assertFalse(flowRelationClone == null);
			assertEquals(f, flowRelationClone);
			assertNotSame(f, flowRelationClone);
		}
		// Check equality for the initial marking
		assertEquals(ptnet1.getInitialMarking(), ptnet1clone.getInitialMarking());
		assertNotSame(ptnet1.getInitialMarking(), ptnet1clone.getInitialMarking());
	}

	/*
	 * Creates the standard P/T-net.
	 */
	private PTNet createPTNet() throws ParameterException {
		PTNet ptnet = null;

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
		PTMarking marking = new PTMarking();
		marking.set("p0", 1);

		// create the P/T-net with all tokens in P0
		ptnet = new PTNet(places, transitions, marking);

		// Add the flow relation
		ptnet.addFlowRelationPT("p0", "t0");
		ptnet.addFlowRelationTP("t0", "p1");
		ptnet.addFlowRelationPT("p1", "t1");
		ptnet.addFlowRelationTP("t1", "p3");
		ptnet.addFlowRelationTP("t1", "p2");
//		ptnet.addFlowRelationPT("p2", "t0");

		return ptnet;
	}
}