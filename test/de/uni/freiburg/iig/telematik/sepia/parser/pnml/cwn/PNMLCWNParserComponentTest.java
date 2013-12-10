package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cwn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.TestResourceFile;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCWN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;

/**
 * <p>
 * Component unit tests for the CWN PNML parser.
 * </p>
 * <p>
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from remote, these tests can take a while.
 * </p>
 * 
 * @author Adrian Lange
 */
@SuppressWarnings("rawtypes")
public class PNMLCWNParserComponentTest {

	/*
	 * Aborts tests after the specified time in milliseconds for the case of connection issues. Especially needed for the validating tests which create a socket connection to a remote server to get the needed RelaxNG schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 20000;

	/* Project intern path to the test resources without leading slash */
	public static final String CWN_TEST_RESOURCES_PATH = "test-resources/pnml/cwn/";

	/* Valid CWN */
	@Rule
	public TestResourceFile CWNResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN.pnml");
	/* CWN without a net ID */
	@Rule
	public TestResourceFile CWNNoNetIDResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noNetID.pnml");
	/* CWN without type attribute */
	@Rule
	public TestResourceFile CWNNoTypeAttributeResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noTypeAttribute.pnml");
	/* CWN with an unknown type */
	@Rule
	public TestResourceFile CWNUnknownTypeResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-unknownType.pnml");
	/* CWN without page tags */
	@Rule
	public TestResourceFile CWNNoPageTagsResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noPageTags.pnml");
	/* CWN with multiple page tags */
	@Rule
	public TestResourceFile CWNMultiplePageTagsResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-multiplePageTags.pnml");
	/* CWN with a missing place ID */
	@Rule
	public TestResourceFile CWNNoPlaceIDResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noPlaceID.pnml");
	/* CWN with a missing place name */
	@Rule
	public TestResourceFile CWNNoPlaceNameResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noPlaceName.pnml");
	/* CWN with an invalid place initial marking */
	@Rule
	public TestResourceFile CWNInvalidInitialMarkingResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-invalidInitialMarking.pnml");
	/* CWN with an invalid place graphics tag */
	@Rule
	public TestResourceFile CWNInvalidPlaceGraphicsResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-invalidPlaceGraphics.pnml");
	/* CWN with an invalid place capacity for the color green */
	@Rule
	public TestResourceFile CWNInvalidPlaceCapacityResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-invalidPlaceCapacity.pnml");
	/* CWN with a missing transition ID */
	@Rule
	public TestResourceFile CWNNoTransitionIDResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noTransitionID.pnml");
	/* CWN with a missing transition name */
	@Rule
	public TestResourceFile CWNNoTransitionNameResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noTransitionName.pnml");
	/* CWN with an invalid transition graphics tag */
	@Rule
	public TestResourceFile CWNInvalidTransitionGraphicsResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-invalidTransitionGraphics.pnml");
	/* CWN with a missing arc ID */
	@Rule
	public TestResourceFile CWNNoArcIDResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noArcID.pnml");
	/* CWN with a missing arc source ID */
	@Rule
	public TestResourceFile CWNNoArcSourceIDResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noArcSourceID.pnml");
	/* CWN with a missing arc target ID */
	@Rule
	public TestResourceFile CWNNoArcTargetIDResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noArcTargetID.pnml");
	/* CWN with a missing arc inscription */
	@Rule
	public TestResourceFile CWNNoArcInscriptionResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-noArcInscription.pnml");
	/* CWN with a negative arc inscription */
	@Rule
	public TestResourceFile CWNNegativeArcInscriptionResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-negativeArcInscription.pnml");
	/* CWN with an invalid arc graphics tag */
	@Rule
	public TestResourceFile CWNInvalidArcGraphicsResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-invalidArcGraphics.pnml");
	/* Valid bounded CWN */
	@Rule
	public TestResourceFile CWNBoundedResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-bounded.pnml");
	/* CWN with incomplete token color RGB values */
	@Rule
	public TestResourceFile CWNIncompleteTokencolorsResource = new TestResourceFile(CWN_TEST_RESOURCES_PATH + "CWN-IncompleteTokencolors.pnml");

	/*
	 * Test if all sample files of the CWN exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void sampleCWNFilesExist() throws Exception {
		assertTrue(CWNResource.getFile().exists());
		assertTrue(CWNNoNetIDResource.getFile().exists());
		assertTrue(CWNNoTypeAttributeResource.getFile().exists());
		assertTrue(CWNUnknownTypeResource.getFile().exists());
		assertTrue(CWNNoPageTagsResource.getFile().exists());
		assertTrue(CWNMultiplePageTagsResource.getFile().exists());
		assertTrue(CWNNoPlaceIDResource.getFile().exists());
		assertTrue(CWNNoPlaceNameResource.getFile().exists());
		assertTrue(CWNInvalidInitialMarkingResource.getFile().exists());
		assertTrue(CWNInvalidPlaceGraphicsResource.getFile().exists());
		assertTrue(CWNInvalidPlaceCapacityResource.getFile().exists());
		assertTrue(CWNNoTransitionIDResource.getFile().exists());
		assertTrue(CWNInvalidTransitionGraphicsResource.getFile().exists());
		assertTrue(CWNNoArcIDResource.getFile().exists());
		assertTrue(CWNNoArcSourceIDResource.getFile().exists());
		assertTrue(CWNNoArcTargetIDResource.getFile().exists());
		assertTrue(CWNNoArcInscriptionResource.getFile().exists());
		assertTrue(CWNNegativeArcInscriptionResource.getFile().exists());
		assertTrue(CWNInvalidArcGraphicsResource.getFile().exists());
		assertTrue(CWNBoundedResource.getFile().exists());
		assertTrue(CWNIncompleteTokencolorsResource.getFile().exists());
	}

	/*
	 * Valid CWN with validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validCWNWithValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CWNResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalCWN);

		GraphicalCWN net = (GraphicalCWN) abstrNet;

		assertEquals("cwn-example", net.getPetriNet().getName());
		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
		assertEquals(2, net.getPetriNetGraphics().getPlaceGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTransitionGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getArcGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getArcAnnotationGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTokenGraphics().size());
		assertEquals(3, net.getPetriNetGraphics().getColors().size());

		assertTrue(net.getPetriNet().isBounded().equals(Boundedness.UNKNOWN));
	}

	/*
	 * Valid CWN without validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validCWNWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CWNResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalCWN);

		GraphicalCWN net = (GraphicalCWN) abstrNet;

		assertEquals("cwn-example", net.getPetriNet().getName());
		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
		assertEquals(2, net.getPetriNetGraphics().getPlaceGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTransitionGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getArcGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getArcAnnotationGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTokenGraphics().size());
		assertEquals(3, net.getPetriNetGraphics().getColors().size());

		assertTrue(net.getPetriNet().isBounded().equals(Boundedness.UNKNOWN));
	}

	/*
	 * NET ID TESTS
	 */

	/*
	 * CWN without a net ID and validation. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noNetIDCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoNetIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN without a net ID and no validation. No exception should be thrown and the net name should be the default name "PetriNet".
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noNetIDCWNWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CWNNoNetIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		assertEquals("PetriNet", abstrNet.getPetriNet().getName());
	}

	/*
	 * TYPE ATTRIBUTE TESTS
	 */

	/*
	 * CWN without type attribute but requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeCWNRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoTypeAttributeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN without type attribute but requiring a valid one. Retrieving the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeCWNRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoTypeAttributeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN without type attribute and not requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeCWNNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoTypeAttributeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN without type attribute and not requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeCWNNotRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoTypeAttributeResource.getFile(), false, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with unknown type attribute but requiring a known one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypeCWNRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNUnknownTypeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with unknown type attribute but requiring a known one. Retrieving the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypeCWNRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNUnknownTypeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with unknown type attribute and not requiring a known one. Validating the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypeCWNNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNUnknownTypeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with unknown type attribute and not requiring a known one. No exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void unknownTypeCWNNotRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNUnknownTypeResource.getFile(), false, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * PAGE TAG TESTS
	 */

	/*
	 * CWN without page tags. The validation should throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPageTagsCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoPageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN without page tags without validation. No exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPageTagsCWNWithoutValidation() {
		try {
			new PNMLParser().parse(CWNNoPageTagsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with multiple page tags. Although this is valid, the parser can't handle it and throws an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNMultiplePageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with multiple page tags. Although this is valid, the parser can't handle it and throws an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsCWNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNMultiplePageTagsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * PLACE TESTS
	 */

	/*
	 * CWN with a missing place ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoPlaceIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing place ID. The parser should throw an exception while reading the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDCWNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoPlaceIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing place ID. As a place name is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlaceNameCWNWithValidation() {
		try {
			new PNMLParser().parse(CWNNoPlaceNameResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing place ID. As a place name is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlaceNameCWNWithoutValidation() {
		try {
			new PNMLParser().parse(CWNNoPlaceNameResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with an invalid place initial marking. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNInvalidInitialMarkingResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with an invalid place initial marking. The parser should throw an exception while reading the places.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingCWNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNInvalidInitialMarkingResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with an invalid place graphics tag. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidPlaceGraphicsCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNInvalidPlaceGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with an invalid place graphics tag. No exception should be thrown and the invalid tag gets ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidPlaceGraphicsCWNWithoutValidation() {
		try {
			new PNMLParser().parse(CWNInvalidPlaceGraphicsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a place capacity of zero for a color. The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidPlaceCapacityCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNInvalidPlaceCapacityResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a place capacity of zero for a color. The parser should throw an exception while setting the capacity.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidPlaceCapacityCWNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNInvalidPlaceCapacityResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * TRANSITION TESTS
	 */

	/*
	 * CWN with a missing transition ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTransitionIDCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoTransitionIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing transition ID. The parser should throw an exception while parsing the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTransitionIDCWNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoTransitionIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing transition name. The parser should not throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noTransitionNameCWNWithValidation() {
		try {
			new PNMLParser().parse(CWNNoTransitionNameResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing transition name. The parser should not throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noTransitionNameCWNWithoutValidation() {
		try {
			new PNMLParser().parse(CWNNoTransitionNameResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with invalid graphical information. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidTransitionGraphicsCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNInvalidTransitionGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with invalid graphical information. No exception should be thrown and the invalid tag gets ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidTransitionGraphicsCWNWithoutValidation() {
		try {
			new PNMLParser().parse(CWNInvalidTransitionGraphicsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * ARC TESTS
	 */

	/*
	 * CWN with a missing arc ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcIDCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoArcIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing arc ID. As a transition ID is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcIDCWNWithoutValidation() {
		try {
			new PNMLParser().parse(CWNNoArcIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing arc source ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcSourceIDCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoArcSourceIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing arc source ID. The parser should throw an exception while creating the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcSourceIDCWNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoArcSourceIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing arc target ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcTargetIDCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoArcTargetIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing arc target ID. The parser should throw an exception while creating the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcTargetIDCWNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoArcTargetIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing arc inscription. As there is control flow dependency in CWNs an exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected=ParserException.class)
	public void noArcInscriptionCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNoArcInscriptionResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a missing arc inscription. As there is no control flow dependency in CWNs, no exception should be thrown and the flow relation should be created with the default constraint.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcInscriptionCWNWithoutValidation() {
		try {
			new PNMLParser().parse(CWNNoArcInscriptionResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a negative arc inscription. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void negativeArcInscriptionCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNegativeArcInscriptionResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a negative arc inscription. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void negativeArcInscriptionCWNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNNegativeArcInscriptionResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a invalid arc graphics. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidArcGraphicsCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNInvalidArcGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with a invalid arc graphics. No exception should be thrown and the invalid tag gets ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidArcGraphicsCWNWithoutValidation() {
		try {
			new PNMLParser().parse(CWNInvalidArcGraphicsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CAPACITY TESTS
	 */

	/*
	 * Valid bounded CWN with validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void boundedCWNWithValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CWNBoundedResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalCWN);

		assertTrue(abstrNet.getPetriNet().isBounded().equals(Boundedness.BOUNDED));
	}

	/*
	 * Valid bounded CWN without validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void boundedCWNWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CWNBoundedResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalCWN);

		assertTrue(abstrNet.getPetriNet().isBounded().equals(Boundedness.BOUNDED));
	}

	/*
	 * TOKENCOLORS TESTS
	 */

	/*
	 * CWN with incomplete token colors RGB values. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void incompleteTokencolorsCWNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CWNIncompleteTokencolorsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * CWN with incomplete token colors RGB values without validation. No parser should be thrown and the missing values should be filled with the default value <code>0</code>.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void incompleteTokencolorsCWNWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CWNIncompleteTokencolorsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		Map<String, Color> tokencolors = ((GraphicalCWN) abstrNet).getPetriNetGraphics().getColors();
		assertEquals(3, tokencolors.size());
		assertEquals(Color.GREEN, tokencolors.get("green"));
		assertEquals(Color.YELLOW, tokencolors.get("yellow"));
		assertEquals(Color.BLUE, tokencolors.get("blue"));
	}
}
