package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import de.invation.code.toval.parser.ParserException;

public class PNMLParserException extends ParserException {

	private static final long serialVersionUID = -2829511252560246958L;

	private final String msg_InvalidFlowRelation = "Invalid flow relation";
	private final String msg_InvalidNetType = "Unknown net type";
	private final String msg_MissingNetTag = "Missing net tag";
	private final String msg_MissingNetTypeAttribute = "Missing net type attribute";
	private final String msg_NotOnOnePage = "Net must be defined on only one page";
	private final String msg_ValidationFailed = "PNML validation failed";
	private final String msg_ValidationConfigurationError = "Misconfigured validation";

	private ErrorCode errorCode = null;
	private Object object = null;

	public PNMLParserException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public PNMLParserException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public PNMLParserException(ErrorCode errorCode, Object object) {
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
		case INVALID_FLOW_RELATION:
			msg.append(msg_InvalidFlowRelation);
			break;
		case INVALID_NET_TYPE:
			msg.append(msg_InvalidNetType);
			break;
		case MISSING_NET_TAG:
			msg.append(msg_MissingNetTag);
			break;
		case MISSING_NET_TYPE_ATTRIBUTE:
			msg.append(msg_MissingNetTypeAttribute);
			break;
		case NOT_ON_ONE_PAGE:
			msg.append(msg_NotOnOnePage);
			break;
		case VALIDATION_FAILED:
			msg.append(msg_ValidationFailed);
			break;
		case VALIDATION_CONFIGURATION_ERROR:
			msg.append(msg_ValidationConfigurationError);
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

	public PNMLParserException.ErrorCode getErrorCode() {
		return errorCode;
	}

	public enum ErrorCode {
		INVALID_FLOW_RELATION, INVALID_NET_TYPE, MISSING_NET_TAG, MISSING_NET_TYPE_ATTRIBUTE, NOT_ON_ONE_PAGE, VALIDATION_FAILED, VALIDATION_CONFIGURATION_ERROR;
	}

}
