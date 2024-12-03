package teamproject.aipro.domain.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import teamproject.aipro.domain.chat.dto.request.AiRequest;
import teamproject.aipro.domain.chat.dto.request.ChatRequest;
import teamproject.aipro.domain.chat.dto.response.ChatResponse;
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

	// RestTmeplate으로 AI 서버의 API 호출
	// 응답을 String 값으로 가져옴
	public ChatResponse question(ChatRequest request, String catalogId, String userId) {
		RestTemplate restTemplate = new RestTemplate();
		AiRequest aiRequest = new AiRequest();
		aiRequest.setUserId(userId);
		aiRequest.setQuestion(request.getQuestion());
		aiRequest.setRole(roleService.getRole(userId));
		aiRequest.setChatHistory(chatHistoryService.getChatHistory(userId));

		try {
			String response = restTemplate.postForObject(uri, aiRequest, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response);

			String message = rootNode.path("message").asText();
			// ChatHistory 저장
			chatHistoryService.saveChatHistory(request.getQuestion(), message, catalogId);

			return new ChatResponse(message, catalogId);
		} catch (Exception e) {
			System.err.println("Error occurred while calling AI server: " + e.getMessage());
			return new ChatResponse("Error: Unable to get response from AI server.", catalogId);// 임시
		}
	}
}
