package teamproject.AIPro.domain.member.controller;

import teamproject.AIPro.domain.member.dto.request.ChatRequest;
import teamproject.AIPro.domain.member.dto.response.ChatResponse;
import teamproject.AIPro.domain.member.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
