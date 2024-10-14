package teamproject.AIPro.domain.chat.controller;

import teamproject.AIPro.domain.chat.dto.request.ChatRequest;
import teamproject.AIPro.domain.chat.dto.response.ChatResponse;
import teamproject.AIPro.domain.chat.service.ChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
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
