package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn;

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
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;

/**
 * <p>
 * Component unit tests for the CPN PNML parser.
 * </p>
 * <p>
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from remote, these tests can take a while.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLCPNParserComponentTest {

	/**
	 * Aborts tests after the specified time in milliseconds for the case of connection issues. Especially needed for the validating tests which create a socket connection to a remote server to get the needed RelaxNG schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 20000;

	/** Project intern path to the test resources without leading slash */
	public static final String CPN_TEST_RESOURCES_PATH = "test-resources/pnml/cpn/";

	/** Valid CPN */
	@Rule
	public TestResourceFile CPNResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN.pnml");
	/** CPN without type attribute */
	@Rule
	public TestResourceFile CPNNoTypeAttributeResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noTypeAttribute.pnml");
	/** CPN with an unknown type */
	@Rule
	public TestResourceFile CPNUnknownTypeResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-unknownType.pnml");
	/** CPN without page tags */
	@Rule
	public TestResourceFile CPNNoPageTagsResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noPageTags.pnml");
	/** CPN with multiple page tags */
	@Rule
	public TestResourceFile CPNMultiplePageTagsResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-multiplePageTags.pnml");
	/** CPN with a missing place ID */
	@Rule
	public TestResourceFile CPNNoPlaceIDResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noPlaceID.pnml");
	/** CPN with a missing place name */
	@Rule
	public TestResourceFile CPNNoPlaceNameResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noPlaceName.pnml");
	/** CPN with an invalid place initial marking */
	@Rule
	public TestResourceFile CPNInvalidInitialMarkingResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-invalidInitialMarking.pnml");
	/** CPN with an invalid place graphics tag */
	@Rule
	public TestResourceFile CPNInvalidPlaceGraphicsResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-invalidPlaceGraphics.pnml");
	/** CPN with an invalid place capacity for the color green */
	@Rule
	public TestResourceFile CPNInvalidPlaceCapacityResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-invalidPlaceCapacity.pnml");
	/** CPN with a missing transition ID */
	@Rule
	public TestResourceFile CPNNoTransitionIDResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noTransitionID.pnml");
	/** CPN with a missing transition name */
	@Rule
	public TestResourceFile CPNNoTransitionNameResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noTransitionName.pnml");
	/** CPN with an invalid transition graphics tag */
	@Rule
	public TestResourceFile CPNInvalidTransitionGraphicsResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-invalidTransitionGraphics.pnml");
	/** CPN with a missing arc ID */
	@Rule
	public TestResourceFile CPNNoArcIDResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noArcID.pnml");
	/** CPN with a missing arc source ID */
	@Rule
	public TestResourceFile CPNNoArcSourceIDResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noArcSourceID.pnml");
	/** CPN with a missing arc target ID */
	@Rule
	public TestResourceFile CPNNoArcTargetIDResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noArcTargetID.pnml");
	/** CPN with a missing arc inscription */
	@Rule
	public TestResourceFile CPNNoArcInscriptionResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-noArcInscription.pnml");
	/** CPN with a negative arc inscription */
	@Rule
	public TestResourceFile CPNNegativeArcInscriptionResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-negativeArcInscription.pnml");
	/** CPN with an invalid arc graphics tag */
	@Rule
	public TestResourceFile CPNInvalidArcGraphicsResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-invalidArcGraphics.pnml");
	/** Valid bounded CPN */
	@Rule
	public TestResourceFile CPNBoundedResource = new TestResourceFile(CPN_TEST_RESOURCES_PATH + "CPN-bounded.pnml");

	/**
	 * Test if all sample files of the CPN exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void sampleCPNFilesExist() throws Exception {
		assertTrue(CPNResource.getFile().exists());
		assertTrue(CPNNoTypeAttributeResource.getFile().exists());
		assertTrue(CPNUnknownTypeResource.getFile().exists());
		assertTrue(CPNNoPageTagsResource.getFile().exists());
		assertTrue(CPNMultiplePageTagsResource.getFile().exists());
		assertTrue(CPNNoPlaceIDResource.getFile().exists());
		assertTrue(CPNNoPlaceNameResource.getFile().exists());
		assertTrue(CPNInvalidInitialMarkingResource.getFile().exists());
		assertTrue(CPNInvalidPlaceGraphicsResource.getFile().exists());
		assertTrue(CPNInvalidPlaceCapacityResource.getFile().exists());
		assertTrue(CPNNoTransitionIDResource.getFile().exists());
		assertTrue(CPNInvalidTransitionGraphicsResource.getFile().exists());
		assertTrue(CPNNoArcIDResource.getFile().exists());
		assertTrue(CPNNoArcSourceIDResource.getFile().exists());
		assertTrue(CPNNoArcTargetIDResource.getFile().exists());
		assertTrue(CPNNoArcInscriptionResource.getFile().exists());
		assertTrue(CPNNegativeArcInscriptionResource.getFile().exists());
		assertTrue(CPNInvalidArcGraphicsResource.getFile().exists());
		assertTrue(CPNBoundedResource.getFile().exists());
	}

	/**
	 * Valid CPN with validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validCPNWithValidation() {
		AbstractGraphicalPN<?, ?, ?, ?, ?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CPNResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalCPN);

		GraphicalCPN net = (GraphicalCPN) abstrNet;

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

	/**
	 * Valid CPN without validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validCPNWithoutValidation() {
		AbstractGraphicalPN<?, ?, ?, ?, ?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CPNResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalCPN);

		GraphicalCPN net = (GraphicalCPN) abstrNet;

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
	 * TYPE ATTRIBUTE TESTS
	 */

	/**
	 * CPN without type attribute but requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeCPNRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoTypeAttributeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN without type attribute but requiring a valid one. Retrieving the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeCPNRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoTypeAttributeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN without type attribute and not requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeCPNNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoTypeAttributeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN without type attribute and not requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeCPNNotRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoTypeAttributeResource.getFile(), false, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with unknown type attribute but requiring a known one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypeCPNRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNUnknownTypeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with unknown type attribute but requiring a known one. Retrieving the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypeCPNRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNUnknownTypeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with unknown type attribute and not requiring a known one. Validating the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypeCPNNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNUnknownTypeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with unknown type attribute and not requiring a known one. No exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void unknownTypeCPNNotRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNUnknownTypeResource.getFile(), false, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * PAGE TAG TESTS
	 */

	/**
	 * CPN without page tags. The validation should throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPageTagsCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoPageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN without page tags without validation. No exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPageTagsCPNWithoutValidation() {
		try {
			new PNMLParser().parse(CPNNoPageTagsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with multiple page tags. Although this is valid, the parser can't handle it and throws an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNMultiplePageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with multiple page tags. Although this is valid, the parser can't handle it and throws an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsCPNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNMultiplePageTagsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * PLACE TESTS
	 */

	/**
	 * CPN with a missing place ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoPlaceIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing place ID. The parser should throw an exception while reading the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDCPNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoPlaceIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing place ID. As a place name is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlaceNameCPNWithValidation() {
		try {
			new PNMLParser().parse(CPNNoPlaceNameResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing place ID. As a place name is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlaceNameCPNWithoutValidation() {
		try {
			new PNMLParser().parse(CPNNoPlaceNameResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with an invalid place initial marking. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNInvalidInitialMarkingResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with an invalid place initial marking. The parser should throw an exception while reading the places.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingCPNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNInvalidInitialMarkingResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with an invalid place graphics tag. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidPlaceGraphicsCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNInvalidPlaceGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with an invalid place graphics tag. No exception should be thrown and the invalid tag gets ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidPlaceGraphicsCPNWithoutValidation() {
		try {
			new PNMLParser().parse(CPNInvalidPlaceGraphicsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a place capacity of zero for a color. The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidPlaceCapacityCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNInvalidPlaceCapacityResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a place capacity of zero for a color. The parser should throw an exception while setting the capacity.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidPlaceCapacityCPNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNInvalidPlaceCapacityResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * TRANSITION TESTS
	 */

	/**
	 * CPN with a missing transition ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTransitionIDCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoTransitionIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing transition ID. The parser should throw an exception while parsing the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTransitionIDCPNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoTransitionIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing transition name. The parser should not throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noTransitionNameCPNWithValidation() {
		try {
			new PNMLParser().parse(CPNNoTransitionNameResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing transition name. The parser should not throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noTransitionNameCPNWithoutValidation() {
		try {
			new PNMLParser().parse(CPNNoTransitionNameResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with invalid graphical information. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidTransitionGraphicsCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNInvalidTransitionGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with invalid graphical information. No exception should be thrown and the invalid tag gets ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidTransitionGraphicsCPNWithoutValidation() {
		try {
			new PNMLParser().parse(CPNInvalidTransitionGraphicsResource.getFile(), true, false);
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

	/**
	 * CPN with a missing arc ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcIDCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoArcIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing arc ID. As a transition ID is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcIDCPNWithoutValidation() {
		try {
			new PNMLParser().parse(CPNNoArcIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing arc source ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcSourceIDCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoArcSourceIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing arc source ID. The parser should throw an exception while creating the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcSourceIDCPNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoArcSourceIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing arc target ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcTargetIDCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoArcTargetIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing arc target ID. The parser should throw an exception while creating the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcTargetIDCPNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNoArcTargetIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing arc inscription. As there is no control flow dependency in CPNs, no exception should be thrown and the flow relation should be created with the default constraint.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcInscriptionCPNWithValidation() {
		try {
			new PNMLParser().parse(CPNNoArcInscriptionResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a missing arc inscription. As there is no control flow dependency in CPNs, no exception should be thrown and the flow relation should be created with the default constraint.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcInscriptionCPNWithoutValidation() {
		try {
			new PNMLParser().parse(CPNNoArcInscriptionResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a negative arc inscription. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void negativeArcInscriptionCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNegativeArcInscriptionResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a negative arc inscription. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void negativeArcInscriptionCPNWithoutValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNNegativeArcInscriptionResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a invalid arc graphics. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidArcGraphicsCPNWithValidation() throws ParserException {
		try {
			new PNMLParser().parse(CPNInvalidArcGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/**
	 * CPN with a invalid arc graphics. No exception should be thrown and the invalid tag gets ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidArcGraphicsCPNWithoutValidation() {
		try {
			new PNMLParser().parse(CPNInvalidArcGraphicsResource.getFile(), true, false);
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

	/**
	 * Valid bounded CPN with validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void boundedCPNWithValidation() {
		AbstractGraphicalPN<?, ?, ?, ?, ?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CPNBoundedResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalCPN);

		assertTrue(abstrNet.getPetriNet().isBounded().equals(Boundedness.BOUNDED));
	}

	/**
	 * Valid bounded CPN without validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void boundedCPNWithoutValidation() {
		AbstractGraphicalPN<?, ?, ?, ?, ?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(CPNBoundedResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalCPN);

		assertTrue(abstrNet.getPetriNet().isBounded().equals(Boundedness.BOUNDED));
	}
}
