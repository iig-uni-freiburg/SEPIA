/* 
 * Copyright (c) 2015, IIG Telematics, Uni Freiburg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of IIG Telematics, Uni Freiburg nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.abstr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import java.util.HashMap;
import java.util.Map;


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
public abstract class AbstractPNNode<E extends AbstractFlowRelation<? extends AbstractPlace<E,?>, ? extends AbstractTransition<E,?>, ?>> implements Serializable{
	
	private static final long serialVersionUID = -809201554674739116L;

	protected final String toStringFormat = "%s[%s]";
	
	/**
	 * Incoming relations of the Petri net node.
	 */
	protected Map<String,E> incomingRelations = new HashMap<>();
	/**
	 * Outgoing relations of the Petri net node.
	 */
	protected Map<String,E> outgoingRelations = new HashMap<>();
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
	
	private PNNodeType type = null;
	
	//------- Constructors ---------------------------------------------------------------------------
	
//	protected AbstractPNNode(PNNodeType type){
//		super();
//		this.type = type;
//	}
	
	/**
	 * Creates a new Petri net node, using the given name.<br>
	 * Ba default, the label of the node equals the name.
	 * @param name The name for the Petri net node.
	 * @throws ParameterException If the given name is <code>null</code>.
	 */
	public AbstractPNNode(PNNodeType type, String name){
		this(type, name, name);
		this.type = type;
	}
	
	/**
	 * Creates a new Petri net node, using the given name and label.
	 * @param name The name for the Petri net node.
	 * @param label The label for the Petri net node.
	 * @throws ParameterException If some parameters are <code>null</code>.
	 */
	public AbstractPNNode(PNNodeType type, String name, String label){
		Validate.notNull(name);
		Validate.notNull(label);
		this.name = name;
		setLabel(label);
		this.type = type;
	}
	
	
	//------- Basic properties -----------------------------------------------------------------------
	
	/**
	 * Returns the name of the Petri net node.
	 * @return The name of the Petri net.
	 */
	public String getName(){
		return name;
	}
	
//	/**
//	 * Sets the name of the Petri net node.<br>
//	 * Implementing classes must make sure, that the net does not contain other nodes with the same name.
//	 * @param name New name for the Petri net node.
//	 * @throws ParameterException If the given name is <code>null</code>.
//	 */
//	protected abstract void setName(String name);
	
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
	public void setLabel(String label){
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
		Set<AbstractPNNode<E>> result = new HashSet<>();
		for(E relation: incomingRelations.values()){
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
		Set<AbstractPNNode<E>> result = new HashSet<>();
		for(E relation: outgoingRelations.values()){
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
		return new ArrayList<>(incomingRelations.values());
	}
	
	/**
	 * Returns the outgoing relations of the Petri net node.
	 * @return A list of all outgoing relations.
	 */
	public List<E> getOutgoingRelations(){
		return new ArrayList<>(outgoingRelations.values());
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
	protected boolean addIncomingRelation(E relation){
		Validate.notNull(relation);
		return (incomingRelations.put(relation.getName(), relation)) == null;
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
	protected boolean addOutgoingRelation(E relation){
		Validate.notNull(relation);
		return (outgoingRelations.put(relation.getName(), relation)) == null;
	}
	
	/**
	 * Removes the given relation from the Petri net node.<br>
	 * The method checks itself if the relation is incoming or outgoing.
	 * @param relation The relation to remove.
	 * @return <code>true</code> if the relation was removed;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 */
	protected boolean removeRelation(E relation){
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
	protected boolean removeIncomingRelation(E relation){
		Validate.notNull(relation);
		return (incomingRelations.remove(relation.getName())) != null;
	}
	
	/**
	 * Removes the given relation from the set of outgoing relations.
	 * @param relation The outgoing relation to remove.
	 * @return <code>true</code> if the relation was removed;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given relation is <code>null</code>.
	 */
	protected boolean removeOutgoingRelation(E relation){
		Validate.notNull(relation);
		return (outgoingRelations.remove(relation.getName())) != null;
	}
	
	/**
	 * Returns the relation from the given Petri net node leading to this node.<br>
	 * The method checks if there is an incoming relation with this property.
	 * @param node The source node of the relation of interest.
	 * @return The incoming relation leading from the given node to this node;<br>
	 * <code>null</code> if there is no such incoming relation.
	 * @throws ParameterException If the given node is <code>null</code>.
	 */
	public <N extends AbstractPNNode<E>> E getRelationFrom(N node){
		Validate.notNull(node);
		for(E incomingRelation: incomingRelations.values()){
			if(incomingRelation.getSource().equals(node)){
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
	public <N extends AbstractPNNode<E>> E getRelationTo(N node){
		Validate.notNull(node);
		for(E outgoingRelation: outgoingRelations.values()){
			if(outgoingRelation.getTarget().equals(node)){
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
	public <N extends AbstractPNNode<E>> boolean containsRelationTo(N node){
		return getRelationTo(node) != null;
	}
	
	/**
	 * Checks if there is an incoming relation from the given Petri net node.
	 * @param node The node of interest.
	 * @return <code>true</code> if there is an incoming relation from the given node;<br>
	 * <code>false</code> otherwise.
	 * @throws ParameterException If the given node is <code>null</code>.
	 */
	public <N extends AbstractPNNode<E>> boolean containsRelationFrom(N node){
		return getRelationFrom(node) != null;
	}

	protected PNNodeType getPNNodeType() {
		return type;
	}

	protected void setType(PNNodeType type) {
		this.type = type;
	}
	
	public int degree(){
		return outgoingRelations.size() + incomingRelations.size();
	}
	
	public boolean isTransition(){
		return type.equals(PNNodeType.TRANSITION);
	}
	
	public boolean isPlace(){
		return type.equals(PNNodeType.PLACE);
	}
	
	
	//------- HashCode and Equals -------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((toStringFormat == null) ? 0 : toStringFormat.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		AbstractPNNode other = (AbstractPNNode) obj;
//		if (incomingRelations == null) {
//			if (other.incomingRelations != null) {
//				return false;
//			}
//		} else if (!incomingRelations.equals(other.incomingRelations)) {
//			if (incomingRelations.size() == other.incomingRelations.size()) {
//				for (E incomingRelation : incomingRelations) {
//					if (!other.incomingRelations.contains(incomingRelation)) {
//						return false;
//					}
//				}
//				return true;
//			}
//			return false;
//		}
		if (label == null) {
			if (other.label != null) {
				return false;
			}
		} else if (!label.equals(other.label)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
//		if (outgoingRelations == null) {
//			if (other.outgoingRelations != null) {
//				return false;
//			}
//		} else if (!outgoingRelations.equals(other.outgoingRelations)) {
//			if (outgoingRelations.size() == other.outgoingRelations.size()) {
//				for (E outgoingRelation : outgoingRelations) {
//					if (!other.outgoingRelations.contains(outgoingRelation)) {
//						return false;
//					}
//				}
//				return true;
//			}
//			return false;
//		}
		if (toStringFormat == null) {
			if (other.toStringFormat != null) {
				return false;
			}
		} else if (!toStringFormat.equals(other.toStringFormat)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
	@Override
	public AbstractPNNode<E> clone() {
		AbstractPNNode<E> result = newInstance(getName());
		result.setLabel(getLabel());
		result.setType(getPNNodeType());
//		for (E incomingRelation: getIncomingRelations())
//			result.addIncomingRelation(incomingRelation);
//		for (E outgoingRelation: getOutgoingRelations())
//			result.addOutgoingRelation(outgoingRelation);
//		-> Is done in AbstractPetriNet.clone()
		return result;
	}
	
	//------- toString -------------------------------------------------------------------------------
	@Override
	public String toString() {
		return String.format(toStringFormat, name, label);
	}

	protected abstract AbstractPNNode<E> newInstance(String name);
}
