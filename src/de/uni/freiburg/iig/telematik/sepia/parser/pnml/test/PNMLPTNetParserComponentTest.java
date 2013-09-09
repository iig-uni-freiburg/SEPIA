package de.uni.freiburg.iig.telematik.sepia.parser.pnml.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.test.TestResourceFile;

/**
 * <p>
 * Component unit tests for the P/T-net PNML parser. The method unit tests for these classes is made in {@link PNMLPTNetParserTest}.
 * </p>
 * <p>
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from remote, these tests can take a while.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLPTNetParserComponentTest {

	/** Project intern path to the test resources without leading slash */
	public static final String PTNET_TEST_RESOURCES_PATH = "test-resources/pnml/pt/";

	/** Valid P/T-net */
	@Rule
	public TestResourceFile PTnetResource = new TestResourceFile(PTNET_TEST_RESOURCES_PATH + "PTnet.pnml");

	/**
	 * Test if all sample files of the P/T-net exist.
	 */
	@Test
	public void samplePTNetFilesExist() throws Exception {
		assertTrue(PTnetResource.getFile().exists());
	}

	/**
	 * Valid P/T-net with validation:<br>
	 * No exception should be thrown.
	 */
	@Test
	public void validPTnetWithValidation() {
		AbstractGraphicalPN<?, ?, ?, ?, ?> net = null;
		try {
			net = new PNMLParser().parse(PTnetResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		assertTrue(net instanceof GraphicalPTNet);

		// TODO other tests
		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(2, net.getPetriNetGraphics().getPlaceGraphics().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(1, net.getPetriNetGraphics().getTransitionGraphics().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
		assertEquals(1, net.getPetriNetGraphics().getArcGraphics().size());
	}

	/**
	 * Valid P/T-net without validation:<br>
	 * No exception should be thrown.
	 */
	@Test
	public void validPTnetWithoutValidation() {
		AbstractGraphicalPN<?, ?, ?, ?, ?> net = null;
		try {
			net = new PNMLParser().parse(PTnetResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}
		assertTrue(net instanceof GraphicalPTNet);

		// TODO other tests
		assertEquals(2, net.getPetriNet().getPlaces().size());
		assertEquals(1, net.getPetriNet().getTransitions().size());
		assertEquals(2, net.getPetriNet().getFlowRelations().size());
	}

	// TODO no type requiring a type attribute
	// TODO no type without requiring a type attribute

	// TODO no pages
	// TODO multiple pages

	// TODO no place id
	// TODO no place name
	// TODO no place initial marking
	// TODO no place graphics
	// TODO invalid place graphics
	// TODO negative place initial marking
	// TODO zero place capacity

	// TODO no transition id
	// TODO no transition name
	// TODO no transition graphics
	// TODO invalid transition graphics

	// TODO no arc id
	// TODO no source id
	// TODO no target id
	// TODO no arc inscription
	// TODO negative arc inscription
	// TODO no arc graphics
	// TODO invalid arc graphics

	// TODO Bounded P/T net isBounded
	// TODO Unbounded P/T net isBounded
}
