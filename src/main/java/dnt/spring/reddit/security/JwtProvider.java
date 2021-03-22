package dnt.spring.reddit.security;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtProvider {
private final String jwtSecret = "anhyeuem";
	
	public String generateToken(Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
		return Jwts.builder()
				.setSubject(principal.getUsername())
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.setExpiration(new Date(System.currentTimeMillis() + 10*60*1000))
				.compact();
	}
	
	public String generateTokenWithUsername(String username) {
		return Jwts.builder()
				.setSubject(username)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.setExpiration(new Date(System.currentTimeMillis() + 10*60*1000))
				.compact();
	}

	public String getUsername(String token) {
		Claims jwt = Jwts.parser()
						.setSigningKey(jwtSecret)
						.parseClaimsJws(token)
						.getBody();
		return jwt.getSubject();
	}

	public boolean validateToken(String token) {
		Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
		return true;
	}
}
