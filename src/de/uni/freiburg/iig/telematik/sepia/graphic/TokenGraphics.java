package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Position;

/**
 * TODO
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class TokenGraphics implements ObjectGraphics {

	/** Default position */
	public static final String DEFAULT_COLOR_NAME = "black";
	/** Default position */
	public static final Position DEFAULT_TOKENPOSITION = new Position();

	private String colorName;
	private Position tokenposition;

	public TokenGraphics() {
		colorName = DEFAULT_COLOR_NAME;
		tokenposition = DEFAULT_TOKENPOSITION;
	}

	public TokenGraphics(String colorName, Position tokenposition) {
		this.colorName = colorName;
		this.tokenposition = tokenposition;
	}

	public String getColorName() {
		return colorName;
	}

	public Position getTokenposition() {
		return tokenposition;
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
		if (colorName != DEFAULT_COLOR_NAME) {
			if (!empty)
				str.append(",");
			str.append(colorName);
		}
		
		str.append("]");
		
		return str.toString();
	}
}
