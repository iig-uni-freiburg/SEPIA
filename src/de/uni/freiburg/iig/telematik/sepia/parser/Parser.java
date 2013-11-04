package de.uni.freiburg.iig.telematik.sepia.parser;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.parser.ParserException.ErrorCode;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
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
	   							S extends Object,
	   					    	N extends AbstractPetriNet<P,T,F,M,S>,
	   					    	G extends AbstractPNGraphics<P,T,F,M,S>>
	
							AbstractGraphicalPN<P, T, F, M, S, N, G>
	
	parse(File file) throws IOException, ParserException, ParameterException {
		validateFile(file);
		ParsingFormat format = guessFormat(file);
		if(format == null)
			throw new ParserException(ErrorCode.UNKNOWN_FILE_EXTENSION);
		ParserInterface parser = getParser(file, format);
		return parser.<P,T,F,M,S,N,G>parse(file);
	}
	
	/**
	 * Parses the given file with the parser respective to the file extension.
	 * 
	 * @param fileName
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
	   							S extends Object,
	   					    	N extends AbstractPetriNet<P,T,F,M,S>,
	   					    	G extends AbstractPNGraphics<P,T,F,M,S>>
	
							AbstractGraphicalPN<P, T, F, M, S, N, G>
	
	parse(String fileName) throws IOException, ParserException, ParameterException {
		Validate.notNull(fileName);
		return Parser.<P,T,F,M,S,N,G>parse(prepareFile(fileName));
	}
	
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
	   							S extends Object,
	   					    	N extends AbstractPetriNet<P,T,F,M,S>,
	   					    	G extends AbstractPNGraphics<P,T,F,M,S>>
	
							AbstractGraphicalPN<P, T, F, M, S, N, G>
	
	parse(File file, ParsingFormat format) throws IOException, ParserException, ParameterException {
		validateFile(file);
		Validate.notNull(format);
		ParserInterface parser = getParser(file, format);
		return parser.<P,T,F,M,S,N,G>parse(file);
	}
	
	/**
	 * Parses the given file with the parser respective to the file extension.
	 * 
	 * @param fileName
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
	   							S extends Object,
	   					    	N extends AbstractPetriNet<P,T,F,M,S>,
	   					    	G extends AbstractPNGraphics<P,T,F,M,S>>
	
							AbstractGraphicalPN<P,T,F,M,S,N,G>
	
	parse(String fileName, ParsingFormat format) throws IOException, ParserException, ParameterException {
		Validate.notNull(fileName);
		return Parser.<P,T,F,M,S,N,G>parse(prepareFile(fileName), format);
	}
	
	private static File prepareFile(String fileName) throws IOException{
		File file = new File(fileName);
		validateFile(file);
		return file;
	}
	
	private static void validateFile(File file) throws IOException{
		if(!file.exists())
			throw new IOException("I/O Error on opening file: File does not exist!");
		if(file.isDirectory())
			throw new IOException("I/O Error on opening file: File is a directory!");
		if(!file.canRead())
			throw new IOException("I/O Error on opening file: Unable to read file!");
	}

	/**
	 * @param file
	 *            File to parse
	 * @return Returns the for the file's extension suitable parser or <code>null</code> if no suitable parser could be found
	 * @throws IOException
	 *             If the file can't be found
	 */
	public static synchronized ParserInterface getParser(File file, ParsingFormat format) throws ParserException {
		switch(format){
		case PNML: return new PNMLParser();
		case PETRIFY:
			//TODO:
			break;
		}
		throw new ParserException(ErrorCode.UNSUPPORTED_FORMAT);
	}
	
	public static ParsingFormat guessFormat(File file){
		for(ParsingFormat format: ParsingFormat.values()){
			if(file.getName().endsWith(format.getFileFormat().getFileExtension())){
				return format;
			}
		}
		return null;
	}
}
