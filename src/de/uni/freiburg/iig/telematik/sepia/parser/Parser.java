package de.uni.freiburg.iig.telematik.sepia.parser;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * <p>
 * Parser class which determines the file type and calls the specific parser implementing the {@link ParserInterface}.
 * </p>
 * <ul>
 * <li>determine file type</li>
 * <li>call respective parser</li>
 * </ul>
 * 
 * @author Adrian Lange
 * 
 */
public class Parser {
	
	/**
	 * Parses the given file with the parser respective to the file extension.
	 * 
	 * @param file
	 *            File to parse
	 * @return A {@link AbstractGraphicalPN}
	 * @throws IOException
	 *             If the file can't be found or read
	 * @throws ParserException
	 *             For exceptions caused by the parsing
	 * @throws ParameterException
	 *             For exceptions caused by the given parameters
	 */
	public static synchronized <P extends AbstractPlace<F,S>, 
	   							T extends AbstractTransition<F,S>, 
	   							F extends AbstractFlowRelation<P,T,S>, 
	   							M extends AbstractMarking<S>, 
	   							S extends Object>
	
							AbstractGraphicalPN<P, T, F, M, S>
	
	parse(File file) throws IOException, ParserException, ParameterException {
		ParserInterface parser = getParser(file);
		Validate.notNull(parser, "No suitable parser for the given file could have been found.");
		return parser.parse(file);
	}

	/**
	 * @param file
	 *            File to parse
	 * @return Returns the for the file's extension suitable parser or <code>null</code> if no suitable parser could be found
	 * @throws IOException
	 *             If the file can't be found
	 */
	private static synchronized ParserInterface getParser(File file) throws IOException {
		if (file.isDirectory())
			throw new IOException("Given file is a directory and therefore not parsable.");

		if (file.getName().endsWith(".pnml"))
			return new PNMLParser();
		return null;
	}
}
