package petrinet.snet.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import petrinet.snet.Labeling;
import petrinet.snet.SNet;
import petrinet.snet.SNetMarking;
import types.Multiset;
import validate.ParameterException;

public class LabelingTest {

	
	//The standard set of transitions
	HashSet<String> transitions = null;
	
	//The standard set of transitions
	HashSet<String> attributes = null;

	//The standard set of transitions
	HashSet<String> subjects = null;
	
	//The standard SNet
	SNet sNet = null;
	
	//The standard Places
	Set<String> places = null;
	
	//The standard snet marking
    SNetMarking m = null;
	
	
	@Before
	public void setUp() throws Exception {
		
		//create standard transitions
		transitions = new HashSet<String>();
		transitions.add("t0");
		transitions.add("t1");
		transitions.add("t2");
		transitions.add("t3");
		transitions.add("t4");
		
		//create standard transitions
		places = new HashSet<String>();
		places.add("p0");
		places.add("p1");
		places.add("p2");
		places.add("p3");
		places.add("p4");
		
		
		//create standard attributes
		attributes = new HashSet<String>();
		attributes.add("c0");
		attributes.add("c1");
		attributes.add("c2");
		attributes.add("c3");
		attributes.add("c4");
		
		//create standard subjects
		subjects = new HashSet<String>();
		subjects.add("s0");
		subjects.add("s1");
		subjects.add("s2");
		subjects.add("s3");
		subjects.add("s4");
		
		//The standard marking
		m = new SNetMarking();
		Multiset<String> placeMarking = new Multiset<String>();
		placeMarking.add("black");
		m.set("p0", placeMarking);
		
		//Create an SNet
		sNet = new SNet(places, transitions, m);
		
	}

	@After
	public void tearDown() throws Exception {
		
		//destroy the standard elements
		transitions = null;
		places = null;
		attributes = null;
		subjects = null;
		m=null;
		sNet = null;
		
	}

	
	//Test the constructors
	@Test
	public void testLabeling(){
		
		//////////////////////////////////
		//Test the "empty" constructor  //
		//////////////////////////////////
		Labeling l1 = new Labeling();
		assertTrue(l1.getActivities().isEmpty());		
		try {
			assertNull(l1.getActivityClassification("fail"));
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {}
		
		
		assertTrue(l1.getAttributes().isEmpty());
		try {
			l1.getAttributeClassification("fail");
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {}
		
		assertTrue(l1.getSubjects().isEmpty());
		try {
			l1.getSubjectClearance("fail");
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {}
		
		
		
		//////////////////////////////////
		//Test the "empty" constructor  //
		//////////////////////////////////
		try {
			Labeling l2 = new Labeling(sNet, subjects);
		} catch (ParameterException e) {
			e.printStackTrace();
			fail("Cannot create SNet!");
		}
		
		
		
		
	}

//	@Test
//	public void testGetActivities() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddActivitiesStringArray() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddActivitiesCollectionOfString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveActivitiesStringArray() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveActivitiesCollectionOfString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSubjects() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddSubjectsStringArray() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddSubjectsCollectionOfString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveSubjectsStringArray() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveSubjectsCollectionOfString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAttributes() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddAttributesStringArray() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddAttributesCollectionOfString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveAttribute() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRemoveAttributes() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetActivityClassification() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetActivityClassification() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetAttributeClassification() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAttributeClassification() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetSubjectClearance() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSubjectClearance() {
//		fail("Not yet implemented");
//	}

}
