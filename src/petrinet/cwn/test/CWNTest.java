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
import exception.PNException;
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
		CWN cwn = CWNTestUtils.createValidCWN();
		
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
		CWN invalidCwn = CWNTestUtils.createValidCWN();
		CWNTestUtils.addSecondInputPlaceP4(invalidCwn);
		CWNTestUtils.addSecondOutputPlaceP5(invalidCwn);

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
		CWN validCwn = CWNTestUtils.createValidCWN();
		try {
			validCwn.checkValidity();
		} catch (PNValidationException e) {
			fail("A valid CWN is detected as invalid!");
		} 

		// create a cwn with to many input places
		CWN invalidCwn1 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addSecondInputPlaceP4(invalidCwn1);
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with two inputplaces) is detected as valid!");
		} catch (PNValidationException e) {			
		}

		// create a cwn with to many output places
		CWN invalidCwn2 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addSecondOutputPlaceP5(invalidCwn2);
		try {
			invalidCwn2.checkValidity();
			fail("An ivalid CWN (with two inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a cwn with to many input and output places
		CWN invalidCwn3 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addSecondInputPlaceP4(invalidCwn3);
		CWNTestUtils.addSecondOutputPlaceP5(invalidCwn3);
		try {
			invalidCwn3.checkValidity();
			fail("An ivalid CWN (with two inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}

		// create a CWN without an input place
		CWN invalidCwn4 = CWNTestUtils.createValidCWN();		
		CWNTestUtils.removeInputPlace(invalidCwn4);
	
		try {
			invalidCwn4.checkValidity();
			fail("An ivalid CWN (without any inputplaces) is detected as valid!");
		} catch (PNValidationException e) {
		}
 
		// create a CWN without an output place
		CWN invalidCwn5 = CWNTestUtils.createValidCWN();
		CWNTestUtils.removeOutputPlace(invalidCwn5);
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
		CWN invalidCwn1 = CWNTestUtils.createValidCWN();
		
		//setup some markings used later on
		//black black in p0
		Multiset<String> placeStatebb = new Multiset<String>("black","black");		
		CWNMarking p0bb = new CWNMarking();		
		p0bb.set("p0", placeStatebb);
		
		//black all empty in p0
		Multiset<String> placeStateEmpty = new Multiset<String>();		
		CWNMarking empty = new CWNMarking();		
		empty.set("p0", placeStateEmpty);
				
		// black in p0 and p3
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

		
		// green and red token in an inputplace
		Multiset<String> placeStateGreenRed = new Multiset<String>("green", "red");
		CWNMarking p0gr = new CWNMarking();
		p0gr.set("p0", placeStateGreenRed);

				
		
		//All needed markings are set up here
		
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
		
		
		//Set one green and one red token to p0
		invalidCwn1.setInitialMarking(p0gr);	
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (with one green and one red token in the input place) is detected as valid!");
		} catch (PNValidationException e) {
		}	
		
		
		//change the petriNet to not produce black tokens in the outputplace
		
		
		
	}  
	
	
	/**
	 * Test the method for checking validity
	 * Focus on the connectedness related part of validity
	 * 
	 */
	@Test
	public void testCWNValidityConnectedness() throws ParameterException {
		
		// Create the standard cwn which is valid
		CWN invalidCwn1 = CWNTestUtils.createValidCWN();
		
		//Add a unconnceted transition 
		CWNTestUtils.addCompletelyUnconectedTransition(invalidCwn1);		
		
		//Check whether the cwn is valid	
		try {
			invalidCwn1.checkValidity();
			fail("An ivalid CWN (which is not strongly connected) is detected as valid!");
		} catch (PNValidationException e) {		
			
		}	
		 		

		
		//Add a livelock
		CWN invalidCwn2 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addLiveLock(invalidCwn2);

		 
		
		
		//Check whether the cwn is valid	
		try {
			invalidCwn2.checkValidity();
			fail("An ivalid CWN (which is not strongly connected) is detected as valid!");
		} catch (PNValidationException e) {	
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
		CWN invalidCwn1 = CWNTestUtils.createValidCWN();
		
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
		
		System.out.println();
		
	}
	
	
	
	/**
	 * Test the method for checking soundness
	 * Focus on the "option to complete" related part of soundness
	 * @throws PNException 
	 * 
	 */
	@Test(timeout=1000)//i.e. 1 second
	public void testCWNSoundnessOptionToComplete() throws ParameterException, PNException {
				
		// Create the standard cwn which is sound
		CWN soundCwn1 = CWNTestUtils.createValidCWN();	
	

		
		try {			
			soundCwn1.checkSoundness();
		} catch (PNSoundnessException e) {
			fail("A sound CWN was reported to not be sound (PNSoundnessException)");
		} catch (PNValidationException e) {			
			fail("A sound CWN was reported to not be sound (PNValidationException)");
		}
		
			
		//Create a cwn which does not create a black token in the sink place
		CWN unSoundCwn1 = CWNTestUtils.createValidCWN();		
		CWNTestUtils.removeBlackFromRelationT3P3(unSoundCwn1);
		
		 
		try {			
			
			unSoundCwn1.checkSoundness();
			fail("A unsound CWN was not detected as unsound");
		} catch (PNSoundnessException e) {} catch (PNValidationException e) {	}
		
		
		//Create a cwn which has a livelock and thus not the option to complete
		CWN unSoundCwn2 = CWNTestUtils.createValidCWN();
		CWNTestUtils.addLiveLock(unSoundCwn2);
		
		
		try {			
			unSoundCwn2.checkSoundness();
			fail("A unsound CWN was not detected as unsound");
		} catch (PNSoundnessException e) {} catch (PNValidationException e) {	}
		
		
		
	}
	
	
	

	
	/**
	 * Test the method for checking soundness
	 * Focus on the "No Dead Transition" related part of soundness
	 * @throws PNException 
	 * 
	 */
	@Test(timeout=1000)//i.e. 1 second
	public void testCWNSoundnessNoDeadTransition() throws ParameterException, PNException {
		
		// Create the standard cwn which is sound
	    CWN unSoundCwn1 = CWNTestUtils.createValidCWN();
	    
	    //add a dead transition
	    CWNTestUtils.addDeadTransition(unSoundCwn1);
	    
	    try {			
	    	unSoundCwn1.checkSoundness();
			fail("A unsound CWN was not detected as unsound");
		} catch (PNSoundnessException e) {} catch (PNValidationException e) {	}
	    
		
	}

		

}
