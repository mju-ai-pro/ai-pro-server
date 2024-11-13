package teamproject.aipro.domain.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import teamproject.aipro.domain.chat.dto.request.ChatRequest;
import teamproject.aipro.domain.chat.dto.response.ChatResponse;
import teamproject.aipro.domain.chat.entity.ChatCatalog;
import teamproject.aipro.domain.chat.service.ChatHistoryService;
import teamproject.aipro.domain.chat.service.ChatService;

@CrossOrigin(origins = {"http://localhost:3000", "https://ai-pro-fe.vercel.app"})
@RestController
@RequestMapping("/api/chat")
public class ChatController {

	private final ChatService chatService;
	private final ChatHistoryService chatHistoryService;

	public ChatController(ChatService chatService, ChatHistoryService chatHistoryService) {
		this.chatService = chatService;
		this.chatHistoryService = chatHistoryService;
	}

	@PostMapping("/question")
	public ResponseEntity<ChatResponse> question(@RequestBody ChatRequest chatRequest, @RequestParam(required = false) String catalogId) {
		// optionalParam이 없으면 처리 로직 추가
		if(catalogId == null || catalogId.trim().isEmpty()) {
			ChatResponse response = chatHistoryService.summary(chatRequest);
			ChatCatalog chatCatalog = new ChatCatalog(chatRequest.getUserId(),response.getMessage());
			Long chatCatalogId = chatHistoryService.saveChatCatalog(chatCatalog.getUserId(), chatCatalog.getChatSummary()).getId();
			response = chatService.question(chatRequest,Long.toString(chatCatalogId));
			return ResponseEntity.ok(response);
		}

		ChatResponse response = chatService.question(chatRequest, catalogId);
		return ResponseEntity.ok(response);
	}
}
