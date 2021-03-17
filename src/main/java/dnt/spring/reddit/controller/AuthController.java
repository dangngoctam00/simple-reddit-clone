package dnt.spring.reddit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnt.spring.reddit.dto.SigninRequest;
import dnt.spring.reddit.dto.SigninResponse;
import dnt.spring.reddit.dto.SignupRequest;
import dnt.spring.reddit.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signin(@RequestBody SignupRequest request) {
		authService.signup(request);
		return new ResponseEntity<String>("Successful", HttpStatus.OK);
	}
	
	@GetMapping("/accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token) {
		authService.verifyAccount(token);
		return new ResponseEntity<String>("Account activated successfully", HttpStatus.OK);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<SigninResponse> login(@RequestBody SigninRequest request) {
		String jwtToken = authService.signin(request);
		return new ResponseEntity<SigninResponse>(new SigninResponse(request.getUsername(), jwtToken), HttpStatus.OK);
	}
}
