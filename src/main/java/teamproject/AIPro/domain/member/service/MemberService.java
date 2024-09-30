package teamproject.AIPro.domain.member.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import teamproject.AIPro.domain.member.dto.request.LoginRequest;
import teamproject.AIPro.domain.member.dto.request.SignupRequest;
import teamproject.AIPro.domain.member.dto.response.MemberResponse;
import teamproject.AIPro.domain.member.entity.Member;
import teamproject.AIPro.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    public String login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            // Generate JWT token upon successful login
            return Jwts.builder()
                    .setSubject(member.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 864_000_00)) // 1 day expiration
                    .signWith(SignatureAlgorithm.HS512, "JwTSeCrEtKeYwiThAtLeAsT64ChArAcTeRs12345678901234567890JwTSeCrEtKeYwiThAtLeAsT64ChArAcTeRs12345678901234567890")  // Use a secure key
                    .compact();
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}
