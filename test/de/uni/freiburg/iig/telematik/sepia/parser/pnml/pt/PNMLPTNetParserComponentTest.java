package de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.TestResourceFile;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;

/**
 * <p>
 * Component unit tests for the P/T-net PNML parser.
 * </p>
 * <p>
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from remote, these tests can take a while.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLPTNetParserComponentTest {

	/*
	 * Aborts tests after the specified time in milliseconds for the case of connection issues. Especially needed for the validating tests which create a socket connection to a remote server to get the needed RelaxNG schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 20000;

	/* Project intern path to the test resources without leading slash */
	public static final String PTNET_TEST_RESOURCES_PATH = "test-resources/pnml/pt/";

	/* Valid P/T-net */
	@Rule
	public TestResourceFile PTnetResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet.pnml");
	/* CPN without a net ID */
	@Rule
	public TestResourceFile PTnetNoNetIDResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noNetID.pnml");
	/* P/T-net without type attribute */
	@Rule
	public TestResourceFile PTnetWithoutTypeResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noTypeAttribute.pnml");
	/* P/T-net without type attribute */
	@Rule
	public TestResourceFile PTnetWithUnknownTypeResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-unknownTypeAttribute.pnml");
	/* P/T-net without page tags */
	@Rule
	public TestResourceFile PTnetWithoutPageTagsResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noPageTags.pnml");
	/* P/T-net without page tags */
	@Rule
	public TestResourceFile PTnetWithMultiplePageTagsResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-multiplePageTags.pnml");
	/* P/T-net without a place ID */
	@Rule
	public TestResourceFile PTnetNoPlaceIDResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noPlaceID.pnml");
	/* P/T-net missing a place name */
	@Rule
	public TestResourceFile PTnetNoPlaceNameResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noPlaceName.pnml");
	/* P/T-net with a place with an invalid initial marking */
	@Rule
	public TestResourceFile PTnetInvalidInitialMarkingResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-invalidPlaceInitialMarking.pnml");
	/* P/T-net with a place with invalid graphical information */
	@Rule
	public TestResourceFile PTnetInvalidPlaceGraphicsResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-invalidPlaceGraphics.pnml");
	/* P/T-net with a place with invalid graphical information */
	@Rule
	public TestResourceFile PTnetInvalidPlaceCapacityResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-invalidPlaceCapacity.pnml");
	/* P/T-net without a transition ID */
	@Rule
	public TestResourceFile PTnetNoTransitionIDResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noTransitionID.pnml");
	/* P/T-net missing a transition name */
	@Rule
	public TestResourceFile PTnetNoTransitionNameResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noTransitionName.pnml");
	/* P/T-net with a transition with invalid graphical information */
	@Rule
	public TestResourceFile PTnetInvalidTransitionGraphicsResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-invalidTransitionGraphics.pnml");
	/* P/T-net without an arc ID */
	@Rule
	public TestResourceFile PTnetNoArcIDResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noArcID.pnml");
	/* P/T-net without an arc source ID */
	@Rule
	public TestResourceFile PTnetNoSourceIDResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noSourceID.pnml");
	/* P/T-net without an arc target ID */
	@Rule
	public TestResourceFile PTnetNoTargetIDResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noTargetID.pnml");
	/* P/T-net without an arc inscription */
	@Rule
	public TestResourceFile PTnetNoArcInscriptionResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-noArcInscription.pnml");
	/* P/T-net with a negative arc inscription */
	@Rule
	public TestResourceFile PTnetNegativeArcInscriptionResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-negativeArcInscription.pnml");
	/* P/T-net with an arc with invalid graphical information */
	@Rule
	public TestResourceFile PTnetInvalidArcGraphicsResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-invalidArcGraphics.pnml");
	/* Valid bounded P/T-net */
	@Rule
	public TestResourceFile PTnetBoundedResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet-bounded.pnml");

	/*
	 * Test if all sample files of the P/T-net exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void samplePTNetFilesExist() throws Exception {
		assertTrue(PTnetResource.getFile().exists());
		assertTrue(PTnetNoNetIDResource.getFile().exists());
		assertTrue(PTnetWithoutTypeResource.getFile().exists());
		assertTrue(PTnetWithUnknownTypeResource.getFile().exists());
		assertTrue(PTnetWithoutPageTagsResource.getFile().exists());
		assertTrue(PTnetWithMultiplePageTagsResource.getFile().exists());
		assertTrue(PTnetNoPlaceIDResource.getFile().exists());
		assertTrue(PTnetNoPlaceNameResource.getFile().exists());
		assertTrue(PTnetInvalidInitialMarkingResource.getFile().exists());
		assertTrue(PTnetInvalidPlaceGraphicsResource.getFile().exists());
		assertTrue(PTnetInvalidPlaceCapacityResource.getFile().exists());
		assertTrue(PTnetNoTransitionIDResource.getFile().exists());
		assertTrue(PTnetNoTransitionNameResource.getFile().exists());
		assertTrue(PTnetInvalidTransitionGraphicsResource.getFile().exists());
		assertTrue(PTnetNoArcIDResource.getFile().exists());
		assertTrue(PTnetNoSourceIDResource.getFile().exists());
		assertTrue(PTnetNoTargetIDResource.getFile().exists());
		assertTrue(PTnetNoArcInscriptionResource.getFile().exists());
		assertTrue(PTnetNegativeArcInscriptionResource.getFile().exists());
		assertTrue(PTnetInvalidArcGraphicsResource.getFile().exists());
		assertTrue(PTnetBoundedResource.getFile().exists());
	}

	/*
	 * Valid P/T-net with validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validPTnetWithValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(PTnetResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalPTNet);

		GraphicalPTNet net = (GraphicalPTNet) abstrNet;

		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
		assertEquals(2, net.getPetriNetGraphics().getPlaceGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTransitionGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getArcGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getArcAnnotationGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTokenGraphics().size());

		assertTrue(net.getPetriNet().getBoundedness().equals(Boundedness.UNKNOWN));
	}

	/*
	 * Valid P/T-net without validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validPTnetWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(PTnetResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalPTNet);

		GraphicalPTNet net = (GraphicalPTNet) abstrNet;

		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
		assertEquals(2, net.getPetriNetGraphics().getPlaceGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTransitionGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getArcGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getArcAnnotationGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTokenGraphics().size());

		assertTrue(net.getPetriNet().getBoundedness().equals(Boundedness.UNKNOWN));
	}

	/*
	 * NET ID TESTS
	 */

	/*
	 * P/T-net without a net ID and validation. Validation should throw a ParserException.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noNetIDPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoNetIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net without a net ID and no validation. No exception should be thrown and the net name should be the default name "PetriNet".
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noNetIDPTnetWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(PTnetNoNetIDResource.getFile(), true, false);
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
	 * P/T-net without type attribute but requiring a valid one. Validation should throw a ParserException.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypePTnetRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutTypeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net without type attribute but requiring a valid one. Retrieving the net type should throw a ParserException.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypePTnetRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutTypeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net without type attribute and not requiring a valid one. Validation should throw a ParserException.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypePTnetNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutTypeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net without type attribute and not requiring a valid one. Retrieving the net type should throw a ParserException.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypePTnetNotRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutTypeResource.getFile(), false, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with unknown type attribute but requiring a known one. Validation should throw a ParserException.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypePTnetRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithUnknownTypeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with unknown type attribute but requiring a known one. Retrieving the net type should throw a ParserException.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypePTnetRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithUnknownTypeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with unknown type attribute and not requiring a known one. Validating the net type should throw a ParserException.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypePTnetNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithUnknownTypeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with unknown type attribute and not requiring a known one. No exception should be thrown.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void unknownTypePTnetNotRequiringTypeWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?,?,?> net = null;
		try {
			net = new PNMLParser().parse(PTnetWithUnknownTypeResource.getFile(), false, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		// Unknown net should be interpreted as P/T-net
		assertTrue(net instanceof GraphicalPTNet);
	}

	/*
	 * PAGE TAG TESTS
	 */

	/*
	 * P/T-net without page tags. The validation should throw an exception.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPageTagsPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithoutPageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net without page tags and no validation. As the page tags are ignored by the parser, no exception should be thrown.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPageTagsPTnetWithoutValidation() {
		try {
			new PNMLParser().parse(PTnetWithoutPageTagsResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with multiple page tags. Although this is valid, the parser can't handle it and throws an exception.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithMultiplePageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with multiple page tags without validation. The parser can't handle this and throws an exception.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetWithMultiplePageTagsResource.getFile(), true, false);
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
	 * P/T-net with a missing place ID. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoPlaceIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing place ID. The parser should throw an exception while reading the flow relations.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoPlaceIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing place name. As a place name is not necessary, no exception should be thrown.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlaceNamePTnetWithValidation() {
		try {
			new PNMLParser().parse(PTnetNoPlaceNameResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing place name. As a place name is not necessary, no exception should be thrown.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlaceNamePTnetWithoutValidation() {
		try {
			new PNMLParser().parse(PTnetNoPlaceNameResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with an invalid place initial marking. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidInitialMarkingResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with an invalid place initial marking. The parser should throw an exception while reading the places.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidInitialMarkingResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a invalid place graphics. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidPlaceGraphicsPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidPlaceGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a invalid place graphics. The invalid offset tag should be ignored.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidPlaceGraphicsPTnetWithoutValidation() {
		try {
			new PNMLParser().parse(PTnetInvalidPlaceGraphicsResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		// A NodeGraphics object can't contain offsets. They are just ignored.
	}

	/*
	 * P/T-net with a place capacity of zero. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidPlaceCapacityPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidPlaceCapacityResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a place capacity of zero. The parser should throw an exception while reading the places.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidPlaceCapacityPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidPlaceCapacityResource.getFile(), true, false);
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
	 * P/T-net with a missing transition ID. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTransitionIDPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoTransitionIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing transition ID. The parser should throw an exception while adding the flow relations.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTransitionIDPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoTransitionIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing transition name. The parser should not throw an exception.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noTransitionNamePTnetWithValidation() {
		try {
			new PNMLParser().parse(PTnetNoTransitionNameResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing transition name. The parser should not throw an exception.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noTransitionNamePTnetWithoutValidation() {
		try {
			new PNMLParser().parse(PTnetNoTransitionNameResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with invalid graphical information. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidTransitionGraphicsPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidTransitionGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with invalid graphical information. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidTransitionGraphicsPTnetWithoutValidation() {
		try {
			new PNMLParser().parse(PTnetInvalidTransitionGraphicsResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		// A NodeGraphics object can't contain offsets. They are just ignored.
	}

	/*
	 * ARC TESTS
	 */

	/*
	 * P/T-net with a missing arc ID. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcIDPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoArcIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing arc ID. As a transition name is not necessary, no exception should be thrown.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcIDPTnetWithoutValidation() {
		try {
			new PNMLParser().parse(PTnetNoArcIDResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing arc source ID. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcSourceIDPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoSourceIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing arc source ID. The parser should throw an exception while reading the flow relations.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcSourceIDPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoSourceIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing arc target ID. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcTargetIDPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoTargetIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing arc target ID. The parser should throw an exception while reading the flow relations.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcTargetIDPTnetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNoTargetIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing arc inscription. As there is no control flow dependency in P/T-nets, no exception should be thrown and the flow relation should be created with the default constraint.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcInscriptionPTnetWithValidation() {
		try {
			new PNMLParser().parse(PTnetNoArcInscriptionResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a missing arc inscription. As there is no control flow dependency in P/T-nets, no exception should be thrown and the flow relation should be created with the default constraint.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcInscriptionPTnetWithoutValidation() {
		try {
			new PNMLParser().parse(PTnetNoArcInscriptionResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a negative arc inscription. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void negativeArcInscriptionPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetNegativeArcInscriptionResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a negative arc inscription. The parser should throw an exception while checking the parameters of the flow relation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void negativeArcInscriptionPTnetWithoutValidation() throws ParameterException {
		try {
			new PNMLParser().parse(PTnetNegativeArcInscriptionResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a invalid arc graphics. The parser should throw an exception while validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidArcGraphicsPTnetWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(PTnetInvalidArcGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * P/T-net with a invalid arc graphics. The invalid offset tag should be ignored.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidArcGraphicsPTnetWithoutValidation() {
		try {
			new PNMLParser().parse(PTnetInvalidArcGraphicsResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		// A ArcGraphics object can't contain offsets. They are just ignored.
	}

	/*
	 * CAPACITY TESTS
	 */

	/*
	 * Valid bounded P/T-net with validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void boundedPTnetWithValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(PTnetBoundedResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalPTNet);

		assertTrue(abstrNet.getPetriNet().getBoundedness().equals(Boundedness.BOUNDED));
	}

	/*
	 * Valid bounded P/T-net with validation.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void boundedPTnetWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(PTnetBoundedResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalPTNet);

		assertTrue(abstrNet.getPetriNet().getBoundedness().equals(Boundedness.BOUNDED));
	}
}
