package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.TestResourceFile;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.IFNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

/**
 * <p>
 * Component unit tests for the IFNet PNML parser.
 * </p>
 * <p>
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from remote, these tests can take a while.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLIFNetParserComponentTest {

	/*
	 * Aborts tests after the specified time in milliseconds for the case of connection issues. Especially needed for the validating tests which create a socket connection to a remote server to get the needed RelaxNG schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 20000;

	/* Project intern path to the test resources without leading slash */
	public static final String IFNet_TEST_RESOURCES_PATH = "test-resources/pnml/ifnet/";

	/* Valid IFNet */
	@Rule
	public TestResourceFile IFNetResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet.pnml");
	/* IFNet without a net ID */
	@Rule
	public TestResourceFile IFNetNoNetIDResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noNetID.pnml");
	/* IFNet without type attribute */
	@Rule
	public TestResourceFile IFNetNoTypeAttributeResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noTypeAttribute.pnml");
	/* IFNet with an unknown type */
	@Rule
	public TestResourceFile IFNetUnknownTypeResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-unknownType.pnml");
	/* IFNet without page tags */
	@Rule
	public TestResourceFile IFNetNoPageTagsResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noPageTags.pnml");
	/* IFNet with multiple page tags */
	@Rule
	public TestResourceFile IFNetMultiplePageTagsResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-multiplePageTags.pnml");
	/* IFNet with a missing place ID */
	@Rule
	public TestResourceFile IFNetNoPlaceIDResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noPlaceID.pnml");
	/* IFNet with a missing place name */
	@Rule
	public TestResourceFile IFNetNoPlaceNameResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noPlaceName.pnml");
	/* IFNet with an invalid place initial marking */
	@Rule
	public TestResourceFile IFNetInvalidInitialMarkingResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-invalidInitialMarking.pnml");
	/* IFNet with an invalid place graphics tag */
	@Rule
	public TestResourceFile IFNetInvalidPlaceGraphicsResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-invalidPlaceGraphics.pnml");
	/* IFNet with an invalid place capacity for the color green */
	@Rule
	public TestResourceFile IFNetInvalidPlaceCapacityResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-invalidPlaceCapacity.pnml");
	/* IFNet with a missing transition ID */
	@Rule
	public TestResourceFile IFNetNoTransitionIDResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noTransitionID.pnml");
	/* IFNet with a missing transition name */
	@Rule
	public TestResourceFile IFNetNoTransitionNameResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noTransitionName.pnml");
	/* IFNet with an invalid transition type */
	@Rule
	public TestResourceFile IFNetInvalidTransitionTypeResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-invalidTransitionType.pnml");
	/* IFNet with invalid transition graphics */
	@Rule
	public TestResourceFile IFNetInvalidTransitionGraphicsResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-invalidTransitionGraphics.pnml");
	/* IFNet with a missing access functions tag */
	@Rule
	public TestResourceFile IFNetNoAccessFunctionsTagResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noAccessFunctionsTag.pnml");
	/* IFNet with a missing access function color tag */
	@Rule
	public TestResourceFile IFNetNoAccessFunctionColorResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noAccessFunctionColor.pnml");
	/* IFNet with incomplete access modes */
	@Rule
	public TestResourceFile IFNetIncompleteAccessModesResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-incompleteAccessModes.pnml");
	/* IFNet with invalid access functions graphics */
	@Rule
	public TestResourceFile IFNetInvalidAccessFunctionsGraphicsResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-invalidAccessFunctionsGraphics.pnml");
	/* IFNet with a missing arc ID */
	@Rule
	public TestResourceFile IFNetNoArcIDResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noArcID.pnml");
	/* IFNet with a missing arc source ID */
	@Rule
	public TestResourceFile IFNetNoArcSourceIDResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noArcSourceID.pnml");
	/* IFNet with a missing arc target ID */
	@Rule
	public TestResourceFile IFNetNoArcTargetIDResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noArcTargetID.pnml");
	/* IFNet with a missing arc inscription */
	@Rule
	public TestResourceFile IFNetNoArcInscriptionResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-noArcInscription.pnml");
	/* IFNet with a negative arc inscription */
	@Rule
	public TestResourceFile IFNetNegativeArcInscriptionResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-negativeArcInscription.pnml");
	/* IFNet with an invalid arc graphics tag */
	@Rule
	public TestResourceFile IFNetInvalidArcGraphicsResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-invalidArcGraphics.pnml");
	/* Valid bounded IFNet */
	@Rule
	public TestResourceFile IFNetBoundedResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-bounded.pnml");
	/* IFNet with incomplete token color RGB values */
	@Rule
	public TestResourceFile IFNetIncompleteTokencolorsResource = new TestResourceFile(IFNet_TEST_RESOURCES_PATH + "IFNet-IncompleteTokencolors.pnml");

	/*
	 * Test if all sample files of the IFNet exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void sampleIFNetFilesExist() throws Exception {
		assertTrue(IFNetResource.getFile().exists());
		assertTrue(IFNetNoNetIDResource.getFile().exists());
		assertTrue(IFNetNoTypeAttributeResource.getFile().exists());
		assertTrue(IFNetUnknownTypeResource.getFile().exists());
		assertTrue(IFNetNoPageTagsResource.getFile().exists());
		assertTrue(IFNetMultiplePageTagsResource.getFile().exists());
		assertTrue(IFNetNoPlaceIDResource.getFile().exists());
		assertTrue(IFNetNoPlaceNameResource.getFile().exists());
		assertTrue(IFNetInvalidInitialMarkingResource.getFile().exists());
		assertTrue(IFNetInvalidPlaceGraphicsResource.getFile().exists());
		assertTrue(IFNetInvalidPlaceCapacityResource.getFile().exists());
		assertTrue(IFNetNoTransitionIDResource.getFile().exists());
		assertTrue(IFNetInvalidTransitionGraphicsResource.getFile().exists());
		assertTrue(IFNetInvalidTransitionTypeResource.getFile().exists());
		assertTrue(IFNetNoAccessFunctionsTagResource.getFile().exists());
		assertTrue(IFNetNoAccessFunctionColorResource.getFile().exists());
		assertTrue(IFNetIncompleteAccessModesResource.getFile().exists());
		assertTrue(IFNetInvalidAccessFunctionsGraphicsResource.getFile().exists());
		assertTrue(IFNetNoArcIDResource.getFile().exists());
		assertTrue(IFNetNoArcSourceIDResource.getFile().exists());
		assertTrue(IFNetNoArcTargetIDResource.getFile().exists());
		assertTrue(IFNetNoArcInscriptionResource.getFile().exists());
		assertTrue(IFNetNegativeArcInscriptionResource.getFile().exists());
		assertTrue(IFNetInvalidArcGraphicsResource.getFile().exists());
		assertTrue(IFNetBoundedResource.getFile().exists());
		assertTrue(IFNetIncompleteTokencolorsResource.getFile().exists());
	}

	/*
	 * Valid IFNet with validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validIFNetWithValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalIFNet);

		GraphicalIFNet net = (GraphicalIFNet) abstrNet;

		assertEquals("ifn-example", net.getPetriNet().getName());
		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
		assertEquals(2, net.getPetriNetGraphics().getPlaceGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTransitionGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getArcGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getArcAnnotationGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTokenGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getColors().size());

		assertTrue(net.getPetriNet().isBounded().equals(Boundedness.UNKNOWN));
	}

	/*
	 * Valid IFNet without validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validIFNetWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalIFNet);

		GraphicalIFNet net = (GraphicalIFNet) abstrNet;

		assertEquals("ifn-example", net.getPetriNet().getName());
		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
		assertEquals(2, net.getPetriNetGraphics().getPlaceGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTransitionGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getArcGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getArcAnnotationGraphics().size());
		assertEquals(1, net.getPetriNetGraphics().getTokenGraphics().size());
		assertEquals(2, net.getPetriNetGraphics().getColors().size());

		assertTrue(net.getPetriNet().isBounded().equals(Boundedness.UNKNOWN));
	}

	/*
	 * NET ID TESTS
	 */

	/*
	 * IFNet without a net ID and validation. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noNetIDIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoNetIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet without a net ID and no validation. No exception should be thrown and the net name should be the default name "PetriNet".
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noNetIDIFNetWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoNetIDResource.getFile(), true, false);
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
	 * IFNet without type attribute but requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeIFNetRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoTypeAttributeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet without type attribute but requiring a valid one. Retrieving the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeIFNetRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoTypeAttributeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet without type attribute and not requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeIFNetNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoTypeAttributeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet without type attribute and not requiring a valid one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTypeIFNetNotRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoTypeAttributeResource.getFile(), false, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with unknown type attribute but requiring a known one. Validation should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypeIFNetRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetUnknownTypeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with unknown type attribute but requiring a known one. Retrieving the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypeIFNetRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetUnknownTypeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with unknown type attribute and not requiring a known one. Validating the net type should throw a ParserException.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void unknownTypeIFNetNotRequiringTypeWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetUnknownTypeResource.getFile(), false, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with unknown type attribute and not requiring a known one. No exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void unknownTypeIFNetNotRequiringTypeWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetUnknownTypeResource.getFile(), false, false);
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
	 * IFNet without page tags. The validation should throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPageTagsIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoPageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet without page tags without validation. No exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPageTagsIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoPageTagsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with multiple page tags. Although this is valid, the parser can't handle it and throws an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetMultiplePageTagsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with multiple page tags. Although this is valid, the parser can't handle it and throws an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void multiplePageTagsIFNetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetMultiplePageTagsResource.getFile(), true, false);
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
	 * IFNet with a missing place ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoPlaceIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing place ID. The parser should throw an exception while reading the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noPlaceIDIFNetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoPlaceIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing place ID. As a place name is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlaceNameIFNetWithValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoPlaceNameResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing place ID. As a place name is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noPlaceNameIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoPlaceNameResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with an invalid place initial marking. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidInitialMarkingResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with an invalid place initial marking. The parser should throw an exception while reading the places.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidInitialMarkingIFNetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidInitialMarkingResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with an invalid place graphics tag. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidPlaceGraphicsIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidPlaceGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with an invalid place graphics tag. No exception should be thrown and the invalid tag gets ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidPlaceGraphicsIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidPlaceGraphicsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a place capacity of zero for a color. The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidPlaceCapacityIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidPlaceCapacityResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a place capacity of zero for a color. The parser should throw an exception while setting the capacity.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidPlaceCapacityIFNetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidPlaceCapacityResource.getFile(), true, false);
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
	 * IFNet with a missing transition ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTransitionIDIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoTransitionIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing transition ID. The parser should throw an exception while parsing the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noTransitionIDIFNetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoTransitionIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing transition name. The parser should not throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noTransitionNameIFNetWithValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoTransitionNameResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing transition name. The parser should not throw an exception.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noTransitionNameIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoTransitionNameResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with invalid graphical information. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidTransitionGraphicsIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidTransitionGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with invalid graphical information. No exception should be thrown and the invalid tag gets ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidTransitionGraphicsIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidTransitionGraphicsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with invalid transition type. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidTransitionTypeIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidTransitionTypeResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with invalid transition type. The parser should throw an exception while parsing.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidTransitionTypeIFNetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidTransitionTypeResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet without an access functions tag with validation. As this is valid, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noAccessFunctionsTagIFNetWithValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoAccessFunctionsTagResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet without an access functions tag without validation. As this is valid, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noAccessFunctionsTagIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoAccessFunctionsTagResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing access function color tag with validation. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected=ParserException.class)
	public void noAccessFunctionColorIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoAccessFunctionColorResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing access function color tag without validation. The parser just ignores the incomplete access function.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noAccessFunctionColorIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoAccessFunctionColorResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with incomplete access modes with validation. No exception should be thrown and the missing values should be filled with default values.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void incompleteAccessModesIFNetWithValidation() throws ParameterException {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetIncompleteAccessModesResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		
		// check values
		if (abstrNet instanceof GraphicalIFNet == false)
			fail("Parsed net is not of the expected type.");
		GraphicalIFNet ifnet = (GraphicalIFNet) abstrNet;
		RegularIFNetTransition regularTransition = (RegularIFNetTransition) ifnet.getPetriNet().getTransition("t1");
		assertEquals(1, regularTransition.getAccessModes("green").size());
		assertTrue(regularTransition.getAccessModes("green").contains(AccessMode.READ));
		assertFalse(regularTransition.getAccessModes("green").contains(AccessMode.CREATE));
		assertFalse(regularTransition.getAccessModes("green").contains(AccessMode.WRITE));
		assertFalse(regularTransition.getAccessModes("green").contains(AccessMode.DELETE));
	}

	/*
	 * IFNet with incomplete access modes without validation. No exception should be thrown and the missing values should be filled with default values.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void incompleteAccessModesIFNetWithoutValidation() throws ParameterException {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetIncompleteAccessModesResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		
		// check values
		if (abstrNet instanceof GraphicalIFNet == false)
			fail("Parsed net is not of the expected type.");
		GraphicalIFNet ifnet = (GraphicalIFNet) abstrNet;
		RegularIFNetTransition regularTransition = (RegularIFNetTransition) ifnet.getPetriNet().getTransition("t1");
		assertEquals(1, regularTransition.getAccessModes("green").size());
		assertTrue(regularTransition.getAccessModes("green").contains(AccessMode.READ));
		assertFalse(regularTransition.getAccessModes("green").contains(AccessMode.CREATE));
		assertFalse(regularTransition.getAccessModes("green").contains(AccessMode.WRITE));
		assertFalse(regularTransition.getAccessModes("green").contains(AccessMode.DELETE));
	}

	/*
	 * IFNet with invalid graphical information for the access functions with validation. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidAccessFunctionsGraphicsIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidAccessFunctionsGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with invalid graphical information for the access functions without validation. The parser should just ignore the invalid tag.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidAccessFunctionsGraphicsIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidAccessFunctionsGraphicsResource.getFile(), true, false);
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
	 * IFNet with a missing arc ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcIDIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoArcIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing arc ID. As a transition ID is not necessary, no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcIDIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoArcIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing arc source ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcSourceIDIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoArcSourceIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing arc source ID. The parser should throw an exception while creating the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcSourceIDIFNetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoArcSourceIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing arc target ID. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcTargetIDIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoArcTargetIDResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing arc target ID. The parser should throw an exception while creating the flow relations.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void noArcTargetIDIFNetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoArcTargetIDResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing arc inscription. As there is control flow dependency in IFNets an exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected=ParserException.class)
	public void noArcInscriptionIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoArcInscriptionResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a missing arc inscription. As there is no control flow dependency in IFNets, no exception should be thrown and the flow relation should be created with the default constraint.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void noArcInscriptionIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNoArcInscriptionResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a negative arc inscription. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void negativeArcInscriptionIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNegativeArcInscriptionResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a negative arc inscription. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void negativeArcInscriptionIFNetWithoutValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetNegativeArcInscriptionResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a invalid arc graphics. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void invalidArcGraphicsIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidArcGraphicsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with a invalid arc graphics. No exception should be thrown and the invalid tag gets ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void invalidArcGraphicsIFNetWithoutValidation() {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetInvalidArcGraphicsResource.getFile(), true, false);
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
	 * Valid bounded IFNet with validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void boundedIFNetWithValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetBoundedResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalIFNet);

		assertTrue(abstrNet.getPetriNet().isBounded().equals(Boundedness.BOUNDED));
	}

	/*
	 * Valid bounded IFNet without validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void boundedIFNetWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetBoundedResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalIFNet);

		assertTrue(abstrNet.getPetriNet().isBounded().equals(Boundedness.BOUNDED));
	}

	/*
	 * TOKENCOLORS TESTS
	 */

	/*
	 * IFNet with incomplete token colors RGB values. The parser should throw an exception while validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = PNMLParserException.class)
	public void incompleteTokencolorsIFNetWithValidation() throws ParserException {
		try {
			new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetIncompleteTokencolorsResource.getFile(), true, true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
	}

	/*
	 * IFNet with incomplete token colors RGB values without validation. No parser should be thrown and the missing values should be filled with the default value <code>0</code>.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void incompleteTokencolorsIFNetWithoutValidation() {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().<IFNetPlace, AbstractIFNetTransition<IFNetFlowRelation>, IFNetFlowRelation, IFNetMarking, Multiset<String>, IFNet, IFNetGraphics>parse(IFNetIncompleteTokencolorsResource.getFile(), true, false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		Map<String, Color> tokencolors = ((GraphicalIFNet) abstrNet).getPetriNetGraphics().getColors();
		assertEquals(2, tokencolors.size());
		assertEquals(Color.GREEN, tokencolors.get("green"));
		assertEquals(Color.YELLOW, tokencolors.get("yellow"));
	}
}
