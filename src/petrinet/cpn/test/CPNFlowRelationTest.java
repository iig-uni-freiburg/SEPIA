/**
 * 
 */
package petrinet.cpn.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;

import petrinet.cpn.CPNFlowRelation;
import petrinet.cpn.CPNPlace;
import petrinet.cpn.CPNTransition;

/**
 * @author boehr
 *
 */
public class CPNFlowRelationTest {

	
	//A CPNTranstion which is used in several test cases
	CPNTransition cpnT = null;
	
	//A CPNPlace which is used in several test cases
	CPNPlace cpnP = null;
		
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		//Setup standad transition and place
		cpnT = new CPNTransition("t0");
		cpnP = new CPNPlace("p0");
			
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test the constructors
	 * @throws ParameterException 
	 */
	@Test
	public void testCPNFlowRelationConstructors() throws ParameterException {
		
		//Constructor 1
		CPNFlowRelation f = new CPNFlowRelation(cpnP, cpnT);
		assertEquals(f.getSource().getName(),"p0");
		assertEquals(f.getTarget().getName(),"t0");
		assertTrue(f.getConstraint().equals(new Multiset<String>("black")));
		
		
		//Constructor 2
		CPNFlowRelation f2 = new CPNFlowRelation(cpnT, cpnP);
		assertEquals(f2.getSource().getName(),"t0");
		assertEquals(f2.getTarget().getName(),"p0");
		assertTrue(f2.getConstraint().equals(new Multiset<String>("black")));
		
		//Constructor 3
		CPNFlowRelation f3 = new CPNFlowRelation(cpnP, cpnT, false);
		assertEquals(f3.getSource().getName(),"p0");
		assertEquals(f3.getTarget().getName(),"t0");
		assertFalse(f3.hasConstraints());		

		
		//Constructor 4
		CPNFlowRelation f4 = new CPNFlowRelation(cpnT, cpnP, false);
		assertEquals(f4.getSource().getName(),"t0");
		assertEquals(f4.getTarget().getName(),"p0");
		assertFalse(f4.hasConstraints());
						
	}
	
	/**
	 * Test methods dealing with constraints
	 * @throws ParameterException 
	 */
	@Test
	public void testCPNFlowRelationConstraints() throws ParameterException{
		
		//Create a cpn flowrelation
		CPNFlowRelation f = new CPNFlowRelation(cpnP, cpnT);
		assertEquals(1, f.getConstraint("black"));
		assertEquals(0, f.getConstraint("pink"));
		assertTrue(f.hasConstraints());
						
	}
	

}
