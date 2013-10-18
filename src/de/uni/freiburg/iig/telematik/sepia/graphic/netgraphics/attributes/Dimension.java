package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes;

/**
 * <p>
 * The dimension attribute class has a X and a Y value. It is used by pages, places, and transitions.
 * </p>
 * 
 * @author Adrian Lange
 */
public class Dimension extends AbstractAttribute {

	private double x;
	private double y;

	public static final double DEFAULT_DIMENSION_X = 30.0;
	public static final double DEFAULT_DIMENSION_Y = 30.0;

	/**
	 * Creates a new dimension attribute with default values.
	 */
	public Dimension() {
		setX(DEFAULT_DIMENSION_X);
		setY(DEFAULT_DIMENSION_Y);
	}

	/**
	 * Creates a new dimension attribute with the specified coordinates.
	 * 
	 * @param x
	 *            X-axis value of the dimension.
	 * @param y
	 *            Y-axis value of the dimension.
	 */
	public Dimension(double x, double y) {
		setX(x);
		setY(y);
	}

	/**
	 * Returns the x-axis value of the dimension.
	 * 
	 * @return X-axis value
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y-axis value of the dimension.
	 * 
	 * @return Y-axis value
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the x value of the dimension.
	 * 
	 * @param x
	 *            x-axis value
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the y value of the dimension.
	 * 
	 * @param y
	 *            y-axis value
	 */
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Dimension( " + getX() + " / " + getY() + " )";
	}

	@Override
	public boolean hasContent() {
		// Because of the default values, this attribute always has content
		return true;
	}
}
