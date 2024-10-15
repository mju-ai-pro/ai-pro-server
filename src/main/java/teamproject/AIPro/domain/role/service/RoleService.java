package teamproject.AIPro.domain.role.service;

import org.springframework.stereotype.Service;
import teamproject.AIPro.domain.role.dto.request.RoleRequest;
import teamproject.AIPro.domain.role.dto.response.RoleResponse;

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
