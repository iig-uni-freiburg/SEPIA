package de.uni.freiburg.iig.telematik.sepia.petrinet.abstr;

import java.io.Serializable;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.event.FlowRelationListener;
import de.uni.freiburg.iig.telematik.sepia.event.FlowRelationListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;

/**
 * Abstract class that defines general properties for flow relations between Petri net places and transitions.<br>
 * Flow relations can lead from transitions to places or the other way round (direction).<br>
 * The direction of the flow relation is defined with the help of the {@link #directionPT}-property.<br>
 * <br>
 * Flow relations have a unique name which is determined by concatenation of place and transition names<br>
 * and the direction property (they have also unique names).
 * 
 * @author Thomas Stocker
 *
 * @param <P> The type of Petri net places.
 * @param <T> The type of Petri net transitions.
 */
public abstract class AbstractFlowRelation<P extends AbstractPlace<? extends AbstractFlowRelation<P,T,S>,S>, 
										   T extends AbstractTransition<? extends AbstractFlowRelation<P,T,S>, S>,
									       S extends Object> implements Serializable{
	
	private static final long serialVersionUID = -3305028252141027555L;
	
	protected final String nameFormatPT = "arcPT_%s%s";
	protected final String nameFormatTP = "arcTP_%s%s";
	protected final String toStringFormat = "%s: %s -%s-> %s";
	
	protected FlowRelationListenerSupport<AbstractFlowRelation<P,T,S>> relationListenerSupport = new FlowRelationListenerSupport<AbstractFlowRelation<P,T,S>>();
	
	protected S constraint = null;
	
	/**
	 * The name of the flow relation.
	 */
	protected String name;
	/**
	 * The place of the flow relation.
	 */
	protected P place;
	/**
	 * The transition of the flow relation.
	 */
	protected T transition;
	/**
	 * The direction of the flow relation.
	 */
	protected boolean directionPT;
	
	
	//------- Constructors ---------------------------------------------------------------------------
	
	/**
	 * Creates a new flow relation leading from the given place to the given transition.
	 * @param place Place where the flow relation starts.
	 * @param transition Transition where the flow relation ends.
	 * @throws ParameterException If some parameters are <code>null</code>.
	 * @see #setValues(AbstractTransition, AbstractPlace)
	 */
	public AbstractFlowRelation(P place, T transition){
		this(place, transition, true, null);
	}
	
	/**
	 * Creates a new flow relation leading from the given transition to the given place.
	 * @param transition Transition where the flow relation starts.
	 * @param place Place where the flow relation ends.
	 * @throws ParameterException If some parameters are <code>null</code>.
	 * @see #setValues(AbstractTransition, AbstractPlace)
	 */
	public AbstractFlowRelation(T transition, P place){
		this(place, transition, false, null);
	}
	
	public AbstractFlowRelation(P place, T transition, S constraint){
		this(place, transition, true, constraint);
	}
	
	public AbstractFlowRelation(T transition, P place, S constraint){
		this(place, transition, false, constraint);
	}
	
	
	private AbstractFlowRelation(P place, T transition, boolean directionPT, S constraint){
		this.directionPT = directionPT;
		setValues(transition, place);
		if(constraint == null){
			setConstraint(getDefaultConstraint());
		} else {
			setConstraint(constraint);
		}
	}
	
	
	//------- Basic properties -----------------------------------------------------------------------
	

	public final void setConstraint(S constraint){
		validateConstraint(constraint);
		this.constraint = constraint;
		relationListenerSupport.notifyCapacityChanged(this);
		getTransition().checkState();
	}
	
	public S getConstraint() {
		return constraint;
	}
	
	protected abstract S getDefaultConstraint();
	
	protected abstract void validateConstraint(S constraint);
	
	/**
	 * Returns the place of the flow relation.
	 * @return The flow relations' place.
	 */
	public P getPlace() {
		return place;
	}
	
	/**
	 * Returns the transition of the flow relation.
	 * @return The flow relations' transition.
	 */
	public T getTransition() {
		return transition;
	}
	
	/**
	 * Returns the direction of the flow relation.
	 * @return <code>true</code> if the relation leads from a place to a transition;<br>
	 * <code>false</code> if the relation leads from a transition to a place.
	 */
	public boolean getDirectionPT() {
		return directionPT;
	}
	
	protected void setDirectionPT(boolean directionPT){
		this.directionPT = directionPT;
	}
	
	/**
	 * This method is used in constructors to set the flow relation place and transition.<br>
	 * It uses {@link #setName(AbstractPlace, AbstractTransition)} to assign the flow relation a unique name.
	 * @param transition The transition for the flow relation.
	 * @param place The place for the flow relation.
	 * @throws ParameterException If some parameters are <code>null</code>.
	 */
	private void setValues(T transition, P place){
		Validate.notNull(place);
		Validate.notNull(transition);
		this.place = place;
		this.transition = transition;
		setName(place, transition);
	}
	
	/**
	 * Sets the name of the flow relation.<br><br>
	 * If this method is used to explicitly set the flow relations' name,<br>
	 * the caller is responsible to guarantee unique flow relation names.<br>
	 * Name collisions can cause unintended or erroneous Petri net behavior.
	 * @param name The name for the flow relation. It must start with a
         * character of the range [a-zA-Z] and must only contain alphanumerical
         * characters and the symbols <code>-_.:</code>.
	 * @throws ParameterException If some parameters are <code>null</code>.
	 */
	public void setName(String name){
		Validate.notNull(name);

                PNUtils.validateElementName(name);

		if(!relationListenerSupport.requestNameChangePermission(this, name))
			throw new ParameterException(ErrorCode.INCONSISTENCY, "A connected Petri net already contains a relation with this name.\n Cancel renaming to avoid name clash.");
		this.name = name;
	}
	
	/**
	 * Automatically sets the name of the flow relation on basis of the names of the given place and transition.
	 * This method guarantees unique flow relation names which is a result of concatenating place and transition names<br>
	 * together with the direction property (they have also unique names).
	 * @param place The place whose name is used for automatic name determination.
	 * @param transition The transition whose name is used for automatic name determination.
	 */
	protected void setName(P place, T transition){
		if(directionPT){						
			name = String.format(nameFormatPT, place.getName(), transition.getName());
		} else {
			name = String.format(nameFormatTP, transition.getName(), place.getName());
		}
	}
	
	/**
	 * Returns the name of the flow relation.
	 * @return The name of the flow relation.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the source node (place or transition) of the flow relation.
	 * @return The source node of the flow relation.
	 */
	public AbstractPNNode<?> getSource(){
		if(directionPT){
			return place;
		} else {
			return transition;
		}
	}
	
	/**
	 * Returns the target node (place or transition) of the flow relation.
	 * @return The target node of the flow relation.
	 */
	public AbstractPNNode<?> getTarget(){
		if(directionPT){
			return transition;
		} else {
			return place;
		}
	}
	
	
	//------- hashCode and equals --------------------------------------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constraint == null) ? 0 : constraint.hashCode());
		result = prime * result + (directionPT ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result	+ ((transition == null) ? 0 : transition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		AbstractFlowRelation other = (AbstractFlowRelation) obj;
		if (constraint == null) {
			if (other.constraint != null)
				return false;
		} else if (!constraint.equals(other.constraint))
			return false;
		if (directionPT != other.directionPT)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (place == null) {
			if (other.place != null)
				return false;
		} else if (!place.equals(other.place))
			return false;
		if (transition == null) {
			if (other.transition != null)
				return false;
		} else if (!transition.equals(other.transition))
			return false;
		return true;
	}
	
	//------- Listener support ------------------------------------------------------------------------
	
		/**
		 * Adds a flow relation listener.
		 * @param listener The flow relation listener to add.
		 * @throws ParameterException If the listener reference is <code>null</code>.
		 */
		public void addRelationListener(FlowRelationListener<AbstractFlowRelation<P,T,S>> listener){
			relationListenerSupport.addListener(listener);
		}
		
		/**
		 * Removes a flow relation listener.
		 * @param listener The flow relation listener to remove.
		 * @throws ParameterException If the listener reference is <code>null</code>.
		 */
		public void removeRelationListener(FlowRelationListener<AbstractFlowRelation<P,T,S>> listener){
			relationListenerSupport.removeListener(listener);
		}
	
	//------- toString -------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		if(directionPT)
			return String.format(toStringFormat, name, place.getName(), getConstraint(), transition.getName());
		return String.format(toStringFormat, name, transition.getName(), getConstraint(), place.getName());
	}
	
	//------- clone ----------------------------------------------------------------------------------
	
	public abstract AbstractFlowRelation<P,T,S> clone(P place, T transition, boolean directionPT);
	
}
