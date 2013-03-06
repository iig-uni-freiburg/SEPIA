/**
 * 
 */
package petrinet.cpn.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import petrinet.cpn.FiringRule;

/**
 * @author boehr
 *
 */
public class FiringRuleTest {

	/**
	 * Test method for checking and adding requirements
	 */
	@Test
	public void testRequirements() {
		FiringRule f = new FiringRule();
		assertFalse(f.containsRequirements());
		
		//Add an empty requirement
		HashMap<String, Integer> requirement = new HashMap<String, Integer>();
		requirement.put("black", 0);
		f.addRequirement("p0", requirement);
		assertFalse(f.containsRequirements());
		
		//Add a requirement		
		requirement.put("green", 3);
		f.addRequirement("p0", requirement);
		assertTrue(f.containsRequirements());
		
	}

	/**
	 * Test method for checking and adding productions
	 */
	@Test
	public void testProductions() {
		FiringRule f = new FiringRule();
		assertFalse(f.containsProductions());
		
		//Add an empty production
		HashMap<String, Integer> productions = new HashMap<String, Integer>();
		productions.put("black", 0);
		f.addProduction("p0", productions);
		assertFalse(f.containsProductions());
		
		//Add a production		
		productions.put("green", 3);
		f.addProduction("p0", productions);
		assertTrue(f.containsProductions());
	}


}
