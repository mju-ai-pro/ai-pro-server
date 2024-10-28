package teamproject.aipro.domain.role.service;

import org.springframework.stereotype.Service;

import teamproject.aipro.domain.role.dto.request.RoleRequest;
import teamproject.aipro.domain.role.dto.response.RoleResponse;

@Service
public class RoleService {

	private String currentRole;

	public RoleResponse setRole(RoleRequest roleRequest) {
		this.currentRole = roleRequest.getRole();
		return new RoleResponse(currentRole);
	}

	public String getRole() {
		if (currentRole == null) {
			return " ";
		}
		return currentRole;
	}
}
