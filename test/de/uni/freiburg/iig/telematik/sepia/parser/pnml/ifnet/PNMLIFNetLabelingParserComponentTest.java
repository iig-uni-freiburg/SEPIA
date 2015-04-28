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
 * Because of the socket connections needed to get the PNTD RelaxNG schemes from remote, these tests can take a while.
 * </p>
 * 
 * @author Adrian Lange
 */
public class PNMLIFNetLabelingParserComponentTest {

	/*
	 * Aborts tests after the specified time in milliseconds for the case of connection issues. Especially needed for the validating tests which create a socket connection to a remote server to get the needed RelaxNG schemes.
	 */
	public static final int VALIDATION_TIMEOUT = 20000;

	/* Project intern path to the test resources without leading slash */
	public static final String AC_TEST_RESOURCES_PATH = "test-resources/pnml/ifnet/labeling/";

	/* Valid analysis context */
	@Rule
	public TestResourceFile validlabelingResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetLabeling.xml");
	/* Analysis context with an invalid attribute */
	@Rule
	public TestResourceFile invalidAttributeLabelingResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetLabeling-invalidAttribute.xml");
	/* Analysis context with an invalid activity security level */
	@Rule
	public TestResourceFile invalidSecurityLevelActivityLabelingResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetLabeling-invalidSecurityLevelActivity.xml");
	/* Analysis context with an invalid attribute security level */
	@Rule
	public TestResourceFile invalidSecurityLevelAttributeLabelingResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetLabeling-invalidSecurityLevelAttribute.xml");
	/* Analysis context with an invalid subject security level */
	@Rule
	public TestResourceFile invalidSecurityLevelSubjectLabelingResource = new TestResourceFile(AC_TEST_RESOURCES_PATH + "IFNetLabeling-invalidSecurityLevelSubject.xml");

	private SOABase base = null;
	private ACLModel acm = null;
	private AnalysisContext ac = null;
	
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

		ac = new AnalysisContext("AC", acm, true);
		ac.setSubjectDescriptor("first activity", "subjectA");
		ac.setSubjectDescriptor("second activity", "subjectE");
		ac.setSubjectDescriptor("third activity", "subjectD");
		ac.setSubjectDescriptor("fourth activity", "subjectB");
		ac.setSubjectDescriptor("fifth activity", "subjectC");
	}

	/*
	 * Test if all sample files of the IFNet analysis context exist.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void sampleIFNetACFilesExist() {
		assertTrue(validlabelingResource.getFile().exists());
		assertTrue(invalidAttributeLabelingResource.getFile().exists());
		assertTrue(invalidSecurityLevelActivityLabelingResource.getFile().exists());
		assertTrue(invalidSecurityLevelAttributeLabelingResource.getFile().exists());
		assertTrue(invalidSecurityLevelSubjectLabelingResource.getFile().exists());
	}

	/*
	 * Valid AnalysisContext with validation, where no exception should be thrown. Performs also some shallow tests.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validACWithValidation() throws ParameterException {
		Labeling labeling = null;
		try {
			labeling = LabelingParser.parse(validlabelingResource.getFile(), true, Arrays.asList(ac));
			ac.setLabeling(labeling);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}

		// Activities
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("first activity"));
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("second activity"));
		assertEquals(SecurityLevel.HIGH, labeling.getActivityClassification("third activity"));
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("fourth activity"));
		assertEquals(SecurityLevel.HIGH, labeling.getActivityClassification("fifth activity"));
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
	 * Valid AnalysisContext with validation, where no exception should be thrown. Performs also some shallow tests.
	 */
	@Test(timeout = VALIDATION_TIMEOUT)
	public void validACWithoutValidation() throws ParameterException {
		Labeling labeling = null;
		try {
			labeling = LabelingParser.parse(validlabelingResource.getFile(), false, Arrays.asList(ac));
			ac.setLabeling(labeling);
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}

		// Activities
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("first activity"));
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("second activity"));
		assertEquals(SecurityLevel.HIGH, labeling.getActivityClassification("third activity"));
		assertEquals(SecurityLevel.LOW, labeling.getActivityClassification("fourth activity"));
		assertEquals(SecurityLevel.HIGH, labeling.getActivityClassification("fifth activity"));
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
	 * ACTIVITIES TESTS
	 */

	/*
	 * AnalysisContext with an invalid activity security level with validation. The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidSecurityLevelActivityACWithValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidSecurityLevelActivityLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid activity security level without validation. The Labeling class should throw an exception while validating the descriptors and not finding the corresponding activity.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void invalidSecurityLevelActivityACWithoutValidation() throws ParameterException {
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
	 * AnalysisContext with an invalid attribute (black) with validation. The Labeling class should throw an exception while validating the attributes.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidAttributeACWithValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidAttributeLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid attribute (black) without validation. The Labeling class should throw an exception while validating the attributes.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidAttributeACWithoutValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidAttributeLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid attribute security level with validation. The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidSecurityLevelAttributeACWithValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidSecurityLevelAttributeLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid attribute security level without validation. No exception should be thrown and the attribute with the invalid security level will be ignored.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void invalidSecurityLevelAttributeACWithoutValidation() {
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
	 * AnalysisContext with an invalid subject security level with validation. The parser should throw an exception while validating.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParserException.class)
	public void invalidSecurityLevelSubjectACWithValidation() throws ParserException {
		try {
			LabelingParser.parse(invalidSecurityLevelSubjectLabelingResource.getFile(), true, Arrays.asList(ac));
		} catch (ParameterException e) {
			fail("Exception caused by an invalid parametrization: " + e.getMessage());
		}
	}

	/*
	 * AnalysisContext with an invalid subject security level without validation. The Labeling class should throw an exception while validating the descriptors and not finding the corresponding subject.
	 */
	@Test(timeout = VALIDATION_TIMEOUT, expected = ParameterException.class)
	public void invalidSecurityLevelSubjectACWithoutValidation() throws ParameterException {
		try {
			LabelingParser.parse(invalidSecurityLevelSubjectLabelingResource.getFile(), false, Arrays.asList(ac));
		} catch (ParserException e) {
			fail("Exception while parsing: " + e.getMessage());
		}
	}
}
