package teamproject.aipro.chat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import teamproject.aipro.domain.chat.dto.request.ChatRequest;
import teamproject.aipro.domain.chat.dto.response.ChatResponse;
import teamproject.aipro.domain.chat.service.ChatHistoryService;
import teamproject.aipro.domain.chat.service.ChatService;
import teamproject.aipro.domain.role.service.RoleService;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @Mock
    private ChatHistoryService chatHistoryService;

    @Mock
    private RoleService roleService;

    @Mock
    private RestTemplate restTemplate;

    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        chatService = new ChatService(chatHistoryService, roleService, restTemplate);

        ReflectionTestUtils.setField(chatService, "uri", "http://test-ai-server.com/api");

        when(roleService.getRole(anyString())).thenReturn("Test Role");
    }

    @Test
    @DisplayName(value = "유효한 AiRequest - 채팅 성공")
    public void whenValidRequest_thenSucess() throws JsonProcessingException {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");
        String opt = "testOpt";

        // AI 서버 응답 Mock
        String mockJsonResponse = new ObjectMapper()
                .writeValueAsString(java.util.Map.of("message", "Test response"));
        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenReturn(mockJsonResponse);

        ChatResponse response = chatService.question(chatRequest, opt);

        assertNotNull(response);
        assertEquals("Test response", response.getMessage());
        assertEquals(opt, response.getCatalogId());

        // saveChatHistory 확인
        verify(chatHistoryService).saveChatHistory(
                eq("Test question"),
                eq("Test response"),
                eq(opt));
    }

    @Test
    @DisplayName(value = "AI 서버 응답 없음 - 서버 에러")
    public void whenEmptyReponse_thenInternalServerError() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");
        String opt = "testOpt";

        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenReturn("");

        ChatResponse response = chatService.question(chatRequest, opt);

        assertEquals("Error: Unable to get response from AI server.", response.getMessage());
    }

    @Test
    @DisplayName(value = "REST 예외 발생 - REST 에러")
    public void whenRestClientException_thenRestError() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");
        String opt = "testOpt";

        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenThrow(new RestClientException("Connection failed"));

        ChatResponse response = chatService.question(chatRequest, opt);

        assertEquals("Error: Unable to reach AI server.", response.getMessage());
    }

    @Test
    @DisplayName(value = "JSON 형식 잘못됨 - JSON 처리 에러")
    public void testJsonProcessingException() throws JsonProcessingException {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");
        String opt = "testOpt";

        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenReturn("{invalid json");

        ChatResponse response = chatService.question(chatRequest, opt);

        assertEquals("Error: Unable to process AI response.", response.getMessage());
    }
}