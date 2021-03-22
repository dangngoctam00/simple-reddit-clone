package dnt.spring.reddit.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnt.spring.reddit.dto.AuthenticationResponse;
import dnt.spring.reddit.dto.RefreshTokenRequest;
import dnt.spring.reddit.dto.SigninRequest;
import dnt.spring.reddit.dto.SignupRequest;
import dnt.spring.reddit.service.AuthService;
import dnt.spring.reddit.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
		authService.signup(request);
		return new ResponseEntity<String>("Successful", HttpStatus.OK);
	}
	
	@GetMapping("/accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token) {
		authService.verifyAccount(token);
		return new ResponseEntity<String>("Account activated successfully", HttpStatus.OK);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody SigninRequest request) {
		return new ResponseEntity<AuthenticationResponse>(authService.signin(request), HttpStatus.OK);
	}
	
	@PostMapping("/refresh/token")
	public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		return new ResponseEntity<AuthenticationResponse>(authService.refreshToken(refreshTokenRequest), HttpStatus.OK);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
		return new ResponseEntity<String>("Refresh token deleted successfully", HttpStatus.OK);
	}
	
}
