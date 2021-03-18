package dnt.spring.reddit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dnt.spring.reddit.dto.PostRequest;
import dnt.spring.reddit.dto.PostResponse;
import dnt.spring.reddit.service.PostService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/post")
@AllArgsConstructor
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@PostMapping
	public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest) {
		postService.create(postRequest);
		return new ResponseEntity<String>("successful", HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<PostResponse>> getAllPosts() {
		return new ResponseEntity<List<PostResponse>>(postService.getAllPosts(), HttpStatus.OK);
	}
}
