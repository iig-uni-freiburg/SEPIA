package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;

/**
 * <p>
 * Unit tests for the PNML IFNet analysis context parser. The component unit tests for these classes is made in {@link PNMLIFNetAnalysisContextParserComponentTest}.
 * </p>
 * 
 * @author Adrian Lange
 * 
 * @see AnalysisContextParser
 */
public class PNMLIFNetLabelingParserTest {

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.PNMLIFNetAnalysisContextParser#readLabeling(org.w3c.dom.Document, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testReadLabeling() throws ParameterException {

		List<String> activities = new ArrayList<>();

		// Correct labeling
		Document labelingDocument = PNMLIFNetLabelingParserTestUtils.createLabeling("classifications", "classification", "activity");
		Map<String, SecurityLevel> labeling = LabelingParser.readLabeling(labelingDocument, "classifications", "classification", "activity", activities);
		assertEquals(5, labeling.size());
		assertTrue(labeling.containsKey("first_activity"));
		assertTrue(labeling.containsKey("second_activity"));
		assertTrue(labeling.containsKey("third_activity"));
		assertTrue(labeling.containsKey("fourth_activity"));
		assertTrue(labeling.containsKey("fifth_activity"));
		assertEquals(SecurityLevel.LOW, labeling.get("first_activity"));
		assertEquals(SecurityLevel.LOW, labeling.get("second_activity"));
		assertEquals(SecurityLevel.HIGH, labeling.get("third_activity"));
		assertEquals(SecurityLevel.LOW, labeling.get("fourth_activity"));
		assertEquals(SecurityLevel.HIGH, labeling.get("fifth_activity"));

		// Wrong list name
		// No labeling should be found
		labelingDocument = PNMLIFNetLabelingParserTestUtils.createLabeling("classifications", "classification", "activity");
		labeling = LabelingParser.readLabeling(labelingDocument, "wrongname", "classification", "activity", activities);
		assertEquals(0, labeling.size());

		// Wrong type name
		// No labeling should be found
		labelingDocument = PNMLIFNetLabelingParserTestUtils.createLabeling("classifications", "classification", "activity");
		labeling = LabelingParser.readLabeling(labelingDocument, "classifications", "wrongname", "activity", activities);
		assertEquals(0, labeling.size());

		// Wrong object descriptor name
		// No labeling should be found
		labelingDocument = PNMLIFNetLabelingParserTestUtils.createLabeling("classifications", "classification", "activity");
		labeling = LabelingParser.readLabeling(labelingDocument, "classifications", "classification", "wrongname", activities);
		assertEquals(0, labeling.size());
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.PNMLIFNetAnalysisContextParser#parse(java.io.File)}.
	 */
	@Ignore
	// is tested by PNMLIFNetAnalysisContextParserComponentTest
	public void testParseFile() {
		fail("Not yet implemented");
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.PNMLIFNetAnalysisContextParser#parse(java.lang.String)}.
	 */
	@Ignore
	// is tested by PNMLIFNetAnalysisContextParserComponentTest
	public void testParseString() {
		fail("Not yet implemented");
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.PNMLIFNetAnalysisContextParser#parse(java.io.File, boolean)}.
	 */
	@Ignore
	// is tested by PNMLIFNetAnalysisContextParserComponentTest
	public void testParseFileBoolean() {
		fail("Not yet implemented");
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.PNMLIFNetAnalysisContextParser#parse(java.lang.String, boolean)}.
	 */
	@Ignore
	// is tested by PNMLIFNetAnalysisContextParserComponentTest
	public void testParseStringBoolean() {
		fail("Not yet implemented");
	}
}
