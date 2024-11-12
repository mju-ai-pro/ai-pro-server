package teamproject.aipro.domain.chat.controller;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import teamproject.aipro.domain.chat.dto.request.ChatHistoryRequest;
import teamproject.aipro.domain.chat.dto.response.ChatHistoryResponse;
import teamproject.aipro.domain.chat.dto.response.ChatInventoryResponse;
import teamproject.aipro.domain.chat.entity.ChatHistory;
import teamproject.aipro.domain.chat.entity.ChatInventory;
import teamproject.aipro.domain.chat.service.ChatHistoryService;
@RestController
@RequestMapping("/api")
public class ChatHistoryController {

	@Autowired
	private ChatHistoryService chatHistoryService;

//	@PostMapping("/saveQuestion")
//	public ChatHistory saveUserQuestion(@RequestBody ChatHistoryRequest request) {
//		return chatHistoryService.saveChatHistory(request.getQuestion(), request.getResponse());
//	}

	@GetMapping("/getChatHistory")
	public List<ChatHistoryResponse> getChatHistory(@RequestParam String chatInvId) {
		return chatHistoryService.getChatHistory(chatInvId);
	}
	@GetMapping("/getChatInventory")
	public List<ChatInventoryResponse> getChatInventory(){
		return chatHistoryService.getChatInventory();
	}

}
