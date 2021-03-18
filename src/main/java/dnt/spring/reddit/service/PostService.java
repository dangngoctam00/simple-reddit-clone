package dnt.spring.reddit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dnt.spring.reddit.dto.PostRequest;
import dnt.spring.reddit.dto.PostResponse;
import dnt.spring.reddit.exception.SubRedditNotFoundException;
import dnt.spring.reddit.mapper.PostMapper;
import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.SubReddit;
import dnt.spring.reddit.repository.PostRepository;
import dnt.spring.reddit.repository.SubRedditRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

	private final PostRepository postRepo;
    private final SubRedditRepository subRedditRepo;
    private final AuthService authService;
    private final PostMapper postMapper;
	
    
    @Transactional(readOnly = true)
	public void create(PostRequest postRequest) {
		SubReddit subReddit = subRedditRepo.findByName(postRequest.getSubredditName())
				.orElseThrow(() -> new SubRedditNotFoundException("Invalid subreddit name"));
		Post post = postMapper.mapToPost(postRequest, authService.getCurrentUser(), subReddit);
		postRepo.save(post);
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getAllPosts() {
		List<Post> p = postRepo.findAll();
		List<PostResponse> posts = postRepo.findAll().stream().map(postMapper::mapToPostResponse).collect(Collectors.toList());
		return posts;
	}

}
