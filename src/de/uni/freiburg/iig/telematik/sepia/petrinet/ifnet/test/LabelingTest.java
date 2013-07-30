package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.SecurityLevel;


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

	// ***************************
	// ** Test the constructors **
	// ***************************

	// ////////////////////////////////
	// Test the "empty" constructor //
	// ////////////////////////////////
	@Test
	public void testEmpltyLabelingConstructor() {

		// ////////////////////////////////
		// Test the "empty" constructor //
		// ////////////////////////////////
		Labeling l1 = new Labeling();
		assertTrue(l1.getActivities().isEmpty());
		try {
			assertNull(l1.getActivityClassification("fail"));
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		assertTrue(l1.getAttributes().isEmpty());
		try {
			l1.getAttributeClassification("fail");
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		assertTrue(l1.getSubjects().isEmpty());
		try {
			l1.getSubjectClearance("fail");
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

	}

	// ///////////////////////////////////////
	// Test the sNet, Subjects constructor //
	// ///////////////////////////////////////
	@Test
	public void testSNetSubjectLabelingConstructor() {

		Labeling l2 = null;
		try {
			l2 = new Labeling(sNet, subjects);
		} catch (ParameterException e) {
			e.printStackTrace();
			fail("Cannot create SNet!");
		}

		// Check whether the activities are setup right
		assertFalse(l2.getActivities().isEmpty());
		try {
			assertNull(l2.getActivityClassification("fail"));
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.LOW, l2.getActivityClassification("t0"));
		} catch (ParameterException e) {
			fail("Not able to get the classification of an activity");
		}

		// Check whether the attributes are setup right
		assertFalse(l2.getAttributes().isEmpty());
		try {
			assertNull(l2.getAttributeClassification("fail"));
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.LOW,
					l2.getAttributeClassification("green"));
		} catch (ParameterException e) {
			fail("Not able to get the classification of an attribute");
		}

		// Check whether the subjects are setup right
		assertFalse(l2.getSubjects().isEmpty());
		try {
			l2.getSubjectClearance("fail");
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.LOW, l2.getSubjectClearance("s0"));
		} catch (ParameterException e) {
			fail("Not able to get the clearence of a subject");
		}

	}

	// ///////////////////////////////////////
	// Test the sNet, Subjects, DefaultSecurityLevel constructor //
	// ///////////////////////////////////////
	@Test
	public void testSNetSubjectDefaultLabelingConstructor() {

		Labeling l2 = null;
		try {
			l2 = new Labeling(sNet, subjects, SecurityLevel.HIGH);
		} catch (ParameterException e) {
			e.printStackTrace();
			fail("Cannot create SNet!");
		}

		// Check whether the activities are setup right
		assertFalse(l2.getActivities().isEmpty());
		try {
			assertNull(l2.getActivityClassification("fail"));
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.HIGH, l2.getActivityClassification("t0"));
		} catch (ParameterException e) {
			fail("Not able to get the classification of an activity");
		}

		// Check whether the attributes are setup right
		assertFalse(l2.getAttributes().isEmpty());
		try {
			assertNull(l2.getAttributeClassification("fail"));
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.HIGH, l2.getAttributeClassification("green"));
		} catch (ParameterException e) {
			fail("Not able to get the classification of an attribute");
		}

		
	
		try {									
			assertEquals(SecurityLevel.HIGH, l2.getAttributeClassification(AbstractCWN.CONTROL_FLOW_TOKEN_COLOR));
			fail("Security level for controlflow was set!");
		} catch (ParameterException e) {}
		
	
		// Check whether the subjects are setup right
		assertFalse(l2.getSubjects().isEmpty());
		try {
			l2.getSubjectClearance("fail");
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.HIGH, l2.getSubjectClearance("s0"));
		} catch (ParameterException e) {
			fail("Not able to get the clearence of a subject");
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Test the activities, attributes, subjects, defaultSecurityLevel
	// constructor 
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testActivitiesAttributesSubjectsDefaultSecurityLevelConstructor() {

		Labeling l2 = null;
		try {
			l2 = new Labeling(transitions, attributes, subjects, SecurityLevel.HIGH);
		} catch (ParameterException e1) {
			fail("Cannot create labeling!");
		}
					

		// Check whether the activities are setup right
		assertFalse(l2.getActivities().isEmpty());
		try {
			assertNull(l2.getActivityClassification("fail"));
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.HIGH, l2.getActivityClassification("t1"));
		} catch (ParameterException e) {
			fail("Not able to get the classification of an activity");
		}

		// Check whether the attributes are setup right
		assertFalse(l2.getAttributes().isEmpty());
		try {
			assertNull(l2.getAttributeClassification("fail"));
			fail("An exception should have been thrown!"); 
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.HIGH, l2.getAttributeClassification("c0"));
		} catch (ParameterException e) {
			fail("Not able to get the classification of an attribute");
		}

		// Check whether the subjects are setup right
		assertFalse(l2.getSubjects().isEmpty());
		try {
			l2.getSubjectClearance("fail");
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.HIGH, l2.getSubjectClearance("s1"));
		} catch (ParameterException e) {
			fail("Not able to get the clearence of a subject");
		}

	}
	
	
	
	

	// ///////////////////////////////////////////////////////////////////////////////
	// Test the activities, attributes, subjects
	// constructor 
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testActivitiesAttributesSubjectsConstructor() {

		Labeling l2 = null;
		try {
			l2 = new Labeling(transitions, attributes, subjects);
		} catch (ParameterException e1) {
			fail("Cannot create labeling!");
		}
					

		// Check whether the activities are setup right
		assertFalse(l2.getActivities().isEmpty());
		try {
			assertNull(l2.getActivityClassification("fail"));
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try { 
			assertEquals(SecurityLevel.LOW, l2.getActivityClassification("t2"));
		} catch (ParameterException e) {
			fail("Not able to get the classification of an activity");
		}

		// Check whether the attributes are setup right
		assertFalse(l2.getAttributes().isEmpty());
		try {
			assertNull(l2.getAttributeClassification("fail"));
			fail("An exception should have been thrown!"); 
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.LOW, l2.getAttributeClassification("c1"));
		} catch (ParameterException e) {
			fail("Not able to get the classification of an attribute");
		}

		// Check whether the subjects are setup right
		assertFalse(l2.getSubjects().isEmpty());
		try {
			l2.getSubjectClearance("fail");
			fail("An exception should have been thrown!");
		} catch (ParameterException e) {
		}

		try {
			assertEquals(SecurityLevel.LOW, l2.getSubjectClearance("s2"));
		} catch (ParameterException e) {
			fail("Not able to get the clearence of a subject");
		}

	}
	
	
	
	// ///////////////////////////////////////////////////////////////////////////////
	// Test the method for adding activities.
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testAddActivities() {
		
		//create an empty set of activities
		HashSet<String> emptyTransitionSet = new HashSet<String>();
		
		Labeling l2 = null;
		try {
			l2 = new Labeling(emptyTransitionSet, attributes, subjects);
		} catch (ParameterException e1) {
			fail("Cannot create labeling!");
		}
		
		//There should be activities
		assertTrue(l2.getActivities().isEmpty());
		
		
		//try to add the same transition more than once
		Labeling l3 = null;
		try {
			l3 = new Labeling(transitions, attributes, subjects);
		} catch (ParameterException e1) {
			fail("Cannot create labeling!");
		}
		
		try {
			l3.addActivities("t0", "t0", "t0", "t0");
		} catch (ParameterException e) {
			fail("Exception while adding an transition to a labeling.");
		}		
	}
	
	


	// ///////////////////////////////////////////////////////////////////////////////
	// Test the method for removing activities.
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testRemoveActivities() {
		
		//create alabeling
		Labeling l3 = null;
		try {
			l3 = new Labeling(transitions, attributes, subjects);
		} catch (ParameterException e1) {
			fail("Cannot create labeling!");
		}
		
		//try to remove a transition
		try {
			l3.removeActivities("t0");
		} catch (ParameterException e) {
			fail("Exception while removing a transition from a labeling.");
		}			
		assertFalse(l3.getActivities().contains("t0"));
		
		//try to remove a transition which is not contained in the labeling
		try {
			l3.removeActivities("t0");
		} catch (ParameterException e) {
			fail("Exception while removing a transition from a labeling.");
		}			
		assertFalse(l3.getActivities().contains("t0"));
		
		
		//try to remove no transition		
		try {			
			l3.removeActivities(new HashSet<String>());			
		} catch (ParameterException e) {
			fail("Eception while removing a transition from a labeling.");
		}
		
		
	}
	

	
	// ///////////////////////////////////////////////////////////////////////////////
	// Test the method for adding subjects.
	// ///////////////////////////////////////////////////////////////////////////////
	@Test
	public void testAddSubjects() {
		
		//create an empty set of subjects
		HashSet<String> emptySubjectSet = new HashSet<String>();
		
		Labeling l2 = null;
		try {
			l2 = new Labeling(transitions, attributes, emptySubjectSet);
		} catch (ParameterException e1) {
			fail("Cannot create labeling!");
		}
		
		//There should be subjects
		assertTrue(l2.getSubjects().isEmpty());
		
		
		 
		//try to add the same subjects more than once
		Labeling l3 = null;
		try {
			l3 = new Labeling(transitions, attributes, subjects);
		} catch (ParameterException e1) {
			fail("Cannot create labeling!");
		}
		
		try {
			l3.addSubjects("s1", "s1", "s1");
		} catch (ParameterException e) {
			fail("Exception while adding an subject to a labeling.");
		}		
	}
	 
	
	
	    ///////////////////////////////////////////////////////////////////////////////
		// Test the method for removing subjects.
		// ///////////////////////////////////////////////////////////////////////////////
		@Test
		public void testRemoveSubjects() {
			
			//create alabeling
			Labeling l3 = null;
			try {
				l3 = new Labeling(transitions, attributes, subjects);
			} catch (ParameterException e1) {
				fail("Cannot create labeling!");
			}
			
			//try to remove a subject
			try {
				l3.removeSubjects("s1"); 
			} catch (ParameterException e) {
				fail("Exception while removing a subject from a labeling.");
			}			
			assertFalse(l3.getSubjects().contains("s1"));
			
			//try to remove a subject which is not contained in the labeling
			try {
				l3.removeSubjects("s1");
			} catch (ParameterException e) {
				fail("Exception while removing a subject from a labeling.");
			}			
			assertFalse(l3.getSubjects().contains("s1"));
			
			
			//try to remove no transition		
			try {			
				l3.removeSubjects(new HashSet<String>());			
			} catch (ParameterException e) {
				fail("Eception while removing a subject from a labeling.");
			}
			
			 
		}
		


		// ///////////////////////////////////////////////////////////////////////////////
		// Test the method for adding attributes.
		// ///////////////////////////////////////////////////////////////////////////////
		@Test
		public void testAddAttributes() {
			
			//create an empty set of attribs
			HashSet<String> emptyAttributeSet = new HashSet<String>();
			
			Labeling l2 = null;
			try {
				l2 = new Labeling(transitions, emptyAttributeSet, subjects);
			} catch (ParameterException e1) {
				fail("Cannot create labeling!");
			}
			
			//There should be attributes
			assertTrue(l2.getAttributes().isEmpty());
			
			
			 
			//try to add the same attribs more than once
			Labeling l3 = null;
			try {
				l3 = new Labeling(transitions, attributes, subjects);
			} catch (ParameterException e1) {
				fail("Cannot create labeling!");
			}
			
			try {
				l3.addAttributes("a1", "a1", "a1");
			} catch (ParameterException e) {
				fail("Exception while adding an attribute to a labeling.");
			}		
		}
		 

	    ///////////////////////////////////////////////////////////////////////////////
		// Test the method for removing attributes
		// ///////////////////////////////////////////////////////////////////////////////
		@Test
		public void testRemoveAttributes() {
			
			//create alabeling
			Labeling l3 = null;
			try {
				l3 = new Labeling(transitions, attributes, subjects);
			} catch (ParameterException e1) {
				fail("Cannot create labeling!");
			}
			
			//try to remove a attributes
			try {
				l3.removeAttribute("c1"); 
			} catch (ParameterException e) {
				fail("Exception while removing a attribute from a labeling.");
			}			
			assertFalse(l3.getAttributes().contains("c1"));
			
			//try to remove an attribute which is not contained in the labeling
			try {
				l3.removeAttribute("c1");
			} catch (ParameterException e) {
				fail("Exception while removing an attribute from a labeling.");
			}			
			assertFalse(l3.getAttributes().contains("s1"));
			
			
			//try to remove no attribute		
			try {			
				l3.removeAttributes(new HashSet<String>());			
			} catch (ParameterException e) {
				fail("Eception while removing an attribute from a labeling.");
			}			 
		} 
		
		
		///////////////////////////////////////////////////////////////////////////////
		// Test the method for setting activity classifications
		// ///////////////////////////////////////////////////////////////////////////////
		@Test
		public void testSetActivityClassification() throws ParameterException {
			
			
			//create a labeling
			Labeling l2 = null;
			try {
				l2 = new Labeling(transitions, attributes, subjects);
			} catch (ParameterException e1) {
				fail("Cannot create labeling!");
			}
			
			//add an activity classification
			l2.setActivityClassification("t0", SecurityLevel.HIGH);
			assertEquals(SecurityLevel.HIGH, l2.getActivityClassification("t0"));
			
			l2.setActivityClassification("t0", SecurityLevel.LOW);
			assertEquals(SecurityLevel.LOW, l2.getActivityClassification("t0"));
			
		} 

		

		///////////////////////////////////////////////////////////////////////////////
		// Test the method for setting attribute classifications
		// ///////////////////////////////////////////////////////////////////////////////
		@Test
		public void testSetAttributeClassification() throws ParameterException {
			
			
			//create a labeling
			Labeling l2 = null;
			try {
				l2 = new Labeling(transitions, attributes, subjects);
			} catch (ParameterException e1) {
				fail("Cannot create labeling!");
			}
			
			//add an activity classification
			l2.setAttributeClassification("c0", SecurityLevel.HIGH);
			assertEquals(SecurityLevel.HIGH, l2.getAttributeClassification("c0"));
			
			l2.setAttributeClassification("c0", SecurityLevel.LOW);
			assertEquals(SecurityLevel.LOW, l2.getAttributeClassification("c0"));
			
		} 

		
		
		///////////////////////////////////////////////////////////////////////////////
		// Test the method for setting subject clearance
		// ///////////////////////////////////////////////////////////////////////////////
		@Test
		public void testSetSubjcetClearance() throws ParameterException {
			
			
			//create a labeling
			Labeling l2 = null;
			try {
				l2 = new Labeling(transitions, attributes, subjects);
			} catch (ParameterException e1) {
				fail("Cannot create labeling!");
			}
			
			//set an subject clearance
			l2.setSubjectClearance("s0", SecurityLevel.HIGH);
			assertEquals(SecurityLevel.HIGH, l2.getSubjectClearance("s0"));
			
			l2.setSubjectClearance("s0", SecurityLevel.LOW);
			assertEquals(SecurityLevel.LOW, l2.getSubjectClearance("s0"));
			
		} 
		
		
///////////////////////////////////////////////////////////////////////////////
// Test the method for getter and setter of default security level
// ///////////////////////////////////////////////////////////////////////////////
@Test
public void testGetSetDefaultSecurityLevel() throws ParameterException {
	
	//create a labeling
	Labeling l2 = null;
	try {
		l2 = new Labeling(transitions, attributes, subjects);
	} catch (ParameterException e1) {
		fail("Cannot create labeling!");
	}
	
	//initially the default is LOW
	assertEquals(SecurityLevel.LOW,l2.getDefaultSecurityLevel());
	
	//Set the default to high
	l2.setDefaultSecurityLevel(SecurityLevel.HIGH);
	
	//default security level should be high now
	assertEquals(SecurityLevel.HIGH,l2.getDefaultSecurityLevel());
	
	
}
		


///////////////////////////////////////////////////////////////////////////////
//Test the method forconverting a labeling to a string
/////////////////////////////////////////////////////////////////////////////////
@Test
public void testToString() throws ParameterException {

//create a labeling
Labeling l2 = null;
try {
l2 = new Labeling(transitions, attributes, subjects);
} catch (ParameterException e1) {
fail("Cannot create labeling!");
}

//The correct string
String output = "Activities: t3[LOW] t2[LOW] t1[LOW] t0[LOW] t4[LOW] " + "\n" + 
			   "Attributes: c1[LOW] c2[LOW] c0[LOW] c4[LOW] c3[LOW] " + "\n" + 
			   "Subjects: s0[LOW] s2[LOW] s1[LOW] s3[LOW] s4[LOW] ";

assertEquals(output, l2.toString());

}


		 
}
