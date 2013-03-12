/**
 * 
 */
package petrinet.snet.test;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import petrinet.snet.AnalysisContext;
import petrinet.snet.Labeling;
import petrinet.snet.SNet;
import petrinet.snet.SNetMarking;
import petrinet.snet.SecurityLevel;
import types.Multiset;
import validate.ParameterException;

/**
 * @author boehr
 *
 */
public class AnalysisContextTest {
	
	// The standard set of transitions
	HashSet<String> transitions = null;

	// The standard set of transitions
	HashSet<String> attributes = null;

	// The standard set of transitions
	HashSet<String> subjects = null;

	// The standard SNet
	SNet sNet = null;

	// The standard Places
	Set<String> places = null;

	// The standard snet marking
	SNetMarking m = null;

	
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		//Set up an SNet
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
		//attributes.add("black");
		attributes.add("green");

		// create standard subjects
		subjects = new HashSet<String>();
		subjects.add("s0");
		subjects.add("s1");
		subjects.add("s2");
		subjects.add("s3");
	
		// The standard marking
		SNetMarking m = new SNetMarking();
		Multiset<String> placeMarking = new Multiset<String>();
		placeMarking.add("black");
		m.set("p0", placeMarking);

		Multiset<String> placeMarking2 = new Multiset<String>();
		placeMarking2.add("green");
		m.set("p1", placeMarking2);

		// Create an SNet
		sNet = new SNet(places, transitions, m); 
		
		
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	//At first test the constructors
	
	/**
	 * Test method for {@link petrinet.snet.AnalysisContext#AnalysisContext()}.
	 * All fields must be initialized properly.
	 */
	@Test
	public void testAnalysisContext() {
	
			AnalysisContext ac = new AnalysisContext();
			assertNotNull(ac.getLabeling());
			assertTrue(ac.getActivities().isEmpty());
			assertTrue(ac.getAttributes().isEmpty());
			assertTrue(ac.getSubjects().isEmpty());
			assertEquals(SecurityLevel.LOW, ac.getLabeling().getDefaultSecurityLevel());
			try {
				//Try to get a subject descriptor of a non existing transition
				ac.getSubjectDescriptor("tFail");
				fail("An exception has not been thrown!");
			} catch (ParameterException e) {}
		
	}

	/**
	 * Test method for {@link petrinet.snet.AnalysisContext#AnalysisContext(petrinet.snet.Labeling)}.
	 * Test whether the labeling is set properly.
	 * @throws ParameterException 
	 */
	@Test
	public void testAnalysisContextLabeling() throws ParameterException {
		
		//Create a labeling first
		Labeling l = new Labeling();
		l.addActivities("t0","t1","t2");
		l.addSubjects("s0","s1","s2");
		l.addAttributes("c0","c1","c2");
		
		//create the analysis context
		AnalysisContext ac = new AnalysisContext(l);
		assertEquals(l, ac.getLabeling());
		
		
	}

	/**
	 * Test method for {@link petrinet.snet.AnalysisContext#AnalysisContext(petrinet.snet.SNet, java.util.Collection)}.
	 * Test whether the labeling is setup properly.
	 * @throws ParameterException 
	 */
	@Test
	public void testAnalysisContextSNetCollectionOfString() throws ParameterException {
		

		//Create the AnalysisCOntext
		AnalysisContext ac = new AnalysisContext(sNet, subjects);
		assertEquals(subjects, ac.getSubjects());
		assertEquals(transitions, ac.getActivities());
		assertEquals(attributes, ac.getAttributes());
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getDefaultSecurityLevel());
				
	}

	/**
	 * Test method for {@link petrinet.snet.AnalysisContext#AnalysisContext(petrinet.snet.SNet, java.util.Collection, petrinet.snet.SecurityLevel)}.
	 * Test whether the default security level is set properly.
	 */
	@Test
	public void testAnalysisContextSNetCollectionOfStringSecurityLevel() {
		
		AnalysisContext ac=null;
		try {
			ac = new AnalysisContext(sNet, subjects, SecurityLevel.HIGH);
		} catch (ParameterException e) {
			fail("Not able to create AnalysisContext.");
		}
		assertEquals(subjects, ac.getSubjects());
		assertEquals(transitions, ac.getActivities());
		assertEquals(attributes, ac.getAttributes());
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getDefaultSecurityLevel());
		
	}

	
	
	/**
	 * Test method for {@link petrinet.snet.AnalysisContext#AnalysisContext(java.util.Collection, java.util.Collection, java.util.Collection, petrinet.snet.SecurityLevel)}.
	 */
	@Test
	public void testAnalysisContextCollectionOfStringCollectionOfStringCollectionOfStringSecurityLevel() {
		
		AnalysisContext ac = null;
		try {
				ac = new AnalysisContext(transitions, attributes, subjects, SecurityLevel.HIGH);
		} catch (ParameterException e) {
			fail("Not able to create AnalysisContext.");
		} 
		
		assertEquals(subjects, ac.getSubjects());
		assertEquals(transitions, ac.getActivities());
		assertEquals(attributes, ac.getAttributes());
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getDefaultSecurityLevel());
	}

	
	
	/**
	 * Test method for {@link petrinet.snet.AnalysisContext#AnalysisContext(java.util.Collection, java.util.Collection, java.util.Collection)}.
	 */
	@Test
	public void testAnalysisContextCollectionOfStringCollectionOfStringCollectionOfString() {
		
		AnalysisContext ac = null;
		try {
				ac = new AnalysisContext(transitions, attributes, subjects);
		} catch (ParameterException e) {
			fail("Not able to create AnalysisContext.");
		}
		
		assertEquals(subjects, ac.getSubjects());
		assertEquals(transitions, ac.getActivities());
		assertEquals(attributes, ac.getAttributes());
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getDefaultSecurityLevel());
	}

	
	
	/**
	 * Test method for {@link petrinet.snet.AnalysisContext#setLabeling(petrinet.snet.Labeling)}.
	 * Test whether the right exceptions are raise if the new labeling is not compatible to the AnalysisCOntext. 
	 * @throws ParameterException 
	 */
	@Test
	public void testSetLabeling() throws ParameterException {
		
		//Create three labelings first
		//The labeling used to construct the ac
		Labeling l1 = new Labeling();
		l1.addActivities("t0","t1","t2");
		l1.addSubjects("s0","s1","s2");
		l1.addAttributes("c0","c1","c2");
		
		//A labeling with the same subjects but different attribs and activities
		Labeling l2 = new Labeling();
		l2.addActivities("t0","t1","t3");
		l2.addSubjects("s0","s1","s2");
		l2.addAttributes("c0","c1","c3");
		
		//A labeling with one different subject
		Labeling l3 = new Labeling();
		l3.addActivities("t0","t1","t2");
		l3.addSubjects("s0","s1","s3");
		l3.addAttributes("c0","c1","c2");
		
		//create the analysis context with labeling l1
		AnalysisContext ac = new AnalysisContext(l1);
		assertEquals(l1, ac.getLabeling());
		
		//set a compatible labeling
		ac.setLabeling(l2);
		assertEquals(l2, ac.getLabeling());
		
		
		
		//set a compatible labeling
		ac.setSubjectDescriptor("t0", "s0");
		try {
			ac.setLabeling(l3);			
		} catch (ParameterException e) {
			fail("No exception should be thrown");
		}
				
							
		//set an noncompatible labeling
		ac.setSubjectDescriptor("t0", "s3");
		try {
			ac.setLabeling(l2);
			fail("An exception should be thrown");
		} catch (ParameterException e) {}
		
	}

	
	

	
	/**
	 * Test method for {@link petrinet.snet.AnalysisContext#setSubjectDescriptor(java.lang.String, java.lang.String)}.
	 * Test all four possible combinations of high low and transitions and subject:
	 * 1) assign high subject to a high transition
	 * 2) assign low subject to a low transition
	 * 3) assign high subject to a low transition
	 * 4) assign low subject to a high transition (==> Exception)
	 */
	@Test
	public void testSetSubjectDescriptor() {
		
		//Create the AnalysisCOntext
		AnalysisContext ac = null;
		try {
			ac = new AnalysisContext(sNet, subjects);
		} catch (ParameterException e) {
			fail("Not able to create AnalysisContext.");
		}
		
		//All subjects and transitions are low by default.
		//set some transitions and some subjects to high
		try {
			ac.getLabeling().setActivityClassification("t0", SecurityLevel.HIGH);
			ac.getLabeling().setSubjectClearance("s0", SecurityLevel.HIGH);
		} catch (ParameterException e) {
			fail("Not able to set up high transitions and subjects");
		}
	
		//Assign a high subject to a high transition
		try {
			ac.setSubjectDescriptor("t0", "s0");
			assertEquals("s0", ac.getSubjectDescriptor("t0"));
		} catch (ParameterException e) {
			fail("High subject cannot be assigned to high transition!");
		}
		
		//Assign a low subject to a low transition
		try {
			ac.setSubjectDescriptor("t1", "s1");
			assertEquals("s1", ac.getSubjectDescriptor("t1"));
		} catch (ParameterException e) {
			fail("Low subject cannot be assigned to Low transition!");
		}

		
		//Assign a high subject to a low transition
		try {
			ac.setSubjectDescriptor("t1", "s0");
			assertEquals("s0", ac.getSubjectDescriptor("t1"));
		} catch (ParameterException e) {
			fail("High subject cannot be assigned to Low transition!");
		}

		
		//Assign a low subject to a high transition
		try {
			ac.setSubjectDescriptor("t0", "s1");
			fail("Low subject can be assigned to High transition!");
		} catch (ParameterException e) {}
		
	}

	
	


}
