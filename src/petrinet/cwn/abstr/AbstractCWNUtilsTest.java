package petrinet.cwn.abstr;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.PNSoundnessException;

import petrinet.cwn.CWN;
import petrinet.cwn.CWNFlowRelation;
import petrinet.cwn.CWNMarking;
import types.Multiset;
import validate.ParameterException;

public class AbstractCWNUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidCompletion() throws ParameterException {
	
		//create a simple cwn with one inputplace, one output place and one transition
		//pIn --black--> t0 --black-->pOut
		
		//create the two places
		Set<String>  places = new HashSet<String>();
		places.add("pIn");
		places.add("pOut");
		
		
		
		//create transition
		Set<String>  transitions = new HashSet<String>();
		transitions.add("t0");
		
		//create the the token colors used in the initial marking		
		Multiset<String> mset = new Multiset<String>();							
		mset.add("black");
		CWNMarking marking = new CWNMarking();
		marking.set("pIn", mset);
													
				
		//create the cwn with one black token in P0
		CWN cwn = new CWN(places, transitions, marking);
				
		 
		//Add the flow relation					
		CWNFlowRelation inRel = cwn.addFlowRelationPT("pIn", "t0", true);
		CWNFlowRelation outRel = cwn.addFlowRelationTP("t0", "pOut", true);
		
		//Set bounds for all places 
		//p0 contains only black			
		cwn.getPlace("pIn").setColorCapacity("black", 1);
		cwn.getPlace("pOut").setColorCapacity("black", 1);
					
		
		//////////////////////////
		//The net is valid now  //
		//////////////////////////
		try {
			AbstractCWNUtils.validCompletion(cwn);
			
		} catch (PNSoundnessException e) {
			fail("A valid net was reported to be unvalid!");
		}
		
		
		/////////////////////////////////////////
		//Add a pink token to the outrelation  //
		//The net is still valid               //
		/////////////////////////////////////////
		outRel.addConstraint("pink", 1);				
		cwn.getPlace("pOut").setColorCapacity("pink", 1);
		
		//The net is still valid now
		try {
			AbstractCWNUtils.validCompletion(cwn);
		} catch (PNSoundnessException e) {
			fail("A valid net was reported to be unvalid!");
		}
		
		
		
		//////////////////////////////////////
		//remove black from the ourelation  //
		//The net gets invalid by doing so  //
		//////////////////////////////////////
		Multiset<String> onlyPinkConst = new Multiset<String>();
		onlyPinkConst.add("pink");
		outRel.setConstraint(onlyPinkConst);
		
		
		try {
				AbstractCWNUtils.validCompletion(cwn);
				fail("A invalid net was reported to be valid!");
		} catch (PNSoundnessException e) {
		}
		
		///////////////////////////////////////
		//remove all constraints from outrel //
		//The net gets invalid by doing so   //
		///////////////////////////////////////
		outRel.setConstraint(new Multiset<String>());
		
		System.out.println(cwn);
		try {
			AbstractCWNUtils.validCompletion(cwn);
			fail("A invalid net was reported to be valid!");
		} catch (PNSoundnessException e) {
		}
		
	}

}
