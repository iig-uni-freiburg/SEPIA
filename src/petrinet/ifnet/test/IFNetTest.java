/**
 * 
 */
package petrinet.ifnet.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;

import exception.PNValidationException;

import petrinet.ifnet.AnalysisContext;
import petrinet.ifnet.DeclassificationTransition;
import petrinet.ifnet.Labeling;
import petrinet.ifnet.RegularIFNetTransition;
import petrinet.ifnet.IFNet;
import petrinet.ifnet.IFNetFlowRelation;
import petrinet.ifnet.IFNetMarking;
import petrinet.ifnet.SecurityLevel;

/**
 * @author boehr
 *
 */
public class IFNetTest {

	//The variable is filled during start up
	private IFNet dSNet = null;
	private DeclassificationTransition td = null;
	private RegularIFNetTransition tr = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		dSNet = IFNetTestUtil.createSimpleSnetWithDeclassification();
		td = dSNet.getDeclassificationTransitions().iterator().next();
		tr = (RegularIFNetTransition) dSNet.getTransition("t0");
	}


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link petrinet.ifnet.IFNet#removeTransition(java.lang.String)}.
	 * @throws ParameterException 
	 */
	@Test
	public void testRemoveTransition() throws ParameterException {

		//remove the transition
		assertTrue(dSNet.containsTransition("t0"));
		dSNet.removeTransition("t0");
		assertFalse(dSNet.containsTransition("t0"));
		assertFalse(dSNet.removeTransition("t0"));
	}

	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#checkValidity()}.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidity() throws ParameterException {
		
		try {
			dSNet.checkValidity();
		} catch (PNValidationException e) {
			fail("A valid sNet  is detected to be invalid!");
		}
		
		//change the flow relation such that there will never be
		//proper completion => SNet is invalid
		IFNetFlowRelation outRel = null;
		for(IFNetFlowRelation f : dSNet.getFlowRelations()){
			if(f.getTarget().getName().equals("pOut")){
				outRel=f;
				break;
			}
		}
		
		//remove the black token and set green instead
		Multiset<String> constraint = new Multiset<String>();
		constraint.add("green");
		constraint.add("black");
		outRel.setConstraint(constraint);
		
		try {
			dSNet.checkValidity();
			fail("An invalid sNet  is not detected!");
		} catch (PNValidationException e) {
		}
		
	}

	
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#checkValidity()}.
	 * Try to set an invalid analysis context where subjects are missing.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityAnalysisContextMissingSubject() throws ParameterException {
		
		IFNet net = IFNetTestUtil.createSimpleSnetWithDeclassificationNoAC();
		try {
			net.checkValidity();
			fail("An invalid sNet  is not detected!");
		} catch (PNValidationException e) {
		}
	
	}


	/**
	 * Test method for {@link petrinet.ifnet.IFNet#checkValidity()}.
	 * Try to set an invalid analysis context where subjects CREATEs
	 * a token with a classification different from the subjects clearence.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityAnalysisContextNonMatchingSecLevels() throws ParameterException {
		
		//blue is created by transition t0. Both the transition and its subject (sh1) are high.
		dSNet.getAnalysisContext().getLabeling().setAttributeClassification("blue", SecurityLevel.LOW);
		try {
			dSNet.checkValidity();
			fail("An invalid sNet  is not detected!");
		} catch (PNValidationException e) {
		}
	}	
	
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#checkValidity()}.
	 * Try to set an invalid analysis context where the classification
	 * of a color created from a declassificationtransition is high
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityAnalysisContextDeclassificationTransCreatesHighColor() throws ParameterException {
		
		//blue is created by transition t0. Both the transition and its subject (sh1) are high.
		dSNet.getAnalysisContext().getLabeling().setAttributeClassification("yellow", SecurityLevel.HIGH);
		try {
			dSNet.checkValidity();
			fail("An invalid sNet  is not detected!");
		} catch (PNValidationException e) {
		}
		
		//create an declassification transition which creates a color for which no label is set
		IFNetFlowRelation tdOutRel = null;
		IFNetFlowRelation tdInRel = null;
		for(IFNetFlowRelation f : dSNet.getFlowRelations()){
			if(f.getSource().getName().equals("td")){
				tdOutRel = f;
			}
			if(f.getTarget().getName().equals("td")){
				tdInRel = f;
			}			
		}
		
	}	 
	


	/**
	 * Test method for {@link petrinet.ifnet.IFNet#checkValidity()}.
	 * Try to set an invalid analysis context where the classification
	 * of a color created from a declassificationtransition is not set.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityAnalysisContextDeclassificationTransCreatesUnlabeldColor() throws ParameterException {
		
				dSNet.getAnalysisContext().getLabeling().removeAttribute("yellow");		
				try {
					dSNet.checkValidity();
					fail("An invalid sNet  is not detected!");
				} catch (PNValidationException e) {
				}
	}	 
	
	
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#checkValidity()}.
	 * Try to set an invalid analysis context where the classification
	 * of  a declassificationtransition is LOW.
	 * @throws ParameterException 
	 */
	@Test
	public void testCheckValidityAnalysisContextDeclassificationTransLabaledLow() throws ParameterException {
		
				dSNet.getAnalysisContext().getLabeling().setActivityClassification("td",SecurityLevel.LOW);					
				try {
					dSNet.checkValidity();
					fail("An invalid sNet  is not detected!");
				} catch (PNValidationException e) {
				} 
				
				//remove a transtion from the labeling a see whether the missing label is detected
				dSNet.getAnalysisContext().getLabeling().removeActivities("td");
				try {
					dSNet.checkValidity();
					fail("An invalid sNet  is not detected!");
				} catch (PNValidationException e) {
				} 
	}	
	
	
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#SNet()}.
	 */
	@Test
	public void testSNet() {
		
		IFNet sNet = new IFNet();
		
		assertTrue(sNet.getMarking().isEmpty());
		assertTrue(sNet.getInitialMarking().isEmpty());
		
	}

	
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#SNet(java.util.Set, java.util.Set, petrinet.ifnet.IFNetMarking)}.
	 * @throws ParameterException 
	 */
	@Test
	public void testSNetSetOfStringSetOfStringSNetMarking() throws ParameterException {
		
		//create place transition and initial makring
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
		
		//create the sNet
		IFNet net = new IFNet(places, transitions, initialMarking);
		
		//check everything is set properly
		assertTrue(net.getPlaces().size()==2);
		assertTrue(net.getPlaces().contains(net.getPlace("p0")));
		assertTrue(net.getPlaces().contains(net.getPlace("p1")));
		
		assertTrue(net.getTransitions().size()==2);
		assertTrue(net.getTransitions().contains(net.getTransition("t0")));
		assertTrue(net.getTransitions().contains(net.getTransition("t1")));
		
		assertTrue(net.getInitialMarking().equals(initialMarking));
		
	}

	 
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#getRegularTransitions()}.
	 */
	@Test
	public void testGetRegularTransitions() {
		
		//get all regular transitions
		Collection<RegularIFNetTransition> transitions =  dSNet.getRegularTransitions();
		
		//check that the amount  is ok
		assertTrue(transitions.size()==4);
		
		//set up a set with all names of regular transitons
		Set<String> transitionNames = new HashSet<String>();
		transitionNames.add("tIn");
		transitionNames.add("t0");
		transitionNames.add("tOut");
		transitionNames.add("t1");
		
		//check all regular transtions are present
		for(RegularIFNetTransition rt : transitions){			
			assertTrue(transitionNames.contains(rt.getName()));
		}
		
	}

	
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#getDeclassificationTransitions()}.
	 */
	@Test
	public void testGetDeclassificationTransitions() {

		//get all regular transitions
		Collection<DeclassificationTransition> transitions =  dSNet.getDeclassificationTransitions();
		
		//check that the amount  is ok
		assertTrue(transitions.size()==1);
		
		//check that the right transitions is found	
		assertTrue(transitions.iterator().next().getName().equals("td"));
	
	}

	
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#addDeclassificationTransition(java.lang.String)}.
	 */
	@Test
	public void testAddDeclassificationTransitionString() {
		
		try {
			dSNet.addDeclassificationTransition("td");
			dSNet.addDeclassificationTransition("td2"); 
		} catch (ParameterException e) {
			fail("Cannot add DeclassificationTransition.");
		}
		
		assertTrue(dSNet.getDeclassificationTransitions().size()==2);
		
		Collection<DeclassificationTransition> dtransitions = dSNet.getDeclassificationTransitions();
		for(DeclassificationTransition dt : dtransitions){
			assertTrue(dt.getName().equals("td")||dt.getName().equals("td2"));
		}
		
	}

 

	
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#getSubjectDescriptors()}.
	 * @throws ParameterException 
	 */
	@Test
	public void testGetSubjectDescriptors() throws ParameterException {
		
		assertTrue(dSNet.getSubjectDescriptors().size()==5);	
		assertTrue(dSNet.getSubjectDescriptors().contains("sh0"));
		assertTrue(dSNet.getSubjectDescriptors().contains("sh1"));
		assertTrue(dSNet.getSubjectDescriptors().contains("sh2"));
		assertTrue(dSNet.getSubjectDescriptors().contains("sh3"));
		assertTrue(dSNet.getSubjectDescriptors().contains("sl0"));
		
		
	}

	/**
	 * Test method for {@link petrinet.ifnet.IFNet#getAnalysisContext()}.
	 * @throws ParameterException 
	 */
	@Test
	public void testGetAnalysisContext() throws ParameterException {
		
		assertTrue(dSNet.getAnalysisContext().getSubjectDescriptor("tIn").equals("sh0"));
		assertTrue(dSNet.getAnalysisContext().getSubjectDescriptor("tOut").equals("sh2"));
		assertTrue(dSNet.getAnalysisContext().getSubjectDescriptor("td").equals("sh3"));
		assertTrue(dSNet.getAnalysisContext().getSubjectDescriptor("t1").equals("sl0"));
		
	

	}

	
	
	/**
	 * Test method for {@link petrinet.ifnet.IFNet#setAnalysisContext(petrinet.ifnet.AnalysisContext)}.
	 * @throws ParameterException 
	 */
	@Test
	public void testSetAnalysisContext() throws ParameterException {
		
		//create labeling
				Labeling l = new Labeling(dSNet, Arrays.asList("sh0", "sh1", "sh2", "sh3", "sl0"));
				
				//Set subject clearence
				l.setSubjectClearance("sh0",SecurityLevel.HIGH);
				l.setSubjectClearance("sh1",SecurityLevel.HIGH);
				l.setSubjectClearance("sh2",SecurityLevel.HIGH);
				l.setSubjectClearance("sh3",SecurityLevel.HIGH);		
				l.setSubjectClearance("sl0",SecurityLevel.LOW);
				
				//set transition classification
				l.setActivityClassification("tIn", SecurityLevel.HIGH);
				l.setActivityClassification("t0", SecurityLevel.HIGH); 
				l.setActivityClassification("tOut", SecurityLevel.HIGH);
				l.setActivityClassification("td", SecurityLevel.HIGH);
				l.setActivityClassification("t1", SecurityLevel.LOW);
				
				//set token color classification
				l.setAttributeClassification("green", SecurityLevel.HIGH);
				l.setAttributeClassification("red", SecurityLevel.HIGH);
				l.setAttributeClassification("blue", SecurityLevel.HIGH);
				l.setAttributeClassification("yellow", SecurityLevel.LOW);
				
				//Create a new analysis context
				AnalysisContext ac = new AnalysisContext();
				ac.setLabeling(l);
				
				//Assign subjects to transitions
				ac.setSubjectDescriptor("tIn", "sh0");
				ac.setSubjectDescriptor("t0", "sh1");
				ac.setSubjectDescriptor("tOut", "sh2");
				ac.setSubjectDescriptor("td", "sh3");
				ac.setSubjectDescriptor("t1", "sl0");
						
				
				//set the labeling
				dSNet.setAnalysisContext(ac);
				
				assertTrue(dSNet.getAnalysisContext().equals(ac));

		
	}
	


}
