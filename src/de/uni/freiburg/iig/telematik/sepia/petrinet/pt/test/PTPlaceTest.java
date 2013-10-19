package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;

/**
 * Tests for the {@link PTPlace}.
 * 
 * @author Adrian Lange
 */
public class PTPlaceTest {

	/*
	 * Test the constructors of PTPlace.
	 */
	@Test
	public void testPTPlaceConstructor() {
		// create some places
		PTPlace p0 = null;
		PTPlace p1 = null;
		try {
			p0 = new PTPlace("name_p0");
			p1 = new PTPlace("name_p1", "label_p1");
		} catch (ParameterException e) {
			fail("Constructor failed!");
			e.printStackTrace();
		}

		// Check that the name an label are set correctly at first
		assertEquals(p0.getName(), "name_p0");
		assertEquals(p0.getLabel(), "name_p0");
		assertEquals(p1.getName(), "name_p1");
		assertEquals(p1.getLabel(), "label_p1");
	}

	/*
	 * Test token consumption and capacity of the PTPlace.
	 */
	@Test
	public void testPTPlaceAddTokens() throws ParameterException {
		// Create place
		PTPlace p1 = new PTPlace("p1_name", "p1_label");
		// Place's state should be empty
		assertTrue(p1.hasEmptyState());
		// Add tokens
		p1.setState(2);
		assertFalse(p1.hasEmptyState());
		assertTrue(p1.canConsume(4));
		// Limit capacity
		p1.setCapacity(5);
		assertFalse(p1.canConsume(4));
		// Reset state
		p1.setEmptyState();
		assertTrue(p1.hasEmptyState());
		assertTrue(p1.canConsume(4));
		try {
			assertFalse(p1.canConsume(6));
			fail("Place shouldn't be able to consume 6 tokens.");
		} catch (ParameterException e) {
		}
		// Remove capacity
		p1.removeCapacity();
		assertTrue(p1.canConsume(6));
	}

	/*
	 * An exception should be thrown
	 */
	@Test(expected = ParameterException.class)
	public void testPTPlaceSetLowCapacity() throws ParameterException {
		// Create place
		PTPlace p1 = new PTPlace("p1_name", "p1_label");
		// Add tokens
		p1.setState(4);
		// A too low capacity got accepted by the place!
		p1.setCapacity(2);
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testPTPlaceClone() throws ParameterException {
		// Create place without capacities
		PTPlace p1 = new PTPlace("p1_name", "p1_label");
		assertFalse(p1.isBounded());
		assertTrue(p1.hasEmptyState());
		p1.setState(5);
		// Clone it
		PTPlace p1Clone = p1.clone();
		assertEquals(p1.getName(), p1Clone.getName());
		assertEquals(p1.getLabel(), p1Clone.getLabel());
		assertEquals(p1.isBounded(), p1Clone.isBounded());
		assertEquals(p1.getCapacity(), p1Clone.getCapacity());
		assertEquals(p1.getState(), p1Clone.getState());
		assertEquals(p1.isPlace(), p1Clone.isPlace());
		assertEquals(p1.isDrain(), p1Clone.isDrain());
		assertEquals(p1.isSource(), p1Clone.isSource());
		assertEquals(p1.isTransition(), p1Clone.isTransition());
	}
}