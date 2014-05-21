package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import java.util.Vector;

import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;

/**
 * <p>
 * Arc graphics attribute class containing the attributes position and line for arcs.
 * </p>
 * 
 * @author Adrian Lange
 */
public class ArcGraphics extends AbstractObjectGraphics {

	/** Default position field */
	public static final Vector<Position> DEFAULT_POSITIONS = new Vector<Position>();
	/** Default line attribute */
	public static final Line DEFAULT_LINE = new Line();

	/** Position field */
	private Vector<Position> positions = new Vector<Position>();
	/** Line attribute */
	private Line line;

	/**
	 * Create edge graphics object with default values.
	 */
	public ArcGraphics() {
		setPositions(DEFAULT_POSITIONS);
		setLine(DEFAULT_LINE);
	}

	/**
	 * Create edge graphics object with the specified values.
	 */
	public ArcGraphics(Vector<Position> positions, Line line) {
		setPositions(positions);
		setLine(line);
	}

	/**
	 * @return the positions
	 */
	public Vector<Position> getPositions() {
		return positions;
	}

	/**
	 * @return the line
	 */
	public Line getLine() {
		return line;
	}

	/**
	 * @param positions
	 *            the positions to set
	 */
	public void setPositions(Vector<Position> positions) {
		Validate.notNull(positions);
		this.positions = positions;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public void setLine(Line line) {
		Validate.notNull(line);
		this.line = line;
	}

	@Override
	public boolean hasContent() {
		return positions.size() > 0 || line.hasContent();
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		boolean empty = true;

		str.append("[");

		if (positions.size() > 0) {
			boolean posEmpty = true;
			str.append("[");
			for (int p = 0; p < positions.size(); p++) {
				if (!posEmpty)
					str.append(",");
				str.append(positions.get(p));
				posEmpty = false;
			}
			str.append("]");
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
