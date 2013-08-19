package de.uni.freiburg.iig.telematik.sepia.graphic;

import java.awt.Color;

import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Position;

/**
 * TODO
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class TokenGraphics implements ObjectGraphics {

	/** Default position */
	public static final Color DEFAULT_COLOR = Color.BLACK;
	/** Default position */
	public static final String DEFAULT_COLOR_NAME = "black";
	/** Default position */
	public static final Position DEFAULT_TOKENPOSITION = new Position();

	private Color color;
	private String colorName;
	private Position tokenposition;

	public TokenGraphics() {
		color = DEFAULT_COLOR;
		colorName = DEFAULT_COLOR_NAME;
		tokenposition = DEFAULT_TOKENPOSITION;
	}

	public TokenGraphics(Color color, String colorName, Position tokenposition) {
		this.color = color;
		this.colorName = colorName;
		this.tokenposition = tokenposition;
	}

	public Color getColor() {
		return color;
	}

	public String getColorName() {
		return colorName;
	}

	public Position getTokenposition() {
		return tokenposition;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public void setTokenposition(Position tokenposition) {
		this.tokenposition = tokenposition;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();

		boolean empty = true;
		
		str.append("[");
		
		if (tokenposition != DEFAULT_TOKENPOSITION) {
			str.append(tokenposition);
			empty = false;
		}
		if (color != DEFAULT_COLOR) {
			if (!empty)
				str.append(",");
			str.append(color);
			empty = false;
		}
		if (colorName != DEFAULT_COLOR_NAME) {
			if (!empty)
				str.append(",");
			str.append(colorName);
		}
		
		str.append("]");
		
		return str.toString();
	}
}
