package teamproject.aipro.domain.chat.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalChatExceptionHandler {

	@ExceptionHandler(ChatException.class)
	public ResponseEntity<Object> handleChatException(ChatException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", ex.getStatus().value());

		return new ResponseEntity<>(body, ex.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "An unexpected error occurred: " + ex.getMessage());
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
