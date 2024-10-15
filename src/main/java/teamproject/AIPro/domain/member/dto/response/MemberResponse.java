package teamproject.AIPro.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

  private Long id;
  private String email;
  private String username;
}
