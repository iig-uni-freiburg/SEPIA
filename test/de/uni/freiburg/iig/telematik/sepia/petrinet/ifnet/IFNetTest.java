package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.properties.cwn.CWNPropertyFlag;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.properties.validity.IFNetValidity;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.properties.validity.IFNetValidityCheckingCallableGenerator;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;

/**
 * @author boehr
 */
public class IFNetTest {

	// The variable is filled during start up
	private IFNet dSNet = null;
	@SuppressWarnings("unused")
	private DeclassificationTransition td = null;
	@SuppressWarnings("unused")
	private RegularIFNetTransition tr = null;

	@Before
	public void setUp() throws Exception {

		dSNet = IFNetTestUtil.createSimpleIFNetWithDeclassification();
		td = dSNet.getDeclassificationTransitions().iterator().next();
		tr = (RegularIFNetTransition) dSNet.getTransition("t0");
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#removeTransition(java.lang.String)}.
	 */
	@Test
	public void testRemoveTransition() {

		// remove the transition
		assertTrue(dSNet.containsTransition("t0"));
		dSNet.removeTransition("t0");
		assertFalse(dSNet.containsTransition("t0"));
		assertFalse(dSNet.removeTransition("t0"));
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#checkValidity()}.
	 */
	@SuppressWarnings("unused")
	@Test
	public void testCheckValidity() throws PNValidationException {
		// create the SNet
		IFNet ifNet = new IFNet();

		// add places with marking and capacity
		ifNet.addPlace("pIn");
		ifNet.getPlace("pIn").setColorCapacity("black", 1);

		ifNet.addPlace("p0");
		ifNet.getPlace("p0").setColorCapacity("black", 1);
		ifNet.getPlace("p0").setColorCapacity("green", 1);

		ifNet.addPlace("p1");
		ifNet.getPlace("p1").setColorCapacity("black", 1);
		ifNet.getPlace("p1").setColorCapacity("red", 1);

		ifNet.addPlace("p2");
		ifNet.getPlace("p2").setColorCapacity("black", 1);
		ifNet.getPlace("p2").setColorCapacity("green", 1);

		ifNet.addPlace("p3");
		ifNet.getPlace("p3").setColorCapacity("black", 1);
		ifNet.getPlace("p3").setColorCapacity("blue", 1);

		ifNet.addPlace("p4");
		ifNet.getPlace("p4").setColorCapacity("black", 1);
		ifNet.getPlace("p4").setColorCapacity("yellow", 1);

		ifNet.addPlace("p5");
		ifNet.getPlace("p5").setColorCapacity("black", 1);
		ifNet.getPlace("p5").setColorCapacity("green", 1);

		ifNet.addPlace("pOut");
		ifNet.getPlace("pOut").setColorCapacity("black", 1);

		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");

		IFNetMarking sm = new IFNetMarking();
		sm.set("pIn", pInMarking);
		ifNet.setInitialMarking(sm);

		// add the transitions
		ifNet.addTransition("tIn");
		ifNet.addTransition("t0");
		ifNet.addTransition("t1");
		ifNet.addDeclassificationTransition("td");
		ifNet.addTransition("tOut");

		// add flowrelations
		IFNetFlowRelation f1 = ifNet.addFlowRelationPT("p0", "t0");
		IFNetFlowRelation f2 = ifNet.addFlowRelationPT("p1", "t0");
		IFNetFlowRelation f3 = ifNet.addFlowRelationTP("t0", "p2");
		IFNetFlowRelation f4 = ifNet.addFlowRelationTP("t0", "p3");

		IFNetFlowRelation f5 = ifNet.addFlowRelationPT("pIn", "tIn");
		IFNetFlowRelation f6 = ifNet.addFlowRelationTP("tIn", "p0");
		IFNetFlowRelation f7 = ifNet.addFlowRelationTP("tIn", "p1");

		IFNetFlowRelation f8 = ifNet.addFlowRelationPT("p2", "t1");
		IFNetFlowRelation f9 = ifNet.addFlowRelationPT("p3", "t1");
		IFNetFlowRelation f10 = ifNet.addFlowRelationTP("t1", "p5");

		IFNetFlowRelation f13 = ifNet.addFlowRelationPT("p5", "td");
		IFNetFlowRelation f14 = ifNet.addFlowRelationTP("td", "p4");

		IFNetFlowRelation f11 = ifNet.addFlowRelationPT("p4", "tOut");
		IFNetFlowRelation f12 = ifNet.addFlowRelationTP("tOut", "pOut");

		// configure flow relations
		f1.addConstraint("green", 1);
		f2.addConstraint("red", 1);
		f3.addConstraint("green", 1);
		f4.addConstraint("blue", 1);
		f6.addConstraint("green", 1);
		f7.addConstraint("red", 1);
		f8.addConstraint("green", 1);
		f9.addConstraint("blue", 1);
		f10.addConstraint("green", 1);
		f11.addConstraint("yellow", 1);
		f13.addConstraint("green", 1);
		f14.addConstraint("yellow", 1);

		// configure read write
		RegularIFNetTransition rst = (RegularIFNetTransition) ifNet.getTransition("t0");
		rst.addAccessMode("green", AccessMode.READ);
		rst.addAccessMode("red", AccessMode.DELETE);
		rst.addAccessMode("blue", AccessMode.CREATE);

		// create labeling
		SOABase context = new SOABase("b");
		context.setSubjects(Arrays.asList("sh0", "sh1", "sh2", "sh3", "sl0"));
		context.setObjects(Arrays.asList("green", "red", "blue", "yellow", "black"));
		context.setActivities(Arrays.asList("tIn", "t0", "t1", "td", "tOut"));

		ACLModel acm = new ACLModel("acl", context);
		acm.addActivityPermission("sh0", "tIn");
		acm.addActivityPermission("sh1", "t0");
		acm.addActivityPermission("sh2", "td");
		acm.addActivityPermission("sh3", "t1");
		acm.addActivityPermission("sl0", "tOut");

		// Create a new analysis context
		AnalysisContext ac = new AnalysisContext("", acm, false);

		// Assign subjects to transitions
		ac.setSubjectDescriptor("tIn", "sh0");
		ac.setSubjectDescriptor("t0", "sh1");
		ac.setSubjectDescriptor("td", "sh2");
		ac.setSubjectDescriptor("t1", "sh3");
		ac.setSubjectDescriptor("tOut", "sl0");

		Labeling l = new Labeling("l", ac);

		// Set subject clearance
		l.setSubjectClearance("sh0", SecurityLevel.HIGH);
		l.setSubjectClearance("sh1", SecurityLevel.HIGH);
		l.setSubjectClearance("sh2", SecurityLevel.HIGH);
		l.setSubjectClearance("sh3", SecurityLevel.HIGH);
		l.setSubjectClearance("sl0", SecurityLevel.LOW);

		// set transition classification
		l.setActivityClassification("tIn", SecurityLevel.HIGH);
		l.setActivityClassification("t0", SecurityLevel.HIGH);
		l.setActivityClassification("t1", SecurityLevel.HIGH);
		l.setActivityClassification("td", SecurityLevel.HIGH);
		l.setActivityClassification("tOut", SecurityLevel.LOW);

		// set token color classification
		l.setAttributeClassification("green", SecurityLevel.HIGH);
		l.setAttributeClassification("red", SecurityLevel.HIGH);
		l.setAttributeClassification("blue", SecurityLevel.HIGH);
		l.setAttributeClassification("yellow", SecurityLevel.LOW);

		// set the labeling
		ac.setLabeling(l);
		ifNet.setAnalysisContext(ac);

		IFNetValidity.checkValidity(ifNet);
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#checkValidity()}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected = PNValidationException.class)
	public void testCheckNonValidity() throws PNValidationException {
		IFNetValidityCheckingCallableGenerator generator = new IFNetValidityCheckingCallableGenerator(dSNet);
		if (generator.getPropertyFlags().contains(CWNPropertyFlag.ACCEPT_REMAINING_CF_TOKENS))
			fail("By default, IF-nets should not accept remaining control flow tokens.");
		IFNetValidity.checkValidity(generator);
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#checkValidity()}. Try to set an invalid analysis context where
	 * subjects CREATEs a token with a classification different from the
	 * subjects clearance.
	 */
	@Test
	public void testCheckValidityAnalysisContextNonMatchingSecLevels() {
		// blue is created by transition t0. Both the transition and its subject
		// (sh1) are high.
		dSNet.getAnalysisContext().getLabeling().setAttributeClassification("blue", SecurityLevel.LOW);
		try {
			IFNetValidity.checkValidity(dSNet);
			fail("An invalid ifNet is not detected!");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#checkValidity()}. Try to set an invalid analysis context where the
	 * classification of a color created from a declassificationtransition is
	 * high.
	 */
	@SuppressWarnings("unused")
	@Test
	public void testCheckValidityAnalysisContextDeclassificationTransCreatesHighColor() {

		// blue is created by transition t0. Both the transition and its subject
		// (sh1) are high.
		dSNet.getAnalysisContext().getLabeling().setAttributeClassification("yellow", SecurityLevel.HIGH);
		try {
			IFNetValidity.checkValidity(dSNet);
			fail("An invalid ifNet  is not detected!");
		} catch (PNValidationException e) {
		}

		// create an declassification transition which creates a color for which
		// no label is set
		IFNetFlowRelation tdOutRel = null;
		IFNetFlowRelation tdInRel = null;
		for (IFNetFlowRelation f : dSNet.getFlowRelations()) {
			if (f.getSource().getName().equals("td")) {
				tdOutRel = f;
			}
			if (f.getTarget().getName().equals("td")) {
				tdInRel = f;
			}
		}
		// TODO do something with the unused flow relations?
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet#SNet()}.
	 */
	@Test
	public void testSNet() {
		IFNet sNet = new IFNet();

		assertTrue(sNet.getMarking().isEmpty());
		assertTrue(sNet.getInitialMarking().isEmpty());
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#SNet(java.util.Set, java.util.Set,
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking)}.
	 */
	@Test
	public void testSNetSetOfStringSetOfStringSNetMarking() {

		// create place transition and initial makring
		Set<String> places = new HashSet<String>();
		places.add("p0");
		places.add("p1");

		Set<String> transitions = new HashSet<String>();
		transitions.add("t0");
		transitions.add("t1");
		IFNetMarking initialMarking = new IFNetMarking();
		Multiset<String> placeMarking = new Multiset<String>();
		placeMarking.add("black");
		initialMarking.set("p0", placeMarking);

		// create the ifNet
		IFNet net = new IFNet(places, transitions, initialMarking);

		// check everything is set properly
		assertTrue(net.getPlaces().size() == 2);
		assertTrue(net.getPlaces().contains(net.getPlace("p0")));
		assertTrue(net.getPlaces().contains(net.getPlace("p1")));

		assertTrue(net.getTransitions().size() == 2);
		assertTrue(net.getTransitions().contains(net.getTransition("t0")));
		assertTrue(net.getTransitions().contains(net.getTransition("t1")));

		assertTrue(net.getInitialMarking().equals(initialMarking));
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#getRegularTransitions()}.
	 */
	@Test
	public void testGetRegularTransitions() {

		// get all regular transitions
		Collection<RegularIFNetTransition> transitions = dSNet.getRegularTransitions();

		// check that the amount is ok
		assertTrue(transitions.size() == 4);

		// set up a set with all names of regular transitons
		Set<String> transitionNames = new HashSet<String>();
		transitionNames.add("tIn");
		transitionNames.add("t0");
		transitionNames.add("tOut");
		transitionNames.add("t1");

		// check all regular transtions are present
		for (RegularIFNetTransition rt : transitions) {
			assertTrue(transitionNames.contains(rt.getName()));
		}
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#getDeclassificationTransitions()}.
	 */
	@Test
	public void testGetDeclassificationTransitions() {

		// get all regular transitions
		Collection<DeclassificationTransition> transitions = dSNet.getDeclassificationTransitions();

		// check that the amount is ok
		assertTrue(transitions.size() == 1);

		// check that the right transitions is found
		assertTrue(transitions.iterator().next().getName().equals("td"));
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#addDeclassificationTransition(java.lang.String)}.
	 */
	@Test
	public void testAddDeclassificationTransitionString() {

		dSNet.addDeclassificationTransition("td");
		dSNet.addDeclassificationTransition("td2");

		assertTrue(dSNet.getDeclassificationTransitions().size() == 2);

		Collection<DeclassificationTransition> dtransitions = dSNet.getDeclassificationTransitions();
		for (DeclassificationTransition dt : dtransitions) {
			assertTrue(dt.getName().equals("td") || dt.getName().equals("td2"));
		}
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#getSubjectDescriptors()}.
	 */
	@Test
	public void testGetSubjectDescriptors() {

		assertTrue(dSNet.getSubjectDescriptors().size() == 5);
		assertTrue(dSNet.getSubjectDescriptors().contains("sh0"));
		assertTrue(dSNet.getSubjectDescriptors().contains("sh1"));
		assertTrue(dSNet.getSubjectDescriptors().contains("sh2"));
		assertTrue(dSNet.getSubjectDescriptors().contains("sh3"));
		assertTrue(dSNet.getSubjectDescriptors().contains("sl0"));
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#getAnalysisContext()}.
	 */
	@Test
	public void testGetAnalysisContext() {

		assertTrue(dSNet.getAnalysisContext().getSubjectDescriptor("tIn").equals("sh0"));
		assertTrue(dSNet.getAnalysisContext().getSubjectDescriptor("tOut").equals("sh2"));
		assertTrue(dSNet.getAnalysisContext().getSubjectDescriptor("td").equals("sh3"));
		assertTrue(dSNet.getAnalysisContext().getSubjectDescriptor("t1").equals("sl0"));
	}

	/*
	 * Test method for {@link
	 * de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.
	 * IFNet#setAnalysisContext
	 * (de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext)}.
	 */
	@Test
	public void testSetAnalysisContext() {

		// create labeling
		SOABase context = new SOABase("base");
		context.setActivities(Arrays.asList("tIn", "t0", "tOut", "td", "t1"));
		context.setSubjects(Arrays.asList("sh0", "sh1", "sh2", "sh3", "sl0"));
		context.setObjects(Arrays.asList("black", "red", "blue", "green", "yellow"));

		ACLModel acm = new ACLModel("acm", context);
		acm.addActivityPermission("sh0", "tIn");
		acm.addActivityPermission("sh1", "t0");
		acm.addActivityPermission("sh2", "tOut");
		acm.addActivityPermission("sh3", "td");
		acm.addActivityPermission("sl0", "t1");

		// Create a new analysis context
		AnalysisContext ac = new AnalysisContext("ac", acm, false);

		// Assign subjects to transitions
		ac.setSubjectDescriptor("tIn", "sh0");
		ac.setSubjectDescriptor("t0", "sh1");
		ac.setSubjectDescriptor("tOut", "sh2");
		ac.setSubjectDescriptor("td", "sh3");
		ac.setSubjectDescriptor("t1", "sl0");

		Labeling l = new Labeling("l", ac);

		// Set subject clearance
		l.setSubjectClearance("sh0", SecurityLevel.HIGH);
		l.setSubjectClearance("sh1", SecurityLevel.HIGH);
		l.setSubjectClearance("sh2", SecurityLevel.HIGH);
		l.setSubjectClearance("sh3", SecurityLevel.HIGH);
		l.setSubjectClearance("sl0", SecurityLevel.LOW);

		// set transition classification
		l.setActivityClassification("tIn", SecurityLevel.HIGH);
		l.setActivityClassification("t0", SecurityLevel.HIGH);
		l.setActivityClassification("tOut", SecurityLevel.HIGH);
		l.setActivityClassification("td", SecurityLevel.HIGH);
		l.setActivityClassification("t1", SecurityLevel.LOW);

		// set token color classification
		l.setAttributeClassification("green", SecurityLevel.HIGH);
		l.setAttributeClassification("red", SecurityLevel.HIGH);
		l.setAttributeClassification("blue", SecurityLevel.HIGH);
		l.setAttributeClassification("yellow", SecurityLevel.LOW);

		// set the labeling
		dSNet.setAnalysisContext(ac);

		assertTrue(dSNet.getAnalysisContext().equals(ac));
	}

	/*
	 * Test the clone() method
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testIFNetClone() {
		/*
		 * Test equal IFNets
		 */
		IFNet ifnet1 = IFNetTestUtil.createSimpleIFNet();
		IFNet ifnet1clone = (IFNet) ifnet1.clone();
		assertNotSame(ifnet1, ifnet1clone);
		// Can't just test equality because of the different order of same
		// lists.
		// assertEquals(ifnet1, ifnet1clone);
		// Check equality for places
		assertEquals(ifnet1.getPlaces().size(), ifnet1clone.getPlaces().size());
		for (IFNetPlace p : ifnet1.getPlaces()) {
			assertTrue(ifnet1clone.getPlace(p.getName()) != null);
			assertNotSame(p, ifnet1clone.getPlace(p.getName()));
			assertEquals(p, ifnet1clone.getPlace(p.getName()));
		}
		// Check equality for transitions
		assertEquals(ifnet1.getTransitions().size(), ifnet1clone.getTransitions().size());
		for (AbstractIFNetTransition t : ifnet1.getTransitions()) {
			assertTrue(ifnet1clone.getTransition(t.getName()) != null);
			assertEquals(t, ifnet1clone.getTransition(t.getName()));
			assertNotSame(t, ifnet1clone.getTransition(t.getName()));
		}
		// Check equality for flow relations
		assertEquals(ifnet1.getFlowRelations().size(), ifnet1clone.getFlowRelations().size());
		for (IFNetFlowRelation f : ifnet1.getFlowRelations()) {
			IFNetFlowRelation flowRelationClone = null;
			for (IFNetFlowRelation fc : ifnet1clone.getFlowRelations()) {
				if (fc.getSource().equals(f.getSource()) && fc.getTarget().equals(f.getTarget()) && fc.getDirectionPT() == f.getDirectionPT() && fc.getConstraint().equals(f.getConstraint())) {
					flowRelationClone = fc;
				}
			}
			assertFalse(flowRelationClone == null);
			assertEquals(f, flowRelationClone);
			assertNotSame(f, flowRelationClone);
		}
		// Check equality for the initial marking
		assertEquals(ifnet1.getInitialMarking(), ifnet1clone.getInitialMarking());
		assertNotSame(ifnet1.getInitialMarking(), ifnet1clone.getInitialMarking());
	}
}
