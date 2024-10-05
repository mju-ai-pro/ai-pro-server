package teamproject.AIPro.domain.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teamproject.AIPro.domain.member.dto.request.ChatHistoryRequest;
import teamproject.AIPro.domain.member.dto.request.ChatRequest;
import teamproject.AIPro.domain.member.entity.ChatHistory;
import teamproject.AIPro.domain.member.repository.ChatHistoryRepository;
import teamproject.AIPro.domain.member.service.ChatHistoryService;

@RestController
@RequestMapping("/api")
public class ChatHistoryController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    @PostMapping("/saveQuestion")
    public ChatHistory saveUserQuestion(@RequestBody ChatHistoryRequest request) {
        return chatHistoryService.saveChatHistory(request.getUserId(), request.getQuestion(), request.getResponse());
    }
}
