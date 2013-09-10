package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.PTSerializer;
import de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException;

public class PTSerializer_PNML<P extends AbstractPTPlace<F>, 
							   T extends AbstractPTTransition<F>, 
							   F extends AbstractPTFlowRelation<P,T>, 
							   M extends AbstractPTMarking> extends PTSerializer<P,T,F,M>{
	
	private static final String ptNetFormat = "<pnml>%n<net id=\"%s\" type=\"http://www.pnml.org/version-2009/grammar/ptnet.pntd\">%n%s</net>%n</pnml>%n";
	private final String ptTransitionFormat = "<transition id=\"%s\">%n <name><text>%s</text></name>%n</transition>%n";
	protected final String ptPlaceFormat = "<place id=\"%s\">%n <name><text>%s</text></name>%n</place>%n";
	protected final String ptPlaceWithMarkingFormat = "<place id=\"%s\">%n <name><text>%s</text></name>%n <initialMarking><text>%s</text></initialMarking></place>%n";
	private final String ptArcFormat = "<arc id=\"%s\" source=\"%s\" target=\"%s\"></arc>%n";

	public PTSerializer_PNML(AbstractPetriNet<P, T, F, M, Integer> petriNet) throws ParameterException {
		super(petriNet);
	}

	@Override
	public String serialize() throws SerializationException {
		AbstractPTNet<P,T,F,M> net = getPetriNet();
		StringBuilder builder = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		for(P p: net.getPlaces())
			try {
				builder.append(toString(p, net.getInitialMarking().get(p.getName())));
			} catch (ParameterException e) {
				e.printStackTrace();
			}
		builder.append(newLine);
		for(T t: net.getTransitions())
			builder.append(toString(t));
		builder.append(newLine);
		for(F r: net.getFlowRelations())
			builder.append(toString(r));
		builder.append(newLine);
		return String.format(ptNetFormat, net.getName(), builder.toString());
	}
	
	/**
	 * Retrieves the PNML-representation of the Petri net transition.<br>
	 * @return The PNML-representation of the transition in String format.
	 */
	private String toString(T transition){
		return String.format(ptTransitionFormat, transition.getName(), transition.getLabel());
	}
	
	/**
	 * Retrieves the PNML-representation of the Petri net place.<br>
	 * @param initialMarking The number of tokens in this place according to the initial marking
	 * @return The PNML-representation of the transition in String format.
	 */
	public String toString(P place, Integer initialMarking) {
		if(initialMarking == null || initialMarking < 1)
			return String.format(ptPlaceFormat, place.getName(), place.getLabel());
		return String.format(ptPlaceWithMarkingFormat, place.getName(), place.getLabel(), initialMarking);
	}

	/**
	 * Retrieves the PNML-representation of the flow relation.<br>
	 * @return The PNML-representation of the relation in String format.
	 */
	public String toString(F relation) {
		if(relation.getDirectionPT())
			return String.format(ptArcFormat, relation.getName().toLowerCase(), relation.getPlace().getName(), relation.getTransition().getName());
		return String.format(ptArcFormat, relation.getName().toLowerCase(), relation.getTransition().getName(), relation.getPlace().getName());
	}

}
