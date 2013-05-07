package petrinet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import validate.ParameterException;
import validate.Validate;

/**
 * This abstract class subsumes common properties of Petri net places and transitions.<br>
 * Petri net places and transitions are defined in the classes {@link AbstractPlace} and {@link AbstractTransition}<br>
 * whoch both inherit from {@link AbstractPNNode}.<br>
 * In detail, these properties are:<br>
 * 
 * <ul>
 * <li>Name: The name of the node (unique).</li>
 * <li>Label: The label of the node (not unique).</li>
 * <li>Relations: Incoming and outgoing relations to other nodes.<br>
 *  This allows nodes to notify other nodes about state changes.<br></li>
 * </ul>
 * 
 * Besides basic properties, this class provides further functionality, <br>
 * such as extraction of child- and parent-nodes and source/drain properties.
 * 
 * @author Thomas Stocker
 *
 * @param <E> Relation Type (inherits from AbstractFlowRelation)
 * @see AbstractFlowRelation
 * @see AbstractPlace
 * @see AbstractTransition
 */
public abstract class AbstractPNNode<E extends AbstractFlowRelation<? extends AbstractPlace<E,?>, ? extends AbstractTransition<E,?>, ?>> {
	
	protected final String toStringFormat = "%s[%s]";
	
	/**
	 * Incoming relations of the Petri net node.
	 */
	protected Set<E> incomingRelations = new HashSet<E>();
	/**
	 * Outgoing relations of the Petri net node.
	 */
	protected Set<E> outgoingRelations = new HashSet<E>();
	/**
	 * The name of the Petri net node.<br>
	 * Node names must be unique within a Petri net and are used for distinction.<br>
	 * Once added to a Petri net, the name property should not be changed to avoid conflicts.
	 */
	protected String name;
	/**
	 * The label of the Petri net node.<br>
	 * The label is not unique and can be altered, whenever convenient.
	 */
	protected String label;
	
	
	//------- Constructors ---------------------------------------------------------------------------
	
	protected AbstractPNNode(){
		super();
	}
	
	/**
	 * Creates a new Petri net node, using the given name.<br>
	 * Ba default, the label of the node equals the name.
	 * @param name The name for the Petri net node.
	 * @throws ParameterException If the given name is <code>null</code>.
	 */
	public AbstractPNNode(String name) throws ParameterException {
		this(name, name);
	}
	
	/**
	 * Creates a new Petri net node, using the given name and label.
	 * @param name The name for the Petri net node.
	 * @param label The label for the Petri net node.
	 * @throws ParameterException If some parameters are <code>null</code>.
	 */
	public AbstractPNNode(String name, String label) throws ParameterException {
		Validate.notNull(name);
		Validate.notNull(label);
		setName(name);
		setLabel(label);
	}
	
	
	//------- Basic properties -----------------------------------------------------------------------
	
	/**
	 * Returns the name of the Petri net node.
	 * @return The name of the Petri net.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the name of the Petri net node.
	 * @param name New name for the Petri net node.
	 * @throws ParameterException If the given name is <code>null</code>.
	 */
	public void setName(String name) throws ParameterException{
		Validate.notNull(name);
		this.name = name;
	}
	
	/**
	 * Returns the label of the Petri net node.
	 * @return The label of the Petri net node.
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Sets the label of the Petri net node.
	 * @param label New label for the Petri net node.
	 * @throws ParameterException If the given label is <code>null</code>.
	 */
	public void setLabel(String label) throws ParameterException{
		Validate.notNull(label);
		this.label = label;
	}
	
	/**
	 * Indicates if the Petri net node is a source, i.e.<br>
	 * it has outgoing relations, but no incoming relations.
	 * @return  <code>true</code> if the node is a source;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isSource(){
		return incomingRelations.isEmpty() && !outgoingRelations.isEmpty();
	}
	
	/**
	 * Indicates if the Petri net node is a drain, i.e.<br>
	 * it has incoming relations, but no outgoing relations.
	 * @return  <code>true</code> if the node is a drain;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isDrain(){
		return !incomingRelations.isEmpty() && outgoingRelations.isEmpty();
	}
	
	/**
	 * Returns all parent nodes of this Petri net node.
	 * @return A set of all parent nodes.
	 */
	@SuppressWarnings("unchecked")
	public Set<AbstractPNNode<E>> getParents(){
		Set<AbstractPNNode<E>> result = new HashSet<AbstractPNNode<E>>();
		for(E relation: incomingRelations){
			result.add((AbstractPNNode<E>) relation.getSource());
		}
		return result;
	}
	
	/**
	 * Returns all child nodes of this Petri net node.
	 * @return A set of all child nodes.
	 */
	@SuppressWarnings("unchecked")
	public Set<AbstractPNNode<E>> getChildren(){
		Set<AbstractPNNode<E>> result = new HashSet<AbstractPNNode<E>>();
		for(E relation: outgoingRelations){
			result.add((AbstractPNNode<E>) relation.getTarget());
		}
		return result;
	}
	
	//------- Relations ------------------------------------------------------------------------------
	
	/**
	 * Returns the incoming relations of the Petri net node.
	 * @return A list of all incoming relations.
	 */
	public List<E> getIncomingRelations(){
		return new ArrayList<E>(incomingRelations);
	}
	
	/**
	 * Returns the outgoing relations of the Petri net node.
	 * @return A list of all outgoing relations.
	 */
	public List<E> getOutgoingRelations(){
		return new ArrayList<E>(outgoingRelations);
	}
	
	/**
	 * Adds an incoming relation to the Petri net node.<br>
	 * This method is protected, since adding relations to nodes should be done by the
	 * corresponding Petri net only.
	 * @param relation The incoming relation to add.
	 * @return <code>true</code> if the relation was added;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 */
	protected boolean addIncomingRelation(E relation) throws ParameterException {
		Validate.notNull(relation);
		return incomingRelations.add(relation);
	}
	
	/**
	 * Adds an outgoing relation to the Petri net node.<br>
	 * This method is protected, since adding relations to nodes should be done by the
	 * corresponding Petri net only.
	 * @param relation The outgoing relation to add.
	 * @return <code>true</code> if the relation was added;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 */
	protected boolean addOutgoingRelation(E relation) throws ParameterException {
		Validate.notNull(relation);
		return outgoingRelations.add(relation);
	}
	
	/**
	 * Removes the given relation from the Petri net node.<br>
	 * The method checks itself if the relation is incoming or outgoing.
	 * @param relation The relation to remove.
	 * @return <code>true</code> if the relation was removed;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 */
	protected boolean removeRelation(E relation) throws ParameterException {
		Validate.notNull(relation);
		if(removeIncomingRelation(relation))
			return true;
		return removeOutgoingRelation(relation);
	}
	
	/**
	 * Removes the given relation from the set of incoming relations.
	 * @param relation The incoming relation to remove.
	 * @return <code>true</code> if the relation was removed;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 */
	protected boolean removeIncomingRelation(E relation) throws ParameterException {
		Validate.notNull(relation);
		return incomingRelations.remove(relation);
	}
	
	/**
	 * Removes the given relation from the set of outgoing relations.
	 * @param relation The outgoing relation to remove.
	 * @return <code>true</code> if the relation was removed;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 */
	protected boolean removeOutgoingRelation(E relation) throws ParameterException {
		Validate.notNull(relation);
		return outgoingRelations.remove(relation);
	}
	
	/**
	 * Returns the relation from the given Petri net node leading to this node.<br>
	 * The method checks if there is an incoming relation with this property.
	 * @param node The source node of the relation of interest.
	 * @return The incoming relation leading from the given node to this node;<br>
	 * <code>null</code> if there is no such incoming relation.
	 * @throws ParameterException If the given node is <code>null</code>.
	 */
	public <N extends AbstractPNNode<E>> E getRelationFrom(N node) throws ParameterException{
		Validate.notNull(node);
		for(E incomingRelation: incomingRelations){
			if(incomingRelation.getPlace().equals(node)){
				return incomingRelation;
			}
		}
		return null;
	}
	
	/**
	 * Returns the relation from this node leading to the given Petri net node.<br>
	 * The method checks if there is an outgoing relation with this property.
	 * @param node The target node of the relation of interest.
	 * @return The outgoing relation leading from this node to the given node;<br>
	 * <code>null</code> if there is no such outgoing relation.
	 * @throws ParameterException If the given node is <code>null</code>.
	 */
	public <N extends AbstractPNNode<E>> E getRelationTo(N node) throws ParameterException{
		Validate.notNull(node);
		for(E outgoingRelation: outgoingRelations){
			if(outgoingRelation.getPlace().equals(node)){
				return outgoingRelation;
			}
		}
		return null;
	}
	
	/**
	 * Checks if there is an outgoing relation leading to the given Petri net node.
	 * @param node The node of interest.
	 * @return <code>true</code> if there is an outgoing relation to the given node;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given node is <code>null</code>.
	 */
	public <N extends AbstractPNNode<E>> boolean containsRelationTo(N node) throws ParameterException{
		return getRelationTo(node) != null;
	}
	
	/**
	 * Checks if there is an incoming relation from the given Petri net node.
	 * @param node The node of interest.
	 * @return <code>true</code> if there is an incoming relation from the given node;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given node is <code>null</code>.
	 */
	public <N extends AbstractPNNode<E>> boolean containsRelationFrom(N node) throws ParameterException{
		return getRelationFrom(node) != null;
	}
	
	
	//------- HashCode and Equals -------------------------------------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		AbstractPNNode other = (AbstractPNNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	//------- toString -------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return String.format(toStringFormat, name, label);
	}
	
	/**
	 * Retrieves the PNML-representation of the Petri net node.<br>
	 * This method is abstract and requires subclasses to implement specific representations.
	 * @return The PNML-representation of the node in String format.
	 */
	public abstract String toPNML();
	
	protected abstract AbstractPNNode<E> newInstance();
	
	@Override
	public AbstractPNNode<E> clone() {
		AbstractPNNode<E> result = (AbstractPNNode<E>) newInstance();
		try {
			result.setName(getName());
			result.setLabel(getLabel());
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
