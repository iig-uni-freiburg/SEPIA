package de.uni.freiburg.iig.telematik.sepia.parser;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * <p>
 * A parser for graphical petri nets must serve a parsing method.
 * </p>
 * 
 * @author Adrian Lange
 */
public interface ParserInterface {

	/**
	 * Parses the given file and returns a {@link AbstractGraphicalPN}.
	 */
	public <P extends AbstractPlace<F,S>, 
	    	T extends AbstractTransition<F,S>, 
	    	F extends AbstractFlowRelation<P,T,S>, 
	    	M extends AbstractMarking<S>, 
	    	S extends Object> 
	
			AbstractGraphicalPN<P, T, F, M, S> parse(File file) throws IOException, ParserException, ParameterException;
}
