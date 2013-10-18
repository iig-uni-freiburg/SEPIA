/**
 * 
 */
package de.uni.freiburg.iig.telematik.sepia.parser.pnml.test;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.XMLParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLPTNetParser;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParserException;

/**
 * <p>
 * Unit tests for the P/T-net PNML parser and the abstract PNML parser class. The component unit tests for these classes is made in
 * {@link PNMLPTNetParserComponentTest}.
 * </p>
 * 
 * @author Adrian Lange
 * 
 * @see AbstractPNMLParser
 * @see PNMLPTNetParser
 */
public class PNMLPTNetParserTest {

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser#readDimension(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadDimension() throws ParameterException {
		// Read complete dimension
		Document place = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList graphicsList1 = place.getElementsByTagName("graphics");
		for (int i = 0; i < graphicsList1.getLength(); i++) {
			if (graphicsList1.item(i).getParentNode().equals(place.getDocumentElement())) {
				Element graphicsElement = (Element) graphicsList1.item(i);
				NodeList dimensionList = graphicsElement.getElementsByTagName("dimension");
				if (dimensionList.getLength() > 0) {
					Element dimensionElement = (Element) dimensionList.item(0);
					Dimension dimension = AbstractPNMLParser.readDimension(dimensionElement);
					assertTrue(dimension.getX() == 37.0);
					assertTrue(dimension.getY() == 33.0);
				}
			}
		}

		// Read incomplete dimension
		Document placeIncompleteDimension = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, false);
		NodeList graphicsList2 = placeIncompleteDimension.getElementsByTagName("graphics");
		for (int i = 0; i < graphicsList2.getLength(); i++) {
			if (graphicsList2.item(i).getParentNode().equals(placeIncompleteDimension.getDocumentElement())) {
				Element graphicsElement = (Element) graphicsList2.item(i);
				NodeList dimensionList = graphicsElement.getElementsByTagName("dimension");
				if (dimensionList.getLength() > 0) {
					Element dimensionElement = (Element) dimensionList.item(0);
					Dimension dimension = AbstractPNMLParser.readDimension(dimensionElement);
					assertTrue(dimension.getX() == 37.0);
					assertTrue(dimension.getY() == Dimension.DEFAULT_DIMENSION_Y);
				}
			}
		}
	}

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser#readFill(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadFill() throws ParameterException, URISyntaxException {
		Document place = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList graphicsList = place.getElementsByTagName("graphics");
		for (int i = 0; i < graphicsList.getLength(); i++) {
			if (graphicsList.item(i).getParentNode().equals(place.getDocumentElement())) {
				Element graphicsElement = (Element) graphicsList.item(i);
				NodeList fillList = graphicsElement.getElementsByTagName("fill");
				if (fillList.getLength() > 0) {
					Element fillElement = (Element) fillList.item(0);
					Fill fill = AbstractPNMLParser.readFill(fillElement);
					assertEquals("#FF0000", fill.getColor());
					assertEquals("#FFFFFF", fill.getGradientColor());
					assertEquals(GradientRotation.DIAGONAL, fill.getGradientRotation());
					assertEquals(new URI("http://www.some-url.com/image.png"), fill.getImage());
				}
			}
		}
	}

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser#readFont(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadFont() throws ParameterException, URISyntaxException {
		Document place = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList nameList = place.getElementsByTagName("name");
		for (int j = 0; j < nameList.getLength(); j++) {
			if (nameList.item(j).getParentNode().equals(place.getDocumentElement())) {
				Element nameElement = (Element) nameList.item(j);
				NodeList graphicsList = nameElement.getElementsByTagName("graphics");
				for (int i = 0; i < graphicsList.getLength(); i++) {
					if (graphicsList.item(i).getParentNode().equals(nameElement)) {
						Element graphicsElement = (Element) graphicsList.item(i);
						NodeList fontList = graphicsElement.getElementsByTagName("font");
						if (fontList.getLength() > 0) {
							Element fontElement = (Element) fontList.item(0);
							Font font = AbstractPNMLParser.readFont(fontElement);
							assertEquals("arial,sans-serif", font.getFamily());
							assertEquals("italic", font.getStyle());
							assertEquals("bold", font.getWeight());
							assertEquals("medium", font.getSize());
							assertEquals(Decoration.UNDERLINE, font.getDecoration());
							assertEquals(Align.LEFT, font.getAlign());
							assertTrue(font.getRotation() == 0.0);
						}
					}
				}
			}
		}
	}

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser#readInitialMarking(org.w3c.dom.Node)}.
	 */
	@Test
	public void testReadInitialMarking() throws XMLParserException, ParameterException {
		Document place = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList initialMarkingList = place.getElementsByTagName("initialMarking");
		for (int i = 0; i < initialMarkingList.getLength(); i++) {
			if (initialMarkingList.item(i).getParentNode().equals(place.getDocumentElement())) {
				Element initialMarkingElement = (Element) initialMarkingList.item(i);
				int initialMarking = AbstractPNMLParser.readInitialMarking(initialMarkingElement);
				assertTrue(initialMarking == 3);
			}
		}
	}

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser#readLine(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadLine() throws ParameterException {
		Document place = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList graphicsList = place.getElementsByTagName("graphics");
		for (int i = 0; i < graphicsList.getLength(); i++) {
			if (graphicsList.item(i).getParentNode().equals(place.getDocumentElement())) {
				Element graphicsElement = (Element) graphicsList.item(i);
				NodeList lineList = graphicsElement.getElementsByTagName("line");
				if (lineList.getLength() > 0) {
					Element lineElement = (Element) lineList.item(0);
					Line line = AbstractPNMLParser.readLine(lineElement);
					assertEquals(Shape.LINE, line.getShape());
					assertEquals("black", line.getColor());
					assertEquals(Style.SOLID, line.getStyle());
					assertTrue(line.getWidth() == 1.5);
				}
			}
		}
	}

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser#readOffset(org.w3c.dom.Element)}.
	 * 
	 * @throws ParameterException
	 */
	@Test
	public void testReadOffset() throws ParameterException {
		// Read complete offset
		Document place = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList nameList1 = place.getElementsByTagName("name");
		for (int j = 0; j < nameList1.getLength(); j++) {
			if (nameList1.item(j).getParentNode().equals(place.getDocumentElement())) {
				Element nameElement = (Element) nameList1.item(j);
				NodeList graphicsList = nameElement.getElementsByTagName("graphics");
				for (int i = 0; i < graphicsList.getLength(); i++) {
					if (graphicsList.item(i).getParentNode().equals(nameElement)) {
						Element graphicsElement = (Element) graphicsList.item(i);
						NodeList offsetList = graphicsElement.getElementsByTagName("offset");
						if (offsetList.getLength() > 0) {
							Element offsetElement = (Element) offsetList.item(0);
							Offset offset = AbstractPNMLParser.readOffset(offsetElement);
							assertTrue(offset.getX() == 1.0);
							assertTrue(offset.getY() == -5.0);
						}
					}
				}
			}
		}

		// Read incomplete dimension
		Document placeIncompleteDimension = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, false, true, true, true, true, true, true, true, true, false);
		NodeList nameList2 = placeIncompleteDimension.getElementsByTagName("name");
		for (int j = 0; j < nameList2.getLength(); j++) {
			if (nameList2.item(j).getParentNode().equals(placeIncompleteDimension.getDocumentElement())) {
				Element nameElement = (Element) nameList2.item(j);
				NodeList graphicsList = nameElement.getElementsByTagName("graphics");
				for (int i = 0; i < graphicsList.getLength(); i++) {
					if (graphicsList.item(i).getParentNode().equals(nameElement)) {
						Element graphicsElement = (Element) graphicsList.item(i);
						NodeList offsetList = graphicsElement.getElementsByTagName("offset");
						if (offsetList.getLength() > 0) {
							Element offsetElement = (Element) offsetList.item(0);
							Offset offset = AbstractPNMLParser.readOffset(offsetElement);
							assertTrue(offset.getX() == 1.0);
							assertTrue(offset.getY() == Offset.DEFAULT_OFFSET_Y);
						}
					}
				}
			}
		}
	}

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser#readPlaceCapacity(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadPlaceCapacity() throws PNMLParserException, ParameterException {
		Document place = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList capacityList = place.getElementsByTagName("capacity");
		for (int i = 0; i < capacityList.getLength(); i++) {
			if (capacityList.item(i).getParentNode().equals(place.getDocumentElement())) {
				Element capacityElement = (Element) capacityList.item(i);
				int capacity = AbstractPNMLParser.readPlaceCapacity(capacityElement);
				assertTrue(capacity == 10);
			}
		}
	}

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser#readPosition(org.w3c.dom.Element)}.
	 */
	@Test
	public void testReadPosition() throws ParameterException {
		// Read complete position
		Document place = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList graphicsList1 = place.getElementsByTagName("graphics");
		for (int i = 0; i < graphicsList1.getLength(); i++) {
			if (graphicsList1.item(i).getParentNode().equals(place.getDocumentElement())) {
				Element graphicsElement = (Element) graphicsList1.item(i);
				NodeList positionList = graphicsElement.getElementsByTagName("position");
				if (positionList.getLength() > 0) {
					Element positionElement = (Element) positionList.item(0);
					Position position = AbstractPNMLParser.readPosition(positionElement);
					assertTrue(position.getX() == 20.0);
					assertTrue(position.getY() == 25.0);
				}
			}
		}

		// Read incomplete dimension
		Document placeIncompletePosition = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, false, true);
		NodeList graphicsList2 = placeIncompletePosition.getElementsByTagName("graphics");
		for (int i = 0; i < graphicsList2.getLength(); i++) {
			if (graphicsList2.item(i).getParentNode().equals(placeIncompletePosition.getDocumentElement())) {
				Element graphicsElement = (Element) graphicsList2.item(i);
				NodeList positionList = graphicsElement.getElementsByTagName("position");
				if (positionList.getLength() > 0) {
					Element positionElement = (Element) positionList.item(0);
					Position position = AbstractPNMLParser.readPosition(positionElement);
					assertTrue(position.getX() == 20.0);
					assertTrue(position.getY() == Position.DEFAULT_POSITION_Y);
				}
			}
		}
	}

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.parser.pnml.AbstractPNMLParser#readText(org.w3c.dom.Node)}.
	 */
	@Test
	public void testReadText() throws XMLParserException, ParameterException {
		Document place = (Document) PNMLPTNetParserTestUtils.createPTNetPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true);
		NodeList nameList = place.getElementsByTagName("name");
		for (int j = 0; j < nameList.getLength(); j++) {
			if (nameList.item(j).getParentNode().equals(place.getDocumentElement())) {
				Element nameElement = (Element) nameList.item(j);
				String text = AbstractPNMLParser.readText(nameElement);
				assertEquals("ready", text);
			}
		}
	}
}
