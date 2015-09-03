package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.TestResourceFile;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
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
public class PNMLIFNetLabelingParserComponentTest {

	/*
	 * Aborts tests after the specified time in milliseconds for the case of
	 * connection issues. Especially needed for the validating tests which
	 * create a socket connection to a remote server to get the needed RelaxNG
	 * schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 20000;

	/* Project intern path to the test resources without leading slash */
	public static final String LABELING_TEST_RESOURCES_PATH = "test-resources/pnml/ifnet/labeling/";

	/* Valid labeling */
	@Rule
	public TestResourceFile validlabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling.xml");
	/* labeling with a high default security level */
	@Rule
	public TestResourceFile highDefaultSecurityLevelLabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling-highDefaultSecurityLevel.xml");
	/* labeling with an invalid default security level */
	@Rule
	public TestResourceFile invalidDefaultSecurityLevelLabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling-invalidDefaultSecurityLevel.xml");
	/* labeling without default security level */
	@Rule
	public TestResourceFile noDefaultSecurityLevelLabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling-noDefaultSecurityLevel.xml");
	/* labeling without analysis context */
	@Rule
	public TestResourceFile noAnalysisContextLabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling-noAnalysisContext.xml");
	/* labeling without known analysis context */
	@Rule
	public TestResourceFile unknownAnalysisContextLabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling-unknownAnalysisContext.xml");
	/* labeling with an invalid attribute */
	@Rule
	public TestResourceFile invalidAttributeLabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling-invalidAttribute.xml");
	/* labeling with an invalid activity security level */
	@Rule
	public TestResourceFile invalidSecurityLevelActivityLabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling-invalidSecurityLevelActivity.xml");
	/* labeling with an invalid attribute security level */
	@Rule
	public TestResourceFile invalidSecurityLevelAttributeLabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling-invalidSecurityLevelAttribute.xml");
	/* labeling with an invalid subject security level */
	@Rule
	public TestResourceFile invalidSecurityLevelSubjectLabelingResource = new TestResourceFile(LABELING_TEST_RESOURCES_PATH + "IFNetLabeling-invalidSecurityLevelSubject.xml");

	private SOABase base = null;
	private ACLModel acm = null;
	private AnalysisContext ac = null;

	@Before
	public void initialize() {
		base = new SOABase("base");
		base.addActivities(Arrays.asList("first_activity", "second_activity", "third_activity", "fourth_activity", "fifth_activity"));
		base.addObjects(Arrays.asList("blue", "green", "red", "pink", "yellow"));
		base.addSubjects(Arrays.asList("subjectA", "subjectB", "subjectC", "subjectD", "subjectE"));

		acm = new ACLModel("ACM", base);
		acm.addActivityPermission("subjectA", "first_activity");
		acm.addActivityPermission("subjectB", "fourth_activity");
		acm.addActivityPermission("subjectC", "fifth_activity");
		acm.addActivityPermission("subjectD", "third_activity");
		acm.addActivityPermission("subjectE", "second_activity");

		ac = new AnalysisContext("AC", acm, true);
		ac.setSubjectDescriptor("first_activity", "subjectA");
		ac.setSubjectDescriptor("second_activity", "subjectE");
		ac.setSubjectDescriptor("third_activity", "subjectD");
		ac.setSubjectDescriptor("fourth_activity", "subjectB");
		ac.setSubjectDescriptor("fifth_activity", "subjectC");
	}

	/*
	 * Test if all sample files of the IFNet analysis context exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void sampleIFNetLabelingFilesExist() {
		assertTrue(validlabelingResource.getFile().exists());
		assertTrue(highDefaultSecurityLevelLabelingResource.getFile().exists());
		assertTrue(invalidDefaultSecurityLevelLabelingResource.getFile().exists());
		assertTrue(noDefaultSecurityLevelLabelingResource.getFile().exists());
		assertTrue(noAnalysisContextLabelingResource.getFile().exists());
		assertTrue(unknownAnalysisContextLabelingResource.getFile().exists());
		assertTrue(invalidAttributeLabelingResource.getFile().exists());
		assertTrue(invalidSecurityLevelActivityLabelingResource.getFile().exists());
		assertTrue(invalidSecurityLevelAttributeLabelingResource.getFile().exists());
		assertTrue(invalidSecurityLevelSubjectLabelingResource.getFile().exists());
	}

	/*
	 * Valid AnalysisContext with validation, where no exception should be
	 * thrown. Performs also some shallow tests.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validLabelingWithValidation() throws ParameterException {
		Labeling labeling = null;
		try {
			labeling = LabelingParser.parse(validlabelingResource.getFile(), true, Arrays.asList(ac));
			ac.setLabeling(labeling);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}

		assertEquals(SecurityLevel.LOW, labeling.getDefaultSecurityLevel());
		assertEquals("AC", labeling.getAnalysisContext().getName());

		// Activities
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("first_activity"));
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("second_activity"));
		assertEquals(SecurityLevel.HIGH, labeling.getActivityClassification("third_activity"));
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("fourth_activity"));
		assertEquals(SecurityLevel.HIGH, labeling.getActivityClassification("fifth_activity"));
		// Attributes
		assertEquals(SecurityLevel.HIGH, labeling.getAttributeClassification("blue"));
		assertEquals(SecurityLevel.LOW, labeling.getAttributeClassification("green"));
		assertEquals(SecurityLevel.LOW, labeling.getAttributeClassification("pink"));
		assertEquals(SecurityLevel.LOW, labeling.getAttributeClassification("red"));
		assertEquals(SecurityLevel.HIGH, labeling.getAttributeClassification("yellow"));
		// Subjects
		assertEquals(SecurityLevel.LOW, labeling.getSubjectClearance("subjectA"));
		assertEquals(SecurityLevel.LOW, labeling.getSubjectClearance("subjectB"));
		assertEquals(SecurityLevel.HIGH, labeling.getSubjectClearance("subjectC"));
		assertEquals(SecurityLevel.HIGH, labeling.getSubjectClearance("subjectD"));
		assertEquals(SecurityLevel.LOW, labeling.getSubjectClearance("subjectE"));
	}

	/*
	 * Valid AnalysisContext with validation, where no exception should be
	 * thrown. Performs also some shallow tests.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validLabelingWithoutValidation() throws ParameterException {
		Labeling labeling = null;
		try {
			labeling = LabelingParser.parse(validlabelingResource.getFile(), false, Arrays.asList(ac));
			ac.setLabeling(labeling);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}

		assertEquals(SecurityLevel.LOW, labeling.getDefaultSecurityLevel());
		assertEquals("AC", labeling.getAnalysisContext().getName());

		// Activities
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("first_activity"));
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("second_activity"));
		assertEquals(SecurityLevel.HIGH, labeling.getActivityClassification("third_activity"));
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("fourth_activity"));
		assertEquals(SecurityLevel.HIGH, labeling.getActivityClassification("fifth_activity"));
		// Attributes
		assertEquals(SecurityLevel.HIGH, labeling.getAttributeClassification("blue"));
		assertEquals(SecurityLevel.LOW, labeling.getAttributeClassification("green"));
		assertEquals(SecurityLevel.LOW, labeling.getAttributeClassification("pink"));
		assertEquals(SecurityLevel.LOW, labeling.getAttributeClassification("red"));
		assertEquals(SecurityLevel.HIGH, labeling.getAttributeClassification("yellow"));
		// Subjects
		assertEquals(SecurityLevel.LOW, labeling.getSubjectClearance("subjectA"));
		assertEquals(SecurityLevel.LOW, labeling.getSubjectClearance("subjectB"));
		assertEquals(SecurityLevel.HIGH, labeling.getSubjectClearance("subjectC"));
		assertEquals(SecurityLevel.HIGH, labeling.getSubjectClearance("subjectD"));
		assertEquals(SecurityLevel.LOW, labeling.getSubjectClearance("subjectE"));
	}

	/*
	 * DEFAULT SECURITY LEVEL TESTS
	 */

	/*
	 * Labeling with a high default security level with validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void highDefaultSecurityLevelWithValidation() {
		Labeling labeling = null;
		try {
			labeling = LabelingParser.parse(highDefaultSecurityLevelLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		}

		assertEquals(SecurityLevel.HIGH, labeling.getDefaultSecurityLevel());
	}

	/*
	 * Labeling with a high default security level without validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void highDefaultSecurityLevelWithoutValidation() {
		Labeling labeling = null;
		try {
			labeling = LabelingParser.parse(highDefaultSecurityLevelLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		}

		assertEquals(SecurityLevel.HIGH, labeling.getDefaultSecurityLevel());
	}

	/*
	 * Labeling with an invalid default security level with validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDefaultSecurityLevelWithValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidDefaultSecurityLevelLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * Labeling with an invalid default security level without validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidDefaultSecurityLevelWithoutValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidDefaultSecurityLevelLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * Labeling without default security level with validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void noDefaultSecurityLevelWithValidation() throws ParserException {
		try {
			LabelingParser.parse(noDefaultSecurityLevelLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * Labeling without default security level without validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void noDefaultSecurityLevelWithoutValidation() throws ParserException {
		try {
			LabelingParser.parse(noDefaultSecurityLevelLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * ANALYSIS CONTEXT TESTS
	 */

	/*
	 * Labeling with unknown analysis context with validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void unknownAnalysisContextWithValidation() throws ParserException {
		try {
			LabelingParser.parse(unknownAnalysisContextLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * Labeling with unknown analysis context without validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void unknownAnalysisContextWithoutValidation() throws ParserException {
		try {
			LabelingParser.parse(unknownAnalysisContextLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * Labeling without analysis context name with validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void noAnalysisContextWithValidation() throws ParserException {
		try {
			LabelingParser.parse(noAnalysisContextLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * Labeling without analysis context name without validation.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void noAnalysisContextWithoutValidation() throws ParserException {
		try {
			LabelingParser.parse(noAnalysisContextLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * ACTIVITIES TESTS
	 */

	/*
	 * AnalysisContext with an invalid activity security level with validation.
	 * The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidSecurityLevelActivityLabelingWithValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidSecurityLevelActivityLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid activity security level without
	 * validation. The Labeling class should throw an exception while validating
	 * the descriptors and not finding the corresponding activity.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void invalidSecurityLevelActivityLabelingWithoutValidation() throws ParameterException {
		try {
			LabelingParser.parse(invalidSecurityLevelActivityLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		}
	}

	/*
	 * ATTRIBUTES TESTS
	 */

	/*
	 * AnalysisContext with an invalid attribute (black) with validation. The
	 * Labeling class should throw an exception while validating the attributes.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidAttributeLabelingWithValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidAttributeLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid attribute (black) without validation. The
	 * Labeling class should throw an exception while validating the attributes.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidAttributeLabelingWithoutValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidAttributeLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid attribute security level with validation.
	 * The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidSecurityLevelAttributeLabelingWithValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidSecurityLevelAttributeLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid attribute security level without
	 * validation. No exception should be thrown and the attribute with the
	 * invalid security level will be ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void invalidSecurityLevelAttributeLabelingWithoutValidation() {
		try {
			LabelingParser.parse(invalidSecurityLevelAttributeLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		}
	}

	/*
	 * SUBJECTS TESTS
	 */

	/*
	 * AnalysisContext with an invalid subject security level with validation.
	 * The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidSecurityLevelSubjectLabelingWithValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidSecurityLevelSubjectLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid subject security level without
	 * validation. The Labeling class should throw an exception while validating
	 * the descriptors and not finding the corresponding subject.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void invalidSecurityLevelSubjectLabelingWithoutValidation() throws ParameterException {
		try {
			LabelingParser.parse(invalidSecurityLevelSubjectLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		}
	}
}
