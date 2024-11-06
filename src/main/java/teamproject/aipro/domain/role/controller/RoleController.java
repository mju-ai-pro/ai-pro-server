package teamproject.aipro.domain.role.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teamproject.aipro.domain.role.dto.request.RoleRequest;
import teamproject.aipro.domain.role.dto.response.RoleResponse;
import teamproject.aipro.domain.role.service.RoleService;

@CrossOrigin(origins = {"http://localhost:3000", "https://ai-pro-fe.vercel.app"})
@RestController
@RequestMapping("/api/role")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@PostMapping("/set")
	public ResponseEntity<RoleResponse> setRole(@RequestBody RoleRequest roleRequest) {
		RoleResponse response = roleService.setRole(roleRequest);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get")
	public String getRole() {
		return roleService.getRole();
	}
}
