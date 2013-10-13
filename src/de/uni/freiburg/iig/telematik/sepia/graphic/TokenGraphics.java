package de.uni.freiburg.iig.telematik.sepia.graphic;

import de.uni.freiburg.iig.telematik.sepia.graphic.attributes.Position;

/**
 * <p>
 * Token graphics attribute class containing the attributes color name and token position.
 * </p>
 * 
 * @author Thomas Stocker
 * @author Adrian Lange
 */
public class TokenGraphics implements ObjectGraphics {

	/** Default position */
	public static final Position DEFAULT_TOKENPOSITION = new Position();

	private Position tokenposition;

	public TokenGraphics() {
		tokenposition = DEFAULT_TOKENPOSITION;
	}

	public TokenGraphics(Position tokenposition) {
		this.tokenposition = tokenposition;
	}

	public Position getTokenposition() {
		return tokenposition;
	}

	public void setTokenposition(Position tokenposition) {
		this.tokenposition = tokenposition;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("[");

		if (tokenposition != DEFAULT_TOKENPOSITION) {
			str.append(tokenposition);
		}

		str.append("]");

		return str.toString();
	}
}
