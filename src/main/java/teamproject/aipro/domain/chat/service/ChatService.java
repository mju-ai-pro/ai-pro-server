package teamproject.aipro.domain.chat.service;

import java.util.List;

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
import teamproject.aipro.domain.chat.entity.ChatCatalog;
import teamproject.aipro.domain.chat.exception.ChatException;
import teamproject.aipro.domain.role.service.RoleService;

@Service
public class ChatService {
	private final ChatHistoryService chatHistoryService;
	private final RoleService roleService;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${ai.uri}")
	private String uri;

	public ChatService(ChatHistoryService chatHistoryService,
		RoleService roleService,
		RestTemplate restTemplate,
		ObjectMapper objectMapper) {
		this.chatHistoryService = chatHistoryService;
		this.roleService = roleService;
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	public ChatResponse question(ChatRequest request, String catalogId, String userId) {
		validateInputs(request, catalogId, userId);

		AiRequest aiRequest = prepareAiRequest(request, catalogId, userId);

		String response = callAiServer(aiRequest);
		String message = extractMessageFromResponse(response);

		chatHistoryService.saveChatHistory(request.getQuestion(), message, catalogId);

		return new ChatResponse(message, catalogId);
	}

	private void validateInputs(ChatRequest request, String catalogId, String userId) {
		if (request == null || request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
			throw new ChatException("Invalid chat request: Question cannot be null or empty");
		}

		if (userId == null || userId.trim().isEmpty()) {
			throw new ChatException("Invalid user ID");
		}

		if (catalogId == null || catalogId.trim().isEmpty()) {
			throw new ChatException("Invalid catalog ID");
		}
	}

	private AiRequest prepareAiRequest(ChatRequest request, String catalogId, String userId) {
		AiRequest aiRequest = new AiRequest();
		aiRequest.setUserId(userId);
		aiRequest.setQuestion(request.getQuestion());
		aiRequest.setRole(roleService.getRole(userId));

		List<String> chatHistory = chatHistoryService.getChatHistoryAsStringList(catalogId);
		aiRequest.setChatHistory(chatHistory);

		return aiRequest;
	}

	private String callAiServer(AiRequest aiRequest) {
		try {
			return restTemplate.postForObject(uri, aiRequest, String.class);
		} catch (RestClientException e) {
			throw new ChatException("Failed to connect to AI server", e);
		}
	}

	private String extractMessageFromResponse(String response) {
		try {

			if (response == null || response.trim().isEmpty()) {
				throw new ChatException("Received empty response from AI server");
			}

			JsonNode rootNode = objectMapper.readTree(response);
			JsonNode messageNode = rootNode.path("message");
			if (messageNode.isMissingNode() || messageNode.isNull()) {
				throw new ChatException("No 'message' field in AI server response");
			}

			String message = messageNode.asText();
			if (message == null || message.trim().isEmpty()) {
				throw new ChatException("Empty 'message' in AI server response");
			}

			return message;
		} catch (JsonProcessingException e) {
			throw new ChatException("Invalid response format from AI server", e);
		}
	}

	public ChatResponse processNewCatalogRequest(ChatRequest chatRequest, String userId) {
		ChatResponse response = chatHistoryService.summary(chatRequest);
		Long newCatalogId = createNewCatalog(userId, response.getMessage());
		return question(chatRequest, String.valueOf(newCatalogId), userId);
	}

	public ChatResponse processExistingCatalogRequest(ChatRequest chatRequest, String catalogId, String userId) {
		return question(chatRequest, catalogId, userId);
	}

	private Long createNewCatalog(String userId, String summaryMessage) {
		ChatCatalog chatCatalog = new ChatCatalog(userId, summaryMessage);
		return chatHistoryService.saveChatCatalog(chatCatalog.getUserId(), chatCatalog.getChatSummary()).getId();
	}
}
