package teamproject.aipro.domain.chat.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import teamproject.aipro.domain.chat.dto.response.ChatHistoryResponse;

@Getter
@Setter
public class AiRequest {
	private String userId;
	private String role;
	private String question;
	private List<ChatHistoryResponse> chatHistory;
}
