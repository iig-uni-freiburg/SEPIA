package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;

public class LabelingTest {

	// The standard set of transitions
	HashSet<String> transitions = null;

	// The standard set of transitions
	HashSet<String> attributes = null;

	// The standard set of transitions
	HashSet<String> subjects = null;

	// The standard SNet
	IFNet sNet = null;

	// The standard Places
	Set<String> places = null;

	// The standard snet marking
	IFNetMarking m = null;
	
	SOABase context = null;
	ACLModel acm = null;
	AnalysisContext ac = null;

	@Before
	public void setUp() throws Exception {

		// create standard transitions
		transitions = new HashSet<String>();
		transitions.add("t0");
		transitions.add("t1");
		transitions.add("t2");
		transitions.add("t3");
		transitions.add("t4");

		// create standard transitions
		places = new HashSet<String>();
		places.add("p0");
		places.add("p1");
		places.add("p2");
		places.add("p3");
		places.add("p4");

		// create standard attributes
		attributes = new HashSet<String>();
		attributes.add("c0");
		attributes.add("c1");
		attributes.add("c2");
		attributes.add("c3");
		attributes.add("c4");

		// create standard subjects
		subjects = new HashSet<String>();
		subjects.add("s0");
		subjects.add("s1");
		subjects.add("s2");
		subjects.add("s3");
		subjects.add("s4");

		// The standard marking
		m = new IFNetMarking();
		Multiset<String> placeMarking = new Multiset<String>();
		placeMarking.add("black");
		m.set("p0", placeMarking);

		Multiset<String> placeMarking2 = new Multiset<String>();
		placeMarking2.add("green");
		m.set("p1", placeMarking2);

		// Create an SNet
		sNet = new IFNet(places, transitions, m);

		// SOABase
		SOABase context = new SOABase("base");
		context.setActivities(transitions);
		context.setObjects(attributes);
		context.setSubjects(subjects);

		// ACModel
		acm = new ACLModel("acm", context);

		// AC
		ac = new AnalysisContext("ac", acm, false);
	}

	@After
	public void tearDown() throws Exception {

		// destroy the standard elements
		transitions = null;
		places = null;
		attributes = null;
		subjects = null;
		m = null;
		sNet = null;

	}

	// ///////////////////////////////////////
	// Test the ifNet, Subjects constructor //
	// ///////////////////////////////////////
	@Test
	public void testSNetSubjectLabelingConstructor() {

		Labeling l1 = new Labeling("l", ac);
		// Check whether the activities are setup right
		assertEquals(l1.getDefaultSecurityLevel(), SecurityLevel.LOW);

		Labeling l2 = new Labeling("l", ac, SecurityLevel.HIGH);
		// Check whether the activities are setup right
		assertEquals(l2.getDefaultSecurityLevel(), SecurityLevel.HIGH);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Test the method for adding activities.
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testAddActivities() {

		// create an empty set of activities
		HashSet<String> emptyTransitionSet = new HashSet<String>();

		SOABase contextEmpty = new SOABase("baseEmptyActivities");
		contextEmpty.setActivities(emptyTransitionSet);
		contextEmpty.setObjects(attributes);
		contextEmpty.setSubjects(subjects);
		ACLModel acm2 = new ACLModel("acl", contextEmpty);
		AnalysisContext ac2 = new AnalysisContext("ac", acm2, true);
		Labeling l2 = new Labeling("l2", ac2);

		// There shouldn't be activities
		assertTrue(l2.getAnalysisContext().getACModel().getContext().getActivities().isEmpty());

		// try to add the same transition more than once
		Labeling l3 = new Labeling("l", ac);
		
		assertFalse(l2.equals(l3));

		try {
			l3.getAnalysisContext().getACModel().getContext().addActivities(Arrays.asList("t0", "t0", "t0", "t0"));
		} catch (ParameterException e) {
			fail("Exception while adding an transition to a labeling.");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Test the method for removing activities.
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testRemoveActivities() {

		// create a labeling
		Labeling l3 = new Labeling("l", ac);

		try {
			l3.getAnalysisContext().getACModel().getContext().removeActivity("t0");
		} catch (ParameterException e) {
			fail("Exception while adding an transition to a labeling.");
		}
		assertFalse(l3.getAnalysisContext().getACModel().getContext().getActivities().contains("t0"));
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Test the method for adding subjects.
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testAddSubjects() {

		// create an empty set of subjects
		HashSet<String> emptySubjectSet = new HashSet<String>();

		SOABase contextEmpty = new SOABase("baseEmptySubjects");
		contextEmpty.setActivities(transitions);
		contextEmpty.setObjects(attributes);
		contextEmpty.setSubjects(emptySubjectSet);
		ACLModel acm2 = new ACLModel("acl", contextEmpty);
		AnalysisContext ac2 = new AnalysisContext("ac", acm2, true);
		Labeling l2 = new Labeling("l2", ac2);

		// There shouldn't be activities
		assertTrue(l2.getAnalysisContext().getACModel().getContext().getSubjects().isEmpty());

		// try to add the same transition more than once
		Labeling l3 = new Labeling("l", ac);
		
		assertFalse(l2.equals(l3));

		try {
			l3.getAnalysisContext().getACModel().getContext().addSubjects(Arrays.asList("s0", "s0", "s0", "s0"));
		} catch (ParameterException e) {
			fail("Exception while adding an transition to a labeling.");
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Test the method for removing subjects.
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testRemoveSubjects() {

		// create a labeling
		Labeling l3 = new Labeling("l", ac);

		try {
			l3.getAnalysisContext().getACModel().getContext().removeSubject("s0");
		} catch (ParameterException e) {
			fail("Exception while adding an transition to a labeling.");
		}
		assertFalse(l3.getAnalysisContext().getACModel().getContext().getSubjects().contains("s0"));
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Test the method for adding attributes.
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testAddAttributes() {

		// create an empty set of activities
		HashSet<String> emptyAttributeSet = new HashSet<String>();

		SOABase contextEmpty = new SOABase("baseEmptyObjetcs");
		contextEmpty.setActivities(transitions);
		contextEmpty.setObjects(emptyAttributeSet);
		contextEmpty.setSubjects(subjects);
		ACLModel acm2 = new ACLModel("acl", contextEmpty);
		AnalysisContext ac2 = new AnalysisContext("ac", acm2, true);
		Labeling l2 = new Labeling("l2", ac2);

		// There shouldn't be activities
		assertTrue(l2.getAnalysisContext().getACModel().getContext().getObjects().isEmpty());

		// try to add the same transition more than once
		Labeling l3 = new Labeling("l", ac);

		assertFalse(l2.equals(l3));

		try {
			l3.getAnalysisContext().getACModel().getContext().addObjects(Arrays.asList("c0", "c0", "c0", "c0"));
		} catch (ParameterException e) {
			fail("Exception while adding an transition to a labeling.");
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Test the method for removing attributes
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testRemoveAttributes() {

		// create a labeling
		Labeling l3 = new Labeling("l", ac);

		try {
			l3.getAnalysisContext().getACModel().getContext().removeObject("c0");
		} catch (ParameterException e) {
			fail("Exception while adding an transition to a labeling.");
		}
		assertFalse(l3.getAnalysisContext().getACModel().getContext().getObjects().contains("c0"));
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Test the method for setting activity classifications
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testSetActivityClassification() throws ParameterException {

		// create a labeling
		Labeling l2 = new Labeling("l", ac);

		// add an activity classification
		l2.setActivityClassification("t0", SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, l2.getActivityClassification("t0"));

		l2.setActivityClassification("t0", SecurityLevel.LOW);
		assertEquals(SecurityLevel.LOW, l2.getActivityClassification("t0"));
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Test the method for setting attribute classifications
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testSetAttributeClassification() throws ParameterException {

		// create a labeling
		Labeling l2 = new Labeling("l", ac);

		// add an activity classification
		l2.setAttributeClassification("c0", SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, l2.getAttributeClassification("c0"));

		l2.setAttributeClassification("c0", SecurityLevel.LOW);
		assertEquals(SecurityLevel.LOW, l2.getAttributeClassification("c0"));
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Test the method for setting subject clearance
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testSetSubjcetClearance() throws ParameterException {

		// create a labeling
		Labeling l2 = new Labeling("l", ac);

		// set an subject clearance
		l2.setSubjectClearance("s0", SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, l2.getSubjectClearance("s0"));

		l2.setSubjectClearance("s0", SecurityLevel.LOW);
		assertEquals(SecurityLevel.LOW, l2.getSubjectClearance("s0"));
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Test the method for getter and setter of default security level
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testGetSetDefaultSecurityLevel() throws ParameterException {

		// create a labeling
		Labeling l2 = new Labeling("l", ac);

		// initially the default is LOW
		assertEquals(SecurityLevel.LOW, l2.getDefaultSecurityLevel());

		// Set the default to high
		l2.setDefaultSecurityLevel(SecurityLevel.HIGH);

		// default security level should be high now
		assertEquals(SecurityLevel.HIGH, l2.getDefaultSecurityLevel());
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Test the method forconverting a labeling to a string // TODO macht eher keinen Sinn!?
	// ///////////////////////////////////////////////////////////////////////////////
//	@Test
//	public void testToString() throws ParameterException {
//
//		// create a labeling
//		Labeling l2 = null;
//		try {
//			SOABase context = new SOABase("");
//			context.setActivities(transitions);
//			context.setObjects(attributes);
//			context.setSubjects(subjects);
//			l2 = new Labeling(context);
//		} catch (ParameterException e1) {
//			fail("Cannot create labeling!");
//		}
//
//		// The correct string
//		String output = "Activities: t3[LOW] t2[LOW] t1[LOW] t0[LOW] t4[LOW] " + "\n" + "Attributes: c1[LOW] c2[LOW] c0[LOW] c4[LOW] c3[LOW] " + "\n" + "Subjects: s0[LOW] s2[LOW] s1[LOW] s3[LOW] s4[LOW] ";
//
//		assertEquals(output, l2.toString());
//	}
}
