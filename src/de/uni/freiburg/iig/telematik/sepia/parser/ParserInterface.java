package de.uni.freiburg.iig.telematik.sepia.parser;

import java.io.File;
import java.io.IOException;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;

/**
 * <p>
 * TODO
 * </p>
 * <ul>
 * <li>determine file type</li>
 * <li>call respective parser</li>
 * </ul>
 * 
 * @author Adrian Lange
 */
public interface ParserInterface {

	/**
	 * TODO
	 * 
	 * @param file
	 * @return
	 */
	public <P extends AbstractPlace<F,S>, 
	    	T extends AbstractTransition<F,S>, 
	    	F extends AbstractFlowRelation<P,T,S>, 
	    	M extends AbstractMarking<S>, 
	    	S extends Object> 
	
			GraphicalPN<P, T, F, M, S> parse(File file) throws IOException, ParserException, ParameterException;
}
