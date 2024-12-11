package teamproject.aipro.domain.chat.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import teamproject.aipro.domain.chat.dto.request.ChatRequest;
import teamproject.aipro.domain.chat.dto.response.ChatResponse;
import teamproject.aipro.domain.chat.exception.ChatException;
import teamproject.aipro.domain.chat.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@PostMapping("/question")
	public ResponseEntity<ChatResponse> question(
		Principal principal,
		@RequestBody ChatRequest chatRequest,
		@RequestParam(required = false) String catalogId) {

		if (principal == null) {
			throw new ChatException("Unauthorized: User authentication failed.", HttpStatus.UNAUTHORIZED);
		}

		String userId = principal.getName();

		if (userId == null || userId.trim().isEmpty()) {
			throw new ChatException("Invalid User: User ID cannot be null or empty", HttpStatus.FORBIDDEN);
		}

		if (chatRequest == null || chatRequest.getQuestion() == null || chatRequest.getQuestion().trim().isEmpty()) {
			throw new ChatException("Invalid Request: Question cannot be null or empty", HttpStatus.BAD_REQUEST);
		}

		try {
			ChatResponse response = (catalogId == null || catalogId.trim().isEmpty())
				? chatService.processNewCatalogRequest(chatRequest, userId)
				: chatService.processExistingCatalogRequest(chatRequest, catalogId, userId);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			throw new ChatException("Error processing chat request: " + e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
