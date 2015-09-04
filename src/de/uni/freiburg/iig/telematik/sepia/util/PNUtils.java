package de.uni.freiburg.iig.telematik.sepia.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.math.MathUtils;
import de.invation.code.toval.types.HashList;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.graph.exception.VertexNotFoundException;
import de.uni.freiburg.iig.telematik.jagal.traverse.TraversalUtils;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PNUtils {

    /**
     * Replacement string for invalid character blocks while sanitizing element
     * names.
     */
    public final static String SANITIZE_INVALID_CHARACTER_REPLACEMENT = "_";

    /**
     * Pattern to define allowed node names.
     */
    public static final Pattern XML_ID_PATTERN = Pattern.compile("^([a-zA-Z][\\w-_\\.:]*)$");

    /**
     * Pattern to define forbidden node name characters.
     */
    public static final Pattern XML_ID_FORBIDDEN_CHARACTERS = Pattern.compile("([^\\w-_:\\.]+)");

    /**
     * Transforms a collection of transitions into a set of activities by
     * choosing the ID of the given transactions as activity names.
     *
     * @param <T> Transition type.
     * @param transitions Collection of transitions to get the labels from.
     * @param includeSilentTransitions Set <code>true</code> if silent
     * transitions should be included.
     * @return A set of activity names.
     */
    public static <T extends AbstractTransition<?, ?>> Set<String> getNameSetFromTransitions(Collection<T> transitions, boolean includeSilentTransitions) {
        Validate.notNull(transitions);
        Validate.noNullElements(transitions);
        Set<String> cActivities = new HashSet<>();
        for (AbstractTransition<?, ?> t : transitions) {
            if (includeSilentTransitions || !t.isSilent()) {
                cActivities.add(t.getName());
            }
        }
        return cActivities;
    }

    /**
     * Transforms a collection of transitions into a list of activities by
     * choosing the ID of the given transactions as activity names.
     *
     * @param <T> Transition type.
     * @param transitions Collection of transitions to get the labels from.
     * @return A list of activity names.
     */
    public static <T extends AbstractTransition<?, ?>> List<String> getNameListFromTransitions(Collection<T> transitions) {
        Validate.notNull(transitions);
        Validate.noNullElements(transitions);
        List<String> cActivities = new HashList<>();
        for (AbstractTransition<?, ?> t : transitions) {
            if (!t.isSilent()) {
                cActivities.add(t.getName());
            }
        }
        return cActivities;
    }

    /**
     * Transforms a collection of transitions into a set of activities by
     * choosing the label of the given transactions as activity names.
     *
     * @param transitions Collection of transitions to get the labels from.
     * @param includeSilentTransitions Set <code>true</code> if silent
     * transitions should be included.
     * @param <T> Transition type.
     * @return A set of activity labels.
     */
    public static <T extends AbstractTransition<?, ?>> Set<String> getLabelSetFromTransitions(Collection<T> transitions, boolean includeSilentTransitions) {
        Validate.notNull(transitions);
        Validate.noNullElements(transitions);
        Set<String> cActivities = new HashSet<>();
        for (AbstractTransition<?, ?> t : transitions) {
            if (includeSilentTransitions || !t.isSilent()) {
                cActivities.add(t.getLabel());
            }
        }
        return cActivities;
    }

    public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object>
            Map<T, Set<T>>
            getAllPredecessors(AbstractPetriNet<P, T, F, M, S> net) {

        Map<T, Set<T>> predecessors = new HashMap<>();
        for (T transition : net.getTransitions()) {
            try {
                predecessors.put(transition, getPredecessors(net, transition));
            } catch (VertexNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return predecessors;
    }

    @SuppressWarnings("unchecked")
    public static <P extends AbstractPlace<F, S>, T extends AbstractTransition<F, S>, F extends AbstractFlowRelation<P, T, S>, M extends AbstractMarking<S>, S extends Object>
            Set<T>
            getPredecessors(AbstractPetriNet<P, T, F, M, S> net, T transition) throws VertexNotFoundException {
        Validate.notNull(net);
        Validate.notNull(transition);

        Set<T> predecessors = new HashSet<>();
        for (AbstractPNNode<F> predecessorNode : TraversalUtils.getPredecessorsFor(net, transition)) {
            if (predecessorNode.isTransition()) {
                predecessors.add((T) predecessorNode);
            }
        }
        return predecessors;
    }

    public static <T extends AbstractTransition<?, ?>> Set<T> getSilentTransitions(Collection<T> transitions) {
        Set<T> result = new HashSet<>();
        for (T transition : transitions) {
            if (transition.isSilent()) {
                result.add(transition);
            }
        }
        return result;
    }

    public static <T extends AbstractTransition<?, ?>> Set<T> getNonSilentTransitions(Collection<T> transitions) {
        Set<T> result = new HashSet<>();
        for (T transition : transitions) {
            if (!transition.isSilent()) {
                result.add(transition);
            }
        }
        return result;
    }

    public static PTNet getORFragment(Set<String> alternatives) {
        PTNet ptNet = new PTNet();
        String[] alt = new String[alternatives.size()];
        alternatives.toArray(alt);

        int k = alternatives.size();

        ptNet.addPlace("in");
        ptNet.addPlace("active");
        ptNet.addPlace("fin");
        ptNet.addPlace("out");

        ptNet.addTransition("start", true);
        ptNet.addTransition("end", true);
        ptNet.addTransition("no_option", true);

        ptNet.addFlowRelationPT("fin", "end");
        ptNet.addFlowRelationTP("end", "out");
        ptNet.addFlowRelationTP("start", "active");
        ptNet.addFlowRelationPT("active", "end");
        ptNet.addFlowRelationPT("in", "start");

        boolean[][] truthTable = MathUtils.getTruthTable(k);

        for (int i = 1; i <= k; i++) {
            String alternative = alt[i - 1];
            ptNet.addPlace("p" + i);
            ptNet.addPlace("p_" + alternative);
            ptNet.addPlace("p_not_" + alternative);

            ptNet.addTransition(alternative);
            ptNet.addTransition("Not " + alternative, true);

            ptNet.addFlowRelationTP("start", "p" + i);
            ptNet.addFlowRelationPT("p" + i, alternative);
            ptNet.addFlowRelationPT("p" + i, "Not " + alternative);
            ptNet.addFlowRelationTP(alternative, "p_" + alternative);
            ptNet.addFlowRelationTP("Not " + alternative, "p_not_" + alternative);
            ptNet.addFlowRelationPT("p_not_" + alternative, "no_option");
            ptNet.addFlowRelationTP("no_option", "p" + i);
        }

        for (int j = 0; j < Math.pow(2, k) - 1; j++) {
            String optionTransitionName = "Option_" + (j + 1);
            ptNet.addTransition(optionTransitionName, true);
            for (int l = 0; l < k; l++) {
                if (truthTable[l][j]) {
                    ptNet.addFlowRelationPT("p_" + alt[l], optionTransitionName);
                } else {
                    ptNet.addFlowRelationPT("p_not_" + alt[l], optionTransitionName);
                }
            }
            ptNet.addFlowRelationTP(optionTransitionName, "fin");
        }

        PTMarking marking = new PTMarking();
        marking.set("in", 1);
        ptNet.setInitialMarking(marking);

        return ptNet;
    }

    /**
     * Sanitizes element names by the XML ID datatype standard. Names must start
     * with a character of the range [a-zA-Z] and must only contain alphanumeric
     * characters and the symbols <code>-_.:</code>.
     *
     * @param name Name to sanitize.
     * @param leadingCharacters String to prepend to the name if it has not a
     * valid beginning.
     * @return Sanitized element name.
     */
    public static String sanitizeElementName(String name, String leadingCharacters) {
        Validate.notEmpty(leadingCharacters);

        // replace forbidden characters by "_"
        name = name.replaceAll(XML_ID_FORBIDDEN_CHARACTERS.pattern(), SANITIZE_INVALID_CHARACTER_REPLACEMENT);
        // check if first element is in range [a-zA-Z]
        if (name.length() == 0 || !name.substring(0, 1).matches("^[a-zA-Z]$")) {
            name = leadingCharacters + name;
        }
        return name;
    }

    /**
     * Validates a given element name.
     *
     * @param name The name for the Petri net node. It must start with a
     * character of the range [a-zA-Z] and must only contain alphanumerical
     * characters and the symbols <code>-_.:</code>.
     */
    public static void validateElementName(String name) {
        Matcher nameMatcher = XML_ID_PATTERN.matcher(name);
        if (!nameMatcher.matches()) {
            throw new ParameterException("Given name \"" + name + "\" is not according to the guidelines. Element names must match the pattern \"" + XML_ID_PATTERN + "\".");
        }
    }
}
