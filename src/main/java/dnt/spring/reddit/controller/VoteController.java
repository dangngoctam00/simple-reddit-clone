package dnt.spring.reddit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnt.spring.reddit.dto.VoteDto;
import dnt.spring.reddit.service.VoteService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("api/votes")
@AllArgsConstructor
@NoArgsConstructor
public class VoteController {
	
	@Autowired
	private VoteService voteService;
	
	@PostMapping
	public ResponseEntity<Void> createVote(@RequestBody VoteDto voteDto) {
		voteService.createVote(voteDto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
}
