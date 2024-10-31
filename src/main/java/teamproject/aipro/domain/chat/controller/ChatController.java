package teamproject.aipro.domain.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teamproject.aipro.domain.chat.dto.request.ChatRequest;
import teamproject.aipro.domain.chat.dto.response.ChatResponse;
import teamproject.aipro.domain.chat.service.ChatService;

@CrossOrigin(origins = {"http://localhost:3000", "https://ai-pro-fe.vercel.app"})
@RestController
@RequestMapping("/api/chat")
public class ChatController {

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@PostMapping("/question")
	public ResponseEntity<ChatResponse> question(@RequestBody ChatRequest chatRequest) {
		ChatResponse response = chatService.question(chatRequest);
		return ResponseEntity.ok(response);
	}
}
