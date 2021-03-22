package dnt.spring.reddit.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
	private String username;
	private String jwtToken;
	private Instant expiredAt;
	private String refreshToken;
}
