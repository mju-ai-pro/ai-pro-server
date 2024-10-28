package teamproject.aipro.domain.chat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
	private String userId;
	private String question;
}
