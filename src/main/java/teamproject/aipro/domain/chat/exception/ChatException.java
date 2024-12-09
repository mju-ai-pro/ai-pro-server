package teamproject.aipro.domain.chat.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ChatException extends RuntimeException {

	private final HttpStatus status;
	private final ErrorType errorType;

	public enum ErrorType {
		INVALID_REQUEST,
		AI_SERVER_ERROR,
		RESPONSE_PARSING_ERROR,
		UNAUTHORIZED,
		UNKNOWN_ERROR
	}

	public ChatException(String message, HttpStatus status) {
		super(message);
		this.status = status;
		this.errorType = ErrorType.UNKNOWN_ERROR;
	}

	public ChatException(String message) {
		this(message, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.UNKNOWN_ERROR);
	}

	public ChatException(String message, ErrorType errorType) {
		this(message, HttpStatus.INTERNAL_SERVER_ERROR, errorType);
	}

	public ChatException(String message, Throwable cause) {
		this(message, cause, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.UNKNOWN_ERROR);
	}

	public ChatException(String message, Throwable cause, ErrorType errorType) {
		this(message, cause, HttpStatus.INTERNAL_SERVER_ERROR, errorType);
	}

	public ChatException(String message, HttpStatus status, ErrorType errorType) {
		super(message);
		this.status = status;
		this.errorType = errorType;
	}

	public ChatException(String message, Throwable cause, HttpStatus status, ErrorType errorType) {
		super(message, cause);
		this.status = status;
		this.errorType = errorType;
	}
}
