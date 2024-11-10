package teamproject.aipro.config.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	private final SecretKey secretKey;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String secret) {
		super(authenticationManager);
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		String token = request.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			token = token.replace("Bearer ", "");
			try {
				Claims claims = Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(token)
					.getBody();
				String username = claims.getSubject();
				if (username != null) {
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						username, null, new ArrayList<>());
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} catch (ExpiredJwtException e) {
				System.out.println("JWT 토큰이 만료되었습니다: " + e.getMessage());
			} catch (SignatureException e) {
				System.out.println("JWT 서명이 유효하지 않습니다: " + e.getMessage());
			} catch (MalformedJwtException e) {
				System.out.println("JWT 형식이 유효하지 않습니다: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("JWT 처리 중 오류 발생: " + e.getMessage());
			}
		}
		chain.doFilter(request, response);
	}
}
