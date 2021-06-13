package dnt.spring.reddit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dnt.spring.reddit.dto.VoteDto;
import dnt.spring.reddit.service.VoteService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("api/votes")
@NoArgsConstructor
public class VoteController {
	
	private VoteService voteService;

	@Autowired
	public VoteController(VoteService voteService) {
		this.voteService = voteService;
	}

	@PostMapping
	public ResponseEntity<Void> createVote(@RequestBody VoteDto voteDto) {
		voteService.createVote(voteDto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@GetMapping("/current-vote/{postId}")
	public ResponseEntity<Integer> getCurrentUserVote(@PathVariable Long postId) {
		return new ResponseEntity<>(voteService.currentUserVoted(postId) ,HttpStatus.OK);
	}
}
