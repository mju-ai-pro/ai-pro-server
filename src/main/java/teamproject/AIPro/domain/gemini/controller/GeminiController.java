package teamproject.AIPro.domain.gemini.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import teamproject.AIPro.domain.gemini.service.GeminiService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/chat")
    public ResponseEntity<?> gemini(@RequestBody Map<String, String> requestBody) {
        try {
            String chatMessage = requestBody.get("chat");
            String chatinstruction = requestBody.get("instruction");
            return ResponseEntity.ok().body(geminiService.getContents(chatinstruction,chatMessage));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public String test() {
        return "test";
    }
}