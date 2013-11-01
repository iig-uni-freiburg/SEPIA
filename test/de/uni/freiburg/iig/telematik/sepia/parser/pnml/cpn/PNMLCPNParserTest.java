package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;

/**
 * <p>
 * Unit tests for the PNML CPN parser. The component unit tests for these classes is made in {@link PNMLCPNParserComponentTest}.
 * </p>
 * 
 * @author Adrian Lange
 * 
 * @see PNMLCPNParser
 */
public class PNMLCPNParserTest {

	public PNMLCPNParser parser = null;

	@Before
	public void setup() {
		parser = new PNMLCPNParser();
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLCPNParser#readColorInscription(org.w3c.dom.Node)}.
	 */
	@Test
	public void testReadColorInscription() throws ParameterException {
		Document arc = (Document) PNMLCPNParserTestUtils.createCPNArc(true, true, true, true, true, true, true, true, 2, true, true, true, true, true, true, true);
		NodeList colorInscriptionNodes = arc.getElementsByTagName("inscription");
		if (colorInscriptionNodes.getLength() == 1) {
			Element colorInscriptionElement = (Element) colorInscriptionNodes.item(0);
			NodeList colorsList = colorInscriptionElement.getElementsByTagName("colors");
			if (colorsList.getLength() == 1) {
				Map<String, Integer> colors = parser.readColorInscription(colorsList.item(0));
				assertEquals(3, colors.size());
				assertTrue(colors.containsKey("green"));
				assertEquals(1, colors.get("green").intValue());
				assertTrue(colors.containsKey("yellow"));
				assertEquals(2, colors.get("yellow").intValue());
				assertTrue(colors.containsKey("blue"));
				assertEquals(1, colors.get("blue").intValue());
				assertFalse(colors.containsKey("pink"));
			} else {
				fail("Wrong amount of colors-tags in the inscription-tag of the full place found!");
			}
		} else {
			fail("Wrong amount of inscription-tags in full place found!");
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLCPNParser#readInitialColorMarking(org.w3c.dom.Node)}.
	 */
	@Test
	public void testReadInitialColorMarking() throws ParameterException {
		Document place = (Document) PNMLCPNParserTestUtils.createCPNPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList initialMarkingNodes = place.getElementsByTagName("initialMarking");
		if (initialMarkingNodes.getLength() == 1) {
			Element initialMarkingElement = (Element) initialMarkingNodes.item(0);
			NodeList colorsList = initialMarkingElement.getElementsByTagName("colors");
			if (colorsList.getLength() == 1) {
				Map<String, Integer> colors = parser.readInitialColorMarking(colorsList.item(0));
				assertEquals(2, colors.size());
				assertTrue(colors.containsKey("green"));
				assertEquals(2, colors.get("green").intValue());
				assertTrue(colors.containsKey("yellow"));
				assertEquals(1, colors.get("yellow").intValue());
				assertFalse(colors.containsKey("blue"));
			} else {
				fail("Wrong amount of colors-tags in the initialMarking-tag of the full place found!");
			}
		} else {
			fail("Wrong amount of initialMarking-tags in full place found!");
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLCPNParser#readPlaceColorCapacities(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadPlaceColorCapacities() throws PNMLParserException, ParameterException {
		Document place = (Document) PNMLCPNParserTestUtils.createCPNPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList capacityList = place.getElementsByTagName("capacities");
		for (int i = 0; i < capacityList.getLength(); i++) {
			if (capacityList.item(i).getParentNode().equals(place.getDocumentElement())) {
				Element capacityElement = (Element) capacityList.item(i);
				Map<String, Integer> capacity = parser.readPlaceColorCapacities(capacityElement);
				assertEquals(2, capacity.size());
				assertTrue(capacity.containsKey("green"));
				assertEquals(2, capacity.get("green").intValue());
				assertTrue(capacity.containsKey("yellow"));
				assertEquals(5, capacity.get("yellow").intValue());
				assertFalse(capacity.containsKey("blue"));
			}
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLCPNParser#readTokenColors(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadTokenColors() throws PNMLParserException, ParameterException {
		Document tokencolors = (Document) PNMLCPNParserTestUtils.createTokenColors(true, true, true);
		Map<String, Color> colors = parser.readTokenColors(tokencolors.getDocumentElement());
		assertEquals(3, colors.size());
		assertTrue(colors.containsKey("green"));
		assertEquals(Color.GREEN, colors.get("green"));
		assertTrue(colors.containsKey("yellow"));
		assertEquals(Color.YELLOW, colors.get("yellow"));
		assertTrue(colors.containsKey("blue"));
		assertEquals(Color.BLUE, colors.get("blue"));
		assertFalse(colors.containsKey("pink"));

		Document tokencolorsNoColorName = (Document) PNMLCPNParserTestUtils.createTokenColors(false, true, true);
		try {
			parser.readTokenColors(tokencolorsNoColorName.getDocumentElement());
			fail("An exception should be thrown because of the missing color name.");
		} catch (Exception e) {
		}

		Document tokencolorsNoRGBColor = (Document) PNMLCPNParserTestUtils.createTokenColors(true, false, true);
		try {
			parser.readTokenColors(tokencolorsNoRGBColor.getDocumentElement());
			fail("An exception should be thrown because of the missing RGB color tag.");
		} catch (Exception e) {
		}

		Document tokencolorsMissingRGBColorAttribute = (Document) PNMLCPNParserTestUtils.createTokenColors(true, true, false);
		try {
			parser.readTokenColors(tokencolorsMissingRGBColorAttribute.getDocumentElement());
		} catch (Exception e) {
			fail("No exception should be thrown if a RGB color attribute is missing.");
		}
	}
}
