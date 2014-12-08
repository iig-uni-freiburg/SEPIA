package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jawl.context.Context;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;

/**
 * @author boehr
 */
public class AnalysisContextTest {

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

	@Before
	public void setUp() throws Exception {

		// Set up an SNet
		// create standard transitions
		transitions = new HashSet<String>();
		transitions.add("t0");
		transitions.add("t1");
		transitions.add("t2");
		transitions.add("t3");

		// create standard transitions
		places = new HashSet<String>();
		places.add("p0");
		places.add("p1");

		// create standard attributes
		attributes = new HashSet<String>();
		// attributes.add("black");
		attributes.add("green");

		// create standard subjects
		subjects = new HashSet<String>();
		subjects.add("s0");
		subjects.add("s1");
		subjects.add("s2");
		subjects.add("s3");

		// The standard marking
		IFNetMarking m = new IFNetMarking();
		Multiset<String> placeMarking = new Multiset<String>();
		placeMarking.add("black");
		m.set("p0", placeMarking);

		Multiset<String> placeMarking2 = new Multiset<String>();
		placeMarking2.add("green");
		m.set("p1", placeMarking2);

		// Create an SNet
		sNet = new IFNet(places, transitions, m);
	}

	@After
	public void tearDown() throws Exception {
	}

	// At first test the constructors

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext#AnalysisContext()}. All fields must be initialized properly.
	 */
	@Test
	public void testAnalysisContext() {

		AnalysisContext ac = new AnalysisContext(new Labeling());
		assertNotNull(ac.getLabeling());
		assertTrue(ac.getActivities().isEmpty());
		assertTrue(ac.getAttributes().isEmpty());
		assertTrue(ac.getSubjects().isEmpty());
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getDefaultSecurityLevel());
		try {
			// Try to get a subject descriptor of a non existing transition
			ac.getSubjectDescriptor("tFail");
			fail("An exception has not been thrown!");
		} catch (Exception e) {
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext#AnalysisContext(de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.Labeling)}. Test whether the labeling is set properly.
	 */
	@Test
	public void testAnalysisContextLabeling() {

		// Create a labeling first
		Context c = new Context("");
		c.setActivities(Arrays.asList("t0", "t1", "t2"));
		c.setSubjects(Arrays.asList("s0", "s1", "s2"));
		c.setObjects(Arrays.asList("c0", "c1", "c2"));
		Labeling l = new Labeling(c);
		
		l.setActivityClassification("t0", SecurityLevel.LOW);
		l.setActivityClassification("t1", SecurityLevel.LOW);
		l.setActivityClassification("t2", SecurityLevel.LOW);

		l.setSubjectClearance("s0", SecurityLevel.LOW);
		l.setSubjectClearance("s1", SecurityLevel.LOW);
		l.setSubjectClearance("s2", SecurityLevel.LOW);

		l.setAttributeClassification("c0", SecurityLevel.LOW);
		l.setAttributeClassification("c1", SecurityLevel.LOW);
		l.setAttributeClassification("c2", SecurityLevel.LOW);

		// create the analysis context
		AnalysisContext ac = new AnalysisContext(l);
		assertEquals(l, ac.getLabeling());

	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext#AnalysisContext(de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet, java.util.Collection)}. Test whether the labeling is setup properly.
	 */
	@Test
	public void testAnalysisContextSNetCollectionOfString() {

		// Create the AnalysisCOntext
		Context context = new Context("");
		context.setSubjects(subjects);
		context.setObjects(attributes);
		context.setActivities(transitions);
		AnalysisContext ac = new AnalysisContext(context);
		assertEquals(subjects, ac.getSubjects());
		assertEquals(transitions, ac.getActivities());
		assertEquals(attributes, ac.getAttributes());
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getDefaultSecurityLevel());
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext#AnalysisContext(java.util.Collection, java.util.Collection, java.util.Collection, de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.SecurityLevel)}.
	 */
	@Test
	public void testAnalysisContextCollectionOfStringCollectionOfStringCollectionOfStringSecurityLevel() {

		AnalysisContext ac = null;
		try {
			Context c = new Context("");
			c.addActivities(transitions);
			c.addSubjects(subjects);
			c.addObjects(attributes);
			ac = new AnalysisContext(c, SecurityLevel.HIGH);
		} catch (ParameterException e) {
			fail("Not able to create AnalysisContext.");
		}

		assertEquals(subjects, ac.getSubjects());
		assertEquals(transitions, ac.getActivities());
		assertEquals(attributes, ac.getAttributes());
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getDefaultSecurityLevel());
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext#AnalysisContext(java.util.Collection, java.util.Collection, java.util.Collection)}.
	 */
	@Test
	public void testAnalysisContextCollectionOfStringCollectionOfStringCollectionOfString() {

		AnalysisContext ac = null;
		try {
			Context c = new Context("");
			c.addActivities(transitions);
			c.addSubjects(subjects);
			c.addObjects(attributes);
			ac = new AnalysisContext(c);
		} catch (ParameterException e) {
			fail("Not able to create AnalysisContext.");
		}

		assertEquals(subjects, ac.getSubjects());
		assertEquals(transitions, ac.getActivities());
		assertEquals(attributes, ac.getAttributes());
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getDefaultSecurityLevel());
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext#setLabeling(de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.Labeling)}. Test whether the right exceptions are raise if the new labeling is not compatible to the AnalysisCOntext.
	 */
	@Test
	public void testSetLabeling() {

		// Create three labelings first
		// The labeling used to construct the ac
		Context c1 = new Context("");
		ArrayList<String> a1 = new ArrayList<String>();
		a1.add("t0");
		a1.add("t1");
		a1.add("t2");
		c1.addActivities(a1);
		ArrayList<String> s1 = new ArrayList<String>();
		s1.add("s0");
		s1.add("s1");
		s1.add("s2");
		c1.addSubjects(s1);
		ArrayList<String> o1 = new ArrayList<String>();
		o1.add("c0");
		o1.add("c1");
		o1.add("c2");
		c1.addObjects(o1);
		Labeling l1 = new Labeling(c1);

		// A labeling with the same subjects but different attributes and activities
		Context c2 = new Context("");
		ArrayList<String> a2 = new ArrayList<String>(a1);
		a2.set(2, "t3");
		c2.setActivities(a2);
		ArrayList<String> s2 = new ArrayList<String>(s1);
		c2.setSubjects(s2);
		ArrayList<String> o2 = new ArrayList<String>(o1);
		o2.set(2, "c3");
		c2.setObjects(o2);
		Labeling l2 = new Labeling(c2);

		// A labeling with one different subject
		Context c3 = new Context("");
		ArrayList<String> a3 = new ArrayList<String>(a1);
		c3.setActivities(a3);
		ArrayList<String> s3 = new ArrayList<String>(s1);
		s3.set(2, "s3");
		c3.setSubjects(s3);
		ArrayList<String> o3 = new ArrayList<String>(o1);
		c3.setObjects(o3);
		Labeling l3 = new Labeling(c3);

		// create the analysis context with labeling l1
		AnalysisContext ac = new AnalysisContext(l1);
		assertEquals(l1, ac.getLabeling());

		// set a compatible labeling
		ac.setLabeling(l2);
		assertEquals(l2, ac.getLabeling());

		// set a compatible labeling
		ac.setSubjectDescriptor("t0", "s0");
		try {
			ac.setLabeling(l3);
		} catch (ParameterException e) {
			fail("No exception should be thrown");
		}

		// set an non-compatible labeling
		ac.setSubjectDescriptor("t0", "s3");
		try {
			ac.setLabeling(l2);
			fail("An exception should be thrown");
		} catch (ParameterException e) {
		}
	}

	/*
	 * <p> Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext#setSubjectDescriptor(java.lang.String, java.lang.String)}. </p> <p> Test all four possible combinations of high low and transitions and subject: </p> <ol> <li>assign high subject to a high transition</li> <li>assign low subject to a low transition</li> <li>assign high subject to a low
	 * transition</li> <li>assign low subject to a high transition (==> Exception)</li> </ol>
	 */
	@Test
	public void testSetSubjectDescriptor() {

		// Create the AnalysisCOntext
		AnalysisContext ac = null;
		try {
			Context context = new Context("");
			context.setObjects(attributes);
			context.setSubjects(subjects);
			context.setActivities(transitions);
			ac = new AnalysisContext(context);
		} catch (ParameterException e) {
			fail("Not able to create AnalysisContext.");
		}

		// All subjects and transitions are low by default.
		// set some transitions and some subjects to high
		try {
			ac.getLabeling().setActivityClassification("t0", SecurityLevel.HIGH);
			ac.getLabeling().setSubjectClearance("s0", SecurityLevel.HIGH);
		} catch (ParameterException e) {
			fail("Not able to set up high transitions and subjects");
		}

		// Assign a high subject to a high transition
		try {
			ac.setSubjectDescriptor("t0", "s0");
			assertEquals("s0", ac.getSubjectDescriptor("t0"));
		} catch (ParameterException e) {
			fail("High subject cannot be assigned to high transition!");
		}

		// Assign a low subject to a low transition
		try {
			ac.setSubjectDescriptor("t1", "s1");
			assertEquals("s1", ac.getSubjectDescriptor("t1"));
		} catch (ParameterException e) {
			fail("Low subject cannot be assigned to Low transition!");
		}

		// Assign a high subject to a low transition
		try {
			ac.setSubjectDescriptor("t1", "s0");
			assertEquals("s0", ac.getSubjectDescriptor("t1"));
		} catch (ParameterException e) {
			fail("High subject cannot be assigned to Low transition!");
		}

		// Assign a low subject to a high transition
		try {
			ac.setSubjectDescriptor("t0", "s1");
			fail("Low subject can be assigned to High transition!");
		} catch (ParameterException e) {
		}
	}
}
