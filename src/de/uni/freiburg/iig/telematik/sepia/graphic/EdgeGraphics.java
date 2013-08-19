package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.util.Vector;

import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Position;

/**
 * <p>
 * Edge graphics attribute class containing the attributes position and line for arcs.
 * </p>
 * 
 * @author Adrian Lange
 */
public class EdgeGraphics implements ObjectGraphics {

	/** Default position field */
	public static final Vector<Position> DEFAULT_POSITIONS = new Vector<Position>();
	/** Default line attribute */
	public static final Line DEFAULT_LINE = new Line();

	/** Position field */
	private Vector<Position> positions;
	/** Line attribute */
	private Line line;

	/**
	 * Create edge graphics object with default values.
	 */
	public EdgeGraphics() {
		setPositions(DEFAULT_POSITIONS);
		setLine(DEFAULT_LINE);
	}

	/**
	 * Create edge graphics object with the specified values.
	 */
	public EdgeGraphics(Vector<Position> positions, Line line) {
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
		this.positions = positions;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public void setLine(Line line) {
		this.line = line;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();

		boolean empty = true;

		str.append("[");

		if (positions.size() > 0) {
			str.append("AAAAAAAAAAAAAAAAAAAAAA" + positions.size());
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
