package de.uni.freiburg.iig.telematik.sepia.petrinet.pt;

import java.util.HashMap;
import java.util.HashSet;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;

public class PTNetUtils {

	/**
	 * Generates a set of transitions labeled with numbers from 1 to number.<br>
	 * The method generated at most 26 transitions.
	 * 
	 * @param number
	 *            The number n of transitions to generate (e in [1;26])
	 * @return A set of transitions.
	 * @throws ParameterException
	 *             if the number parameter lies outside the valid range [1;26]
	 */
	public static HashSet<PTTransition> generateTransitions(int number) {
		Validate.inclusiveBetween(1, 26, number);
		HashSet<PTTransition> transitions = new HashSet<PTTransition>();
		for (int i = 0; i < number; i++)
			transitions.add(new PTTransition(String.valueOf((char) (65 + i))));
		return transitions;
	}

	public static HashMap<PTPlace, Integer> createSimpleMarking(PTPlace... places) {
		Validate.notNull(places);
		Validate.noNullElements(places);
		HashMap<PTPlace, Integer> marking = new HashMap<PTPlace, Integer>();
		for (PTPlace p : places)
			marking.put(p, 1);
		return marking;
	}

}
