package teamproject.aipro.domain.chat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import teamproject.aipro.domain.chat.dto.request.ChatRequest;
import teamproject.aipro.domain.chat.dto.response.ChatResponse;
import teamproject.aipro.domain.chat.entity.ChatCatalog;
import teamproject.aipro.domain.chat.service.ChatHistoryService;
import teamproject.aipro.domain.chat.service.ChatService;

@CrossOrigin(origins = {"http://localhost:3000", "https://ai-pro-fe.vercel.app"})
@RestController
@RequestMapping("/api/chat")
public class ChatController {

	@Value("${jwt.secret}")
	private String secretKey;

	private final ChatService chatService;
	private final ChatHistoryService chatHistoryService;

	public ChatController(ChatService chatService, ChatHistoryService chatHistoryService) {
		this.chatService = chatService;
		this.chatHistoryService = chatHistoryService;
	}

	@PostMapping("/question")
	public ResponseEntity<ChatResponse> question(
		@RequestHeader("Authorization") String authHeader,
		@RequestBody ChatRequest chatRequest,
		@RequestParam(required = false) String catalogId) {

		// Authorization 헤더에서 토큰을 파싱하여 사용자 ID 추출
		String userId = extractUserIdFromToken(authHeader);
		System.out.println("userId = " + userId);
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		ChatResponse response = (catalogId == null || catalogId.trim().isEmpty())
			? processNewCatalogRequest(chatRequest, userId)
			: processExistingCatalogRequest(chatRequest, catalogId);

		return ResponseEntity.ok(response);
	}

	// 토큰에서 사용자 ID 추출 메서드
	private String extractUserIdFromToken(String authHeader) {
		try {
			if (authHeader == null) {
				return null; // 토큰이 없거나 잘못된 형식인 경우
			}
			String jwtToken = authHeader.replace("Bearer ", "");
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(secretKey.getBytes()) // secretKey를 JWT 서명 검증에 사용
				.build()
				.parseClaimsJws(jwtToken)
				.getBody();
			return claims.getSubject(); // JWT의 subject 필드에서 userId 반환
		} catch (Exception e) {
			System.err.println("Error decoding JWT: " + e.getMessage());
			return null; // 토큰 검증 실패 시 null 반환
		}
	}

	private ChatResponse processNewCatalogRequest(ChatRequest chatRequest, String userId) {
		// AI 서버로부터 요약 받기
		ChatResponse response = chatHistoryService.summary(chatRequest);
		Long newCatalogId = createNewCatalog(userId, response.getMessage());
		// 새로운 ChatHistory 저장
		// chatHistoryService.saveChatHistory(chatRequest.getQuestion(), response.getMessage(), Long.toString(newCatalogId));
		return chatService.question(chatRequest, String.valueOf(newCatalogId));
	}


	private Long createNewCatalog(String userId, String summaryMessage) {
		ChatCatalog chatCatalog = new ChatCatalog(userId, summaryMessage);
		return chatHistoryService.saveChatCatalog(chatCatalog.getUserId(), chatCatalog.getChatSummary()).getId();
	}

	private ChatResponse processExistingCatalogRequest(ChatRequest chatRequest, String catalogId) {
		return chatService.question(chatRequest, catalogId);
	}
}
