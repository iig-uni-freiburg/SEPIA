package de.uni.freiburg.iig.telematik.sepia.serialize.test;

import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws ParameterException 
	 * @throws ParserException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParserException, ParameterException {
		PNMLParser parser = new PNMLParser();
		AbstractGraphicalPN net = parser.parse("/Users/stocker/Desktop/ptnet.pnml");
		System.out.println(net.getPetriNet().getNetType());
		System.out.println(net.getPetriNet());
		System.out.println(PNSerialization.serialize(net, PNSerializationFormat.PNML));
	}

}
