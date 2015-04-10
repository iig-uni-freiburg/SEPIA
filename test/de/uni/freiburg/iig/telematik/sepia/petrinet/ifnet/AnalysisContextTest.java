package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;

/**
 * @author boehr
 */
public class AnalysisContextTest {

	// The standard set of transitions
	HashSet<String> activities = null;

	// The standard set of transitions
	HashSet<String> objects = null;

	// The standard set of transitions
	HashSet<String> subjects = null;

	// The standard SNet
	IFNet sNet = null;

	// The standard Places
	Set<String> places = null;

	// The standard snet marking
	IFNetMarking m = null;

	SOABase base = null;
	@SuppressWarnings("rawtypes")
	AbstractACModel acm = null;

	@Before
	public void setUp() throws Exception {

		// Set up an SNet
		// create standard transitions
		activities = new HashSet<String>();
		activities.add("t0");
		activities.add("t1");
		activities.add("t2");
		activities.add("t3");

		// create standard transitions
		places = new HashSet<String>();
		places.add("p0");
		places.add("p1");

		// create standard attributes
		objects = new HashSet<String>();
		// attributes.add("black");
		objects.add("green");

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
		sNet = new IFNet(places, activities, m);

		base = new SOABase("base");
		base.setSubjects(subjects);
		base.setObjects(objects);
		base.setActivities(activities);

		acm = new ACLModel("ACL", base);
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

		AnalysisContext ac1 = new AnalysisContext("AC1", acm, true);
		assertEquals(ac1.getName(), "AC1");
		assertNotNull(ac1.getLabeling());
		assertNotNull(ac1.getACModel());
		assertFalse(ac1.getACModel().getContext().getActivities().isEmpty());
		assertFalse(ac1.getACModel().getContext().getObjects().isEmpty());
		assertFalse(ac1.getACModel().getContext().getSubjects().isEmpty());
		assertEquals(SecurityLevel.LOW, ac1.getLabeling().getDefaultSecurityLevel());
		try {
			// Try to get a subject descriptor of a non existing transition
			ac1.getSubjectDescriptor("tFail");
			fail("An exception has not been thrown!");
		} catch (Exception e) {
		}

		AnalysisContext ac2 = new AnalysisContext("AC2", acm, false);
		assertEquals(ac2.getName(), "AC2");
		assertNull(ac2.getLabeling());
		assertNotNull(ac2.getACModel());

		AnalysisContext ac3 = new AnalysisContext("AC3", acm, true, SecurityLevel.LOW);
		assertEquals(ac3.getName(), "AC3");
		assertNotNull(ac3.getLabeling());
		assertNotNull(ac3.getACModel());
		assertFalse(ac3.getACModel().getContext().getActivities().isEmpty());
		assertFalse(ac3.getACModel().getContext().getObjects().isEmpty());
		assertFalse(ac3.getACModel().getContext().getSubjects().isEmpty());
		assertEquals(SecurityLevel.LOW, ac3.getLabeling().getDefaultSecurityLevel());

		AnalysisContext ac4 = new AnalysisContext("AC4", acm, true, SecurityLevel.LOW);
		assertEquals(ac4.getName(), "AC4");
		assertNotNull(ac4.getLabeling());
		assertNotNull(ac4.getACModel());
		assertEquals(SecurityLevel.LOW, ac4.getLabeling().getDefaultSecurityLevel());

		AnalysisContext ac5 = new AnalysisContext("AC5", acm, true, SecurityLevel.HIGH);
		assertEquals(ac5.getName(), "AC5");
		assertNotNull(ac5.getLabeling());
		assertNotNull(ac5.getACModel());
		assertEquals(SecurityLevel.HIGH, ac5.getLabeling().getDefaultSecurityLevel());
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext#AnalysisContext(de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.Labeling)}. Test whether the labeling is set properly.
	 */
	@Test
	public void testAnalysisContextLabeling() {
		AnalysisContext ac = new AnalysisContext("AC", acm, false);

		Labeling l = new Labeling("l", ac);

		l.setActivityClassification("t0", SecurityLevel.LOW);
		l.setActivityClassification("t1", SecurityLevel.LOW);
		l.setActivityClassification("t2", SecurityLevel.LOW);
		l.setActivityClassification("t3", SecurityLevel.LOW);

		l.setSubjectClearance("s0", SecurityLevel.LOW);
		l.setSubjectClearance("s1", SecurityLevel.LOW);
		l.setSubjectClearance("s2", SecurityLevel.LOW);
		l.setSubjectClearance("s3", SecurityLevel.LOW);

		l.setAttributeClassification("green", SecurityLevel.LOW);

		ac.setLabeling(l);
		assertEquals(l, ac.getLabeling());

		Labeling lh = new Labeling("lh", ac, SecurityLevel.HIGH);
		assertEquals(SecurityLevel.HIGH, lh.getDefaultSecurityLevel());
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AnalysisContext#AnalysisContext(de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet, java.util.Collection)}. Test whether the labeling is setup properly.
	 */
	@Test
	public void testAnalysisContextIFNetCollectionOfString() {

		// Create the AnalysisCOntext
		AnalysisContext ac = new AnalysisContext("ac", acm, true);
		assertEquals(subjects, ac.getACModel().getContext().getSubjects());
		assertEquals(activities, ac.getACModel().getContext().getActivities());
		assertEquals(objects, ac.getACModel().getContext().getObjects());
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getDefaultSecurityLevel());

		AnalysisContext ach = new AnalysisContext("ac", acm, true, SecurityLevel.HIGH);
		assertEquals(subjects, ach.getACModel().getContext().getSubjects());
		assertEquals(activities, ach.getACModel().getContext().getActivities());
		assertEquals(objects, ach.getACModel().getContext().getObjects());
		assertEquals(SecurityLevel.HIGH, ach.getLabeling().getDefaultSecurityLevel());
	}
}
