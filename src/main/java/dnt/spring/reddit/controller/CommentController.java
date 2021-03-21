package dnt.spring.reddit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnt.spring.reddit.dto.CommentDto;
import dnt.spring.reddit.service.CommentService;

@RestController
@RequestMapping("api/comments")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@PostMapping
	public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto) {
		commentService.save(commentDto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<CommentDto>> getAllComments() {
		return new ResponseEntity<List<CommentDto>>(commentService.getAllComments(), HttpStatus.OK);
	}
	
	@GetMapping("/by-post/{postId}")
	public ResponseEntity<List<CommentDto>> getCommentsForPost(@PathVariable Long postId) {
		return new ResponseEntity<List<CommentDto>>(commentService.getCommentsForPost(postId), HttpStatus.OK);
	}
	
	@GetMapping("/by-user/{userName}")
	public ResponseEntity<List<CommentDto>> getCommentsForUser(@PathVariable String userName) {
		return new ResponseEntity<List<CommentDto>>(commentService.getCommentsForUser(userName), HttpStatus.OK);
	}
}
