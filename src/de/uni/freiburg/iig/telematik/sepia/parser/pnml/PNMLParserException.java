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

	@Override
	public Object getObject() {
		return object;
	}

	@Override
	protected StringBuffer checkErrorCode() {
		StringBuffer buffer = new StringBuffer();
		switch (errorCode) {
		case INVALID_FLOW_RELATION:
			buffer.append(msg_InvalidFlowRelation);
			break;
		case INVALID_NET_TYPE:
			buffer.append(msg_InvalidNetType);
			break;
		case MISSING_NET_TAG:
			buffer.append(msg_MissingNetTag);
			break;
		case MISSING_NET_TYPE_ATTRIBUTE:
			buffer.append(msg_MissingNetTypeAttribute);
			break;
		case NOT_ON_ONE_PAGE:
			buffer.append(msg_NotOnOnePage);
			break;
		case VALIDATION_FAILED:
			buffer.append(msg_ValidationFailed);
			break;
		case VALIDATION_CONFIGURATION_ERROR:
			buffer.append(msg_ValidationConfigurationError);
			break;
		}
		return buffer;
	}

	public enum ErrorCode {
		INVALID_FLOW_RELATION, INVALID_NET_TYPE, MISSING_NET_TAG, MISSING_NET_TYPE_ATTRIBUTE, NOT_ON_ONE_PAGE, VALIDATION_FAILED, VALIDATION_CONFIGURATION_ERROR;
	}
}
