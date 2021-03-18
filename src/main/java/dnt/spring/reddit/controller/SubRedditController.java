package dnt.spring.reddit.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnt.spring.reddit.dto.SubRedditDto;
import dnt.spring.reddit.service.SubRedditService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/subreddit")
@AllArgsConstructor
public class SubRedditController {
	
	@Autowired
	private SubRedditService subRedditService;
	
	@PostMapping
	public ResponseEntity<SubRedditDto> createSubReddit(@RequestBody SubRedditDto subRedditDto) {
		return new ResponseEntity<SubRedditDto>(subRedditService.createSubReddit(subRedditDto), HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<Collection<SubRedditDto>> getAllSubReddits() {
		return new ResponseEntity<Collection<SubRedditDto>>(subRedditService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SubRedditDto> getSubRedditById(@PathVariable Long id) {
		return new ResponseEntity<SubRedditDto>(subRedditService.getSubRedditById(id), HttpStatus.OK);
	}
}
