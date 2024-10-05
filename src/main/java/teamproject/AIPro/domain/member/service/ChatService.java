package teamproject.AIPro.domain.member.service;

import teamproject.AIPro.domain.member.dto.request.AiRequest;
import teamproject.AIPro.domain.member.dto.request.ChatRequest;
import teamproject.AIPro.domain.member.dto.response.ChatResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    private ChatHistoryService chatHistoryService;

    @Value("${ai.uri}")
    private String uri;

    public ChatService(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    // RestTmeplate으로 AI 서버의 API 호출
    // 응답을 String 값으로 가져옴
    public ChatResponse question(ChatRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        AiRequest aiRequest = new AiRequest();
        aiRequest.setUserId(request.getUserId());
        aiRequest.setQuestion(request.getQuestion());
        aiRequest.setRole(request.getRole());
        aiRequest.setChatHistory(chatHistoryService.getChatHistory(request.getUserId()));
        ChatResponse response = new ChatResponse(restTemplate.postForObject(uri, request,String.class));

        return response;    
    }
}