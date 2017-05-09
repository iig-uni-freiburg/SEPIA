package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ptc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.TestResourceFile;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTCNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet.Boundedness;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessCheck;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessException;

/**
 * <p>
 * Component unit tests for the P/T-net PNML parser.
 * </p>
 * <p>
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from remote, these tests can take a while.
 * </p>
 * 
 * @author Julius Holderer
 * @author Adrian Lange
 */
public class PNMLPTCNetParserComponentTest {

	/*
	 * Aborts tests after the specified time in milliseconds for the case of connection issues. Especially needed for the validating tests which create a socket connection to a remote server to get the needed RelaxNG schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 20000;

	/* Project intern path to the test resources without leading slash */
	public static final String PTCNET_TEST_RESOURCES_PATH = "test-resources/pnml/ptc/";

	/* Valid P/T-net */
	@Rule
	public TestResourceFile PTCnetResource = new TestResourceFile(PTCNET_TEST_RESOURCES_PATH + "PTCnet.pnml");


	/*
	 * Test if all sample files of the P/T-net exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void samplePTCNetFilesExist() throws Exception {
		assertTrue(PTCnetResource.getFile().exists());
	}

	/*
	 * Valid P/TCost-net with validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validPTCnetWithValidation() throws BoundednessException {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(PTCnetResource.getFile(), true, true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalPTCNet);

		GraphicalPTCNet net = (GraphicalPTCNet) abstrNet;

		assertTrue(BoundednessCheck.getBoundedness(net.getPetriNet()).getBoundedness().equals(Boundedness.BOUNDED));
		
	}

	/*
	 * Valid P/TCost-net without validation, where no exception should be thrown. Performs also some shallow tests to check the correct amount of places, transitions, arcs and graphical information.
	 */
	@SuppressWarnings("rawtypes")
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validPTCnetWithoutValidation() throws BoundednessException {
		AbstractGraphicalPN<?,?,?,?,?,?,?> abstrNet = null;
		try {
			abstrNet = new PNMLParser().parse(PTCnetResource.getFile(), true, false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read PNML file: " + e.getMessage());
		}

		assertTrue(abstrNet instanceof GraphicalPTCNet);

		GraphicalPTCNet net = (GraphicalPTCNet) abstrNet;

		assertTrue(BoundednessCheck.getBoundedness(net.getPetriNet()).getBoundedness().equals(Boundedness.BOUNDED));
	}

	
}
