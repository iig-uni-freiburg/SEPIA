package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNUtils;

public class AbstractCWNUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidCompletion() throws ParameterException {

		// Get a simple CWN
		CWN cwn = CWNTestUtils.createSimpleCWN();

		//////////////////////////
		// The net is valid now //
		//////////////////////////
		try {
			AbstractCWNUtils.validCompletion(cwn);

		} catch (PNSoundnessException e) {
			fail("A valid net was reported to be unvalid!");
		}

		//////////////////////////////////////////
		// Add a pink token to the out relation //
		// The net is still valid               //
		//////////////////////////////////////////
		CWNFlowRelation outRel = null;
		for (CWNFlowRelation f : cwn.getFlowRelations()) {
			if (f.getTarget().getName().equals("pOut")) {
				outRel = f;
			}
		}

		outRel.addConstraint("pink", 1);
		cwn.getPlace("pOut").setColorCapacity("pink", 1);

		// The net is still valid now
		try {
			AbstractCWNUtils.validCompletion(cwn);
		} catch (PNSoundnessException e) {
			fail("A valid net was reported to be unvalid!");
		}

		////////////////////////////////////////
		// remove black from the out relation //
		// The net gets invalid by doing so   //
		////////////////////////////////////////
		Multiset<String> onlyPinkConst = new Multiset<String>();
		onlyPinkConst.add("pink");
		outRel.setConstraint(onlyPinkConst);

		try {
			AbstractCWNUtils.validCompletion(cwn);
			fail("A invalid net was reported to be valid!");
		} catch (PNSoundnessException e) {
		}

		///////////////////////////////////////////////////////////////////////////
		// change the net to contain a non terminal state where a black token is //
		// in a place other than the sink place. ==> add a black token to the    //
		// sink place in the inital marking                                      //
		///////////////////////////////////////////////////////////////////////////
		CWNMarking newInitialMarking = new CWNMarking();
		Multiset<String> b1 = new Multiset<String>();
		Multiset<String> b2 = new Multiset<String>();
		b1.add("black");
		b2.add("black");
		newInitialMarking.set("pIn", b1);
		newInitialMarking.set("pOut", b2);
		cwn.setInitialMarking(newInitialMarking);
		try {
			AbstractCWNUtils.validCompletion(cwn);
			fail("A invalid net was reported to be valid!");
		} catch (PNSoundnessException e) {
		}
	}
}
