package dnt.spring.reddit.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@AllArgsConstructor
public class AuthService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private VerificationTokenRepository verificationTokenRepo;
//	@Autowired
//	private MailContentBuilder mailContentBuilder;
	@Autowired
	private MailService mailService;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtProvider jwtProvider;
	
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

	public String signin(SigninRequest request) {
		Authentication authenticate = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), 
				request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		return jwtProvider.generateToken(authenticate);
	}

}
