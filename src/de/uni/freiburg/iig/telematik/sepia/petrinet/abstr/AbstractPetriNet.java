package de.uni.freiburg.iig.telematik.sepia.petrinet.abstr;

import de.invation.code.toval.misc.NamedComponent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.jagal.traverse.Traversable;
import de.uni.freiburg.iig.telematik.sepia.event.CapacityEvent;
import de.uni.freiburg.iig.telematik.sepia.event.FlowRelationListener;
import de.uni.freiburg.iig.telematik.sepia.event.MarkingChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.PNMarkingListener;
import de.uni.freiburg.iig.telematik.sepia.event.PNMarkingListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.event.PNStructureListener;
import de.uni.freiburg.iig.telematik.sepia.event.PNStructureListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.event.PlaceChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.PlaceListener;
import de.uni.freiburg.iig.telematik.sepia.event.RelationChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.RelationConstraintEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TokenEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TokenListener;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionChangeEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionListener;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.PNComplexity;
import java.util.regex.Pattern;

/**
 * Abstract class for defining Petri nets and their properties.<br>
 * <br>
 * It contains major properties like the places, transitions and relations of a
 * Petri net and provides methods for adding/removing them.<br>
 * <br>
 * It keeps track of the nets' state by remembering the marking of the net which
 * describes the number and kind of tokens contained in net places. The net
 * state changes when transitions fire. Using listeners, the net reacts on
 * changes of transitions and places that belong to the net.
 * <br>
 * It also keeps track of enabled transitions, transition and place
 * sources/drains. A transition is enabled if all its precondition hold.<br>
 * Preconditions are defined in the sense of constraints on the number and
 * kind<br>
 * of tokens contained in places that have outgoing relations to the
 * transition.<br>
 * In case an enabled transition fires, it removes tokens from incoming places
 * and puts tokens into outgoing places.
 * <br>
 * This class allows to fire an enabled transition.<br>
 * In case there is more than one transition, the FlowControl determines the
 * transition to fire.
 *
 * @author Thomas Stocker
 *
 * @param <P> Type of Petri net places
 * @param <T> Type of Petri net transitions
 * @param <F> Type of Petri net relations
 * @param <M> Type of Petri net markings
 * @param <S> Type of Petri net place states
 */
public abstract class AbstractPetriNet< P extends AbstractPlace<F, S>,
                                        T extends AbstractTransition<F, S>,
                                        F extends AbstractFlowRelation<P, T, S>,
                                        M extends AbstractMarking<S>,
                                        S extends Object>
        implements  TransitionListener<AbstractTransition<F, S>>,
                    TokenListener<AbstractPlace<F, S>>,
                    Traversable<AbstractPNNode<F>>,
                    FlowRelationListener<AbstractFlowRelation<P, T, S>>,
                    PlaceListener<AbstractPlace<F, S>>,
                    PNStructureListener<P, T, F, M, S>,
                    PNMarkingListener<S, M>,
                    Serializable,
                    NamedComponent {

    private static final long serialVersionUID = 7324151598039390349L;

    /**
     * Pattern to define allowed node names.
     */
    public static final Pattern XML_ID_PATTERN = Pattern.compile("^([a-zA-Z][\\w-_\\.:]*)$");

    /**
     * Pattern to define forbidden node name characters.
     */
    public static final Pattern XML_ID_FORBIDDEN_CHARACTERS = Pattern.compile("([^\\w-_:\\.]+)");

    /**
     * Name of the Petri net.
     */
    protected String name = "PetriNet";
    /**
     * Map that contains Petri net places indexed with their names.
     */
    protected Map<String, P> places = new HashMap<>();
    /**
     * Map that contains Petri net places without incoming relations.
     */
    protected Map<String, P> sourcePlaces = new HashMap<>();
    /**
     * Map that contains Petri net places without outgoing relations.
     */
    protected Map<String, P> drainPlaces = new HashMap<>();
    /**
     * Map that contains Petri net transitions indexed with their names.
     */
    protected Map<String, T> transitions = new HashMap<>();
    /**
     * Map that contains Petri net transitions without incoming relations.
     */
    protected Map<String, T> sourceTransitions = new HashMap<>();
    /**
     * Map that contains Petri net transitions without outgoing relations.
     */
    protected Map<String, T> drainTransitions = new HashMap<>();
    /**
     * Map that contains Petri net relations indexed with their names.
     */
    protected Map<String, F> relations = new HashMap<>();
    /**
     * A list that contains all enabled transitions.<br>
     */
    protected List<T> enabledTransitions = new ArrayList<>();
    /**
     * The last fired transition.
     */
    protected T lastFiredTransition = null;
    /**
     * The initial marking of the Petri net.<br>
     * This marking defines the initial state of a Petri net, before the firing
     * of any transitions.
     */
    protected M initialMarking = null;
    /**
     * The actual marking of the Petri net.<br>
     * This marking changes whenever transitions fire and tokens are added to or
     * removed from net places.
     */
    protected M marking = null;

    protected PNStructureListenerSupport<P, T, F, M, S> structureListenerSupport = new PNStructureListenerSupport<>();

    protected PNMarkingListenerSupport<S, M> markingListenerSupport = new PNMarkingListenerSupport<>();

	//------- Constructors --------------------------------------------------------------------------
    /**
     * Creates a new Petri net, using a DefaultFlow as flow control.<br>
     * The next enabled transition is chosen randomly.
     */
    public AbstractPetriNet() {
        initialize();
        initialMarking = createNewMarking();
        marking = createNewMarking();
        structureListenerSupport.addListener(this);
    }

    /**
     * Creates a new Petri net using a DefaultFlow as flow control.<br>
     * The next enabled transition is chosen randomly.
     *
     * @param places Names of Petri net places to add.
     * @param transitions Names of Petri net transition to add.
     * @throws ParameterException If some parameters are <code>null</code> or
     * contain <code>null</code>-values.
     */
    public AbstractPetriNet(Set<String> places, Set<String> transitions) {
        this();
        addTransitions(transitions);
        addPlaces(places);
    }

    /**
     * Initialization method for basic properties and fields. <br>
     * Called by default constructor.
     */
    protected void initialize() {
    }

    ;
	
	
	//------- Basic properties ----------------------------------------------------------------------
	
	/**
	 * Returns the name of the Petri net.
	 * @return The name of the Petri net.
	 */
        @Override
    public final String getName() {
        return name;
    }

    /**
     * Sets the name of the Petri net to the given name.
     *
     * @param name New name for the Petri net.
     * @throws ParameterException If the given name is <code>null</code>.
     */
    @Override
    public final void setName(String name) {
        Validate.notNull(name);

        this.name = name;
    }

    /**
     * Checks if the Petri net has enabled transitions that are ready to fire.
     *
     * @return <code>true</code> if the net has enabled transitions;<br>
     * <code>false</code> otherwise.
     */
    public boolean hasEnabledTransitions() {
        return enabledTransitions.size() > 0;
    }

    /**
     * Returns a list of all enabled transitions.
     *
     * @return A list of enabled transitions.
     */
    public List<T> getEnabledTransitions() {
        return Collections.unmodifiableList(enabledTransitions);
    }

    /**
     * Returns the last fired transition.<br>
     *
     * @return The last fired transition or <code>null</code> if no transition
     * was fired so far.
     */
    public T getLastFiredTransition() {
        return lastFiredTransition;
    }

    public abstract NetType getNetType();

    public boolean isEmpty() {
        return places.isEmpty() && transitions.isEmpty() && relations.isEmpty();
    }

    public abstract Class<?> getMarkingGraphClass();

	//------- Transitions ------------------------------------------------------------------------
    /**
     * Returns the Petri net transitions.
     *
     * @return A collection of Petri net transitions.
     */
    public Collection<T> getTransitions() {
        return Collections.unmodifiableCollection(transitions.values());
    }

    public int getTransitionCount() {
        return transitions.size();
    }

    public Collection<T> getTransitions(boolean includeSilentTransitions) {
        if (includeSilentTransitions) {
            return getTransitions();
        }
        List<T> result = new ArrayList<>(getTransitions());
        result.removeAll(getSilentTransitions());
        return result;
    }

    public Collection<T> getSilentTransitions() {
        List<T> result = new ArrayList<>();
        for (T transition : transitions.values()) {
            if (transition.isSilent()) {
                result.add(transition);
            }
        }
        return result;
    }

    /**
     * Returns the Petri net source transitions.
     *
     * @return A collection of Petri net transitions.
     */
    public Collection<T> getSourceTransitions() {
        return Collections.unmodifiableCollection(sourceTransitions.values());
    }

    /**
     * Returns the Petri net drain transitions.
     *
     * @return A collection of Petri net transitions.
     */
    public Collection<T> getDrainTransitions() {
        return Collections.unmodifiableCollection(drainTransitions.values());
    }

    /**
     * Returns the transition with the given name.
     *
     * @param transitionName The name of the desired transition.
     * @return The Petri net transition with the given name<br>
     * or <code>null</code> if the net does not contain a transition with the
     * given name.
     */
    public T getTransition(String transitionName) {
        return transitions.get(transitionName);
    }

    /**
     * Returns all transitions with the given label.
     *
     * @param label The label of all desired transitions.
     * @return A set of all transitions with the given label.
     */
    public Set<T> getTransitions(String label) {
        Set<T> result = new HashSet<>();
        for (T transition : transitions.values()) {
            if (transition.getLabel().equals(label)) {
                result.add(transition);
            }
        }
        return result;
    }

    /**
     * Adds transitions with the given names to the Petri net.<br>
     * Transitions names have to be unique. In case the net already contains
     * transitions with given names, less transitions than the given number of
     * arguments may be added to the Petri net.<br>
     * This method calls {@link #addTransition(String)} for each transition
     * name.
     *
     * @param transitionNames Names for the Petri net transitions.
     * @return <code>true</code> if at least one transition was successfully
     * added;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the set of transition names is
     * <code>null</code> or some transition names are <code>null</code>.
     * @see #addTransition(String)
     */
    public final boolean addTransitions(Collection<String> transitionNames) {
        return addTransitions(transitionNames, true);
    }

    /**
     * Adds transitions with the given names to the Petri net.<br>
     * Transitions names have to be unique. In case the net already contains
     * transitions with given names, less transitions than the given number of
     * arguments may be added to the Petri net.<br>
     * This method calls {@link #addTransition(String)} for each transition
     * name.
     *
     * @param transitionNames Names for the Petri net transitions.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if at least one transition was successfully
     * added;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the set of transition names is
     * <code>null</code> or some transition names are <code>null</code>.
     * @see #addTransition(String)
     */
    public final boolean addTransitions(Collection<String> transitionNames, boolean notifyListeners) {
        Validate.notNull(transitionNames);
        boolean updated = false;
        for (String transitionName : transitionNames) {
            if (addTransition(transitionName, notifyListeners)) {
                updated = true;
            }
        }
        return updated;
    }

    /**
     * Adds a transition with the given name to the Petri net.<br>
     * Transitions names have to be unique. In case the net already contains a
     * transition with the given name, no transition is added to the net.
     *
     * @param transitionName The name for the Petri net transition.
     * @return <code>true</code> if the transition was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the transition name is <code>null</code>.
     */
    public boolean addTransition(String transitionName) {
        return addTransition(transitionName, true);
    }

    /**
     * Adds a transition with the given name to the Petri net.<br>
     * Transitions names have to be unique. In case the net already contains a
     * transition with the given name, no transition is added to the net.
     *
     * @param transitionName The name for the Petri net transition.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the transition was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the transition name is <code>null</code>.
     */
    public boolean addTransition(String transitionName, boolean notifyListeners) {
        return addTransition(transitionName, transitionName, notifyListeners);
    }

    /**
     * Adds a transition with the given name and label to the Petri net.<br>
     * Transitions names have to be unique. In case the net already contains a
     * transition with the given name, no transition is added to the net.
     *
     * @param transitionName The name for the Petri net transition.
     * @param transitionLabel the label for the Petri net transition.
     * @return <code>true</code> if the transition was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the transition name is <code>null</code>.
     */
    public boolean addTransition(String transitionName, String transitionLabel) {
        return addTransition(transitionName, transitionLabel, true);
    }

    /**
     * Adds a transition with the given name and label to the Petri net.<br>
     * Transitions names have to be unique. In case the net already contains a
     * transition with the given name, no transition is added to the net.
     *
     * @param transitionName The name for the Petri net transition.
     * @param transitionLabel the label for the Petri net transition.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the transition was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the transition name is <code>null</code>.
     */
    public boolean addTransition(String transitionName, String transitionLabel, boolean notifyListeners) {
        if (containsTransition(transitionName)) {
            return false;
        }
        return addTransition(createNewTransition(transitionName, transitionLabel, false), notifyListeners);
    }

    /**
     * Adds a transition with the given name and silent-state to the Petri
     * net.<br>
     * Transitions names have to be unique. In case the net already contains a
     * transition with the given name, no transition is added to the net.
     *
     * @param transitionName The name for the Petri net transition.
     * @return <code>true</code> if the transition was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the transition name is <code>null</code>.
     */
    public boolean addSilentTransition(String transitionName) {
        return addSilentTransition(transitionName, true);
    }

    /**
     * Adds a transition with the given name and silent-state to the Petri
     * net.<br>
     * Transitions names have to be unique. In case the net already contains a
     * transition with the given name, no transition is added to the net.
     *
     * @param transitionName The name for the Petri net transition.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the transition was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the transition name is <code>null</code>.
     */
    public boolean addSilentTransition(String transitionName, boolean notifyListeners) {
        return addSilentTransition(transitionName, transitionName, notifyListeners);
    }

    /**
     * Adds a transition with the given name, label and silent-state to the
     * Petri net.<br>
     * Transitions names have to be unique. In case the net already contains a
     * transition with the given name, no transition is added to the net.
     *
     * @param transitionName The name for the Petri net transition.
     * @param transitionLabel the label for the Petri net transition.
     * @return <code>true</code> if the transition was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the transition name is <code>null</code>.
     */
    public boolean addSilentTransition(String transitionName, String transitionLabel) {
        return addSilentTransition(transitionName, transitionLabel, true);
    }

    /**
     * Adds a transition with the given name, label and silent-state to the
     * Petri net.<br>
     * Transitions names have to be unique. In case the net already contains a
     * transition with the given name, no transition is added to the net.
     *
     * @param transitionName The name for the Petri net transition.
     * @param transitionLabel the label for the Petri net transition.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the transition was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the transition name is <code>null</code>.
     */
    public boolean addSilentTransition(String transitionName, String transitionLabel, boolean notifyListeners) {
        if (containsTransition(transitionName)) {
            return false;
        }
        return addTransition(createNewTransition(transitionName, transitionLabel, true), notifyListeners);
    }

    /**
     * Protected method for adding a transition of type <code>T</code> to the
     * transition map.<br>
     * This method is called from {@link #addTransition(String)} which checks if
     * there already exists a transition with equal name before making the call.
     *
     * @param transition The Petri net transition to add.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if transition has been added successvully.
     * @throws ParameterException If the given transition is <code>null</code>.
     */
    protected boolean addTransition(T transition, boolean notifyListeners) {
        Validate.notNull(transition);
        if (containsTransition(transition)) {
            return false;
        }
        transitions.put(transition.getName(), transition);
        transition.addTransitionListener(this);
        enabledTransitions.add(transition);
        if (notifyListeners) {
            structureListenerSupport.notifyTransitionAdded(transition);
        }
        return true;
    }

    /**
     * Checks if the Petri net contains the given transition.
     *
     * @param transition The transition to check.
     * @return <code>true</code> if the net contains the transition;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the given transition is <code>null</code>.
     */
    protected boolean containsTransition(T transition) {
        return containsTransition(transition.getName());
    }

    /**
     * Checks if the Petri net contains a transition with the given name.
     *
     * @param transitionName The name of the transition to check.
     * @return <code>true</code> if the net contains a transition with the given
     * name;<br>
     * <code>false</code> otherwise.
     */
    public boolean containsTransition(String transitionName) {
        return transitions.keySet().contains(transitionName);
    }

    /**
     * Removes the transition with the given name from the Petri net.<br>
     * On removing a transition, all corresponding relations are removed as
     * well.
     *
     * @param transitionName The name of the place to remove
     * @return <code>true</code> if the transition was successfully removed from
     * the net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the given transition name is
     * <code>null</code>.
     */
    public boolean removeTransition(String transitionName) {
        return removeTransition(transitionName, true);
    }

    /**
     * Removes the transition with the given name from the Petri net.<br>
     * On removing a transition, all corresponding relations are removed as
     * well.
     *
     * @param transitionName The name of the place to remove
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the transition was successfully removed from
     * the net;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the given transition name is
     * <code>null</code>.
     */
    public boolean removeTransition(String transitionName, boolean notifyListeners) {
        if (!containsTransition(transitionName)) {
            return false;
        }
        return removeTransition(transitions.get(transitionName), notifyListeners);
    }

    protected boolean removeTransition(T transition, boolean notifyListeners) {
        if (!containsTransition(transition)) {
            return false;
        }
        int affectedRelations = transition.degree();
        for (F relation : transition.getIncomingRelations()) {
            removeFlowRelation(relation, notifyListeners);
        }
        for (F relation : transition.getOutgoingRelations()) {
            removeFlowRelation(relation, notifyListeners);
        }
        transition.removeTransitionListener(this);
        enabledTransitions.remove(transition);
        transitions.remove(transition.getName());
        sourceTransitions.remove(transition.getName());
        drainTransitions.remove(transition.getName());
        if (notifyListeners) {
            structureListenerSupport.notifyTransitionRemoved(transition, affectedRelations);
        }
        return true;
    }

    /**
     * Creates a new transition of type <code>T</code> with the given name.<br>
     * This method is abstract because only subclasses know the type
     * <code>T</code> of their transitions.
     *
     * @param name The name for the new transition.
     * @param label The label for the new transition.
     * @param isSilent Set <code>true</code> if new transition should be silent.
     * @return A new transition of type <code>T</code> with the given name.
     */
    protected abstract T createNewTransition(String name, String label, boolean isSilent);

	//------- Places ---------------------------------------------------------------------------------
    /**
     * Returns all Petri net places.
     *
     * @return A collection of all Petri net places.
     */
    public Collection<P> getPlaces() {
        return Collections.unmodifiableCollection(places.values());
    }

    public int getPlaceCount() {
        return places.size();
    }

    /**
     * Returns all Petri net source places.
     *
     * @return A collection of Petri net places.
     */
    public Collection<P> getSourcePlaces() {
        return Collections.unmodifiableCollection(sourcePlaces.values());
    }

    /**
     * Returns all Petri net drain places.
     *
     * @return A collection of Petri net places.
     */
    public Collection<P> getDrainPlaces() {
        return Collections.unmodifiableCollection(drainPlaces.values());
    }

    /**
     * Returns the net place with the given name.
     *
     * @param placeName The name of the desired place.
     * @return The Petri net place with the given name<br>
     * or <code>null</code> if the net does not contain a place with the given
     * name.
     */
    public P getPlace(String placeName) {
        return places.get(placeName);
    }

    /**
     * Adds places with the given names to the Petri net.<br>
     * Place names have to be unique. In case the net already contains places
     * with given names, less places than the given number of arguments may be
     * added to the Petri net.<br>
     * This method calls {@link #addPlace(String)} for each place name.
     *
     * @param placeNames Names for the Petri net places to add.
     * @return <code>true</code> if at least one place was successfully
     * added;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the set of place names is <code>null</code>
     * or some place names are <code>null</code>.
     * @see #addPlace(String)
     */
    public final boolean addPlaces(Set<String> placeNames) {
        return addPlaces(placeNames, true);
    }

    /**
     * Adds places with the given names to the Petri net.<br>
     * Place names have to be unique. In case the net already contains places
     * with given names, less places than the given number of arguments may be
     * added to the Petri net.<br>
     * This method calls {@link #addPlace(String)} for each place name.
     *
     * @param placeNames Names for the Petri net places to add.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if at least one place was successfully
     * added;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the set of place names is <code>null</code>
     * or some place names are <code>null</code>.
     * @see #addPlace(String)
     */
    public final boolean addPlaces(Set<String> placeNames, boolean notifyListeners) {
        Validate.notNull(placeNames);
        boolean updated = false;
        for (String placeName : placeNames) {
            if (addPlace(placeName, notifyListeners)) {
                updated = true;
            }
        }
        return updated;
    }

    /**
     * Adds a place with the given name to the Petri net.<br>
     * Place names have to be unique. In case the net already contains a place
     * with the given name, no place is added to the net.
     *
     * @param placeName The name for the Petri net place.
     * @return <code>true</code> if the place was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     */
    public boolean addPlace(String placeName) {
        return addPlace(placeName, true);
    }

    /**
     * Adds a place with the given name to the Petri net.<br>
     * Place names have to be unique. In case the net already contains a place
     * with the given name, no place is added to the net.
     *
     * @param placeName The name for the Petri net place.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the place was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     */
    public boolean addPlace(String placeName, boolean notifyListeners) {
        return addPlace(placeName, placeName, notifyListeners);
    }

    /**
     * Adds a place with the given name to the Petri net.<br>
     * Place names have to be unique. In case the net already contains a place
     * with the given name, no place is added to the net. Place labels do not
     * have to be unique.
     *
     * @param placeName The name for the Petri net place.
     * @param placeLabel The label for the Petri net place.
     * @return <code>true</code> if the place was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     */
    public boolean addPlace(String placeName, String placeLabel) {
        return addPlace(placeName, placeLabel, true);
    }

    /**
     * Adds a place with the given name to the Petri net.<br>
     * Place names have to be unique. In case the net already contains a place
     * with the given name, no place is added to the net. Place labels do not
     * have to be unique.
     *
     * @param placeName The name for the Petri net place.
     * @param placeLabel The label for the Petri net place.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the place was successfully added to the
     * net;<br>
     * <code>false</code> otherwise.
     */
    public boolean addPlace(String placeName, String placeLabel, boolean notifyListeners) {
        if (containsPlace(placeName)) {
            return false;
        }
        return addPlace(createNewPlace(placeName, placeLabel), notifyListeners);
    }

    /**
     * Protected method for adding a place of type <code>P</code> to the place
     * map.<br>
     * This method is called from {@link #addPlace(String)} which checks if
     * there already exists a place with equal name before making the call.
     *
     * @param place The Petri net place to add.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if place has been added successfully.
     * @throws ParameterException If the given place is <code>null</code>.
     */
    protected boolean addPlace(P place, boolean notifyListeners) {
        Validate.notNull(place);
        if (containsPlace(place)) {
            return false;
        }
        places.put(place.getName(), place);
        place.addTokenListener(this);
        place.addPlaceListener(this);
        if (notifyListeners) {
            structureListenerSupport.notifyPlaceAdded(place);
        }
        return true;
    }

    /**
     * Checks if the Petri net contains the given place.
     *
     * @param place The place to check.
     * @return <code>true</code> if the net contains the transition;<br>
     * <code>false</code> otherwise.
     */
    protected boolean containsPlace(P place) {
        return containsPlace(place.getName());
    }

    /**
     * Checks if the Petri net contains a place with the given name.
     *
     * @param placeName The name of the place to check.
     * @return <code>true</code> if the net contains a place with the given
     * name;<br>
     * <code>false</code> otherwise.
     */
    public boolean containsPlace(String placeName) {
        return places.keySet().contains(placeName);
    }

    /**
     * Creates a new place of type <code>P</code> with the given name.<br>
     * This method is abstract because only subclasses know the type
     * <code>P</code> of their places.
     *
     * @param name The name for the new place.
     * @param label The label for the new place.
     * @return A new place of type <code>P</code> with the given name.
     */
    protected abstract P createNewPlace(String name, String label);

    /**
     * Removes the place with the given name from the Petri net.<br>
     * On removing a place, all corresponding relations are removed as well.
     *
     * @param placeName The name of the place to remove
     * @return <code>true</code> if the place was successfully removed from the
     * net;<br>
     * <code>false</code> otherwise.
     */
    public boolean removePlace(String placeName) {
        return removePlace(placeName, true);
    }

    /**
     * Removes the place with the given name from the Petri net.<br>
     * On removing a place, all corresponding relations are removed as well.
     *
     * @param placeName The name of the place to remove
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the place was successfully removed from the
     * net;<br>
     * <code>false</code> otherwise.
     */
    public boolean removePlace(String placeName, boolean notifyListeners) {
        if (!containsPlace(placeName)) {
            return false;
        }
        return removePlace(places.get(placeName), notifyListeners);
    }

    protected boolean removePlace(P place, boolean notifyListeners) {
        if (!containsPlace(place)) {
            return false;
        }
        int affectedRelations = place.degree();
        for (F relation : place.getIncomingRelations()) {
            removeFlowRelation(relation, notifyListeners);
        }
        for (F relation : place.getOutgoingRelations()) {
            removeFlowRelation(relation, notifyListeners);
        }
        place.removePlaceListener(this);
        place.removeTokenListener(this);
        places.remove(place.getName());
        sourcePlaces.remove(place.getName());
        drainPlaces.remove(place.getName());
        initialMarking.remove(place.getName());
        marking.remove(place.getName());
        if (notifyListeners) {
            structureListenerSupport.notifyPlaceRemoved(place, affectedRelations);
        }
        return true;
    }

	//------- Flow Relations ------------------------------------------------------------------------
    public F getFlowRelation(String name) {
        return relations.get(name);
    }

    /**
     * Returns the flow relations of the Petri net.
     *
     * @return A collection of flow relations.
     */
    public Collection<F> getFlowRelations() {
        return Collections.unmodifiableCollection(relations.values());
    }

    public int getFlowRelationCount() {
        return relations.size();
    }

    /**
     * Adds a flow relation starting at a place and ending at a transition.
     *
     * @param placeName The name of the place where toe relation starts.
     * @param transitionName The name of the transition where the relation ends.
     * @param constraint The constraint for the flow relation.
     * @return <code>true</code> if the flow relation was successfully
     * added;<br>
     * <code>false</code> otherwise.
     */
    public F addFlowRelationPT(String placeName, String transitionName, S constraint) {
        return addFlowRelationPT(placeName, transitionName, constraint, true);
    }

    /**
     * Adds a flow relation starting at a place and ending at a transition.
     *
     * @param placeName The name of the place where toe relation starts.
     * @param transitionName The name of the transition where the relation ends.
     * @param constraint The constraint for the flow relation.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the flow relation was successfully
     * added;<br>
     * <code>false</code> otherwise.
     */
    public F addFlowRelationPT(String placeName, String transitionName, S constraint, boolean notifyListeners) {
        validatePlace(placeName);
        validateTransition(transitionName);
        if (containsRelationPT(placeName, transitionName)) {
            return null;
        }
        F newFlowRelation = createNewFlowRelation(places.get(placeName), transitions.get(transitionName), constraint);
        if (addFlowRelation(newFlowRelation, notifyListeners)) {
            return newFlowRelation;
        }
        return null;
    }

    /**
     * Adds a flow relation starting at a place and ending at a transition.
     *
     * @param placeName The name of the place where toe relation starts.
     * @param transitionName The name of the transition where the relation ends.
     * @return <code>true</code> if the flow relation was successfully
     * added;<br>
     * <code>false</code> otherwise.
     */
    public F addFlowRelationPT(String placeName, String transitionName) {
        return addFlowRelationPT(placeName, transitionName, true);
    }

    /**
     * Adds a flow relation starting at a place and ending at a transition.
     *
     * @param placeName The name of the place where toe relation starts.
     * @param transitionName The name of the transition where the relation ends.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the flow relation was successfully
     * added;<br>
     * <code>false</code> otherwise.
     */
    public F addFlowRelationPT(String placeName, String transitionName, boolean notifyListeners) {
        validatePlace(placeName);
        validateTransition(transitionName);
        if (containsRelationPT(placeName, transitionName)) {
            return null;
        }
        F newFlowRelation = createNewFlowRelation(places.get(placeName), transitions.get(transitionName));
        if (addFlowRelation(newFlowRelation, notifyListeners)) {
            return newFlowRelation;
        }
        return null;
    }

    /**
     * Adds a flow relation starting at a transition and ending at a place.
     *
     * @param transitionName The name of the transition where the relation ends.
     * @param placeName The name of the place where toe relation starts.
     * @param constraint Constraint of the flow relation.
     * @return <code>true</code> if the flow relation was successfully
     * added;<br>
     * <code>false</code> otherwise.
     */
    public F addFlowRelationTP(String transitionName, String placeName, S constraint) {
        return addFlowRelationTP(transitionName, placeName, constraint, true);
    }

    /**
     * Adds a flow relation starting at a transition and ending at a place.
     *
     * @param transitionName The name of the transition where the relation ends.
     * @param placeName The name of the place where toe relation starts.
     * @param constraint Constraint of the flow relation.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the flow relation was successfully
     * added;<br>
     * <code>false</code> otherwise.
     */
    public F addFlowRelationTP(String transitionName, String placeName, S constraint, boolean notifyListeners) {
        validatePlace(placeName);
        validateTransition(transitionName);
        if (containsRelationTP(transitionName, placeName)) {
            return null;
        }
        F newFlowRelation = createNewFlowRelation(transitions.get(transitionName), places.get(placeName), constraint);
        if (addFlowRelation(newFlowRelation, notifyListeners)) {
            return newFlowRelation;
        }

        return null;
    }

    /**
     * Adds a flow relation starting at a transition and ending at a place.
     *
     * @param transitionName The name of the transition where the relation ends.
     * @param placeName The name of the place where toe relation starts.
     * @return <code>true</code> if the flow relation was successfully
     * added;<br>
     * <code>false</code> otherwise.
     */
    public F addFlowRelationTP(String transitionName, String placeName) {
        return addFlowRelationTP(transitionName, placeName, true);
    }

    /**
     * Adds a flow relation starting at a transition and ending at a place.
     *
     * @param transitionName The name of the transition where the relation ends.
     * @param placeName The name of the place where toe relation starts.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if the flow relation was successfully
     * added;<br>
     * <code>false</code> otherwise.
     */
    public F addFlowRelationTP(String transitionName, String placeName, boolean notifyListeners) {
        validatePlace(placeName);
        validateTransition(transitionName);
        if (containsRelationTP(transitionName, placeName)) {
            return null;
        }
        F newFlowRelation = createNewFlowRelation(transitions.get(transitionName), places.get(placeName));
        if (addFlowRelation(newFlowRelation, notifyListeners)) {
            return newFlowRelation;
        }
        return null;
    }

    /**
     * Protected method for adding a relation of type <code>F</code> to the
     * relation map.<br>
     * This method is called from {@link #addFlowRelationPT(String, String)} and
     * {@link #addFlowRelationTP(String, String)} which both check if there
     * already exists a relation with equal name before making the call.<br>
     * They also ensure that the place/transition of the relation are
     * places/transitions of the net.
     *
     * @param relation The Petri net relation to add.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified.
     * @return <code>true</code> if flow relation has been added successfully.
     * @see #checkSD(AbstractPlace)
     * @see #checkSD(AbstractTransition)
     */
    protected boolean addFlowRelation(F relation, boolean notifyListeners) {
        Validate.notNull(relation);
        if (containsFlowRelation(relation)) {
            return false;
        }

        if (relation.getDirectionPT()) {
            relation.getPlace().addOutgoingRelation(relation);
            relation.getTransition().addIncomingRelation(relation);
        } else {
            relation.getTransition().addOutgoingRelation(relation);
            relation.getPlace().addIncomingRelation(relation);
        }
        relation.getTransition().checkState();
        relations.put(relation.getName(), relation);
        checkSD(relation.getPlace());
        checkSD(relation.getTransition());
        relation.addRelationListener(this);
        if (notifyListeners) {
            structureListenerSupport.notifyRelationAdded(relation);
        }
        return true;
    }

    public boolean containsFlowRelation(String relationName) {
        return relations.keySet().contains(relationName);
    }

    protected boolean containsFlowRelation(F relation) {
        for (F existingRelation : getFlowRelations()) {
            if (existingRelation.equals(relation)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsRelationPT(String placeName, String transitionName) {
        validatePlace(placeName);
        validateTransition(transitionName);
        for (F outgoingRelation : getPlace(placeName).getOutgoingRelations()) {
            if (outgoingRelation.getTransition().getName().equals(transitionName)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsRelationTP(String transitionName, String placeName) {
        validatePlace(placeName);
        validateTransition(transitionName);
        for (F outgoingRelation : getTransition(transitionName).getOutgoingRelations()) {
            if (outgoingRelation.getPlace().getName().equals(placeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the source and drain property of the given place<br>
     * and adds/removes it from the list of source/drain places.
     *
     * @param place The Petri net place to check.
     */
    private void checkSD(P place) {
        if (place.isSource()) {
            sourcePlaces.put(place.getName(), place);
        } else {
            sourcePlaces.remove(place.getName());
        }
        if (place.isDrain()) {
            drainPlaces.put(place.getName(), place);
        } else {
            drainPlaces.remove(place.getName());
        }
    }

    /**
     * Checks the source and drain property of the given transition<br>
     * and adds/removes it from the list of source/drain transitions.
     *
     * @param transition The Petri net transition to check.
     */
    private void checkSD(T transition) {
        if (transition.isSource()) {
            sourceTransitions.put(transition.getName(), transition);
        } else {
            sourceTransitions.remove(transition.getName());
        }
        if (transition.isDrain()) {
            drainTransitions.put(transition.getName(), transition);
        } else {
            drainTransitions.remove(transition.getName());
        }
    }

    /**
     * Checks if the Petri net contains the given relation.
     *
     * @param relation The relation to check.
     * @return <code>true</code> if the net contains the relation;<br>
     * <code>false</code> otherwise.
     */
    protected boolean containsRelation(F relation) {
        return relations.values().contains(relation);
    }

    /**
     * Creates a new relation of type <code>F</code> from the given place to the
     * given transition. This method is abstract because only subclasses know
     * the type <code>F</code> of their relations.
     *
     * @param place The place where the relation starts.
     * @param transition The transition where the relation ends.
     * @param constraint Constraint of the new flow relation.
     * @return A new relation of type <code>F</code>.
     */
    protected abstract F createNewFlowRelation(P place, T transition, S constraint);

    /**
     * Creates a new relation of type <code>F</code> from the given transition
     * to the given place. This method is abstract because only subclasses know
     * the type <code>F</code> of their relations.
     *
     * @param transition The transition where the relation starts
     * @param place The place where the relation ends.
     * @param constraint Constraint of the new flow relation.
     * @return A new relation of type <code>F</code>.
     */
    protected abstract F createNewFlowRelation(T transition, P place, S constraint);

    /**
     * Creates a new relation of type <code>F</code> from the given place to the
     * given transition. This method is abstract because only subclasses know
     * the type <code>F</code> of their relations.
     *
     * @param place The place where the relation starts.
     * @param transition The transition where the relation ends.
     * @return A new relation of type <code>F</code>.
     */
    protected abstract F createNewFlowRelation(P place, T transition);

    /**
     * Creates a new relation of type <code>F</code> from the given transition
     * to the given place. This method is abstract because only subclasses know
     * the type <code>F</code> of their relations.
     *
     * @param transition The transition where the relation starts
     * @param place The place where the relation ends.
     * @return A new relation of type <code>F</code>.
     */
    protected abstract F createNewFlowRelation(T transition, P place);

    /**
     * Removes the given relation from the net.<br>
     * This method is used by {@link #removePlace(String)} and should not be
     * accessed in a public way.<br>
     * The caller is responsible for parameter validity.
     *
     * @param relation The relation to remove.
     * @param notifyListeners Set <code>true</code> if listeners should be
     * notified
     * @return <code>true</code> if flow relation has been removed successfully,
     * <code>false</code> otherwise
     */
    protected boolean removeFlowRelation(F relation, boolean notifyListeners) {
        if (!containsRelation(relation)) {
            return false;
        }
        relation.getPlace().removeRelation(relation);
        relation.getTransition().removeRelation(relation);
        relations.remove(relation.getName());
        relation.removeRelationListener(this);
        checkSD(relation.getPlace());
        checkSD(relation.getTransition());
        if (notifyListeners) {
            structureListenerSupport.notifyRelationRemoved(relation);
        }
        return true;
    }

    public boolean removeFlowRelation(String relationName) {
        return removeFlowRelation(relationName, true);
    }

    public boolean removeFlowRelation(String relationName, boolean notifyListeners) {
        if (!containsFlowRelation(relationName)) {
            return false;
        }
        return removeFlowRelation(relations.get(relationName), notifyListeners);
    }

    //------- Markings -------------------------------------------------------------------------------
    /**
     * <b>Warning:</b> This non-final method gets called by constructors.
     *
     * @return Returns the created marking
     */
    public abstract M createNewMarking();

    /**
     * Sets the initial marking of the Petri net.<br>
     * The initial marking defines the initial state of a Petri net in the sense
     * of tokens contained in places. When a Petri net is reset
     * ({@link #reset()}), it should reach its initial marking.<br>
     * The initial marking does not have to contain all net places. For places
     * not contained in the given initial marking, the net assumes 0 tokens in
     * the initial net state.<br>
     * This method uses {@link #setInitialMarking(AbstractMarking)} to actually
     * transform the net state to the initial state.
     *
     * @param marking The marking used as initial marking (state) of the net.
     */
    @SuppressWarnings("unchecked")
    public void setInitialMarking(M marking) {
        validateMarking(marking);
//		if(initialMarking != null)
//			initialMarking.clear();
        initialMarking = createNewMarking();
        for (String placeName : marking.places()) {
            initialMarking.set(placeName, marking.get(placeName));
        }
        markingListenerSupport.notifyInitialMarkingChanged(new MarkingChangeEvent<>(initialMarking));
        setMarking((M) initialMarking.clone());
    }

    /**
     * Returns the initial marking of the Petri net.<br>
     * The initial marking defines the initial state of a Petri net in the sense
     * of tokens contained in places.
     *
     * @return The initial marking of the net.
     */
    @SuppressWarnings("unchecked")
    public M getInitialMarking() {
        return (M) initialMarking.clone();
    }

    /**
     * Sets the marking of the Petri net.<br>
     * The net marking defines states for Petri net classes.<br>
     * In case the given marking does not define a state for a net place,<br>
     * its state is set to empty, i.e. all tokens are removed.
     *
     * @param marking The desired net marking.
     */
    public void setMarking(M marking) {
        validateMarking(marking);
//		if(this.marking != null)
//			this.marking.clear();
        this.marking = createNewMarking();
        for (String placeName : places.keySet()) {
            if (marking.contains(placeName)) {
                this.marking.set(placeName, marking.get(placeName));
                getPlace(placeName).setState(marking.get(placeName));
            } else {
                getPlace(placeName).setEmptyState();
            }
        }
        lastFiredTransition = null;
        markingListenerSupport.notifyMarkingChanged(new MarkingChangeEvent<>(marking));
    }

    /**
     * Returns the actual marking (state) of the Petri net which is defined by
     * the number of tokens contained in net places.
     *
     * @return The actual marking (state) of the net.
     */
    @SuppressWarnings("unchecked")
    public M getMarking() {
        return (M) marking.clone();
    }

    /**
     * Updates the marking (state) of the Petri net for the given place.<br>
     * When the number of tokens in places change, the marking of the net which
     * describes its actual state in the sense of tokens in net places has to be
     * updated.
     *
     * @param place Place whose token number has changed.
     */
    protected void updateMarking(P place) {
        Validate.notNull(place);
        if (!places.containsKey(place.getName())) {
            return;
        }
        if (!place.hasEmptyState()) {
            marking.set(place.getName(), place.getState());
        } else {
            marking.remove(place.getName());
        }
        markingListenerSupport.notifyMarkingChanged(new MarkingChangeEvent<>(marking));
    }

	//------- Functionality ------------------------------------------------------------------------
    /**
     * Fires the net transition with the given name.
     *
     * @param transitionName The name of the transition to fire.
     * @return <code>true</code> if the transition could be fired;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the given transition name is
     * <code>null</code><br>
     * or the net does not contain a transition with the given name.
     * @throws PNException If the net transition with the given name is not
     * enabled.
     */
    public T fire(String transitionName) throws PNException {
        validateFireTransition(transitionName);
        T transition = getTransition(transitionName);
        transition.fire();
        lastFiredTransition = transition;
        return transition;
    }

    /**
     * Returns the marking which is the result of firing the transition with the
     * given name.<br>
     * The transition is not actually fired, but its firing impact with respect
     * to the actual net state<br>
     * simulated and returned in form of a resulting marking.
     *
     * @param transitionName The name of the transition to fire.
     * @return <code>true</code> if the transition could be fired;<br>
     * <code>false</code> otherwise.
     * @throws ParameterException If the given transition name is
     * <code>null</code><br>
     * or the net does not contain a transition with the given name.
     * @throws PNException If the net transition with the given name is not
     * enabled.
     */
    public abstract M fireCheck(String transitionName) throws PNException;

    /**
     * Returns a copy of the actual net state (marking).
     *
     * @return A copy of the nets' actual marking.
     */
    @SuppressWarnings("unchecked")
    public M cloneMarking() {
        return (M) getMarking().clone();
    }

    /**
     * Resets the net to the initial state.<br>
     * This includes setting the actual marking to the actual marking and
     * setting the last fired transition to <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public void reset() {
        setMarking((M) initialMarking.clone());
    }

	//------- Validation methods --------------------------------------------------------------------
    @Override
    public void capacityChanged(CapacityEvent<? extends AbstractPlace<F, S>> o) {
    }

    /**
     * This method can be used to validate transition names.<br>
     * It checks if it is <code>null</code> and if the Petri net contains a
     * transition with this name.
     *
     * @param transitionName The name of the transition in question.
     */
    protected void validateTransition(String transitionName) {
        Validate.notNull(transitionName);
        if (!containsTransition(transitionName)) {
            throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a transition with name \"" + transitionName + "\"");
        }
    }

    /**
     * This method can be used to validate place names.<br>
     * It checks if it is <code>null</code> and if the Petri net contains a
     * place with this name.
     *
     * @param placeName The name of the place in question.
     */
    protected void validatePlace(String placeName) {
        Validate.notNull(placeName);
        if (!containsPlace(placeName)) {
            throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Net does not contain a place with name \"" + placeName + "\"");
        }
    }

    /**
     * This method can be used to validate the name of a transition to fire.<br>
     * It checks if the given name is <code>null</code>, if the Petri net
     * contains a transition with this name<br>
     * and if this transition is enabled.
     *
     * @param transitionName The name of the transition in question.
     * @throws PNException If the net transition with the given name is not
     * enabled.
     */
    protected void validateFireTransition(String transitionName) throws PNException {
        validateTransition(transitionName);
        T transition = getTransition(transitionName);
        if (!enabledTransitions.contains(transition)) {
            throw new PNException("Cannot fire disabled transition " + transitionName);
        }
    }

    /**
     * This method can be used to evaluate the compatibility markings to be
     * assigned to the Petri net.<br>
     * It checks if the Petri net contains all places for which the marking
     * holds states.
     *
     * @param marking The marking in question.
     */
    protected void validateMarking(M marking) {
        Validate.notNull(marking);
        if (!places.keySet().containsAll(marking.places())) {
            throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Places in initial marking must be places of the net.");
        }
    }

	//------- Interface Methods ----------------------------------------------------------------------
    @Override
    public Set<AbstractPNNode<F>> getParents(AbstractPNNode<F> node) {
        return node.getParents();
    }

    @Override
    public Set<AbstractPNNode<F>> getChildren(AbstractPNNode<F> node) {
        return node.getChildren();
    }

    @Override
    public int nodeCount() {
        return transitions.size() + places.size();
    }

    @Override
    public Set<AbstractPNNode<F>> getNodes() {
        Set<AbstractPNNode<F>> result = new HashSet<>();
        result.addAll(getTransitions());
        result.addAll(getPlaces());
        return result;
    }

	//------ Listener methods ------------------------------------------------------------------------
    public boolean addStructureListener(PNStructureListener<P, T, F, M, S> listener) {
        return structureListenerSupport.addListener(listener);
    }

    public boolean removeStructureListener(PNStructureListener<P, T, F, M, S> listener) {
        return structureListenerSupport.removeListener(listener);
    }

    public boolean addMarkingListener(PNMarkingListener<S, M> listener) {
        return markingListenerSupport.addListener(listener);
    }

    public boolean removeMarkingListener(PNMarkingListener<S, M> listener) {
        return markingListenerSupport.removeListener(listener);
    }

    /**
     * When a transition changes its state to enabled,<br>
     * the net adds it to the set of enabled transitions.
     *
     * @param e
     */
    @SuppressWarnings("unchecked")
    @Override
    public void transitionEnabled(TransitionEvent<? extends AbstractTransition<F, S>> e) {

        enabledTransitions.add((T) e.getSource());
    }

    /**
     * When a transition changes its state to disabled,<br>
     * the net removes it from the set of enabled transitions.
     *
     * @param e
     */
    @Override
    public void transitionDisabled(TransitionEvent<? extends AbstractTransition<F, S>> e) {
        enabledTransitions.remove(e.getSource());
    }

    /**
     * When tokens are added to net places,<br>
     * the Petri net updates its state accordingly by adding tokens to its
     * marking.
     *
     * @param o
     * @see #updateMarking(AbstractPlace)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void tokensAdded(TokenEvent<? extends AbstractPlace<F, S>> o) {
        updateMarking((P) o.getSource());
    }

    /**
     * When tokens are removed from net places,<br>
     * the Petri net updates its state accordingly by removing tokens from its
     * marking.
     *
     * @param o
     * @see #updateMarking(AbstractPlace)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void tokensRemoved(TokenEvent<? extends AbstractPlace<F, S>> o) {
        updateMarking((P) o.getSource());
    }

    @Override
    public void markingChanged(MarkingChangeEvent<S, M> markingEvent) {
    }

    @Override
    public void initialMarkingChanged(MarkingChangeEvent<S, M> markingEvent) {
    }

    @Override
    public void relationConstraintChanged(RelationConstraintEvent<? extends AbstractFlowRelation<P, T, S>> e) {
        structureListenerSupport.notifyStructureChange();
    }

    @Override
    public void structureChanged() {
    }

    @Override
    public void placeAdded(PlaceChangeEvent<P> event) {
    }

    @Override
    public void placeRemoved(PlaceChangeEvent<P> event) {
    }

    @Override
    public void transitionAdded(TransitionChangeEvent<T> event) {
    }

    @Override
    public void transitionRemoved(TransitionChangeEvent<T> event) {
    }

    @Override
    public void relationAdded(RelationChangeEvent<F> event) {
    }

    @Override
    public void relationRemoved(RelationChangeEvent<F> event) {
    }

	//------- clone ----------------------------------------------------------------------------------
    @Override
    public boolean nameChangeRequest(AbstractPlace<F, S> place, String newName) {
        if (!places.containsValue(place)) {
			// This Petri net does not contain the place whose name is changed.
            // -> Don't care and return true
            return true;
        }
        if (containsPlace(newName) && getPlace(newName) != place) {
			// The Petri net already contains another place with the same name
            // -> Don't allow the name to be changed!
            return false;
        }
        if (containsTransition(newName)) {
			// The Petri net contains a transition with the same name
            // -> Don't allow the name to be changed!
            return false;
        }
		// The Petri net contains the given place and no other node with the desired name.
        // -> Allow the name change and store the place under the new name
        P memberPlace = places.remove(place.getName());
        places.put(newName, memberPlace);
        return true;
    }

    @Override
    public boolean nameChangeRequest(AbstractTransition<F, S> transition, String newName) {
        if (!transitions.containsValue(transition)) {
			// This Petri net does not contain the transition whose name is changed.
            // -> Don't care and return true
            return true;
        }
        if (containsTransition(newName) && getTransition(newName) != transition) {
			// The Petri net already contains another transition with the same name
            // -> Don't allow the name to be changed!
            return false;
        }
        if (containsPlace(newName)) {
			// The Petri net contains a place with the same name
            // -> Don't allow the name to be changed!
            return false;
        }
		// The Petri net contains the given transition and no other node with the desired name.
        // -> Allow the name change and store the transition under the new name
        T memberTransition = transitions.remove(transition.getName());
        transitions.put(newName, memberTransition);
        return true;
    }

    @Override
    public boolean nameChangeRequest(AbstractFlowRelation<P, T, S> relation, String newName) {
        if (!relations.containsValue(relation)) {
			// This Petri net does not contain the flow relation whose name is changed.
            // -> Don't care and return true
            return true;
        }
        if (containsFlowRelation(newName) && getFlowRelation(newName) != relation) {
			// The Petri net already contains another flow relation with the same name
            // -> Don't allow the name to be changed!
            return false;
        }
		// The Petri net contains the given flow relation and no other flow relation with the desired name.
        // -> Allow the name change and store the relation under the new name
        F memberRelation = relations.remove(relation.getName());
        relations.put(newName, memberRelation);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AbstractPetriNet<P, T, F, M, S> clone() {
        AbstractPetriNet<P, T, F, M, S> result = newInstance();
        Map<T, T> clonedTransitions = new HashMap<>();
        Map<P, P> clonedPlaces = new HashMap<>();

        for (T ownTransition : getTransitions()) {
            clonedTransitions.put(ownTransition, (T) ownTransition.clone());
            result.addTransition(clonedTransitions.get(ownTransition), false);
        }
        for (P ownPlace : getPlaces()) {
            clonedPlaces.put(ownPlace, (P) ownPlace.clone());
            result.addPlace(clonedPlaces.get(ownPlace), false);
        }
        for (F ownRelation : getFlowRelations()) {
            result.addFlowRelation((F) ownRelation.clone(clonedPlaces.get(ownRelation.getPlace()),
                    clonedTransitions.get(ownRelation.getTransition()),
                    ownRelation.getDirectionPT()), false);
        }
        result.setInitialMarking((M) getInitialMarking().clone());

        return result;
    }

    public abstract AbstractPetriNet<P, T, F, M, S> newInstance();

//	@SuppressWarnings("unchecked")
//	public AbstractMarkingGraph<M,S,X,Y> buildMarkingGraph() throws PNException{
//
//		if (!isBounded())
//			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "Cannot determine marking graph for unbounded nets.");
//
//		ArrayBlockingQueue<M> queue = new ArrayBlockingQueue<M>(10);
//		Set<M> allKnownStates = new HashSet<M>();
//
//		allKnownStates.add(getInitialMarking());
//		AbstractMarkingGraph<M, S, X, Y> markingGraph = createNewMarkingGraph();
//		int stateCount = 0;
//		Map<Integer, String> stateNames = new HashMap<Integer, String>();
//		M initialMarking = getInitialMarking();
//		queue.offer(initialMarking);
//		String stateName = String.format(rgGraphNodeFormat, stateCount++);
//		markingGraph.addState(stateName, (M) initialMarking.clone());
//		markingGraph.setInitialState(markingGraph.getState(stateName));
//		markingGraph.addStartState(stateName);
//		stateNames.put(initialMarking.hashCode(), stateName);
//		allKnownStates.add((M) initialMarking.clone());
//
//		while (!queue.isEmpty()) {
//			M nextMarking = queue.poll();
//			// System.out.println("Next marking: " + nextMarking);
//			setMarking(nextMarking);
//			// M marking = (M) nextMarking.clone();
//			String nextStateName = stateNames.get(nextMarking.hashCode());
//			// System.out.println(nextStateName + " " + nextMarking);
//
//			if (hasEnabledTransitions()) {
//				String newStateName = null;
//				for (T enabledTransition : getEnabledTransitions()) {
//
//					// System.out.println("enabled: " + enabledTransition.getName());
//					M newMarking = fireCheck(enabledTransition.getName());
//					int newMarkingHash = newMarking.hashCode();
//					// System.out.println("new marking: " + newMarking);
//
//					// Check if this marking is already known
//					M equalMarking = null;
//					for (M storedMarking : allKnownStates) {
//						if (storedMarking.equals(newMarking)) {
//							equalMarking = storedMarking;
//							break;
//						}
//					}
//
//					// System.out.println("new marking: " + newMarking);
//					if (equalMarking == null) {
//						// This is a new marking
//						// System.out.println("New marking");
//						queue.offer(newMarking);
//						allKnownStates.add((M) newMarking.clone());
//						newStateName = String.format(rgGraphNodeFormat, stateCount++);
//						markingGraph.addState(newStateName, (M) newMarking.clone());
//						stateNames.put(newMarkingHash, newStateName);
//					} else {
//						// This marking is already known
//						// System.out.println("Known marking");
//						newStateName = stateNames.get(newMarkingHash);
//					}
//					if (!markingGraph.containsEvent(enabledTransition.getName())) {
//						markingGraph.addEvent(enabledTransition.getName(), enabledTransition.getLabel());
//					}
//					// System.out.println("add relation: " + nextStateName + " to " + newStateName + " via " + enabledTransition.getName());
//					try {
//						markingGraph.addRelation(nextStateName, newStateName, enabledTransition.getName());
//					} catch (Exception e) {
//						throw new PNException("TS-Exception while building marking graph.<br>Reason: " + e.getMessage());
//					}
//				}
//			} else {
//				markingGraph.addEndState(nextStateName);
//			}
//
//		}
//		this.markingGraph = markingGraph;
//		reset();
//		return markingGraph;
//	}
//	
    public PNComplexity getComplexity() {
        return new PNComplexity(getPlaceCount(), getTransitionCount(), getFlowRelationCount());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((initialMarking == null) ? 0 : initialMarking.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((places == null) ? 0 : places.hashCode());
        result = prime * result + ((relations == null) ? 0 : relations.hashCode());
        result = prime * result + ((transitions == null) ? 0 : transitions.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        AbstractPetriNet other = (AbstractPetriNet) obj;
        if (initialMarking == null) {
            if (other.initialMarking != null) {
                return false;
            }
        } else if (!initialMarking.equals(other.initialMarking)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (places == null) {
            if (other.places != null) {
                return false;
            }
        } else if (!places.equals(other.places)) {
            return false;
        }
        if (relations == null) {
            if (other.relations != null) {
                return false;
            }
        } else if (!relations.equals(other.relations)) {
            return false;
        }
        if (transitions == null) {
            if (other.transitions != null) {
                return false;
            }
        } else if (!transitions.equals(other.transitions)) {
            return false;
        }
        return true;
    }

    public enum Boundedness {

        BOUNDED, UNBOUNDED, UNKNOWN
    }

}
