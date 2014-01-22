package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;

/**
 * <p>
 * Annotation graphics attribute class containing the attributes offset, fill, line and font for all kinds of annotations.
 * </p>
 * 
 * @author Adrian Lange
 */
public class AnnotationGraphics extends AbstractObjectGraphics {

	/** Default offset attribute */
	public static final Offset DEFAULT_OFFSET = new Offset();
	/** Default fill attribute */
	public static final Fill DEFAULT_FILL = new Fill();
	/** Default line attribute */
	public static final Line DEFAULT_LINE = new Line();
	/** Default font attribute */
	public static final Font DEFAULT_FONT = new Font();
	/** Default visibility */
	public static final boolean DEFAULT_VISIBILITY = true;

	private Offset offset;
	private Fill fill;
	private Line line;
	private Font font;
	private boolean visibility;

	/**
	 * Creates a new annotation graphics object with default values.
	 */
	public AnnotationGraphics() throws ParameterException {
		setOffset(DEFAULT_OFFSET);
		setFill(DEFAULT_FILL);
		setLine(DEFAULT_LINE);
		setFont(DEFAULT_FONT);
		setVisibility(DEFAULT_VISIBILITY);
	}

	/**
	 * Creates a new annotation graphics object with the specified values.
	 */
	public AnnotationGraphics(Offset offset, Fill fill, Line line, Font font, boolean visibility) throws ParameterException {
		setOffset(offset);
		setFill(fill);
		setLine(line);
		setFont(font);
		setVisibility(visibility);
	}

	/**
	 * @return the offset
	 */
	public Offset getOffset() {
		return offset;
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
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	@Override
	public boolean hasContent() {
		return offset.hasContent() || fill.hasContent() || line.hasContent() || font.hasContent();
	}

	/**
	 * @return visibility
	 */
	public boolean isVisible() {
		return visibility;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(Offset offset) throws ParameterException {
		Validate.notNull(offset);
		this.offset = offset;
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

	/**
	 * @param font
	 *            the font to set
	 */
	public void setFont(Font font) throws ParameterException {
		Validate.notNull(font);
		this.font = font;
	}

	/**
	 * @param visibility
	 *            the visibility to set
	 */
	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();

		boolean empty = true;

		str.append("[");

		if (offset != DEFAULT_OFFSET) {
			str.append(offset);
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
			empty = false;
		}
		if (font != DEFAULT_FONT) {
			if (!empty)
				str.append(",");
			str.append(font);
			empty = false;
		}
		str.append(",");
		str.append(visibility ? "visible" : "invisible");

		str.append("]");

		return str.toString();
	}
}
