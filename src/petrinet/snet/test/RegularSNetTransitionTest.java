/**
 * 
 */
package petrinet.snet.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.PNValidationException;

import petrinet.snet.RegularSNetTransition;
import petrinet.snet.SNet;
import validate.ParameterException;

/**
 * @author boehr
 *
 */
public class RegularSNetTransitionTest {
	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#RegularSNetTransition(java.lang.String)}.
	 * Test whether the name is set correctly.
	 */
	@Test
	public void testRegularSNetTransitionString() {
		RegularSNetTransition rst = null;
		try {
			  rst = new RegularSNetTransition("TransitionName");
		} catch (ParameterException e) {
			fail("Unable to create RegularSNetTransition.");
		}
		
		//check that the name got set
		assertEquals("TransitionName", rst.getName());
		assertEquals("TransitionName", rst.getLabel());
		assertFalse(rst.isSilent());
		
	}
	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#RegularSNetTransition(java.lang.String, boolean)}.
	 */
	@Test
	public void testRegularSNetTransitionStringBoolean() {
		RegularSNetTransition rst = null;
		try {
			  rst = new RegularSNetTransition("TransitionName", true);
		} catch (ParameterException e) {
			fail("Unable to create RegularSNetTransition.");
		}
		
		//check that the name got set
		assertEquals("TransitionName", rst.getName());
		assertEquals("TransitionName", rst.getLabel());
		assertTrue(rst.isSilent());
	}
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#RegularSNetTransition(java.lang.String, java.lang.String, boolean)}.
	 */
	@Test
	public void testRegularSNetTransitionStringStringBoolean() {
		RegularSNetTransition rst = null;
		try {
			  rst = new RegularSNetTransition("TransitionName", "TransitionLabel", true);
		} catch (ParameterException e) {
			fail("Unable to create RegularSNetTransition.");
		}
		
		//check that the name got set
		assertEquals("TransitionName", rst.getName());
		assertEquals("TransitionLabel", rst.getLabel());
		assertTrue(rst.isSilent());
	}

	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#RegularSNetTransition(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRegularSNetTransitionStringString() {
		RegularSNetTransition rst = null;
		try {
			  rst = new RegularSNetTransition("TransitionName", "TransitionLabel");
		} catch (ParameterException e) {
			fail("Unable to create RegularSNetTransition.");
		}
		
		//check that the name got set
		assertEquals("TransitionName", rst.getName());
		assertEquals("TransitionLabel", rst.getLabel());
		assertFalse(rst.isSilent());
	}


	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#checkValidity()}.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidity() throws ParameterException {
		
		//Get a simple standard SNet
		System.out.println("vor");
		SNet sNet = SNetTestUtil.createSimpleSnet();
		System.out.println("nach");
		
		try {
			sNet.checkValidity();
		} catch (PNValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#checkState()}.
	 */
	@Test
	public void testCheckState() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#isDeclassificator()}.
	 */
	@Test
	public void testIsDeclassificator() {
		fail("Not yet implemented");
	}

	


	

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#setGuardDataContainer(petrinet.snet.GuardDataContainer)}.
	 */
	@Test
	public void testSetGuardDataContainer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#setAccessMode(java.lang.String, java.util.Collection)}.
	 */
	@Test
	public void testSetAccessModeStringCollectionOfAccessMode() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#setAccessMode(java.lang.String, petrinet.snet.AccessMode[])}.
	 */
	@Test
	public void testSetAccessModeStringAccessModeArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#addGuard(constraint.AbstractConstraint)}.
	 */
	@Test
	public void testAddGuard() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#removeGuard(constraint.AbstractConstraint)}.
	 */
	@Test
	public void testRemoveGuard() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getGuards()}.
	 */
	@Test
	public void testGetGuards() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getAccessModes()}.
	 */
	@Test
	public void testGetAccessModes() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getAccessModes(java.lang.String)}.
	 */
	@Test
	public void testGetAccessModesString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#addAccessMode(java.lang.String, petrinet.snet.AccessMode[])}.
	 */
	@Test
	public void testAddAccessModeStringAccessModeArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#addAccessMode(java.lang.String, java.util.Collection)}.
	 */
	@Test
	public void testAddAccessModeStringCollectionOfAccessMode() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#removeAccessMode(java.lang.String, petrinet.snet.AccessMode[])}.
	 */
	@Test
	public void testRemoveAccessModeStringAccessModeArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#removeAccessMode(java.lang.String, java.util.Collection)}.
	 */
	@Test
	public void testRemoveAccessModeStringCollectionOfAccessMode() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#removeAccessModes(java.lang.String)}.
	 */
	@Test
	public void testRemoveAccessModes() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getProcessedColors(petrinet.snet.AccessMode[])}.
	 */
	@Test
	public void testGetProcessedColorsAccessModeArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getProcessedColors(java.util.Collection)}.
	 */
	@Test
	public void testGetProcessedColorsCollectionOfAccessMode() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getConsumedColors(petrinet.snet.AccessMode[])}.
	 */
	@Test
	public void testGetConsumedColorsAccessModeArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getConsumedColors(java.util.Collection)}.
	 */
	@Test
	public void testGetConsumedColorsCollectionOfAccessMode() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getProducedColors(petrinet.snet.AccessMode[])}.
	 */
	@Test
	public void testGetProducedColorsAccessModeArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getProducedColors(java.util.Collection)}.
	 */
	@Test
	public void testGetProducedColorsCollectionOfAccessMode() {
		fail("Not yet implemented");
	}

}
