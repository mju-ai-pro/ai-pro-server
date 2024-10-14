package teamproject.AIPro.domain.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teamproject.AIPro.domain.chat.dto.request.ChatHistoryRequest;
import teamproject.AIPro.domain.chat.entity.ChatHistory;
import teamproject.AIPro.domain.chat.service.ChatHistoryService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatHistoryController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    @PostMapping("/saveQuestion")
    public ChatHistory saveUserQuestion(@RequestBody ChatHistoryRequest request) {
        return chatHistoryService.saveChatHistory(request.getUserId(), request.getQuestion(), request.getResponse());
    }
}
