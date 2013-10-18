package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes;

/**
 * Abstract superclass of all graphic attributes.
 * 
 * @author Adrian Lange
 */
public abstract class AbstractAttribute {

	/**
	 * Returns <code>true</code> if at least one of the attribute's values is not <code>null</code>.
	 */
	public abstract boolean hasContent();
}