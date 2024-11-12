package teamproject.aipro.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatInventoryResponse {
    private String userId;
    private Long chatInvId;
    private String summary;

}
