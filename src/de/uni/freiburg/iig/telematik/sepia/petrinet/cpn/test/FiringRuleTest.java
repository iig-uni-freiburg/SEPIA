package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.FiringRule;

/**
 * @author boehr
 */
public class FiringRuleTest {

	/*
	 * Test method for checking and adding requirements
	 */
	@Test
	public void testRequirements() {
		FiringRule f = new FiringRule();
		assertFalse(f.containsRequirements());

		// Add an empty requirement
		HashMap<String, Integer> requirement = new HashMap<String, Integer>();
		requirement.put("black", 0);
		try {
			f.addRequirement("p0", requirement);
			fail("Allowed to add empty requirement!");
		} catch (ParameterException e) {
		}
		assertFalse(f.containsRequirements());

		HashMap<String, Integer> requirement2 = new HashMap<String, Integer>();
		// Add a requirement
		requirement2.put("green", 3);
		try {
			f.addRequirement("p0", requirement2);
		} catch (ParameterException e) {
			System.out.println(e);
			fail("Did not allow to add a valid requirement!");
		}
		assertTrue(f.containsRequirements());

	}

	/*
	 * Test method for checking and adding productions
	 */
	@Test
	public void testProductions() {
		FiringRule f = new FiringRule();
		assertFalse(f.containsProductions());

		// Add an empty production
		HashMap<String, Integer> productions = new HashMap<String, Integer>();
		productions.put("black", 0);
		try {
			f.addProduction("p0", productions);
			fail("Allowed to add an empty production!");
		} catch (ParameterException e) {
		}
		assertFalse(f.containsProductions());

		// Add a production
		HashMap<String, Integer> productions2 = new HashMap<String, Integer>();
		productions2.put("green", 3);
		try {
			f.addProduction("p0", productions2);
		} catch (ParameterException e) {
			fail("Did not allow to add a valid production!");
		}
		assertTrue(f.containsProductions());
	}
}
