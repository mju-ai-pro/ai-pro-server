package teamproject.aipro.domain.chat.controller;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teamproject.aipro.domain.chat.dto.request.ChatHistoryRequest;
import teamproject.aipro.domain.chat.entity.ChatHistory;
import teamproject.aipro.domain.chat.service.ChatHistoryService;
@RestController
@RequestMapping("/api")
public class ChatHistoryController {

	@Autowired
	private ChatHistoryService chatHistoryService;

	@PostMapping("/saveQuestion")
	public ChatHistory saveUserQuestion(@RequestBody ChatHistoryRequest request) {
		return chatHistoryService.saveChatHistory(request.getUserId(), request.getQuestion(), request.getResponse());
	}
	@PostMapping("/getChatHistory")
	public List<ChatHistory> getChatHistory(@RequestBody TempDto tempDto){
		return chatHistoryService.getChatHistory(tempDto.getUserId());
	}
	@Data
	@Getter@Setter
	public static class TempDto{
		String userId;
	}
}
