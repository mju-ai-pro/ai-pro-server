package teamproject.AIPro.domain.member.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import teamproject.AIPro.domain.member.dto.request.LoginRequest;
import teamproject.AIPro.domain.member.dto.request.SignupRequest;
import teamproject.AIPro.domain.member.dto.response.MemberResponse;
import teamproject.AIPro.domain.member.entity.Member;
import teamproject.AIPro.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class MemberService {

    @Value("${jwt.secret}")
    private String secret;

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
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 비밀 키 생성
            // Generate JWT token upon successful login
            return Jwts.builder()
                    .setSubject(member.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 864_000_00)) // 1 day expiration
                    .signWith(secretKey, SignatureAlgorithm.HS512)  // Use a secure key
                    .compact();
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}
