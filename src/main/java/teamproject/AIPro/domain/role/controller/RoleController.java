package teamproject.AIPro.domain.role.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamproject.AIPro.domain.role.dto.request.RoleRequest;
import teamproject.AIPro.domain.role.dto.response.RoleResponse;
import teamproject.AIPro.domain.role.service.RoleService;

@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "*")
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
