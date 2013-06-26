/**
 * 
 */
package petrinet.ifnet.test;

import static org.junit.Assert.*;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import constraint.AbstractConstraint;
import constraint.NumberConstraint;
import constraint.NumberOperator;
import constraint.StringConstraint;
import constraint.StringOperator;

import exception.PNValidationException;

import petrinet.ifnet.AccessMode;
import petrinet.ifnet.DeclassificationTransition;
import petrinet.ifnet.GuardDataContainer;
import petrinet.ifnet.RegularIFNetTransition;
import petrinet.ifnet.IFNet;
import petrinet.ifnet.IFNetPlace;
import types.Multiset;
import validate.ParameterException;

/**
 * @author boehr
 *
 */
public class RegularIFNetTransitionTest {
	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#RegularSNetTransition(java.lang.String)}.
	 * Test whether the name is set correctly.
	 */
	@Test
	public void testRegularSNetTransitionString() {
		RegularIFNetTransition rst = null;
		try { 
			  rst = new RegularIFNetTransition("TransitionName");
		} catch (ParameterException e) {
			fail("Unable to create RegularSNetTransition.");
		}
		
		//check that the name got set
		assertEquals("TransitionName", rst.getName());
		assertEquals("TransitionName", rst.getLabel());
		assertFalse(rst.isSilent());
		
	}
	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#RegularSNetTransition(java.lang.String, boolean)}.
	 */
	@Test
	public void testRegularSNetTransitionStringBoolean() {
		RegularIFNetTransition rst = null;
		try {
			  rst = new RegularIFNetTransition("TransitionName", true);
		} catch (ParameterException e) {
			fail("Unable to create RegularSNetTransition.");
		}
		
		//check that the name got set
		assertEquals("TransitionName", rst.getName());
		assertEquals("TransitionName", rst.getLabel());
		assertTrue(rst.isSilent());
	}
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#RegularSNetTransition(java.lang.String, java.lang.String, boolean)}.
	 */
	@Test
	public void testRegularSNetTransitionStringStringBoolean() {
		RegularIFNetTransition rst = null;
		try {
			  rst = new RegularIFNetTransition("TransitionName", "TransitionLabel", true);
		} catch (ParameterException e) {
			fail("Unable to create RegularSNetTransition.");
		}
		
		//check that the name got set
		assertEquals("TransitionName", rst.getName());
		assertEquals("TransitionLabel", rst.getLabel());
		assertTrue(rst.isSilent());
	}

	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#RegularSNetTransition(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRegularSNetTransitionStringString() {
		RegularIFNetTransition rst = null;
		try {
			  rst = new RegularIFNetTransition("TransitionName", "TransitionLabel");
		} catch (ParameterException e) {
			fail("Unable to create RegularSNetTransition.");
		}
		
		//check that the name got set
		assertEquals("TransitionName", rst.getName());
		assertEquals("TransitionLabel", rst.getLabel());
		assertFalse(rst.isSilent());
	}


	

	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#checkValidity()}.
	 * Check whether a valid transition is reported to be valid. 
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityValid() throws ParameterException {
		
		//Get a simple standard SNet		
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		
		RegularIFNetTransition rt = (RegularIFNetTransition)sNet.getTransition("t0");
		try {
			rt.checkValidity();
		} catch (PNValidationException e1) {
			fail("CheckValidity throws an exception for a valid SNet transition.");
		}
		
	}
	
	
	
	
	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#checkValidity()}.
	 * Check whether a transition with create and delete is reported to be valid. 
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityCreateDelete() throws ParameterException {
		
		//Get a simple standard SNet and one of its transtitions		
		IFNet sNet = IFNetTestUtil.createSimpleSnet();		
		RegularIFNetTransition rt = (RegularIFNetTransition)sNet.getTransition("t0");
		
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
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#checkValidity()}.
	 * Check whether a transition with create produces but does not consume a token with 
	 * a certain color. 
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityCreate() throws ParameterException {
		
		//Get a simple standard SNet and one of its transitions		
		IFNet sNet = IFNetTestUtil.createSimpleSnet();		
		RegularIFNetTransition rt = (RegularIFNetTransition)sNet.getTransition("t0");
		
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
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#checkValidity()}.
	 * Check whether a transition with delete consumes but does not produce a token with 
	 * a certain color. 
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityDelete() throws ParameterException {
		
		//Get a simple standard SNet and one of its transitions		
		IFNet sNet = IFNetTestUtil.createSimpleSnet();		
		RegularIFNetTransition rt = (RegularIFNetTransition)sNet.getTransition("t0");
		
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
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#checkValidity()}.
	 * Check whether a transition with neither creates nor deletes on a certain token color
	 * process that color.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityProcess() throws ParameterException {
		
		//Get a simple standard SNet and one of its transitions		
		IFNet sNet = IFNetTestUtil.createSimpleSnet();		
		RegularIFNetTransition rt = (RegularIFNetTransition)sNet.getTransition("t0");
		
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
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#checkState()}.
	 * Check whether an enabled transition is reported to be enabled
	 * and a disable transition to be disabled.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckState() throws ParameterException {
		
		//Get a simple standard SNet and one of its transitions		
		IFNet sNet = IFNetTestUtil.createSimpleSnet();		
		RegularIFNetTransition disabledTrans = (RegularIFNetTransition)sNet.getTransition("t0");
		RegularIFNetTransition enabledTrans = (RegularIFNetTransition)sNet.getTransition("tIn");
		
		//check the states
		disabledTrans.checkState();
		assertFalse("A disabled transition is reported to be enabled",disabledTrans.isEnabled());
		enabledTrans.checkState();
		assertTrue("An enabled transition is reported to be disabled",enabledTrans.isEnabled());
	}
	
	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#checkState()}.
	 * Check whether a disabled transition which is disabled because of
	 * to few space in one of its output places is disabled.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckStateNotEnoughOutputSpace() throws ParameterException {
		
		//Get a simple standard SNet ...
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("tIn");
		
		//check the state is enabled initialy		
		trans.checkState();
		assertTrue("An enabled transition is reported to be disabled",trans.isEnabled());
		
		
		//... add a green token to place p0
		IFNetPlace p0 = sNet.getPlace("p0");
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
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#checkState()}.
	 * Check whether an enabled transition is reported to be enabled
	 * and a disable transition to be disabled based on guards
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckStateGuard() throws ParameterException {
		
		//Get a simple standard SNet and two of its transitions		
		IFNet sNet = IFNetTestUtil.createSimpleSnet();				
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("tIn");
		
		//create aGuardDataContainer
		TestGuardDataContainer tgdc = new TestGuardDataContainer(sNet.getTokenColors());
		
		//create two guards
		NumberConstraint trueConstraint = new NumberConstraint("green", NumberOperator.IN_INTERVAL, -1,1);
		NumberConstraint falseConstraint = new NumberConstraint("green", NumberOperator.IN_INTERVAL, 2,3);
		
		//check state with true constraint		
		trans.setGuardDataContainer(tgdc);
		trans.addGuard(trueConstraint);
		trans.checkState();
		assertTrue("An enabled transition is reported to be disabled",trans.isEnabled());
		
		//check state with additional false constraint		
		trans.addGuard(falseConstraint);
		trans.checkState();
		assertFalse("An disabled transition is reported to be enabled",trans.isEnabled());
		
		
	}

	

	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#isDeclassificator()}.
	 * @throws ParameterException 
	 */
	@Test
	public void testIsDeclassificator() throws ParameterException {
		
		//Get a simple standard SNet ...
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("tIn");
		assertFalse("A regular transtion seems to be a Declassificator", trans.isDeclassificator());
	
		
	}

	


	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#setGuardDataContainer(petrinet.ifnet.GuardDataContainer)}.
	 * Try to set an valid instance of TestGuradDataContainer as a GuardDataContainer.
	 * @throws ParameterException 
	 */
	@Test
	public void testSetGuardDataContainer() throws ParameterException {
		
		//create the simple standard SNet
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
		
		//Create an instance of TestGuardDataContainer
		TestGuardDataContainer tgdc = new TestGuardDataContainer(sNet.getTokenColors());		
		trans.setGuardDataContainer(tgdc);
		
		//Test is ok if no exception gets thrown!
	}
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#setGuardDataContainer(petrinet.ifnet.GuardDataContainer)}.
	 * Try to set an invalid instance of TestGuradDataContainer as a GuardDataContainer.
	 * @throws ParameterException 
	 */
	@Test(expected=ParameterException.class)
	public void testSetGuardDataContainerInvalid() throws ParameterException {
		
		//create the simple standard SNet
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
		
		
		//Create an invald instance of TestGuardDataContainer
		Set<String> colors = sNet.getTokenColors();
		colors.remove("green");
		TestGuardDataContainer tgdc = new TestGuardDataContainer(colors);		
		trans.setGuardDataContainer(tgdc);
		
		//Test is ok if a parameter exception is thrown
	}
	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#setGuardDataContainer(petrinet.ifnet.GuardDataContainer)}.
	 * Try to set an valid instance of TestGuradDataContainer as a GuardDataContainer.
	 * Add Gurds additionally to check whether the gurads and the container fit.
	 * @throws ParameterException 
	 */
	@Test
	public void testSetGuardDataContainerGuardFit() throws ParameterException {
		
		//create the simple standard SNet
		IFNet sNet = null;
		try {
			sNet = IFNetTestUtil.createSimpleSnet();
		} catch (ParameterException e) {
			fail("Cannot create SNet");
		}
		
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
		
		//Create an instance of TestGuardDataContainer (must be added before guard)
		TestGuardDataContainer tgdc = new TestGuardDataContainer(sNet.getTokenColors());		
		try {
			trans.setGuardDataContainer(tgdc);
		} catch (ParameterException e) {
			fail("Exception while setting a vaild GuardDataContainer");
		}
		
		//add guards
		NumberConstraint guard = null;
		try {
			guard = new NumberConstraint("green", NumberOperator.NOT_IN_INTERVAL, -1, 1);
		} catch (ParameterException e) {
			fail("Cannot create NumberConstraint");
		}
		
		try {
			trans.addGuard(guard);
		} catch (ParameterException e) {
			fail("Cannot add a valid guard!");
		}
		
		
		
		
		//Create an instance of TestGuardDataContainer
		//check whether the data container is compatible with the guard
		TestGuardDataContainer tgdc2 = new TestGuardDataContainer(sNet.getTokenColors(), String.class);		
		try {
			trans.setGuardDataContainer(tgdc2);
			fail("Invaild GuardDataContainer was set successfully!");
		} catch (ParameterException e) {
			
		}
		
	
		
		
	}
	
	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#setAccessMode(java.lang.String, java.util.Collection)}.
	 * @throws ParameterException 
	 */
	@Test
	public void testSetAccessModeStringCollectionOfAccessMode() throws ParameterException {
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("tIn");
		
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

	
	
	

	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#addGuard(constraint.AbstractConstraint)}.
	 * Try to add various invalid guards
	 * @throws ParameterException 
	 */
	@Test
	public void testAddGuard(){
		IFNet sNet=null;
		try {
			sNet = IFNetTestUtil.createSimpleSnet();
		} catch (ParameterException e) {
			fail("Cannot create SNet");
		}
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("tIn");
		
		//Try to add an guard before adding a guraddatecontainer				
		try {
			NumberConstraint nc = new NumberConstraint("green", NumberOperator.IN_INTERVAL, -1,1);
			trans.addGuard(nc);
			fail("Guard was added without a GuardDataContainer beeing set.");
		} catch (ParameterException e) {}
		
		//Add the missing guard datacontainer
		TestGuardDataContainer tgdc = new TestGuardDataContainer(sNet.getTokenColors());
		try {
			trans.setGuardDataContainer(tgdc);
		} catch (ParameterException e1) {
			fail("Cannot set GuardDataContainer!");
		}
		
		//Try to add an guard which contains colors not processed by the transition
		try {
			NumberConstraint nc = new NumberConstraint("pink", NumberOperator.IN_INTERVAL, -1,1);
			trans.addGuard(nc);
			fail("Guard was added with colors not processed by the transtion");
		} catch (ParameterException e) {}
		
	
		
		//Try to add an guard which has no values incommen with the data container
		try {
			NumberConstraint nc = new NumberConstraint("green", NumberOperator.IN_INTERVAL, -1,1);
			RegularIFNetTransition t0 = (RegularIFNetTransition)sNet.getTransition("t0");
			t0.setGuardDataContainer(tgdc);
			tgdc.removeAttribute("green");			
			t0.addGuard(nc);
			fail("Guard was added with colors not contained in the GuardDataContainer");
		} catch (ParameterException e) {
		}
		
		//Try to add an guard which is not compatible
		try {
			StringConstraint sc = new StringConstraint("green", StringOperator.EQUAL, "fail");
			
			RegularIFNetTransition t0 = (RegularIFNetTransition)sNet.getTransition("t0");
			t0.setGuardDataContainer(new TestGuardDataContainer(sNet.getTokenColors()));
			tgdc.removeAttribute("green");			
			t0.addGuard(sc);
			fail("Guard was added which has values of the wrong type.");
		} catch (ParameterException e) {			
		}	
		 
		
	}  

	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#removeGuard(constraint.AbstractConstraint)}.
	 * @throws ParameterException 
	 */
	@Test
	public void testRemoveGuard() throws ParameterException {
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("tIn");
		
		//Get the gurads and check there are no guards initially
		Set<AbstractConstraint<?>> guards = trans.getGuards();
		assertTrue(guards.isEmpty());
		
		//add a guard and make sure it is really there
		trans.setGuardDataContainer(new TestGuardDataContainer(sNet.getTokenColors()));
		NumberConstraint nc = new NumberConstraint("green", NumberOperator.LARGER, -3); 
		trans.addGuard(nc);
		assertFalse(guards.isEmpty());
		
		//remove it again
		trans.removeGuard(nc);
		assertTrue(guards.isEmpty());
	}

	
	

	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#getAccessModes()}.
	 * Check whether the access modes are returned correctly.
	 * @throws ParameterException 
	 */
	@Test
	public void testGetAccessModes() throws ParameterException {
		
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
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
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#getAccessModes(java.lang.String)}.
	 * Check whether the access modes are returned correctly.
	 * @throws ParameterException 
	 */
	@Test
	public void testGetAccessModesString() throws ParameterException {
		
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
		
		
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
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#addAccessMode(java.lang.String, petrinet.ifnet.AccessMode[])}.
	 * Set some accessmodes. Use colors which already have access modes and new colors.
	 * @throws ParameterException 
	 */
	@Test
	public void testAddAccessModeStringAccessModeArray() throws ParameterException {
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
		
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

	
	
	


	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#removeAccessMode(java.lang.String, petrinet.ifnet.AccessMode[])}.
	 * @throws ParameterException 
	 */
	@Test
	public void testRemoveAccessModeStringAccessModeArray() throws ParameterException {
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
		
		//remove a non existent access mode				
		assertFalse("Removing an non existent access mode returned true", trans.removeAccessMode("pink", AccessMode.READ));
		
		
		//remove the read access
		trans.removeAccessMode("green", AccessMode.READ);
		Set<AccessMode> greenModes = trans.getAccessModes("green");
		assertTrue("An access mode got not removed", greenModes.isEmpty());
		
		//remove all access modes and then try to remove one more
		      
		trans.removeAccessModes("green");
		trans.removeAccessModes("red");
		trans.removeAccessModes("blue");
		assertFalse("Removing an non existent access mode returned true", trans.removeAccessModes("green"));
		
		
	}
	
	
	

	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#getProcessedColors(petrinet.ifnet.AccessMode[])}.
	 * @throws ParameterException 
	 */
	@Test
	public void testGetProcessedColorsAccessModeArray() throws ParameterException {
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
		
		//get the colors which are processed by t0 with read access
		Set<String> colors = trans.getProcessedAttributes(AccessMode.READ);
		
		Set<String> greenSet = new HashSet<String>();
		greenSet.add("green");
		assertTrue("Wrong colors returned.", colors.equals(greenSet));
		
	}


	
	
	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#getConsumedColors(petrinet.ifnet.AccessMode[])}.
	 * @throws ParameterException 
	 */
	@Test
	public void testGetConsumedColorsAccessModeArray() throws ParameterException {
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
		
		//get the colors which are processed by t0 with read access
		Set<String> colors = trans.getConsumedAttributes(AccessMode.DELETE);
		
		Set<String> redSet = new HashSet<String>();
		redSet.add("red");
		assertTrue("Wrong colors returned.", colors.equals(redSet));
	}

	
	
	

	/**
	 * Test method for {@link petrinet.ifnet.RegularIFNetTransition#getProducedColors(petrinet.ifnet.AccessMode[])}.
	 * @throws ParameterException 
	 */
	@Test
	public void testGetProducedColorsAccessModeArray() throws ParameterException {
		IFNet sNet = IFNetTestUtil.createSimpleSnet();
		RegularIFNetTransition trans = (RegularIFNetTransition)sNet.getTransition("t0");
		
		//get the colors which are processed by t0 with read access
		Set<String> colors = trans.getProducedAttributes(AccessMode.CREATE);
		
		Set<String> blueSet = new HashSet<String>();
		blueSet.add("blue");
		assertTrue("Wrong colors returned.", colors.equals(blueSet));
	}
	
	


}
