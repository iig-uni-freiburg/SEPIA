package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;

/**
 * <p>
 * Node graphics attribute class containing the attributes position, dimension, fill and line for places and transitions.
 * </p>
 * 
 * @author Adrian Lange
 */
public class NodeGraphics extends AbstractObjectGraphics {

	/** Default position */
	public static final Position DEFAULT_POSITION = new Position();
	/** Default dimension */
	public static final Dimension DEFAULT_DIMENSION = new Dimension();
	/** Default fill */
	public static final Fill DEFAULT_FILL = new Fill();
	/** Default line */
	public static final Line DEFAULT_LINE = new Line();

	private Position position = null;
	private Dimension dimension = null;
	private Fill fill = null;
	private Line line = null;

	/**
	 * Create new node graphic object with default values.
	 */
	public NodeGraphics() throws ParameterException {
		setPosition(DEFAULT_POSITION);
		setDimension(DEFAULT_DIMENSION);
		setFill(DEFAULT_FILL);
		setLine(DEFAULT_LINE);
	}

	/**
	 * Create new node graphic object with the specified values.
	 */
	public NodeGraphics(Position position, Dimension dimension, Fill fill, Line line) throws ParameterException {
		setPosition(position);
		setDimension(dimension);
		setFill(fill);
		setLine(line);
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @return the dimension
	 */
	public Dimension getDimension() {
		return dimension;
	}

	/**
	 * @return the fill
	 */
	public Fill getFill() {
		return fill;
	}

	/**
	 * @return the line
	 */
	public Line getLine() {
		return line;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Position position) throws ParameterException {
		Validate.notNull(position);
		this.position = position;
	}

	/**
	 * @param dimension
	 *            the dimension to set
	 */
	public void setDimension(Dimension dimension) throws ParameterException {
		Validate.notNull(dimension);
		this.dimension = dimension;
	}

	/**
	 * @param fill
	 *            the fill to set
	 */
	public void setFill(Fill fill) throws ParameterException {
		Validate.notNull(fill);
		this.fill = fill;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public void setLine(Line line) throws ParameterException {
		Validate.notNull(line);
		this.line = line;
	}

	@Override
	public boolean hasContent() {
		return position.hasContent() || dimension.hasContent() || line.hasContent() || fill.hasContent();
	}

	public String toString() {
		StringBuilder str = new StringBuilder();

		boolean empty = true;

		str.append("[");

		if (position != DEFAULT_POSITION) {
			str.append(position);
			empty = false;
		}
		if (dimension != DEFAULT_DIMENSION) {
			if (!empty)
				str.append(",");
			str.append(dimension);
			empty = false;
		}
		if (fill != DEFAULT_FILL) {
			if (!empty)
				str.append(",");
			str.append(fill);
			empty = false;
		}
		if (line != DEFAULT_LINE) {
			if (!empty)
				str.append(",");
			str.append(line);
		}

		str.append("]");

		return str.toString();
	}
}
