package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cwn;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.PNMLCPNParserTestUtils;

/**
 * @author Adrian Lange
 */
public class PNMLCWNParserTestUtils extends PNMLCPNParserTestUtils {

	public static void main(String[] args) throws TransformerFactoryConfigurationError, TransformerException {
		System.out.println(toXML(createArc(true, true, true, true, true, true, true, true, 2, true, true, true, true, true, true, true)));
		System.out.println(toXML(createPlace(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)));
		System.out.println(toXML(createTokenColors(true, true, true)));
		System.out.println(toXML(createTransition(true, true, true, true, true, true, true, true, true, true, false, true)));
	}
}
