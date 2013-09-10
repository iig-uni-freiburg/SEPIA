package de.uni.freiburg.iig.telematik.sepia.serialize;

import de.invation.code.toval.parser.ParserException;

public class SerializationException extends ParserException {

	private static final long serialVersionUID = -8045175661721715415L;
	
	private final String msg_unsupportedNetType = "Unsupported net type";
	private final String msg_unsupportedFormat = "Unsupported serialization format";

	private ErrorCode errorCode = null;
	private Object object = null;

	public SerializationException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public SerializationException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public SerializationException(ErrorCode errorCode, Object object) {
		super();
		this.errorCode = errorCode;
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	@Override
	public String getMessage() {
		StringBuffer msg = new StringBuffer();

		switch (errorCode) {
		case UNSUPPORTED_NET_TYPE:
			msg.append(msg_unsupportedNetType);
			break;
		case UNSUPPORTED_FORMAT:
			msg.append(msg_unsupportedFormat);
			break;
		default:
			break;
		}
		if (msg != null) {
			if (object == null) {
				msg.append(".");
			} else {
				msg.append(": ").append(object.toString());
			}
		}

		String msgSuper = super.getMessage();

		if (msg.length() == 0)
			return msgSuper;

		if (msgSuper != null)
			return msg.append("\n").append(msgSuper).toString();

		return msg.toString();
	}

	public SerializationException.ErrorCode getErrorCode() {
		return errorCode;
	}

	public enum ErrorCode {
		UNSUPPORTED_NET_TYPE, UNSUPPORTED_FORMAT;
	}

}
