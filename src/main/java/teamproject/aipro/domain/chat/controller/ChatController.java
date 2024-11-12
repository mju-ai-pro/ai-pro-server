package teamproject.aipro.domain.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import teamproject.aipro.domain.chat.dto.request.ChatRequest;
import teamproject.aipro.domain.chat.dto.response.ChatResponse;
import teamproject.aipro.domain.chat.entity.ChatInventory;
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
	public ResponseEntity<ChatResponse> question(@RequestBody ChatRequest chatRequest, @RequestParam(required = false) String optionalParam) {

		System.out.println("optioanlParam = " + optionalParam);
		// optionalParam이 없으면 처리 로직 추가
		if(optionalParam == null || optionalParam.trim().isEmpty()) {
			ChatResponse response = chatHistoryService.summary(chatRequest);
			ChatInventory chatInventory = new ChatInventory(chatRequest.getUserId(),response.getMessage());
			Long chatInvId = chatHistoryService.saveChatInventory(chatInventory.getUserId(),chatInventory.getChatSummary()).getId();
			response = chatService.question(chatRequest,Long.toString(chatInvId));
			return ResponseEntity.ok(response);
		}

		ChatResponse response = chatService.question(chatRequest, optionalParam);
		return ResponseEntity.ok(response);
	}
}
