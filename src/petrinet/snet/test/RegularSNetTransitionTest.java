/**
 * 
 */
package petrinet.snet.test;

import static org.junit.Assert.*;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.PNValidationException;

import petrinet.snet.AccessMode;
import petrinet.snet.DeclassificationTransition;
import petrinet.snet.RegularSNetTransition;
import petrinet.snet.SNet;
import petrinet.snet.SNetPlace;
import types.Multiset;
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
	 * Test method for {@link petrinet.snet.RegularSNetTransition#checkValidity()}.
	 * Check whether a valid transition is reported to be valid. 
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityValid() throws ParameterException {
		
		//Get a simple standard SNet		
		SNet sNet = SNetTestUtil.createSimpleSnet();
		
		RegularSNetTransition rt = (RegularSNetTransition)sNet.getTransition("t0");
		try {
			rt.checkValidity();
		} catch (PNValidationException e1) {
			fail("CheckValidity throws an exception for a valid SNet transition.");
		}
		
		
	
		

	}
	
	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#checkValidity()}.
	 * Check whether a transition with create and delete is reported to be valid. 
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityCreateDelete() throws ParameterException {
		
		//Get a simple standard SNet and one of its transtitions		
		SNet sNet = SNetTestUtil.createSimpleSnet();		
		RegularSNetTransition rt = (RegularSNetTransition)sNet.getTransition("t0");
		
		//Set "create" and "delete" mode for one color
		HashSet<AccessMode> cdAccess = new HashSet<AccessMode>();
		cdAccess.add(AccessMode.DELETE);
		cdAccess.add(AccessMode.CREATE);
		rt.setAccessMode("pink",cdAccess);
		 
		
		try {
			rt.checkValidity();
			fail("A transition with create and delete on the same color is not valid!.");
		} catch (PNValidationException e1) {			
		}
		
	}
	


	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#checkValidity()}.
	 * Check whether a transition with create produces but does not consume a token with 
	 * a certain color. 
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityCreate() throws ParameterException {
		
		//Get a simple standard SNet and one of its transitions		
		SNet sNet = SNetTestUtil.createSimpleSnet();		
		RegularSNetTransition rt = (RegularSNetTransition)sNet.getTransition("t0");
		
		//Set "create" to a consumed color
		HashSet<AccessMode> cAccess = new HashSet<AccessMode>();		
		cAccess.add(AccessMode.CREATE);
		rt.setAccessMode("green",cAccess);
		 
		
		try {
			rt.checkValidity();
			fail("A transition with create on a certain color cannot consume this color!");
		} catch (PNValidationException e1) {			
		}
		
		//clean up ...
		cAccess.clear();
		cAccess.add(AccessMode.READ);
		rt.setAccessMode("green",cAccess); 
		
		
		//set create to a color which is not produced
		//Set "create" to a consumed color
		HashSet<AccessMode> cAccess2 = new HashSet<AccessMode>();		
		cAccess2.add(AccessMode.CREATE);
		rt.setAccessMode("pink",cAccess2);
		
		try {
			rt.checkValidity();
			fail("A transition with create on a certain color must produce this color!");
		} catch (PNValidationException e1) {			
		}
		
	}
	
	
	
	

	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#checkValidity()}.
	 * Check whether a transition with delete consumes but does not produce a token with 
	 * a certain color. 
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityDelete() throws ParameterException {
		
		//Get a simple standard SNet and one of its transitions		
		SNet sNet = SNetTestUtil.createSimpleSnet();		
		RegularSNetTransition rt = (RegularSNetTransition)sNet.getTransition("t0");
		
		//Set "delete" to a produced color
		HashSet<AccessMode> dAccess = new HashSet<AccessMode>();		
		dAccess.add(AccessMode.DELETE);
		rt.setAccessMode("blue",dAccess);
		 
		
		try {
			rt.checkValidity();
			fail("A transition with delete on a certain color cannot produce this color!");
		} catch (PNValidationException e1) {			
		}
		
		//clean up ...
		dAccess.clear();
		dAccess.add(AccessMode.CREATE);
		rt.setAccessMode("blue",dAccess);  
		
		
		//set delete to a color which is not consumed
		HashSet<AccessMode> dAccess2 = new HashSet<AccessMode>();		
		dAccess2.add(AccessMode.DELETE);
		rt.setAccessMode("pink",dAccess2);
		
		try {
			rt.checkValidity();
			fail("A transition with delete on a certain color must consume this color!");
		} catch (PNValidationException e1) {			
		}
		
	}
	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#checkValidity()}.
	 * Check whether a transition with neither creates nor deletes on a certain token color
	 * process that color.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityProcess() throws ParameterException {
		
		//Get a simple standard SNet and one of its transitions		
		SNet sNet = SNetTestUtil.createSimpleSnet();		
		RegularSNetTransition rt = (RegularSNetTransition)sNet.getTransition("t0");
		
		//Set "read" to a consumed but not produced color
		HashSet<AccessMode> rAccess = new HashSet<AccessMode>();		
		rAccess.add(AccessMode.READ);
		rt.setAccessMode("red",rAccess);
		 
		
		try {
			rt.checkValidity();
			fail("A token color which is only read must be consumed and produced.");
		} catch (PNValidationException e1) {			
		}
						
	}

	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#checkState()}.
	 * Check whether an enabled transition is reported to be enabled
	 * and a disable transition to be disabled.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckState() throws ParameterException {
		
		//Get a simple standard SNet and one of its transitions		
		SNet sNet = SNetTestUtil.createSimpleSnet();		
		RegularSNetTransition disabledTrans = (RegularSNetTransition)sNet.getTransition("t0");
		RegularSNetTransition enabledTrans = (RegularSNetTransition)sNet.getTransition("tIn");
		
		//check the states
		disabledTrans.checkState();
		assertFalse("A disabled transition is reported to be enabled",disabledTrans.isEnabled());
		enabledTrans.checkState();
		assertTrue("An enabled transition is reported to be disabled",enabledTrans.isEnabled());
	}
	
	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#checkState()}.
	 * Check whether a disabled transition which is disabled because of
	 * to few space in one of its output places is disabled.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckStateNotEnoughOutputSpace() throws ParameterException {
		
		//Get a simple standard SNet ...
		SNet sNet = SNetTestUtil.createSimpleSnet();
		RegularSNetTransition trans = (RegularSNetTransition)sNet.getTransition("tIn");
		
		//check the state is enabled initialy		
		trans.checkState();
		assertTrue("An enabled transition is reported to be disabled",trans.isEnabled());
		
		
		//... add a green token to place p0
		SNetPlace p0 = sNet.getPlace("p0");
		p0.setState(new Multiset<String>("green"));
		
		//check the state of a now disabled transition		
		trans.checkState();
		assertFalse("A disabled transition is reported to be enabled",trans.isEnabled());
		
		
		//removing of green enables the transition again
		p0.getState().clear();
		trans.checkState();
		assertTrue("An enabled transition is reported to be disabled",trans.isEnabled());
	}	
	
	

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#isDeclassificator()}.
	 * @throws ParameterException 
	 */
	@Test
	public void testIsDeclassificator() throws ParameterException {
		
		//Get a simple standard SNet ...
		SNet sNet = SNetTestUtil.createSimpleSnet();
		RegularSNetTransition trans = (RegularSNetTransition)sNet.getTransition("tIn");
		assertFalse("A regular transtion seems to be a Declassificator", trans.isDeclassificator());
	
		
	}

	

	
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#setGuardDataContainer(petrinet.snet.GuardDataContainer)}.
//	 */
//	@Test
//	public void testSetGuardDataContainer() {
//		fail("Not yet implemented");
//	}
//
	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#setAccessMode(java.lang.String, java.util.Collection)}.
	 * @throws ParameterException 
	 */
	@Test
	public void testSetAccessModeStringCollectionOfAccessMode() throws ParameterException {
		SNet sNet = SNetTestUtil.createSimpleSnet();
		RegularSNetTransition trans = (RegularSNetTransition)sNet.getTransition("tIn");
		
		//set access mode
		trans.setAccessMode("pink", AccessMode.READ, AccessMode.CREATE);
		Map<String, Set<AccessMode>> modes = trans.getAccessModes();
		Set<AccessMode> pinkAccess = modes.get("pink");
		assertTrue("A given accessmode (READ) was not set.", pinkAccess.contains(AccessMode.READ));
		assertTrue("A given accessmode (CREATE) was not set.", pinkAccess.contains(AccessMode.CREATE));
		
		//Check there are no other modes
		pinkAccess.remove(AccessMode.READ);
		pinkAccess.remove(AccessMode.CREATE);
		assertTrue("A non given accessmode was set.", pinkAccess.isEmpty());
		
	}

	
	
	
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#addGuard(constraint.AbstractConstraint)}.
//	 */
//	@Test
//	public void testAddGuard() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#removeGuard(constraint.AbstractConstraint)}.
//	 */
//	@Test
//	public void testRemoveGuard() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#getGuards()}.
//	 */
//	@Test
//	public void testGetGuards() {
//		fail("Not yet implemented");
//	}
//
	
	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getAccessModes()}.
	 * Check whether the access modes are returned correctly.
	 * @throws ParameterException 
	 */
	@Test
	public void testGetAccessModes() throws ParameterException {
		
		SNet sNet = SNetTestUtil.createSimpleSnet();
		RegularSNetTransition trans = (RegularSNetTransition)sNet.getTransition("t0");
		Map<String, Set<AccessMode>> modes = trans.getAccessModes();
		
		//contained
		Set<AccessMode> greenAccess = modes.get("green");
		Set<AccessMode> redAccess = modes.get("red");
		Set<AccessMode> blueAccess = modes.get("blue");

		
		assertTrue("A given accessmode was not set.", greenAccess.contains(AccessMode.READ));
		assertTrue("A given accessmode was not set.", redAccess.contains(AccessMode.DELETE));
		assertTrue("A given accessmode was not set.", blueAccess.contains(AccessMode.CREATE));
		
		
	}

	
	
	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#getAccessModes(java.lang.String)}.
	 * Check whether the access modes are returned correctly.
	 * @throws ParameterException 
	 */
	@Test
	public void testGetAccessModesString() throws ParameterException {
		
		SNet sNet = SNetTestUtil.createSimpleSnet();
		RegularSNetTransition trans = (RegularSNetTransition)sNet.getTransition("t0");
		
		
		//contained
		Set<AccessMode> greenAccess = trans.getAccessModes("green");
		Set<AccessMode> redAccess = trans.getAccessModes("red");
		Set<AccessMode> blueAccess = trans.getAccessModes("blue");
		Set<AccessMode> pinkAccess = trans.getAccessModes("pink");

		
		assertTrue("A given accessmode was not set.", greenAccess.contains(AccessMode.READ));
		assertTrue("A given accessmode was not set.", redAccess.contains(AccessMode.DELETE));
		assertTrue("A given accessmode was not set.", blueAccess.contains(AccessMode.CREATE));
		assertTrue("A non given accessmode was set.", pinkAccess.isEmpty());
	}
	
	
	

	/**
	 * Test method for {@link petrinet.snet.RegularSNetTransition#addAccessMode(java.lang.String, petrinet.snet.AccessMode[])}.
	 * Set some accessmodes. Use colors which already have access modes and new colors.
	 * @throws ParameterException 
	 */
	@Test
	public void testAddAccessModeStringAccessModeArray() throws ParameterException {
		SNet sNet = SNetTestUtil.createSimpleSnet();
		RegularSNetTransition trans = (RegularSNetTransition)sNet.getTransition("t0");
		
		HashSet<AccessMode> createMode = new HashSet<AccessMode>();
		HashSet<AccessMode> deleteMode = new HashSet<AccessMode>();
		createMode.add(AccessMode.CREATE);
		deleteMode.add(AccessMode.DELETE);
		
		
		trans.addAccessMode("green", createMode);
		trans.addAccessMode("pink", deleteMode);
		
		//Check all are contained
		Set<AccessMode> greenModes = trans.getAccessModes("green");
		assertTrue("An access mode is missing", greenModes.contains(AccessMode.CREATE));
		assertTrue("An access mode is missing", greenModes.contains(AccessMode.READ));
		Set<AccessMode> pinkModes = trans.getAccessModes("pink");
		assertTrue("An access mode is missing", pinkModes.contains(AccessMode.DELETE));		
		
	}

	
	
	
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#addAccessMode(java.lang.String, java.util.Collection)}.
//	 */
//	@Test
//	public void testAddAccessModeStringCollectionOfAccessMode() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#removeAccessMode(java.lang.String, petrinet.snet.AccessMode[])}.
//	 */
//	@Test
//	public void testRemoveAccessModeStringAccessModeArray() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#removeAccessMode(java.lang.String, java.util.Collection)}.
//	 */
//	@Test
//	public void testRemoveAccessModeStringCollectionOfAccessMode() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#removeAccessModes(java.lang.String)}.
//	 */
//	@Test
//	public void testRemoveAccessModes() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#getProcessedColors(petrinet.snet.AccessMode[])}.
//	 */
//	@Test
//	public void testGetProcessedColorsAccessModeArray() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#getProcessedColors(java.util.Collection)}.
//	 */
//	@Test
//	public void testGetProcessedColorsCollectionOfAccessMode() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#getConsumedColors(petrinet.snet.AccessMode[])}.
//	 */
//	@Test
//	public void testGetConsumedColorsAccessModeArray() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#getConsumedColors(java.util.Collection)}.
//	 */
//	@Test
//	public void testGetConsumedColorsCollectionOfAccessMode() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#getProducedColors(petrinet.snet.AccessMode[])}.
//	 */
//	@Test
//	public void testGetProducedColorsAccessModeArray() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for {@link petrinet.snet.RegularSNetTransition#getProducedColors(java.util.Collection)}.
//	 */
//	@Test
//	public void testGetProducedColorsCollectionOfAccessMode() {
//		fail("Not yet implemented");
//	}

}
