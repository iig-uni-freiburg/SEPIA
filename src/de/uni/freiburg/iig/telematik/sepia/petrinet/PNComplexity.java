package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.io.Serializable;

public class PNComplexity  implements Serializable{
	
	private static final long serialVersionUID = 5621037978404236958L;

	private static final String complexityFormat = "|P| = %s, |T| = %s, |F| = %s";

	public int numPlaces = -1;
	public int numTransitions = -1;
	public int numRelations = -1;
	
	public PNComplexity(int numPlaces, int numTransitions, int numRelations) {
		super();
		this.numPlaces = numPlaces;
		this.numTransitions = numTransitions;
		this.numRelations = numRelations;
	}
	
	@Override
	public String toString(){
		return String.format(complexityFormat, numPlaces, numTransitions, numRelations);
	}
	
}
