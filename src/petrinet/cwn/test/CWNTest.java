/**
 * 
 */
package petrinet.cwn.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import petrinet.cwn.CWN;
import petrinet.cwn.CWNFlowRelation;
import petrinet.cwn.CWNMarking;
import petrinet.cwn.CWNPlace;
import types.Multiset;
import validate.ParameterException;
import exception.PNSoundnessException;
import exception.PNValidationException;

/**
 * @author boehr
 *
 */
public class CWNTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test the methods for getting input and output places 
	 * @throws ParameterException 
	 */
	@Test
	public void testCWN() throws ParameterException {
		
		//Create the standard cwn
		CWN cwn = createValidCWN();
		
		//This cwn has exactly one input (p0) and one output place (p3)
		
		//Check input place
		HashSet<CWNPlace> inPlaces = new HashSet<CWNPlace>(cwn.getInputPlaces());
		assertEquals("Wrong number of input places detected", 1, inPlaces.size());
		assertEquals("The wrong place was detected as inputplace", "p0", inPlaces.iterator().next().getName());
		assertEquals("The two methods for getting input places return different places", cwn.getInputPlace().getName(), inPlaces.iterator().next().getName());
		
		//Check output place
		HashSet<CWNPlace> outPlaces = new HashSet<CWNPlace>(cwn.getOutputPlaces());
		assertEquals("Wrong number of output places detected output place", 1, outPlaces.size());
		assertEquals("The wrong place was detected as outputplace", "p3", outPlaces.iterator().next().getName());
		assertEquals("The two methods for getting output places return different places", cwn.getOutputPlace().getName(), outPlaces.iterator().next().getName());
		
		
		//Create a CPN with more than one input and output place
		//CWN invalidCwn = createMultiInOutPlaceCWN();
		CWN invalidCwn = createValidCWN();
		addSecondInputPlaceP4(invalidCwn);
		addSecondOutputPlaceP5(invalidCwn);

		//Check input place
				inPlaces = new HashSet<CWNPlace>(invalidCwn.getInputPlaces());
		assertEquals("Wrong number of input places detected", 2, inPlaces.size());
		HashSet<CWNPlace> correctInPlaces = new HashSet<CWNPlace>();
		correctInPlaces.add(new CWNPlace("p0"));
		correctInPlaces.add(new CWNPlace("p4"));
		assertEquals("The wrong places got detected as inputplaces", correctInPlaces, inPlaces);
		
		//Check output place
		outPlaces = new HashSet<CWNPlace>(invalidCwn.getOutputPlaces());
		assertEquals("Wrong number of output places detected", 2, inPlaces.size());
		HashSet<CWNPlace> correctOutPlaces = new HashSet<CWNPlace>();
		correctOutPlaces.add(new CWNPlace("p3"));
		correctOutPlaces.add(new CWNPlace("p5"));
		assertEquals("The wrong places got detected as outputplaces", correctOutPlaces, outPlaces);
		
		
		//Create empty cwn
		CWN emptyCWN = new CWN();		
		assertNull("A non null value for inputplaces got returned for en empty CWN", emptyCWN.getInputPlace());
		assertTrue("A non empty collection for inputplaces got returned for an empty CWN", emptyCWN.getInputPlaces().isEmpty());
		assertNull("A non null value for outputplaces got returned for an empty CWN", emptyCWN.getOutputPlace());
		assertTrue("A non empty collection for outputplaces got returned for an empty CWN", emptyCWN.getOutputPlaces().isEmpty());
		
		
		
	}
 
	/**
	 * Test the method for checking validity
	 * Focus on the input output place related part of validity
	 * @throws ParameterException
	 */
	@Test
	public void testCWNValidityInputOutPutPlaces() throws ParameterException {

		// Create the standard cwn which is valid
		CWN validCwn = createValidCWN();
		try {
			validCwn.checkValidity();
		} catch (PNValidationException e) {
			e.printStackTrace();
			fail("A valid CWN is detected as invalid!");
		}

		// create a cwn with a non valid amount of input places
		CWN invalidCwn1 = createValidCWN();
		addSecondInputPlaceP4(invalidCwn1);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with two inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a cwn with a non valid amount of output places
		CWN invalidCwn2 = createValidCWN();
		addSecondOutputPlaceP5(invalidCwn2);
		try {
			invalidCwn2.checkValidity();
			fail("An ivalid CWN (with two inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a cwn with a non valid amount of input and output places
		CWN invalidCwn3 = createValidCWN();
		addSecondInputPlaceP4(invalidCwn3);
		addSecondOutputPlaceP5(invalidCwn3);
		try {
			invalidCwn3.checkValidity();
			fail("An ivalid CWN (with two inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a CWN without an input place
		CWN invalidCwn4 = createValidCWN();
		invalidCwn4.removePlace("p0");
				
		
		try {
			invalidCwn4.checkValidity();
			fail("An ivalid CWN (without any inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a CWN without an output place
		CWN invalidCwn5 = createValidCWN();
		invalidCwn5.removePlace("p3");
		try {
			invalidCwn5.checkValidity();
			fail("An ivalid CWN (without any outputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

	}
	
	
	/**
	 * Test the method for checking validity
	 * Focus on the marking related part of validity
	 * 
	 */
	@Test
	public void testCWNValidityMarking() throws ParameterException {
		
		// Create the standard cwn which is valid
		CWN invalidCwn1 = createValidCWN();
		
		//setup some markings used lateron
		//black black in p0
		Multiset<String> placeStatebb = new Multiset<String>("black","black");		
		CWNMarking p0bb = new CWNMarking();		
		p0bb.set("p0", placeStatebb);
		
		//black all empty in p0
		Multiset<String> placeStateEmpty = new Multiset<String>();		
		CWNMarking empty = new CWNMarking();		
		empty.set("p0", placeStateEmpty);
				
		// black all empty in p0
		Multiset<String> placeStateP0P3 = new Multiset<String>("black");
		CWNMarking p0p3 = new CWNMarking();
		p0p3.set("p0", placeStateP0P3);
		p0p3.set("p3", placeStateP0P3);
		
		//black token in a place which is not the input place
		Multiset<String> placeStateb = new Multiset<String>("black");		
		CWNMarking p3b = new CWNMarking();		
		p3b.set("p3", placeStateb);
		
		// green token in an inputplace
		Multiset<String> placeStateGreen = new Multiset<String>("green");
		CWNMarking p0g = new CWNMarking();
		p0g.set("p0", placeStateGreen);

		
		// Set two tokens to place p0
		invalidCwn1.setInitialMarking(p0bb);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with two black tokens in the input place) is detected as valid!");
		} catch (PNValidationException e) {
		}
				
 
				
		//Set zero tokens to place p0
		invalidCwn1.setInitialMarking(empty);	
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with no tokens in the input place) is detected as valid!");
		} catch (PNValidationException e) {
		}		
		
		
		//Set one black token to p0 and one black token to p3
		invalidCwn1.setInitialMarking(p0p3);	
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with two black tokens in different places) is detected as valid!");
		} catch (PNValidationException e) {
		}				
				
		
		//Set one black token to p3
		invalidCwn1.setInitialMarking(p3b);	
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with one black token in a non input place) is detected as valid!");
		} catch (PNValidationException e) {
		}				

		//Set one green token to p0
		invalidCwn1.setInitialMarking(p0g);	
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with one green token in the input place) is detected as valid!");
		} catch (PNValidationException e) {
		}			
		
	}  
	
	
	/**
	 * Test the method for checking validity
	 * Focus on the connectedness related part of validity
	 * 
	 */
	@Test
	public void testCWNValidityConnectedness() throws ParameterException {
		
		// Create the standard cwn which is valid
		CWN invalidCwn1 = createValidCWN();
		
		//Add a transitin without any relations
		invalidCwn1.addTransition("TUnconnected");
		
		//Check whether the cwn is valid	
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (which is not strongly connected) is detected as valid!");
		} catch (PNValidationException e) {			
		}	
		 		
		//Tidy up
		invalidCwn1.removeTransition("TUnconnected");
		
		
		//Add a transition which is only connected via incomming arcs
		invalidCwn1.addTransition("TOnlyIncommingConnections");
		invalidCwn1.addFlowRelationPT("p1", "TOnlyIncommingConnections");
		invalidCwn1.addFlowRelationPT("p2", "TOnlyIncommingConnections");
		
		//Check whether the cwn is valid	
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with a transtion only having incomming arcs) is detected as valid!");
		} catch (PNValidationException e) {			
		}			
		//Tidy up
		invalidCwn1.removeTransition("TOnlyIncommingConnections");
		
		// Add a transition which is only connected via outgoing arcs
		invalidCwn1.addTransition("TOnlyOutgoingConnections");
		invalidCwn1.addFlowRelationTP("TOnlyOutgoingConnections", "p1");
		invalidCwn1.addFlowRelationTP("TOnlyOutgoingConnections", "p2");

		// Check whether the cwn is valid
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with a transtion only having outgoing arcs) is detected as valid!");
		} catch (PNValidationException e) {
		}
		// Tidy up
		invalidCwn1.removeTransition("TOnlyOutgoingConnections");
		
	}
	
	/**
	    * Test the method for checking soundness
	    * Focus on the "option to complete" related part of soundness
	    *
	    */
	   @Test
	   public void testCWNSoundnessOptionToComplete() throws ParameterException {

	       // Create the standard cwn which is sound
	       CWN soundCwn1 = createValidCWN();

	       try {
	           soundCwn1.checkSoundness();
	       } catch (PNSoundnessException e) {
	           fail("A sound CWN was reported to not be sound (PNSoundnessException)");
	       } catch (PNValidationException e) {
	           fail("A sound CWN was reported to not be sound (PNValidationException)");
	       } catch (Exception e) {

	           e.printStackTrace();

	       }

	   }

	

	/**
	 * Test the method for checking validity
	 * Focus on the boundedness related part of validity
	 * 
	 */
	@Test
	public void testCWNValidityBoundedness() throws ParameterException {
		
		// Create the standard cwn which is valid
		CWN invalidCwn1 = createValidCWN();
		
		//Make all places unbounded
		for(CWNPlace p :invalidCwn1.getPlaces()){
			p.removeColorCapacity("black");
			p.removeColorCapacity("green");
			p.removeColorCapacity("red");
		
		}
		
		//Check whether the cwn is valid	
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with all places beeing unbounded) is detected as valid!");
		} catch (PNValidationException e) {			
		}	
		
		
	}
	
	
	
	
	    //Creates the standard cwn.
		//The standard cpn has four places and four transitions.
		//                	 -> t1
		//              	/       \
		//    p0 -> T0-> p1|         p2 -> t3 -> p3
		//              	\       /
		//               	 <- t2<-
		// 
		//p0: black
		private CWN createValidCWN(){
			
			CWN cwn = null;
			
			
			try {
			//Create places	
			Set<String>  places = new HashSet<String>();
			places.add("p0");
			places.add("p1");
			places.add("p2");
			places.add("p3");
			
			
			//create transitions
			Set<String>  transitions = new HashSet<String>();
			transitions.add("t0");
			transitions.add("t1");
			transitions.add("t2");
			transitions.add("t3");
					
			//create the the token colors used in the initial marking		
			Multiset<String> mset = new Multiset<String>();							
			mset.add("black");
			CWNMarking marking = new CWNMarking();
			marking.set("p0", mset);
												
			
			//create the cwn with all tokens in P0
			cwn = new CWN(places, transitions, marking);
			
			//Set bounds for all places 
			//p0 contains only black			
			cwn.getPlace("p0").setColorCapacity("black", 2);
			//p1 contains black and green
			cwn.getPlace("p1").setColorCapacity("black", 2);
			cwn.getPlace("p1").setColorCapacity("green", 2);
			//p2 contains black and red
			cwn.getPlace("p2").setColorCapacity("black", 2);
			cwn.getPlace("p2").setColorCapacity("red", 2);
			//p3 contains black
			cwn.getPlace("p3").setColorCapacity("black", 2);
			
			
			
			//Add the flow relation					
			CWNFlowRelation f1 = cwn.addFlowRelationPT("p0", "t0", true);
			CWNFlowRelation f2 = cwn.addFlowRelationTP("t0", "p1", true);
			CWNFlowRelation f3 = cwn.addFlowRelationPT("p1", "t1", true);
			CWNFlowRelation f4 = cwn.addFlowRelationTP("t1", "p2", true);
			CWNFlowRelation f5 = cwn.addFlowRelationPT("p2", "t2", true);
			CWNFlowRelation f6 = cwn.addFlowRelationTP("t2", "p1", true);
			CWNFlowRelation f7 = cwn.addFlowRelationPT("p2", "t3", true);
			CWNFlowRelation f8 = cwn.addFlowRelationTP("t3", "p3", true);
				
			//configure flow reltions
			f2.addConstraint("green", 1);
			f3.addConstraint("green", 1);
			f4.addConstraint("red", 1);
			f5.addConstraint("red", 1);
			f6.addConstraint("green", 1);
			f7.addConstraint("red", 1);
				
				
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			return cwn;
		}

	
	
	    //Creates a cwn with two input places
		//by adding a place to the standard cwn
		//                	 -> t1
		//              	/       \
		//    p0 -> T0-> p1|         p2 -> t3 -> p3
		//        /     	\       /       
		//     p4       	 <- t2<-          
		// 
		//p0: black
		private void addSecondInputPlaceP4(CWN origCWN){
			
												
			try {
			//Add place	
		    origCWN.addPlace("p4");		    
			
			//Add two flow relation					
			CWNFlowRelation f9 = origCWN.addFlowRelationPT("p4", "t0", true);			
		
			//put a black token in p4
			Multiset<String> mp4 =  new Multiset<String>();
			mp4.add("black");
			origCWN.getMarking().set("p4", mp4);
			
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}									
		}
		
		
		
		
		
		//Creates a cwn with two output places
		//by adding a place to the standard cwn
		//                	 -> t1
		//              	/       \
		//    p0 -> T0-> p1|         p2 -> t3 -> p3
		//              	\       /       \
		//              	 <- t2<-          ->p5
		// 
		//p0: black
		private void addSecondOutputPlaceP5(CWN origCWN){
												
			
			try {
			//Add place			    
			origCWN.addPlace("p5");
			
			//Add flow relation								
			CWNFlowRelation f10 = origCWN.addFlowRelationTP("t3", "p5", true);
		
			
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
									
		}
 
		
		
		

}
