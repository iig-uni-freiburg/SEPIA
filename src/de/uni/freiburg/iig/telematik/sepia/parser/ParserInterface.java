package de.uni.freiburg.iig.telematik.sepia.parser;

import java.io.File;

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPetriNet;

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
	public GraphicalPetriNet<?, ?, ?, ?, ?> parse(File file) throws Exception;
}
