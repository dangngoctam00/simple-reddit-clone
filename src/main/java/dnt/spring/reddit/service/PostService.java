package dnt.spring.reddit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import dnt.spring.reddit.dto.PostRequest;
import dnt.spring.reddit.dto.PostResponse;
import dnt.spring.reddit.exception.PostNotFoundException;
import dnt.spring.reddit.exception.SubRedditNotFoundException;
import dnt.spring.reddit.mapper.PostMapper;
import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.SubReddit;
import dnt.spring.reddit.model.User;
import dnt.spring.reddit.repository.PostRepository;
import dnt.spring.reddit.repository.SubRedditRepository;
import dnt.spring.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
public class PostService {
	public PostService(UserRepository userRepo, PostRepository postRepo,
					   SubRedditRepository subRedditRepo, AuthService authService, PostMapper postMapper) {
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.subRedditRepo = subRedditRepo;
		this.authService = authService;
		this.postMapper = postMapper;
	}

	private final UserRepository userRepo;
	private final PostRepository postRepo;
    private final SubRedditRepository subRedditRepo;
    private final AuthService authService;
    private final PostMapper postMapper;
	
	public void create(PostRequest postRequest) {
		SubReddit subReddit = subRedditRepo.findByName(postRequest.getSubredditName())
				.orElseThrow(() -> new SubRedditNotFoundException("Invalid subreddit name"));
		Post post = postMapper.mapToPost(postRequest, authService.getCurrentUser(), subReddit);
		post.setSubReddit(subReddit);
		authService.getCurrentUser().getPosts().add(post);
		post = postRepo.save(post);
		subReddit.getPosts().add(post);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<PostResponse> getAllPosts() {
		List<PostResponse> posts = postRepo.findAll()
				.stream().map(postMapper::mapToPostResponse)
				.collect(Collectors.toList());
		System.out.println("get all posts method " + TransactionAspectSupport.currentTransactionStatus());
		return posts;
	}

	@Transactional(readOnly = true)
	public PostResponse getPost(Long postId) {
		System.out.println("get post method " + TransactionAspectSupport.currentTransactionStatus());
		return postMapper.mapToPostResponse(postRepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException(String.format("Post with %l not found", postId))));
	}

	public List<PostResponse> getPostsBySubReddit(Long id) {
		SubReddit subReddit = subRedditRepo.findById(id)
				.orElseThrow(() -> new SubRedditNotFoundException("SubReddit with id " + id + " not found"));
		return postRepo.findAllBySubReddit(subReddit).stream().map(postMapper::mapToPostResponse).collect(Collectors.toList());
	}

	public List<PostResponse> getPostsByUser(String name) {
		User user = userRepo.findByUsername(name).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with id %s not found", name)));
		return postRepo.findAllByUser(user).stream().map(postMapper::mapToPostResponse).collect(Collectors.toList());
	}

}
