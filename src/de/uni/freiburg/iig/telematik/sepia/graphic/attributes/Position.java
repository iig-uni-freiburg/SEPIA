package de.uni.freiburg.iig.telematik.sepia.graphic.attributes;

/**
 * <p>
 * The position attribute class can define a X and a Y coordinate value. It is used by places, transitions, pages, and edges.
 * </p>
 * 
 * @author Adrian Lange
 */
public class Position extends AbstractAttribute {

	public static final double DEFAULT_POSITION_X = 0.0;
	public static final double DEFAULT_POSITION_Y = 0.0;
	private double x;
	private double y;

	/**
	 * Creates a new position attribute with default values.
	 */
	public Position() {
		setX(DEFAULT_POSITION_X);
		setY(DEFAULT_POSITION_Y);
	}

	/**
	 * Creates a new position attribute with the specified coordinates.
	 * 
	 * @param x
	 *            X-axis value of the position.
	 * @param y
	 *            Y-axis value of the position.
	 */
	public Position(double x, double y) {
		setX(x);
		setY(y);
	}

	/**
	 * Returns the x-axis value of the position.
	 * 
	 * @return X-axis value
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y-axis value of the position.
	 * 
	 * @return Y-axis value
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the x value of the position.
	 * 
	 * @param x
	 *            x-axis value
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the y value of the position.
	 * 
	 * @param y
	 *            y-axis value
	 */
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Position( " + getX() + " / " + getY() + " )";
	}

	@Override
	public boolean hasContent() {
		// Because of the default values, this attribute always has content
		return true;
	}
}
