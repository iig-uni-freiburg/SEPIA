package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.properties.validity.IFNetValidity;

/**
 * @author boehr
 */
public class DeclassificationTransitionTest {

	/* The variable is filled during start up */
	private IFNet dSNet = null;
	/* The variable is filled during start up */
	private DeclassificationTransition td = null;

	@Before
	public void setUp() throws Exception {

		dSNet = IFNetTestUtil.createSimpleIFNetWithDeclassification();
		td = dSNet.getDeclassificationTransitions().iterator().next();
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether a valid declassifcation transition is recognized as such.
	 */
	@Test
	public void testCheckValidity() {
		try {
			td.checkValidity();
		} catch (PNValidationException e) {
			fail("A valid declassification transition is reported to be invalid.");
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an invalid declassifcation transition with a wrong number of intput places is recognized as such.
	 */
	@Test
	public void testCheckValidityWrongAmountOfInPlaces() {

		// add an input place to the declassification transiotn td
		dSNet.addPlace("pTDIN");

		// connect the extraplace to td
		dSNet.addFlowRelationPT("pTDIN", "td");

		try {
			td.checkValidity();
			fail("An invalid declassification transition is reported to be valid.");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an invalid declassifcation transition with a wrong number of output places is recognized as such.
	 */
	@Test
	public void testCheckValidityWrongAmountOfOutPlaces() {

		// add an input place to the declassification transiotn td
		dSNet.addPlace("pTDOUT");

		// connect the extraplace to td
		dSNet.addFlowRelationTP("td", "pTDOUT");

		try {
			td.checkValidity();
			fail("An invalid declassification transition is reported to be valid.");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an invalid declassifcation transition which does not consume at least one colored token is recognized as such.
	 */
	@Test
	public void testCheckValidityNotEffective() {

		// get the flow relation connected to td
		IFNetFlowRelation dtInRelation = null;
		for (IFNetFlowRelation f : dSNet.getFlowRelations()) {
			if (f.getTarget().getName().equals("td")) {
				dtInRelation = f;
				break;
			}
		}

		if (dtInRelation != null) {
			// Set the constraint of the relation to only black
			Multiset<String> constraint = new Multiset<String>();
			constraint.addAll("black");
			dtInRelation.setConstraint(constraint);
		} else {
			fail("Couldn't find a flow relation with the specified name.");
		}

		try {
			td.checkValidity();
			fail("An invalid declassification transition is reported to be valid.");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an invalid declassifcation transition which has common colors consumed and produced is recognized as such.
	 */
	@Test
	public void testCheckValiditySameColorInAndOut() {

		// get the flow relation connected to td
		IFNetFlowRelation dtInRelation = null;
		IFNetFlowRelation dtOutRelation = null;
		for (IFNetFlowRelation f : dSNet.getFlowRelations()) {
			if (f.getTarget().getName().equals("td")) {
				dtInRelation = f;
			}
			if (f.getSource().getName().equals("td")) {
				dtOutRelation = f;
			}
		}

		// Set the constraint of the relation to the same
		// constraint as the constraint of the out relation
		dtInRelation.setConstraint(dtOutRelation.getConstraint());

		try {
			td.checkValidity();
			fail("An invalid declassification transition is reported to be valid.");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an invalid declassifcation transition which produces a color also CREATed by a regular transition is recognized as such.
	 */
	@Test
	public void testCheckValidityProducedColorCreatedByRegularTransition() {

		// get the flow relation connected to td
		IFNetFlowRelation dtOutRelation = null;
		for (IFNetFlowRelation f : dSNet.getFlowRelations()) {
			if (f.getSource().getName().equals("td")) {
				dtOutRelation = f;
				break;
			}
		}

		if (dtOutRelation != null) {
			// Set the constraint of the relation to the same
			// constraint as the constraint of an outrelation of
			// a regular transition (t0 creates blue)
			Multiset<String> constraint = new Multiset<String>();
			constraint.addAll("black");
			constraint.addAll("blue");
			dtOutRelation.setConstraint(constraint);
			((RegularIFNetTransition) dSNet.getTransition("t0")).setAccessMode("blue", AccessMode.CREATE);
			// System.out.println(dSNet.getTransition("t0").producesColor("blue"));
			// System.out.println(((RegularIFNetTransition) dSNet.getTransition("t0")).getAccessModes("blue").contains(AccessMode.CREATE));
			// System.out.println(dSNet.getDeclassificationTransitions());
		} else {
			fail("Couldn't find a flow relation with the specified name.");
		}

		try {
			IFNetValidity.checkValidity(dSNet);
			fail("An invalid declassification transition is reported to be valid.");
		} catch (PNValidationException e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an valid declassifcation transition which produces a color also processed (but notCREATed) by a regular transition is recognized as such.
	 */
	@Test
	public void testCheckValidityProducedColorProcessedByRegularTransition() {

		// get the flow relation connected to td
		IFNetFlowRelation dtOutRelation = null;
		for (IFNetFlowRelation f : dSNet.getFlowRelations()) {
			if (f.getSource().getName().equals("td")) {
				dtOutRelation = f;
				break;
			}
		}

		if (dtOutRelation != null) {
			// Set the constraint of the relation to the same
			// constraint as the constraint of an outrelation of
			// a regular transition (t0 creates blue)
			Multiset<String> constraint = new Multiset<String>();
			constraint.addAll("black");
			constraint.addAll("green");
			dtOutRelation.setConstraint(constraint);
		} else {
			fail("Couldn't find a flow relation with the specified name.");
		}

		try {
			td.checkValidity();
		} catch (PNValidationException e) {
			fail("A valid declassification transition is reported to be invalid.");
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an invalid declassifcation transition which produces a color also produced by another declassification transition is recognized as such.
	 */
	@Test
	public void testCheckValidityProducedColorCreatedByOtherDeclassTransition() {

		// get the flow relations connected to td
		IFNetFlowRelation dtInRelation = null;
		IFNetFlowRelation dtOutRelation = null;
		for (IFNetFlowRelation f : dSNet.getFlowRelations()) {
			if (f.getTarget().getName().equals("td")) {
				dtInRelation = f;
			}
			if (f.getSource().getName().equals("td")) {
				dtOutRelation = f;
			}
		}

		// Add a further declassification transition to the net
		dSNet.addDeclassificationTransition("td2");

		// add relations of the second declassification function
		// with the same constraints as td
		IFNetFlowRelation dt2InRel = dSNet.addFlowRelationPT("p1", "td2");
		IFNetFlowRelation dt2OutRel = dSNet.addFlowRelationTP("td2", "p4");

		// Set the constraints of the relations of td2 to the same
		// constraint as the constraint of the relations of td
		dt2InRel.setConstraint(dtInRelation.getConstraint());
		dt2OutRel.setConstraint(dtOutRelation.getConstraint());

		try {
			IFNetValidity.checkValidity(dSNet);
			fail("An invalid declassification transition is reported to be valid.");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an valid declassifcation transition which produces a color not produced by another declassification transition is recognized as such.
	 */
	@Test
	public void testCheckValidityProducedColorNotCreatedByOtherDeclassTransition() {

		// Add a further declassification transition to the net
		dSNet.addDeclassificationTransition("td2");

		// add relations of the second declassification function
		// with the same constraints as td
		IFNetFlowRelation dt2InRel = dSNet.addFlowRelationPT("p1", "td2");
		IFNetFlowRelation dt2OutRel = dSNet.addFlowRelationTP("td2", "p4");

		// Set the constraints of the relations of td2 to be different
		// constraint as the constraint of the relations of td
		Multiset<String> inConstraint = new Multiset<String>();
		inConstraint.addAll("black");
		inConstraint.addAll("red");

		Multiset<String> outConstraint = new Multiset<String>();
		outConstraint.addAll("black");
		outConstraint.addAll("orange");

		dt2InRel.setConstraint(inConstraint);
		dt2OutRel.setConstraint(outConstraint);

		try {
			td.checkValidity();
		} catch (PNValidationException e) {
			fail("A valid declassification transition is reported to be invalid.");
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an invalid declassifcation transition where the number of consumed token colors does not match the number of produced token colors is recognized as such.
	 */
	@Test
	public void testCheckValidityProducedConsumedColorsDoNotMatch() {

		// get the flow relations connected to td
		IFNetFlowRelation dtInRelation = null;
		IFNetFlowRelation dtOutRelation = null;
		for (IFNetFlowRelation f : dSNet.getFlowRelations()) {
			if (f.getTarget().getName().equals("td")) {
				dtInRelation = f;
			}
			if (f.getSource().getName().equals("td")) {
				dtOutRelation = f;
			}
		}

		// Set the constraints of the relations of td to be
		// to have non matching token colors
		Multiset<String> inConstraint = new Multiset<String>();
		inConstraint.addAll("black");
		inConstraint.addAll("red");

		Multiset<String> outConstraint = new Multiset<String>();
		outConstraint.addAll("black");
		outConstraint.addAll("yellow");
		outConstraint.addAll("pink");

		dtInRelation.setConstraint(inConstraint);
		dtOutRelation.setConstraint(outConstraint);

		try {
			td.checkValidity();
			fail("An invalid declassification transition is reported to be valid.");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#checkValidity()}.<br/> Check whether an invalid declassifcation transition where the number of tokens of a consumed token colors does not match the number of tokens of produced token colors is recognized as such.
	 */
	@Test
	public void testCheckValidityProducedConsumedColorsAmountDoesNotMatch() {

		// get the flow relations connected to td
		IFNetFlowRelation dtInRelation = null;
		IFNetFlowRelation dtOutRelation = null;
		for (IFNetFlowRelation f : dSNet.getFlowRelations()) {
			if (f.getTarget().getName().equals("td")) {
				dtInRelation = f;
			}
			if (f.getSource().getName().equals("td")) {
				dtOutRelation = f;
			}
		}

		// Set the constraints of the relations of td
		// to have non matching token amounts for a color colors
		Multiset<String> inConstraint = new Multiset<String>();
		inConstraint.addAll("black");
		inConstraint.addAll("red");

		Multiset<String> outConstraint = new Multiset<String>();
		outConstraint.addAll("black");
		outConstraint.addAll("yellow", "yellow");

		dtInRelation.setConstraint(inConstraint);
		dtOutRelation.setConstraint(outConstraint);

		try {
			td.checkValidity();
			fail("An invalid declassification transition is reported to be valid.");
		} catch (PNValidationException e) {
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#isDeclassificator()}.
	 */
	@Test
	public void testIsDeclassificator() {
		assertTrue(td.isDeclassificator());
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#DeclassificationTransition(de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet, java.lang.String, java.lang.String, boolean)}.
	 */
	@Test
	public void testDeclassificationTransitionSNetStringStringBoolean() {

		DeclassificationTransition dt = null;
		try {
			dt = new DeclassificationTransition("dec", true);
		} catch (ParameterException e) {
			fail("Cannot create DeclassificationTransition.");
		}

		// Check the varaibles are set correctly
		assertTrue(dt.getName().equals("dec"));
		assertTrue(dt.getName().equals(dt.getLabel()));
		assertTrue(dt.isSilent());

		DeclassificationTransition dt2 = null;
		try {
			dt2 = new DeclassificationTransition("dec", false);
		} catch (ParameterException e) {
			fail("Cannot create DeclassificationTransition.");
		}

		// Check the varaibles are set correctly
		assertTrue(dt2.getName().equals("dec"));
		assertTrue(dt2.getName().equals(dt.getLabel()));
		assertFalse(dt2.isSilent());

	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.DeclassificationTransition#DeclassificationTransition(de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet, java.lang.String)}.
	 */
	@Test
	public void testDeclassificationTransitionSNetString() {

		DeclassificationTransition dt = null;
		try {
			dt = new DeclassificationTransition("dec");
		} catch (ParameterException e) {
			fail("Cannot create DeclassificationTransition.");
		}

		// Check the varaibles are set correctly
		assertTrue(dt.getName().equals("dec"));
		assertTrue(dt.getName().equals(dt.getLabel()));
		assertFalse(dt.isSilent());
	}

	/*
	 * Test the clone() method
	 */
	@Test
	public void testDeclassificationTransitionClone() {
		IFNet sNet = IFNetTestUtil.createSimpleIFNetWithDeclassificationNoAC();
		DeclassificationTransition t = (DeclassificationTransition) sNet.getTransition("td");
		t.setSilent(true);

		DeclassificationTransition tClone = t.clone();
		assertEquals(t, tClone);
		assertNotSame(t, tClone);
		assertTrue(tClone.isSilent());
		assertEquals(t.getName(), tClone.getName());
		assertEquals(t.getLabel(), tClone.getLabel());
		assertEquals(t.isPlace(), tClone.isPlace());
		assertEquals(t.isDrain(), tClone.isDrain());
		assertEquals(t.isSource(), tClone.isSource());
		assertEquals(t.isTransition(), tClone.isTransition());
	}
}
