package teamproject.aipro.domain.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import teamproject.aipro.domain.chat.dto.response.ChatCatalogResponse;
import teamproject.aipro.domain.chat.dto.response.ChatHistoryResponse;
import teamproject.aipro.domain.chat.service.ChatHistoryService;

@RestController
@RequestMapping("/api")
public class ChatHistoryController {

	@Autowired
	private ChatHistoryService chatHistoryService;

	@GetMapping("/getChatHistory")
	public List<ChatHistoryResponse> getChatHistory(@RequestParam String catalogId) {
		return chatHistoryService.getChatHistory(catalogId);
	}

	@GetMapping("/getChatCatalog")
	public List<ChatCatalogResponse> getChatCatalog() {
		return chatHistoryService.getChatCatalog();
	}

}
