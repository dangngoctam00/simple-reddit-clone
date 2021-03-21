package dnt.spring.reddit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dnt.spring.reddit.dto.CommentDto;
import dnt.spring.reddit.exception.PostNotFoundException;
import dnt.spring.reddit.mapper.CommentMapper;
import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.User;
import dnt.spring.reddit.repository.CommentRepository;
import dnt.spring.reddit.repository.PostRepository;
import dnt.spring.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepo;
	@Autowired
	private CommentMapper commentMapper;
	@Autowired 
	private PostRepository postRepo;
	@Autowired
	private UserRepository userRepo;
	
	public void save(CommentDto commentDto) {
		commentRepo.save(commentMapper.mapDtoToComment(commentDto));
	}

	public List<CommentDto> getAllComments() {
		return commentRepo.findAll().stream().map(commentMapper::mapToCommentDto).collect(Collectors.toList());
	}

	public List<CommentDto> getCommentsForPost(Long postId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(String.format("Post id %s not found", postId)));
		return commentRepo.findAllByPost(post).stream().map(commentMapper::mapToCommentDto).collect(Collectors.toList());
	}

	public List<CommentDto> getCommentsForUser(String userName) {
		User user = userRepo.findByUsername(userName)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", userName)));
		return commentRepo.findAllByUser(user).stream().map(commentMapper::mapToCommentDto).collect(Collectors.toList());
	}
}
