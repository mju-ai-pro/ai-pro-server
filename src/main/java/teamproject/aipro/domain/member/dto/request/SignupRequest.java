package teamproject.aipro.domain.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	private String email;
	private String password;
	private String username;
}