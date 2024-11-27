package teamproject.aipro.chat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import teamproject.aipro.domain.chat.controller.ChatController;
import teamproject.aipro.domain.chat.dto.request.ChatRequest;
import teamproject.aipro.domain.chat.dto.response.ChatResponse;
import teamproject.aipro.domain.chat.entity.ChatCatalog;
import teamproject.aipro.domain.chat.service.ChatHistoryService;
import teamproject.aipro.domain.chat.service.ChatService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @Mock
    private ChatHistoryService chatHistoryService;

    @InjectMocks
    private ChatController chatController;

    private final String secretKey = "test-secret-key-2432-gardgsdfge-sgdf-dgds-sdfgd-dgf-dgfg-dfgsdg-dgdfgdsfg-dfgsdg-dsgfdsfs";
    private String validToken;
    private final String invalidToken = "Bearer invalid.token.here";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(chatController, "secretKey", secretKey);
        
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        validToken = "Bearer " + Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();
    }

    @Test
    @DisplayName(value = "유효한 토큰/새 catalog - 채팅 성공")
    void whenValidTokenAndNewCatalog_thenSuccess() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");
        ChatResponse mockSummaryResponse = new ChatResponse("Summary response", null);
        ChatResponse mockFinalResponse = new ChatResponse("Final response", "1");
        ChatCatalog mockCatalog = new ChatCatalog("testUser", "Summary response");

        when(chatHistoryService.summary(any(ChatRequest.class))).thenReturn(mockSummaryResponse);
        when(chatHistoryService.saveChatCatalog(anyString(), anyString())).thenReturn(mockCatalog);
        when(chatService.question(any(ChatRequest.class), anyString())).thenReturn(mockFinalResponse);

        ResponseEntity<ChatResponse> response = chatController.question(validToken, chatRequest, null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Final response", response.getBody().getMessage());
    }

    @Test
    @DisplayName(value = "무효한 토큰/catalog 없음 - 인증 실패")
    void whenInvalidToken_thenUnauthorized() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");

        ResponseEntity<ChatResponse> response = chatController.question(invalidToken, chatRequest, null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token.", response.getBody().getMessage());
    }

    @Test
    @DisplayName(value = "토큰 없음/catalog 없음 - 인증 실패")
    void whenNoToken_thenUnauthorized() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");

        ResponseEntity<ChatResponse> response = chatController.question(null, chatRequest, null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid or missing Authorization header.", response.getBody().getMessage());
    }

    @Test
    @DisplayName(value = "유효한 토큰토큰/기존 catalog - 인증 실패")
    void whenValidTokenAndExistingCatalog_thenSuccess() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");
        String catalogId = "1";
        ChatResponse mockResponse = new ChatResponse("Test response", catalogId);

        when(chatService.question(any(ChatRequest.class), anyString())).thenReturn(mockResponse);

        ResponseEntity<ChatResponse> response = chatController.question(validToken, chatRequest, catalogId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test response", response.getBody().getMessage());
        assertEquals(catalogId, response.getBody().getCatalogId());
    }

    @Test
    @DisplayName(value = "서비스 에러 - 서버 에러")
    void whenServiceThrowsException_thenInternalServerError() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");
        when(chatHistoryService.summary(any(ChatRequest.class)))
            .thenThrow(new RuntimeException("Service error"));

        ResponseEntity<ChatResponse> response = chatController.question(validToken, chatRequest, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while processing the request.", response.getBody().getMessage());
    }

    @Test
    @DisplayName(value = "잘못된 Argument - AI 서버에 잘못된 요청")
    void whenServiceThrowsIllegalArgument_thenBadRequest() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setQuestion("Test question");
        when(chatHistoryService.summary(any(ChatRequest.class)))
            .thenThrow(new IllegalArgumentException("Invalid argument"));

        ResponseEntity<ChatResponse> response = chatController.question(validToken, chatRequest, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody().getMessage());
    }
}