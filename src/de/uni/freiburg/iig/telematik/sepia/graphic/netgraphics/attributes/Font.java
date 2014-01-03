package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes;

/**
 * <p>
 * The font attribute class can define an align, a text decoration, a font family, a rotation, a font size, a font style, and a font weight. It is used by
 * annotations.
 * </p>
 * 
 * @author Adrian Lange
 */
public class Font extends AbstractAttribute {

	/** Default align */
	public static final Align DEFAULT_ALIGN = null;
	/** Default text decoration */
	public static final Decoration DEFAULT_DECORATION = null;
	/** Default font family as CSS2 font family string */
	public static final String DEFAULT_FAMILY = null;
	/** Default text rotation as CSS2 text rotation value */
	public static final double DEFAULT_ROTATION = 0.0;
	/** Default font size as CSS2 font size string */
	public static final String DEFAULT_SIZE = null;
	/** Default font style as CSS2 font style string */
	public static final String DEFAULT_STYLE = null;
	/** Default font weight as CSS2 font weight string */
	public static final String DEFAULT_WEIGHT = null;
	/** Align */
	private Align align = DEFAULT_ALIGN;
	/** Text decoration */
	private Decoration decoration = DEFAULT_DECORATION;
	/** Font family as CSS2 font family string */
	private String family = DEFAULT_FAMILY;
	/** Text rotation as CSS2 text rotation value */
	private double rotation = DEFAULT_ROTATION;
	/** Font size as CSS2 font size string */
	private String size = DEFAULT_SIZE;
	/** Font style as CSS2 font style string */
	private String style = DEFAULT_STYLE;
	/** Font weight as CSS2 font weight string */
	private String weight = DEFAULT_WEIGHT;

	/**
	 * Creates new font attribute with default values.
	 */
	public Font() {}

	/**
	 * Creates new font attribute with the specified values.
	 * 
	 * @param align
	 *            Align or <code>null</code>
	 * @param decoration
	 *            Text decoration or <code>null</code>
	 * @param family
	 *            Font family as CSS2 font family string
	 * @param rotation
	 *            Text rotation with <code>0.0</code> as default value
	 * @param size
	 *            Font size as CSS2 font size string or <code>null</code>
	 * @param style
	 *            Font style as CSS2 font style string or <code>null</code>
	 * @param weight
	 *            Font weight as CSS2 font weight string or <code>null</code>
	 */
	public Font(Align align, Decoration decoration, String family, double rotation, String size, String style, String weight) {
		setAlign(align);
		setDecoration(decoration);
		setFamily(family);
		setRotation(rotation);
		setSize(size);
		setStyle(style);
		setWeight(weight);
	}

	/**
	 * Returns the align or <code>null</code> if it should be ignored.
	 * 
	 * @return the align
	 */
	public Align getAlign() {
		return align;
	}

	/**
	 * Returns the decoration or <code>null</code> if it should be ignored.
	 * 
	 * @return the decoration
	 */
	public Decoration getDecoration() {
		return decoration;
	}

	/**
	 * Returns the font family or <code>null</code> if it should be ignored.
	 * 
	 * @return the family
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * Returns the rotation value or <code>0.0</code> if it should be ignored.
	 * 
	 * @return the rotation
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Returns the size string or <code>null</code> if it should be ignored.
	 * 
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Returns the style string or <code>null</code> if it should be ignored.
	 * 
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Returns the font weight string or <code>null</code> if it should be ignored.
	 * 
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * Sets the text align. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param align
	 *            the align to set
	 */
	public void setAlign(Align align) {
		this.align = align;
	}

	/**
	 * Sets the font decoration. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param decoration
	 *            the decoration to set
	 */
	public void setDecoration(Decoration decoration) {
		this.decoration = decoration;
	}

	/**
	 * Sets the font family as CSS2 font family string. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param family
	 *            the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * Sets the rotation value. Gets ignored if it has the value <code>0.0</code>.
	 * 
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * Sets the font size as CSS2 font size string. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Sets the text style as CSS2 text style string. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param style
	 *            the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Sets the font weight as CSS2 font weight string. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * Enumeration to set the text alignment
	 * 
	 * @author Adrian Lange
	 */
	public static enum Align {
		LEFT, CENTER, RIGHT;

		public static Align getAlign(String alignStr) {
			if (alignStr.equals("left"))
				return Align.LEFT;
			else if (alignStr.equals("center"))
				return Align.CENTER;
			else if (alignStr.equals("right"))
				return Align.RIGHT;
			else
				return null;
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	/**
	 * Enumeration to set the font decoration
	 * 
	 * @author Adrian Lange
	 */
	public static enum Decoration {
		UNDERLINE, OVERLINE, LINE_THROUGH;

		public static Decoration getDecoration(String decorationStr) {
			if (decorationStr.equals("underline"))
				return Decoration.UNDERLINE;
			else if (decorationStr.equals("overline"))
				return Decoration.OVERLINE;
			else if (decorationStr.equals("line-through"))
				return Decoration.LINE_THROUGH;
			else
				return null;
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase().replace("_", "-");
		}
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("Font( ");
		if (getAlign() != null)
			str.append("align:" + getAlign() + " ");
		if (getDecoration() != null)
			str.append("decoration:" + getDecoration() + " ");
		if (getFamily() != null)
			str.append("family:" + getFamily() + " ");
		if (getRotation() != DEFAULT_ROTATION)
			str.append("rotation:" + getRotation() + " ");
		if (getSize() != null)
			str.append("size:" + getSize() + " ");
		if (getStyle() != null)
			str.append("style:" + getStyle() + " ");
		if (getWeight() != null)
			str.append("weight:" + getWeight() + " ");
		str.append(")");

		return str.toString();
	}

	@Override
	public boolean hasContent() {
		return align != null || decoration != null || family != null || rotation != 0.0 || size != null || style != null || weight != null;
	}
}
