package teamproject.AIPro.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
    private String userId;
    private String question;
    private String response;

}
