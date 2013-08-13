package de.uni.freiburg.iig.telematik.sepia.parser;

import de.invation.code.toval.parser.ParserException;

public class PNMLParserException extends ParserException {
	
	private static final long serialVersionUID = -2829511252560246958L;

	private final String msg_InvalidNetType = "Unknown net type";
	private final String msg_MissingNetTag = "Missing net tag";
	private final String msg_MissingNetTypeAttribute = "Missing net type attribute";
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
	
	public Object getObject(){
		return object;
	}
	
	@Override
	public String getMessage(){
		String msg = null;
		
		switch(errorCode){
			case INVALID_NET_TYPE: 
				msg = msg_InvalidNetType;
				break;
			case MISSING_NET_TAG: 
				msg = msg_MissingNetTag;
				break;
			case MISSING_NET_TYPE_ATTRIBUTE: 
				msg = msg_MissingNetTypeAttribute;
				break;
			case VALIDATION_FAILED: 
				msg = msg_ValidationFailed;
				break;
			case VALIDATION_CONFIGURATION_ERROR: 
				msg = msg_ValidationConfigurationError;
				break;
		}
		if(msg != null){
			if(object == null){
				msg = msg.concat(".");
			} else {
				msg.concat(": ").concat(object.toString());
			}
		}
		
		String msgSuper = super.getMessage();
	
		if(msg == null)
			return msgSuper;
		
		if(msgSuper != null)
			return msg + '\n' + msgSuper;
		
		return msg;
	}
	
	public PNMLParserException.ErrorCode getErrorCode(){
		return errorCode;
	}
	
	public enum ErrorCode { 
		INVALID_NET_TYPE, MISSING_NET_TAG, MISSING_NET_TYPE_ATTRIBUTE, VALIDATION_FAILED, VALIDATION_CONFIGURATION_ERROR;
	}

}
