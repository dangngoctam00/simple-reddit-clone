package dnt.spring.reddit.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnt.spring.reddit.exception.SpringRedditException;
import dnt.spring.reddit.model.RefreshToken;
import dnt.spring.reddit.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;

@Service
@Transactional
public class RefreshTokenService {
	
	private final RefreshTokenRepository refreshTokenRepo;
	
	@Autowired
	public RefreshTokenService(RefreshTokenRepository refreshTokenRepo) {
		this.refreshTokenRepo = refreshTokenRepo;
	}

	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setCreatedDate(Instant.now());
		refreshToken.setToken(UUID.randomUUID().toString());
		return refreshTokenRepo.save(refreshToken);
	}
	
	public void validateRefreshToken(String refreshToken) {
		refreshTokenRepo.findByToken(refreshToken)
		.orElseThrow(() -> new SpringRedditException("Invalid refresh token"));
	}
	
	public void deleteRefreshToken(String refreshToken) {
		refreshTokenRepo.deleteByToken(refreshToken);
	}
}
