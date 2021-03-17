package dnt.spring.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninResponse {
	private String username;
	private String jwtToken;
}
