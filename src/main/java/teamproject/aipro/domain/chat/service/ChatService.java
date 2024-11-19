package teamproject.aipro.domain.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

	// RestTmeplate으로 AI 서버의 API 호출
	// 응답을 String 값으로 가져옴
	public ChatResponse question(ChatRequest request, String opt) {
		RestTemplate restTemplate = new RestTemplate();
		AiRequest aiRequest = new AiRequest();
		aiRequest.setUserId("a"); //수정
		aiRequest.setQuestion(request.getQuestion());
		aiRequest.setRole(roleService.getRole());
		aiRequest.setChatHistory(new ArrayList<>());

		try {
			String response = restTemplate.postForObject(uri, aiRequest, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response);

			String message = rootNode.path("message").asText();
			//ChatHistory 저장
			chatHistoryService.saveChatHistory(request.getQuestion(), message, opt);

			return new ChatResponse(message,opt);
		} catch (Exception e) {
			System.err.println("Error occurred while calling AI server: " + e.getMessage());
			return new ChatResponse("Error: Unable to get response from AI server.",opt);//임시
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
