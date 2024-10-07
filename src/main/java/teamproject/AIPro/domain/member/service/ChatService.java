package teamproject.AIPro.domain.member.service;

import teamproject.AIPro.domain.member.dto.request.AiRequest;
import teamproject.AIPro.domain.member.dto.request.ChatRequest;
import teamproject.AIPro.domain.member.dto.response.ChatResponse;
import teamproject.AIPro.domain.member.entity.ChatHistory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import teamproject.AIPro.domain.role.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatHistoryService chatHistoryService;
    private final RoleService roleService;


    @Value("${ai.uri}")
    private String uri;

    public ChatService(ChatHistoryService chatHistoryService, RoleService roleService) {
        this.chatHistoryService = chatHistoryService;
        this.roleService = roleService;
    }

    // RestTmeplate으로 AI 서버의 API 호출
    // 응답을 String 값으로 가져옴
    public ChatResponse question(ChatRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        AiRequest aiRequest = new AiRequest();
        aiRequest.setUserId(request.getUserId());
        aiRequest.setQuestion(request.getQuestion());
        aiRequest.setRole(roleService.getRole());

        List<String> chatHistory = convertChatHistoryToList(chatHistoryService.getChatHistory(request.getUserId()));
        aiRequest.setChatHistory(chatHistory); 

        try {
            String response = restTemplate.postForObject(uri, aiRequest, String.class);
            return new ChatResponse(response);
        } catch (Exception e) {
            System.err.println("Error occurred while calling AI server: " + e.getMessage());
            return new ChatResponse("Error: Unable to get response from AI server.");
        }
    }

    private List<String> convertChatHistoryToList(List<ChatHistory> chatHistories) {
        if (chatHistories == null || chatHistories.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        // 각 대화 내역을 "User: 질문\nBot: 응답" 형태의 문자열로 변환하여 리스트로 반환
        return chatHistories.stream()
            .map(history -> "User: " + history.getQuestion() + "\nBot: " + history.getResponse())
            .collect(Collectors.toList());
    }
}