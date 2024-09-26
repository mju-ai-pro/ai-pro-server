package teamproject.AIPro.domain.member.service;


import teamproject.AIPro.domain.member.dto.request.LoginRequest;
import teamproject.AIPro.domain.member.dto.request.SignupRequest;
import teamproject.AIPro.domain.member.dto.response.MemberResponse;
import teamproject.AIPro.domain.member.entity.Member;
import teamproject.AIPro.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponse signup(SignupRequest request) {
        Member member = new Member(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getUsername());
        memberRepository.save(member);
        return new MemberResponse(member.getId(), member.getEmail(), member.getUsername());
    }

    public boolean login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // 단순 비밀번호 확인
        if (passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            return true;  // 로그인 성공
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}