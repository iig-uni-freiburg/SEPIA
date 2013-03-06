package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import petrinet.AbstractFlowRelation;
import petrinet.AbstractMarking;
import petrinet.AbstractPetriNet;
import petrinet.AbstractPlace;
import petrinet.AbstractTransition;

import validate.ParameterException;

/**
 * This class provides methods for Petri net refinement.<br>
 * Refinement operations aim to reduce Petri nets in shape and structure without changing their behavior.
 * 
 * @author Thomas Stocker
 *
 */
public class PNRefinement {
	
	/**
	 * This method removes redundant places from the given Petri net.<br>
	 * A place is redundant when the net contains another place with exactly the same
	 * input and output transitions.<br>
	 * 
	 * @param net Petri net to refine.
	 */
	@SuppressWarnings("unchecked")
	public static <P extends AbstractPlace<F,S>, T extends AbstractTransition<F,S>, F extends AbstractFlowRelation<P,T,S>, M extends AbstractMarking<S>, S extends Object> void refine(AbstractPetriNet<P,T,F,M,S> net){
		List<P> placeList = new ArrayList<P>(net.getPlaces());
		//Build equivalence classes of places having the same relations.
		List<List<P>> eqClasses = new ArrayList<List<P>>();
		try {
			for (P place : placeList) {
				boolean inserted = false;
				for (List<P> eqClass : eqClasses) {
					if (eqClass.get(0).hasEqualRelations(place)) {
						eqClass.add(place);
						inserted = true;
						break;
					}
				}
				if (!inserted) {
					eqClasses.add(new ArrayList<P>(Arrays.asList(place)));
				}
			}
			// Find redundant places
			// -> Each equivalence class that holds more than one element
			// contains such places
			placeList.clear();
			for (List<P> eqClass : eqClasses) {
				if (eqClass.size() > 1) {
					placeList.addAll(eqClass.subList(1, eqClass.size()));
				}
			}
			// Remove redundant places.
			for (P place : placeList) {
				net.removePlace(place.getName());
			}
		} catch(ParameterException e){
			// Cannot happen, since only places of the net itself are used in computation
			e.printStackTrace();
		}
	}

}
