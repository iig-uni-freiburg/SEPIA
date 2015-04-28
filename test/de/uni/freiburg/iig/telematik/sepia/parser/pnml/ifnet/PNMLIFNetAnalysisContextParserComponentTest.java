package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.TestResourceFile;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.acl.ACLModel;

/**
 * <p>
 * Component unit tests for the IFNet PNML analysis context parser.
 * </p>
 * <p>
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from
 * remote, these tests can take a while.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLIFNetAnalysisContextParserComponentTest {

	/*
	 * Aborts tests after the specified time in milliseconds for the case of
	 * connection issues. Especially needed for the validating tests which
	 * create a socket connection to a remote server to get the needed RelaxNG
	 * schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 20000;

	/* Project intern path to the test resources without leading slash */
	public static final String AC_TEST_RESOURCES_PATH = "test-resources/pnml/ifnet/ac/";

	/* Valid analysis context */
	@Rule
	public TestResourceFile validACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC.xml");
	/* Analysis context with missing descriptors */
	@Rule
	public TestResourceFile missingDescriptorsACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-missingDescriptors.xml");
	/* Analysis context with an invalid descriptor activity */
	@Rule
	public TestResourceFile invalidDescriptorActivityACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-invalidDescriptorActivity.xml");
	/* Analysis context with an invalid descriptor subject */
	@Rule
	public TestResourceFile invalidDescriptorSubjectACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-invalidDescriptorSubject.xml");

	private ACLModel acm = null;
	private SOABase base = null;
	
	@Before
	public void initialize() {
		base = new SOABase("base");
		base.addActivities(Arrays.asList("first activity", "second activity", "third activity", "fourth activity", "fifth activity"));
		base.addObjects(Arrays.asList("blue", "green", "red", "pink", "yellow"));
		base.addSubjects(Arrays.asList("subjectA", "subjectB", "subjectC", "subjectD", "subjectE"));

		acm = new ACLModel("ACM", base);
		acm.addActivityPermission("subjectA", "first activity");
		acm.addActivityPermission("subjectB", "fourth activity");
		acm.addActivityPermission("subjectC", "fifth activity");
		acm.addActivityPermission("subjectD", "third activity");
		acm.addActivityPermission("subjectE", "second activity");
	}

	/*
	 * Test if all sample files of the IFNet analysis context exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void sampleIFNetACFilesExist() throws Exception {
		assertTrue(validACResource.getFile().exists());
		assertTrue(missingDescriptorsACResource.getFile().exists());
		assertTrue(invalidDescriptorActivityACResource.getFile().exists());
		assertTrue(invalidDescriptorSubjectACResource.getFile().exists());
	}

	/*
	 * Valid AnalysisContext with validation, where no exception should be
	 * thrown. Performs also some shallow tests.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validACWithValidation() throws ParameterException {
		AnalysisContext ac = null;
		try {
			ac = AnalysisContextParser.parse(validACResource.getFile(), true, Arrays.asList(acm));
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}

		// Activities
		assertEquals(5, ac.getSubjectDescriptors().size());
		assertTrue(ac.getSubjectDescriptors().containsKey("first activity"));
		assertTrue(ac.getSubjectDescriptors().containsKey("second activity"));
		assertTrue(ac.getSubjectDescriptors().containsKey("third activity"));
		assertTrue(ac.getSubjectDescriptors().containsKey("fourth activity"));
		assertTrue(ac.getSubjectDescriptors().containsKey("fifth activity"));

		// Activity descriptors
		assertEquals("subjectA", ac.getSubjectDescriptor("first activity"));
		assertEquals("subjectE", ac.getSubjectDescriptor("second activity"));
		assertEquals("subjectD", ac.getSubjectDescriptor("third activity"));
		assertEquals("subjectB", ac.getSubjectDescriptor("fourth activity"));
		assertEquals("subjectC", ac.getSubjectDescriptor("fifth activity"));
	}

	/*
	 * Valid AnalysisContext with validation, where no exception should be
	 * thrown. Performs also some shallow tests.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validACWithoutValidation() throws ParameterException {
		AnalysisContext ac = null;
		try {
			ac = AnalysisContextParser.parse(validACResource.getFile(), false, Arrays.asList(acm));
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}

		// Activities
		assertEquals(5, ac.getSubjectDescriptors().size());
		assertTrue(ac.getSubjectDescriptors().containsKey("first activity"));
		assertTrue(ac.getSubjectDescriptors().containsKey("second activity"));
		assertTrue(ac.getSubjectDescriptors().containsKey("third activity"));
		assertTrue(ac.getSubjectDescriptors().containsKey("fourth activity"));
		assertTrue(ac.getSubjectDescriptors().containsKey("fifth activity"));

		// Activity descriptors
		assertEquals("subjectA", ac.getSubjectDescriptor("first activity"));
		assertEquals("subjectE", ac.getSubjectDescriptor("second activity"));
		assertEquals("subjectD", ac.getSubjectDescriptor("third activity"));
		assertEquals("subjectB", ac.getSubjectDescriptor("fourth activity"));
		assertEquals("subjectC", ac.getSubjectDescriptor("fifth activity"));
	}

	/*
	 * AnalysisContext where some descriptors are missing with validation, where
	 * no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void missingDescriptorsACWithValidation() throws ParameterException {
		AnalysisContext ac = null;
		try {
			ac = AnalysisContextParser.parse(missingDescriptorsACResource.getFile(), true, Arrays.asList(acm));
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}

		// Activity descriptors
		assertEquals(null, ac.getSubjectDescriptor("first activity"));
		assertEquals(null, ac.getSubjectDescriptor("second activity"));
		assertEquals("subjectC", ac.getSubjectDescriptor("fifth activity"));
		assertEquals("subjectB", ac.getSubjectDescriptor("fourth activity"));
		assertEquals(null, ac.getSubjectDescriptor("third activity"));
	}

	/*
	 * AnalysisContext where some descriptors are missing with validation, where
	 * no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void missingDescriptorsACWithoutValidation() throws ParameterException {
		AnalysisContext ac = null;
		try {
			ac = AnalysisContextParser.parse(missingDescriptorsACResource.getFile(), false, Arrays.asList(acm));
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}

		// Activity descriptors
		assertEquals(null, ac.getSubjectDescriptor("first activity"));
		assertEquals(null, ac.getSubjectDescriptor("second activity"));
		assertEquals("subjectC", ac.getSubjectDescriptor("fifth activity"));
		assertEquals("subjectB", ac.getSubjectDescriptor("fourth activity"));
		assertEquals(null, ac.getSubjectDescriptor("third activity"));
	}

	/*
	 * AnalysisContext with an invalid descriptor activity with validation. The
	 * Labeling class should throw an exception while validating the
	 * descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDescriptorActivityACWithValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidDescriptorActivityACResource.getFile(), true, Arrays.asList(acm));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid descriptor activity without validation.
	 * The Labeling class should throw an exception while validating the
	 * descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDescriptorActivityACWithoutValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidDescriptorActivityACResource.getFile(), false, Arrays.asList(acm));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid descriptor subject with validation. The
	 * Labeling class should throw an exception while validating the
	 * descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDescriptorSubjectACWithValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidDescriptorSubjectACResource.getFile(), true, Arrays.asList(acm));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid descriptor subject without validation.
	 * The Labeling class should throw an exception while validating the
	 * descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDescriptorSubjectACWithoutValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidDescriptorSubjectACResource.getFile(), false, Arrays.asList(acm));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}
}
