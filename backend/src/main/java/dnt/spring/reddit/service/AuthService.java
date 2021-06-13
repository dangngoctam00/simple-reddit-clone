package dnt.spring.reddit.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnt.spring.reddit.dto.AuthenticationResponse;
import dnt.spring.reddit.dto.RefreshTokenRequest;
import dnt.spring.reddit.dto.SigninRequest;
import dnt.spring.reddit.dto.SignupRequest;
import dnt.spring.reddit.exception.SpringRedditException;
import dnt.spring.reddit.model.NotificationEmail;
import dnt.spring.reddit.model.User;
import dnt.spring.reddit.model.VerificationToken;
import dnt.spring.reddit.repository.UserRepository;
import dnt.spring.reddit.repository.VerificationTokenRepository;
import dnt.spring.reddit.security.JwtProvider;
import lombok.AllArgsConstructor;

@Service
public class AuthService {

	@Autowired
	public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder,
					   VerificationTokenRepository verificationTokenRepo, MailService mailService,
					   AuthenticationManager authManager, JwtProvider jwtProvider,
					   RefreshTokenService refreshTokenService) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.verificationTokenRepo = verificationTokenRepo;
		this.mailService = mailService;
		this.authManager = authManager;
		this.jwtProvider = jwtProvider;
		this.refreshTokenService = refreshTokenService;
	}

	private UserRepository userRepo;
	private PasswordEncoder passwordEncoder;
	private VerificationTokenRepository verificationTokenRepo;
	private MailService mailService;
	private AuthenticationManager authManager;
	private JwtProvider jwtProvider;
	private RefreshTokenService refreshTokenService;

	@Transactional
	public void signup(SignupRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		user.setCreated(Instant.now());
		user.setEnabled(false);
		userRepo.save(user);
		
		String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
	}
	
	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verfificationToken = new VerificationToken();
		verfificationToken.setToken(token);
		verfificationToken.setUser(user);
		verificationTokenRepo.save(verfificationToken);
		return token;
	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verifyToken = verificationTokenRepo.findByToken(token);
		verifyToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(verifyToken.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		Optional<User> userOpt = userRepo.findByUsername(username);
		userOpt.orElseThrow(() -> new SpringRedditException("User not found"));
		userOpt.get().setEnabled(true);
		userRepo.save(userOpt.get());
	}

	public ResponseEntity<AuthenticationResponse> login(SigninRequest request) {
		try {
			Authentication authenticate = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
					request.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authenticate);
			String jwt = jwtProvider.generateToken(authenticate);
			return new ResponseEntity<>(AuthenticationResponse.builder()
					.jwtToken(jwt)
					.refreshToken(refreshTokenService.generateRefreshToken().getToken())
					.expiredAt(Instant.now().plusMillis(60*10*1000))
					.username(request.getUsername())
					.build(), HttpStatus.OK);
		}
		catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	}
	
	@Transactional(readOnly = true)
	public User getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = 
				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepo.findByUsername(principal.getUsername()).orElseThrow(() -> new IllegalArgumentException("Username not found"));
	}
	
	public boolean isLoggined() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}
	
	public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
		return AuthenticationResponse.builder()
				.jwtToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiredAt(Instant.now().plusMillis(10*60*1000))
				.username(refreshTokenRequest.getUsername())
				.build();
	}

}
