package teamproject.AIPro.domain.member.controller;

import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamproject.AIPro.domain.member.dto.request.LoginRequest;
import teamproject.AIPro.domain.member.dto.request.SignupRequest;
import teamproject.AIPro.domain.member.dto.response.MemberResponse;
import teamproject.AIPro.domain.member.entity.Member;
import teamproject.AIPro.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/member")
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping("/signup")
  public ResponseEntity<MemberResponse> signup(@RequestBody SignupRequest signupRequest) {
    MemberResponse response = memberService.signup(signupRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
    String token = memberService.login(loginRequest);
    return ResponseEntity.ok(token);
  }

  @GetMapping("/jwttest")
  public String test() {
    return "test";
  }

  @GetMapping("/user")
  public Member getMemberInfo(Principal principal) {
    String email = principal.getName(); // 이메일을 가져옴
    return memberService.findByEmail(email);
  }
}
