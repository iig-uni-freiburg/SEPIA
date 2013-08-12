package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Offset;

/**
 * <p>
 * Annotation graphics attribute class containing the attributes offset, fill, line and font for all kinds of annotations.
 * </p>
 * 
 * @author Adrian Lange
 */
public class AnnotationGraphics {

	/** Default offset attribute */
	public static final Offset DEFAULT_OFFSET = new Offset();
	/** Default fill attribute */
	public static final Fill DEFAULT_FILL = new Fill();
	/** Default line attribute */
	public static final Line DEFAULT_LINE = new Line();
	/** Default font attribute */
	public static final Font DEFAULT_FONT = new Font();

	private Offset offset;
	private Fill fill;
	private Line line;
	private Font font;

	/**
	 * Creates a new annotation graphics object with default values.
	 */
	public AnnotationGraphics() {
		setOffset(DEFAULT_OFFSET);
		setFill(DEFAULT_FILL);
		setLine(DEFAULT_LINE);
		setFont(DEFAULT_FONT);
	}

	/**
	 * Creates a new annotation graphics object with the specified values.
	 */
	public AnnotationGraphics(Offset offset, Fill fill, Line line, Font font) {
		setOffset(offset);
		setFill(fill);
		setLine(line);
		setFont(font);
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

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(Offset offset) {
		this.offset = offset;
	}

	/**
	 * @param fill
	 *            the fill to set
	 */
	public void setFill(Fill fill) {
		this.fill = fill;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public void setLine(Line line) {
		this.line = line;
	}

	/**
	 * @param font
	 *            the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}
}
