package de.uni.freiburg.iig.telematik.sepia.export;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTTransition;

public class PNMLSerializerAbstractPTNet<P extends AbstractPTPlace<F>, 
							 T extends AbstractPTTransition<F>, 
							 F extends AbstractPTFlowRelation<P,T>, 
							 M extends AbstractPTMarking> 

							 implements PNSerializer<AbstractPTNet<P,T,F,M>,P,T,F,M,Integer>{
	
	//TODO: Unterstützung offizieller PNML-Standard!!!
	
	private static final String ptNetFormat = "<pnml><net id=\"%s\" type=\"http://www.pnml.org/version-2009/grammar/ptnet.pntd\">%n%s</net>%n</pnml>%n";
	private final String ptTransitionFormat = "<transition id=\"%s\">%n <name><text>%s</text></name>%n</transition>%n";
	protected final String ptPlaceFormat = "<place id=\"%s\">%n <name><text>%s</text></name>%n</place>%n";
	protected final String ptPlaceWithMarkingFormat = "<place id=\"%s\">%n <name><text>%s</text></name>%n <initialMarking><text>%s</text></initialMarking></place>%n";

	
	/**
	 * Returns a PNML description of the Petri net.
	 * @return A string describing the net in the PNML format.
	 */
	@Override
	public String toString(AbstractPTNet<P, T, F, M> net) {
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
		int count = 0;
		for(F r: net.getFlowRelations())
			builder.append(r.toPNML(count++));
		builder.append(newLine);
		return String.format(ptNetFormat, net.getName(), builder.toString());
	}
	
	/**
	 * Retrieves the PNML-representation of the Petri net transition.<br>
	 * This method is abstract and requires subclasses to implement specific representations.
	 * @return The PNML-representation of the transition in String format.
	 */
	private String toString(T transition){
		return String.format(ptTransitionFormat, transition.getName(), transition.getLabel());
	}
	
	/**
	 * Retrieves the PNML-representation of the Petri net place.<br>
	 * This method is abstract and requires subclasses to implement specific representations.
	 * @param initialMarking The number of tokens in this place according to the initial marking
	 * @return The PNML-representation of the transition in String format.
	 */
	public String toString(P place, Integer initialMarking) {
		if(initialMarking == null || initialMarking < 1)
			return String.format(ptPlaceFormat, place.getName(), place.getLabel());
		return String.format(ptPlaceWithMarkingFormat, place.getName(), place.getLabel(), initialMarking);
	}

}
