package de.uni.freiburg.iig.telematik.sepia.graphic.attributes;

/**
 * <p>
 * The offset attribute class can define a X and a Y coordinate value. It is used by annotations.
 * </p>
 * 
 * @author Adrian Lange
 */
public class Offset {

	public static final double DEFAULT_OFFSET_X = 0.0;
	public static final double DEFAULT_OFFSET_Y = 0.0;
	private double x;
	private double y;

	/**
	 * Creates a new offset attribute with default values.
	 */
	public Offset() {
		setX(DEFAULT_OFFSET_X);
		setY(DEFAULT_OFFSET_Y);
	}

	/**
	 * Creates a new offset attribute with the specified coordinates.
	 * 
	 * @param x
	 *            X-axis value of the offset.
	 * @param y
	 *            Y-axis value of the offset.
	 */
	public Offset(double x, double y) {
		setX(x);
		setY(y);
	}

	/**
	 * Returns the x-axis value of the offset.
	 * 
	 * @return X-axis value
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y-axis value of the offset.
	 * 
	 * @return Y-axis value
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the x value of the offset.
	 * 
	 * @param x
	 *            x-axis value
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the y value of the offset.
	 * 
	 * @param y
	 *            y-axis value
	 */
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Offset( " + getX() + " / " + getY() + " )";
	}
}
