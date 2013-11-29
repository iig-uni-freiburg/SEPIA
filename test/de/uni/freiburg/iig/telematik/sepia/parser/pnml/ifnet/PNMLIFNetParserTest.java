package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;

/**
 * <p>
 * Unit tests for the PNML IFNet parser. The component unit tests for these classes is made in {@link PNMLIFNetParserComponentTest}.
 * </p>
 * 
 * @author Adrian Lange
 * 
 * @see PNMLIFNetParser
 */
public class PNMLIFNetParserTest {

	public PNMLIFNetParser parser = null;

	@Before
	public void setup() {
		parser = new PNMLIFNetParser();
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLIFNetParser#readAccessFunctions(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadAccessFunctions() throws ParameterException {
		Document transition = (Document) PNMLIFNetParserTestUtils.createTransition(true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, true);
		NodeList accessFunctionsNodes = transition.getElementsByTagName("accessfunctions");
		for (int a = 0; a < accessFunctionsNodes.getLength(); a++) {
			if (accessFunctionsNodes.item(a).getNodeType() == Node.ELEMENT_NODE && accessFunctionsNodes.item(a).getParentNode().equals(transition)) {
				Element accessFunctionsElement = (Element) accessFunctionsNodes.item(a);
				Map<String, Collection<AccessMode>> accessFunctions = parser.readAccessFunctions(accessFunctionsElement);
				assertEquals(1, accessFunctions.size());
				assertTrue(accessFunctions.containsKey("green"));
				Collection<AccessMode> accessModes = accessFunctions.get("green");
				assertEquals(2, accessModes.size());
				assertTrue(accessModes.contains("read"));
				assertTrue(accessModes.contains("create"));
				assertFalse(accessModes.contains("write"));
				assertFalse(accessModes.contains("delete"));
			}
		}

		transition = (Document) PNMLIFNetParserTestUtils.createTransition(true, true, true, true, true, true, true, true, true, true, true, false, true, true, false, false, true);
		accessFunctionsNodes = transition.getElementsByTagName("accessfunctions");
		assertEquals(0, accessFunctionsNodes.getLength());
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLIFNetParser#readColorInscription(org.w3c.dom.Node)}.
	 */
	@Test
	public void testReadColorInscription() throws ParameterException {
		Document arc = (Document) PNMLIFNetParserTestUtils.createArc(true, true, true, true, true, true, true, true, 2, true, true, true, true, true, true, true);
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
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLIFNetParser#readInitialColorMarking(org.w3c.dom.Node)}.
	 */
	@Test
	public void testReadInitialColorMarking() throws ParameterException {
		Document place = (Document) PNMLIFNetParserTestUtils.createPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
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
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLIFNetParser#readPlaceColorCapacities(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadPlaceColorCapacities() throws PNMLParserException, ParameterException {
		Document place = (Document) PNMLIFNetParserTestUtils.createPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
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
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLIFNetParser#readTokenColors(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadTokenColors() throws PNMLParserException, ParameterException {
		Document tokencolors = (Document) PNMLIFNetParserTestUtils.createTokenColors(true, true, true);
		Map<String, Color> colors = parser.readTokenColors(tokencolors.getDocumentElement());
		assertEquals(3, colors.size());
		assertTrue(colors.containsKey("green"));
		assertEquals(Color.GREEN, colors.get("green"));
		assertTrue(colors.containsKey("yellow"));
		assertEquals(Color.YELLOW, colors.get("yellow"));
		assertTrue(colors.containsKey("blue"));
		assertEquals(Color.BLUE, colors.get("blue"));
		assertFalse(colors.containsKey("pink"));

		Document tokencolorsNoColorName = (Document) PNMLIFNetParserTestUtils.createTokenColors(false, true, true);
		try {
			parser.readTokenColors(tokencolorsNoColorName.getDocumentElement());
			fail("An exception should be thrown because of the missing color name.");
		} catch (Exception e) {
		}

		Document tokencolorsNoRGBColor = (Document) PNMLIFNetParserTestUtils.createTokenColors(true, false, true);
		try {
			parser.readTokenColors(tokencolorsNoRGBColor.getDocumentElement());
			fail("An exception should be thrown because of the missing RGB color tag.");
		} catch (Exception e) {
		}

		Document tokencolorsMissingRGBColorAttribute = (Document) PNMLIFNetParserTestUtils.createTokenColors(true, true, false);
		try {
			parser.readTokenColors(tokencolorsMissingRGBColorAttribute.getDocumentElement());
		} catch (Exception e) {
			fail("No exception should be thrown if a RGB color attribute is missing.");
		}
	}

	/*
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AbstractPNMLIFNetParser#readTransitionType(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadTransitionType() throws ParameterException {
		Document transition = (Document) PNMLIFNetParserTestUtils.createTransition(true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, true);
		Element transitionElement = transition.getDocumentElement();
		String transitionType = parser.readTransitionType(transitionElement);
		assertEquals("regular", transitionType);

		transition = (Document) PNMLIFNetParserTestUtils.createTransition(true, true, true, true, true, true, true, true, true, true, false, true, true, true, false, false, true);
		transitionElement = transition.getDocumentElement();
		transitionType = parser.readTransitionType(transitionElement);
		assertEquals("declassification", transitionType);
	}
}
