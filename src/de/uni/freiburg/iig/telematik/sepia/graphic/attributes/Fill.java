package de.uni.freiburg.iig.telematik.sepia.graphic.attributes;

import java.net.URI;

/**
 * <p>
 * The fill attribute class can define a color, a gradient color, a gradient rotation, and a filling image. It is used by annotations, pages, transitions and
 * pages.
 * </p>
 * 
 * @author Adrian Lange
 */
public class Fill {

	/** Default color as CSS2 color string */
	public static final String DEFAULT_COLOR = null;
	/** Default gradient color as CSS2 color string */
	public static final String DEFAULT_GRADIENT_COLOR = null;
	/** Default gradient rotation */
	public static final GradientRotation DEFAULT_GRADIENT_ROTATION = null;
	/** Default image */
	public static final URI DFAULT_IMAGE = null;
	/** CSS2 color string */
	private String color;
	/** CSS2 color string */
	private String gradientColor;
	/** Gradient rotation enumeration value */
	private GradientRotation gradientRotation;
	/** URI to an image */
	private URI image;

	/**
	 * Creates new fill attribute with default values.
	 */
	public Fill() {
		setColor(DEFAULT_COLOR);
		setGradientColor(DEFAULT_GRADIENT_COLOR);
		setGradientRotation(DEFAULT_GRADIENT_ROTATION);
		setImage(DFAULT_IMAGE);
	}

	/**
	 * Creates a new fill attribute with the specified values.
	 * 
	 * @param color
	 *            CSS2 color string for the filling color or <code>null</code> if it should be ignored.
	 * @param gradientColor
	 *            CSS2 color string for the gradient color or <code>null</code> if it should be ignored.
	 * @param gradientRotation
	 *            Rotation of the gradient or <code>null</code> if it should be ignored.
	 * @param image
	 *            Filling image URI or <code>null</code> if it should be ignored.
	 */
	public Fill(String color, String gradientColor, GradientRotation gradientRotation, URI image) {
		setColor(color);
		setGradientColor(gradientColor);
		setGradientRotation(gradientRotation);
		setImage(image);
	}

	/**
	 * Returns the fill color as CSS2 color string or <code>null</code> if no fill color has been specified.
	 * 
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Returns the gradient color as CSS2 color string or <code>null</code> if no gradient color has been specified.
	 * 
	 * @return the gradientColor
	 */
	public String getGradientColor() {
		return gradientColor;
	}

	/**
	 * Returns the gradient rotation or <code>null</code> if no gradient rotation has been specified.
	 * 
	 * @return the gradientRotation
	 */
	public GradientRotation getGradientRotation() {
		return gradientRotation;
	}

	/**
	 * Returns the image as URI or <code>null</code> if no image has been specified.
	 * 
	 * @return the image
	 */
	public URI getImage() {
		return image;
	}

	/**
	 * Sets the color of the fill attribute as CSS2 color string. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Sets the gradient color for the fill attribute as CSS2 color string. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param gradientColor
	 *            the gradientColor to set
	 */
	public void setGradientColor(String gradientColor) {
		this.gradientColor = gradientColor;
	}

	/**
	 * Sets the gradient rotation for the fill attribute. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param gradientRotation
	 *            the gradientRotation to set
	 */
	public void setGradientRotation(GradientRotation gradientRotation) {
		this.gradientRotation = gradientRotation;
	}

	/**
	 * Sets the image for the fill attribute. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param image
	 *            the image to set
	 */
	public void setImage(URI image) {
		this.image = image;
	}

	/**
	 * TODO
	 * 
	 * @author Adrian Lange
	 */
	public static enum GradientRotation {
		VERTICAL, HORIZONTAL, DIAGONAL;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
}
