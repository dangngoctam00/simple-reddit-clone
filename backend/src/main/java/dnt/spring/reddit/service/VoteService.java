package dnt.spring.reddit.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dnt.spring.reddit.dto.VoteDto;
import dnt.spring.reddit.exception.PostNotFoundException;
import dnt.spring.reddit.exception.SpringRedditException;
import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.Vote;
import dnt.spring.reddit.repository.PostRepository;
import dnt.spring.reddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import static dnt.spring.reddit.model.VoteType.*;
@Service
//@AllArgsConstructor
@NoArgsConstructor
public class VoteService {
	@Autowired
	public VoteService(VoteRepository voteRepo, AuthService authService, PostRepository postRepo) {
		this.voteRepo = voteRepo;
		this.authService = authService;
		this.postRepo = postRepo;
	}

	private VoteRepository voteRepo;
	private AuthService authService;
	private PostRepository postRepo;
	
	public void createVote(VoteDto voteDto) {
		Post post = postRepo.findById(voteDto.getPostId())
	                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
		Optional<Vote> voteByPostAndUserOpt = voteRepo.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
		if (voteByPostAndUserOpt.isPresent() && voteByPostAndUserOpt.get().getVoteType().equals(voteDto.getVoteType())) {
			voteRepo.delete(voteByPostAndUserOpt.get());
			if (voteDto.getVoteType().equals(UPVOTE)) {
				post.setVoteCount(post.getVoteCount() - 1);
			}
			else {
				post.setVoteCount(post.getVoteCount() + 1);
			}
		}
		else if (UPVOTE.equals(voteDto.getVoteType())) {
			if (voteByPostAndUserOpt.isPresent()) {
				voteRepo.delete(voteByPostAndUserOpt.get());
				post.setVoteCount(post.getVoteCount() + 2);
			}
			else {
				post.setVoteCount(post.getVoteCount() + 1);
			}
			voteRepo.save(mapToVote(voteDto, post));
		}
		else {
			if (voteByPostAndUserOpt.isPresent()) {
				voteRepo.delete(voteByPostAndUserOpt.get());
				post.setVoteCount(post.getVoteCount() - 2);
			}
			else {
				post.setVoteCount(post.getVoteCount() - 1);
			}
			voteRepo.save(mapToVote(voteDto, post));
		}
		postRepo.save(post);
	}

	public int currentUserVoted(Long postId) {
		Post post = postRepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + postId));
		Optional<Vote> voteOpt = voteRepo.findByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
		if (!voteOpt.isPresent()) {
			return 0;
		}
		else if (UPVOTE.equals(voteOpt.get().getVoteType()))  {
			return 1;
		}
		else {
			return -1;
		}
	}
	
	Vote mapToVote(VoteDto voteDto, Post post) {
		return Vote.builder()
				.voteType(voteDto.getVoteType())
				.user(authService.getCurrentUser())
				.post(post)
				.build();
	}

}
