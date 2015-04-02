package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.TestResourceFile;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;

/**
 * <p>
 * Component unit tests for the IFNet PNML analysis context parser.
 * </p>
 * <p>
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from remote, these tests can take a while.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLIFNetAnalysisContextParserComponentTest {

	/*
	 * Aborts tests after the specified time in milliseconds for the case of connection issues. Especially needed for the validating tests which create a socket connection to a remote server to get the needed RelaxNG schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 20000;

	/* Project intern path to the test resources without leading slash */
	public static final String AC_TEST_RESOURCES_PATH = "test-resources/pnml/ifnet/ac/";

	/* Valid analysis context */
	@Rule
	public TestResourceFile validACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC.xml");
	/* Analysis context with an invalid attribute */
	@Rule
	public TestResourceFile invalidAttributeACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-invalidAttribute.xml");
	/* Analysis context with missing descriptors */
	@Rule
	public TestResourceFile missingDescriptorsACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-missingDescriptors.xml");
	/* Analysis context with an uneven descriptor A */
	@Rule
	public TestResourceFile unevenDescriptorsAACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-unevenDescriptorsA.xml");
	/* Analysis context with an uneven descriptor B */
	@Rule
	public TestResourceFile unevenDescriptorsBACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-unevenDescriptorsB.xml");
	/* Analysis context with an invalid descriptor activity */
	@Rule
	public TestResourceFile invalidDescriptorActivityACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-invalidDescriptorActivity.xml");
	/* Analysis context with an invalid descriptor subject */
	@Rule
	public TestResourceFile invalidDescriptorSubjectACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-invalidDescriptorSubject.xml");
	/* Analysis context with an invalid activity security level */
	@Rule
	public TestResourceFile invalidSecurityLevelActivityACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-invalidSecurityLevelActivity.xml");
	/* Analysis context with an invalid attribute security level */
	@Rule
	public TestResourceFile invalidSecurityLevelAttributeACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-invalidSecurityLevelAttribute.xml");
	/* Analysis context with an invalid subject security level */
	@Rule
	public TestResourceFile invalidSecurityLevelSubjectACResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetAC-invalidSecurityLevelSubject.xml");

	/*
	 * Test if all sample files of the IFNet analysis context exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void sampleIFNetACFilesExist() throws Exception {
		assertTrue(validACResource.getFile().exists());
		assertTrue(invalidAttributeACResource.getFile().exists());
		assertTrue(missingDescriptorsACResource.getFile().exists());
		assertTrue(unevenDescriptorsAACResource.getFile().exists());
		assertTrue(unevenDescriptorsBACResource.getFile().exists());
		assertTrue(invalidDescriptorActivityACResource.getFile().exists());
		assertTrue(invalidDescriptorSubjectACResource.getFile().exists());
		assertTrue(invalidSecurityLevelActivityACResource.getFile().exists());
		assertTrue(invalidSecurityLevelAttributeACResource.getFile().exists());
		assertTrue(invalidSecurityLevelSubjectACResource.getFile().exists());
	}

	/*
	 * Valid AnalysisContext with validation, where no exception should be thrown. Performs also some shallow tests.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validACWithValidation() throws ParameterException {
		AnalysisContext ac = null;
		try {
			ac = AnalysisContextParser.parse(validACResource.getFile(), true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}

		// Activities
		assertEquals(5, ac.getLabeling().getActivities().size());
		assertTrue(ac.getLabeling().getActivities().contains("first activity"));
		assertTrue(ac.getLabeling().getActivities().contains("second activity"));
		assertTrue(ac.getLabeling().getActivities().contains("third activity"));
		assertTrue(ac.getLabeling().getActivities().contains("fourth activity"));
		assertTrue(ac.getLabeling().getActivities().contains("fifth activity"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getActivityClassification("first activity"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getActivityClassification("second activity"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getActivityClassification("third activity"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getActivityClassification("fourth activity"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getActivityClassification("fifth activity"));
		// Attributes
		assertEquals(5, ac.getLabeling().getAttributes().size());
		assertTrue(ac.getLabeling().getAttributes().contains("blue"));
		assertTrue(ac.getLabeling().getAttributes().contains("green"));
		assertTrue(ac.getLabeling().getAttributes().contains("pink"));
		assertTrue(ac.getLabeling().getAttributes().contains("red"));
		assertTrue(ac.getLabeling().getAttributes().contains("yellow"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getAttributeClassification("blue"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getAttributeClassification("green"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getAttributeClassification("pink"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getAttributeClassification("red"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getAttributeClassification("yellow"));
		// Subjects
		assertEquals(5, ac.getLabeling().getSubjects().size());
		assertTrue(ac.getLabeling().getSubjects().contains("subjectA"));
		assertTrue(ac.getLabeling().getSubjects().contains("subjectB"));
		assertTrue(ac.getLabeling().getSubjects().contains("subjectC"));
		assertTrue(ac.getLabeling().getSubjects().contains("subjectD"));
		assertTrue(ac.getLabeling().getSubjects().contains("subjectE"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getSubjectClearance("subjectA"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getSubjectClearance("subjectB"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getSubjectClearance("subjectC"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getSubjectClearance("subjectD"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getSubjectClearance("subjectE"));
		// Activity descriptors
		assertEquals("subjectA", ac.getSubjectDescriptor("first activity"));
		assertEquals("subjectE", ac.getSubjectDescriptor("second activity"));
		assertEquals("subjectD", ac.getSubjectDescriptor("third activity"));
		assertEquals("subjectB", ac.getSubjectDescriptor("fourth activity"));
		assertEquals("subjectC", ac.getSubjectDescriptor("fifth activity"));
	}

	/*
	 * Valid AnalysisContext with validation, where no exception should be thrown. Performs also some shallow tests.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validACWithoutValidation() throws ParameterException {
		AnalysisContext ac = null;
		try {
			ac = AnalysisContextParser.parse(validACResource.getFile(), false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}

		// Activities
		assertEquals(5, ac.getLabeling().getActivities().size());
		assertTrue(ac.getLabeling().getActivities().contains("first activity"));
		assertTrue(ac.getLabeling().getActivities().contains("second activity"));
		assertTrue(ac.getLabeling().getActivities().contains("third activity"));
		assertTrue(ac.getLabeling().getActivities().contains("fourth activity"));
		assertTrue(ac.getLabeling().getActivities().contains("fifth activity"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getActivityClassification("first activity"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getActivityClassification("second activity"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getActivityClassification("third activity"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getActivityClassification("fourth activity"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getActivityClassification("fifth activity"));
		// Attributes
		assertEquals(5, ac.getLabeling().getAttributes().size());
		assertTrue(ac.getLabeling().getAttributes().contains("blue"));
		assertTrue(ac.getLabeling().getAttributes().contains("green"));
		assertTrue(ac.getLabeling().getAttributes().contains("pink"));
		assertTrue(ac.getLabeling().getAttributes().contains("red"));
		assertTrue(ac.getLabeling().getAttributes().contains("yellow"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getAttributeClassification("blue"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getAttributeClassification("green"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getAttributeClassification("pink"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getAttributeClassification("red"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getAttributeClassification("yellow"));
		// Subjects
		assertEquals(5, ac.getLabeling().getSubjects().size());
		assertTrue(ac.getLabeling().getSubjects().contains("subjectA"));
		assertTrue(ac.getLabeling().getSubjects().contains("subjectB"));
		assertTrue(ac.getLabeling().getSubjects().contains("subjectC"));
		assertTrue(ac.getLabeling().getSubjects().contains("subjectD"));
		assertTrue(ac.getLabeling().getSubjects().contains("subjectE"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getSubjectClearance("subjectA"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getSubjectClearance("subjectB"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getSubjectClearance("subjectC"));
		assertEquals(SecurityLevel.HIGH, ac.getLabeling().getSubjectClearance("subjectD"));
		assertEquals(SecurityLevel.LOW, ac.getLabeling().getSubjectClearance("subjectE"));
		// Activity descriptors
		assertEquals("subjectA", ac.getSubjectDescriptor("first activity"));
		assertEquals("subjectE", ac.getSubjectDescriptor("second activity"));
		assertEquals("subjectD", ac.getSubjectDescriptor("third activity"));
		assertEquals("subjectB", ac.getSubjectDescriptor("fourth activity"));
		assertEquals("subjectC", ac.getSubjectDescriptor("fifth activity"));
	}
	
	/*
	 * ACTIVITIES TESTS
	 */

	/*
	 * AnalysisContext with an invalid activity security level with validation. The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidSecurityLevelActivityACWithValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidSecurityLevelActivityACResource.getFile(), true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid activity security level without validation. The Labeling class should throw an exception while validating the descriptors and not finding the corresponding activity.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void invalidSecurityLevelActivityACWithoutValidation() throws ParameterException {
		try {
			AnalysisContextParser.parse(invalidSecurityLevelActivityACResource.getFile(), false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * ATTRIBUTES TESTS
	 */

	/*
	 * AnalysisContext with an invalid attribute (black) with validation. The Labeling class should throw an exception while validating the attributes.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidAttributeACWithValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidAttributeACResource.getFile(), true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid attribute (black) without validation. The Labeling class should throw an exception while validating the attributes.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidAttributeACWithoutValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidAttributeACResource.getFile(), false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid attribute security level with validation. The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidSecurityLevelAttributeACWithValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidSecurityLevelAttributeACResource.getFile(), true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid attribute security level without validation. No exception should be thrown and the attribute with the invalid security level will be ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void invalidSecurityLevelAttributeACWithoutValidation() {
		AnalysisContext ac = null;
		try {
			ac = AnalysisContextParser.parse(invalidSecurityLevelAttributeACResource.getFile(), false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}

		assertFalse(ac.getLabeling().getAttributes().contains("blue"));
		assertTrue(ac.getLabeling().getAttributes().contains("green"));
		assertTrue(ac.getLabeling().getAttributes().contains("pink"));
		assertTrue(ac.getLabeling().getAttributes().contains("red"));
		assertTrue(ac.getLabeling().getAttributes().contains("yellow"));
	}
	
	/*
	 * SUBJECTS TESTS
	 */

	/*
	 * AnalysisContext with an invalid subject security level with validation. The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidSecurityLevelSubjectACWithValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidSecurityLevelSubjectACResource.getFile(), true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid subject security level without validation. The Labeling class should throw an exception while validating the descriptors and not finding the corresponding subject.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void invalidSecurityLevelSubjectACWithoutValidation() throws ParameterException {
		try {
			AnalysisContextParser.parse(invalidSecurityLevelSubjectACResource.getFile(), false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}
	
	/*
	 * ACTIVITY DESCRIPTORS TEST
	 */

	/*
	 * AnalysisContext where some descriptors are missing with validation, where no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void missingDescriptorsACWithValidation() throws ParameterException {
		AnalysisContext ac = null;
		try {
			ac = AnalysisContextParser.parse(missingDescriptorsACResource.getFile(), true);
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
		assertEquals("subjectD", ac.getSubjectDescriptor("third activity"));
		assertEquals("subjectB", ac.getSubjectDescriptor("fourth activity"));
		assertEquals(null, ac.getSubjectDescriptor("fifth activity"));
	}

	/*
	 * AnalysisContext where some descriptors are missing with validation, where no exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void missingDescriptorsACWithoutValidation() throws ParameterException {
		AnalysisContext ac = null;
		try {
			ac = AnalysisContextParser.parse(missingDescriptorsACResource.getFile(), false);
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
		assertEquals("subjectD", ac.getSubjectDescriptor("third activity"));
		assertEquals("subjectB", ac.getSubjectDescriptor("fourth activity"));
		assertEquals(null, ac.getSubjectDescriptor("fifth activity"));
	}

	/*
	 * AnalysisContext with an uneven descriptor (A) with validation. The Labeling class should throw an exception while validating the descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void unevenDescriptorsAACWithValidation() throws ParameterException {
		try {
			AnalysisContextParser.parse(unevenDescriptorsAACResource.getFile(), true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an uneven descriptor (A) without validation. The Labeling class should throw an exception while validating the descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void unevenDescriptorsAACWithoutValidation() throws ParameterException {
		try {
			AnalysisContextParser.parse(unevenDescriptorsAACResource.getFile(), false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an uneven descriptor (B) with validation. No exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void unevenDescriptorsBACWithValidation() throws ParameterException {
		try {
			AnalysisContextParser.parse(unevenDescriptorsBACResource.getFile(), true);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an uneven descriptor (B) without validation. No exception should be thrown.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void unevenDescriptorsBACWithoutValidation() throws ParameterException {
		try {
			AnalysisContextParser.parse(unevenDescriptorsBACResource.getFile(), false);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid descriptor activity with validation. The Labeling class should throw an exception while validating the descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDescriptorActivityACWithValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidDescriptorActivityACResource.getFile(), true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid descriptor activity without validation. The Labeling class should throw an exception while validating the descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDescriptorActivityACWithoutValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidDescriptorActivityACResource.getFile(), false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid descriptor subject with validation. The Labeling class should throw an exception while validating the descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDescriptorSubjectACWithValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidDescriptorSubjectACResource.getFile(), true);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid descriptor subject without validation. The Labeling class should throw an exception while validating the descriptors.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDescriptorSubjectACWithoutValidation() throws ParserException {
		try {
			AnalysisContextParser.parse(invalidDescriptorSubjectACResource.getFile(), false);
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (IOException e) {
			fail("Couldn't read analysis context file: " + e.getMessage());
		}
	}
}
