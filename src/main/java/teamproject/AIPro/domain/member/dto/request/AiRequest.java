package teamproject.AIPro.domain.member.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import teamproject.AIPro.domain.member.entity.ChatHistory;

@Getter
@Setter
public class AiRequest {
    private String userId;
    private String role;
    private String question;
    private List<ChatHistory> chatHistory;
}