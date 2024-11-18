package teamproject.aipro.domain.chat.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import teamproject.aipro.domain.chat.dto.request.AiRequest;
import teamproject.aipro.domain.chat.dto.request.ChatRequest;
import teamproject.aipro.domain.chat.dto.response.ChatResponse;
import teamproject.aipro.domain.chat.entity.ChatHistory;
import teamproject.aipro.domain.role.service.RoleService;

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

    public ChatResponse question(ChatRequest request) {
        if (request == null || request.getUserId() == null || request.getQuestion() == null) {
            return new ChatResponse("Error: Invalid request. Missing required fields.");
        }

        RestTemplate restTemplate = new RestTemplate();
        AiRequest aiRequest = new AiRequest();

        try {
            aiRequest.setUserId(request.getUserId());
            aiRequest.setQuestion(request.getQuestion());
            aiRequest.setRole(getUserRoleWithFallback());

            List<String> chatHistory = convertChatHistoryToList(
                chatHistoryService.getChatHistory(request.getUserId())
            );
            aiRequest.setChatHistory(chatHistory);

            // REST API 호출 및 응답 처리
            String response = restTemplate.postForObject(uri, aiRequest, String.class);
            return parseResponse(response);

        } catch (RestClientException e) {
            System.err.println("Error occurred while calling AI server: " + e.getMessage());
            return new ChatResponse("Error: Unable to connect to AI server.");
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing AI server response: " + e.getMessage());
            return new ChatResponse("Error: Failed to process AI server response.");
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            return new ChatResponse("Error: An unexpected error occurred.");
        }
    }

	// TO-Do: AI 서버 일정시간 응답 지연 시 에러 <= 보류됨

    private List<String> convertChatHistoryToList(List<ChatHistory> chatHistories) {
        if (chatHistories == null || chatHistories.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }

        try {
            return chatHistories.stream()
                .map(history -> {
                    String question = history.getQuestion() != null ? history.getQuestion() : "N/A";
                    String response = history.getResponse() != null ? history.getResponse() : "N/A";
                    return "User: " + question + "\nBot: " + response;
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error converting chat history: " + e.getMessage());
            return Collections.emptyList(); // 오류 시 빈 리스트 반환
        }
    }

    private ChatResponse parseResponse(String response) throws JsonProcessingException {
        if (response == null || response.isBlank()) {
            return new ChatResponse("Error: AI server returned an empty response.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

        if (rootNode == null || !rootNode.has("message")) {
            return new ChatResponse("Error: AI server response is missing the 'message' field.");
        }

        String message = rootNode.path("message").asText();
        return new ChatResponse(message);
    }

    private String getUserRoleWithFallback() {
        try {
            return roleService.getRole();
        } catch (Exception e) {
            System.err.println("Error fetching user role: " + e.getMessage());
            return "default"; // 기본값 설정
        }
    }
}
