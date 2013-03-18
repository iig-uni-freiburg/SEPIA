/**
 * 
 */
package petrinet.snet.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.PNValidationException;

import petrinet.snet.DeclassificationTransition;
import petrinet.snet.SNet;

/**
 * @author boehr
 *
 */
public class DeclassificationTransitionTest {

	//The variable is filled during start up
	private SNet dSNet = null;
	private DeclassificationTransition td = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		dSNet = SNetTestUtil.createSimpleSnetWithDeclassification();
		td = dSNet.getDeclassificationTransitions().iterator().next();
	}

//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//	}
//
	/**
	 * Test method for {@link petrinet.snet.DeclassificationTransition#checkValidity()}.
	 * Check whether a valid declassifcation transition is recongnized as such.
	 */
	@Test
	public void testCheckValidity() {
		
		try {
			
			System.out.println(td.getConsumedAttributes());
			System.out.println(td.getProducedAttributes());
			
			
			
			td.checkValidity();
		} catch (PNValidationException e) {
			
			e.printStackTrace();
			fail("A valid declassification transition is reported to be invalid.");
		}
		
	}

	/**
	 * Test method for {@link petrinet.snet.DeclassificationTransition#isDeclassificator()}.
	 */
	@Test
	public void testIsDeclassificator() {
		assertTrue(td.isDeclassificator());
	}

//	/**
//	 * Test method for {@link petrinet.snet.DeclassificationTransition#DeclassificationTransition(petrinet.snet.SNet, java.lang.String, java.lang.String, boolean)}.
//	 */
//	@Test
//	public void testDeclassificationTransitionSNetStringStringBoolean() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.DeclassificationTransition#DeclassificationTransition(petrinet.snet.SNet, java.lang.String, boolean)}.
//	 */
//	@Test
//	public void testDeclassificationTransitionSNetStringBoolean() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.DeclassificationTransition#DeclassificationTransition(petrinet.snet.SNet, java.lang.String, java.lang.String)}.
//	 */
//	@Test
//	public void testDeclassificationTransitionSNetStringString() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.DeclassificationTransition#DeclassificationTransition(petrinet.snet.SNet, java.lang.String)}.
//	 */
//	@Test
//	public void testDeclassificationTransitionSNetString() {
//		fail("Not yet implemented");
//	}

}
