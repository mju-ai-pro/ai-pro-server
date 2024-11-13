package teamproject.aipro.domain.chat.controller;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import teamproject.aipro.domain.chat.dto.response.ChatHistoryResponse;
import teamproject.aipro.domain.chat.dto.response.ChatCatalogResponse;
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
	public List<ChatHistoryResponse> getChatHistory(@RequestParam String catalogId) {
		return chatHistoryService.getChatHistory(catalogId);
	}

	@GetMapping("/getChatCatalog")
	public List<ChatCatalogResponse> getChatCatalog(){
		return chatHistoryService.getChatCatalog();
	}
  
}
