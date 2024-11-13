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
	public ResponseEntity<ChatResponse> question(@RequestBody ChatRequest chatRequest,
		@RequestParam(required = false) String catalogId) {
		ChatResponse response = (catalogId == null || catalogId.trim().isEmpty())
			? processNewCatalogRequest(chatRequest)
			: processExistingCatalogRequest(chatRequest, catalogId);
		return ResponseEntity.ok(response);
	}

	private ChatResponse processNewCatalogRequest(ChatRequest chatRequest) {
		ChatResponse response = chatHistoryService.summary(chatRequest);
		Long newCatalogId = createNewCatalog(chatRequest, response.getMessage());
		return chatService.question(chatRequest, Long.toString(newCatalogId));
	}

	private Long createNewCatalog(ChatRequest chatRequest, String summaryMessage) {
		ChatCatalog chatCatalog = new ChatCatalog(chatRequest.getUserId(), summaryMessage);
		return chatHistoryService.saveChatCatalog(chatCatalog.getUserId(), chatCatalog.getChatSummary()).getId();
	}

	private ChatResponse processExistingCatalogRequest(ChatRequest chatRequest, String catalogId) {
		return chatService.question(chatRequest, catalogId);
	}

}
