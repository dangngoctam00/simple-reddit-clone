package dnt.spring.reddit.service;

import java.util.*;
import java.util.stream.Collectors;

import dnt.spring.reddit.exception.SpringRedditException;
import dnt.spring.reddit.model.Comment;
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
@NoArgsConstructor
public class CommentService {
	@Autowired
	public CommentService(CommentRepository commentRepo, CommentMapper commentMapper, PostRepository postRepo, UserRepository userRepo) {
		this.commentRepo = commentRepo;
		this.commentMapper = commentMapper;
		this.postRepo = postRepo;
		this.userRepo = userRepo;
	}

	private CommentRepository commentRepo;
	private CommentMapper commentMapper;
	private PostRepository postRepo;
	private UserRepository userRepo;
	
	public void save(CommentDto commentDto) {
		Comment comment = commentMapper.mapDtoToComment(commentDto);
		if (commentDto.getParentCommentId() != null) {
			comment.getParentComment().getComments().add(comment);
		}
		commentRepo.save(comment);
	}

	public List<CommentDto> getAllComments() {
		return commentRepo.findAll().stream().map(commentMapper::mapToCommentDto).collect(Collectors.toList());
	}


	public TreeSet<Comment> getCommentsForPostInOrder(Long postId) {
		TreeSet<Comment> comments = getCommentsForPostWithoutDto(postId);
		Set<Long> visited = new HashSet<>();
		getCommentsInOrderHelper(comments, visited);
		return comments;
	}

	public TreeSet<Comment> getCommentsForPostWithoutDto(Long postId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(String.format("Post id %s not found", postId)));
		return commentRepo.findAllByPost(post);
	}


	private void getCommentsInOrderHelper(Set<Comment> comments, Set<Long> visited) {
		Iterator<Comment> it = comments.iterator();
		while (it.hasNext()) {
			Comment comment = it.next();
			if (visited.contains(comment.getId())) {
				it.remove();
			}
			else {
				visited.add(comment.getId());
				if (comment.getComments() != null) {
					getCommentsInOrderHelper(comment.getComments(), visited);
				}
			}
		}
	}

	public TreeSet<CommentDto> getCommentsForPost(Long postId) {
		Set<Comment> commentsInOrder = getCommentsForPostInOrder(postId);
		Set<CommentDto> commentsDto = new TreeSet<>();
		getCommentsForPostHelper(commentsInOrder, commentsDto);
		return (TreeSet)(commentsDto);
	}

	public TreeSet<CommentDto> getCommentById(Long commentId) {
		Comment comment = commentRepo.findById(commentId)
				.orElseThrow(() -> new SpringRedditException(String.format("Comment with id %s not found", commentId)));
		Set<Comment> comments = new TreeSet<>();
		comments.add(comment);
		Set<CommentDto> commentsDto = new TreeSet<>();
		getCommentsForPostHelper(comments, commentsDto);
		return (TreeSet<CommentDto>)(commentsDto);
	}

	private void getCommentsForPostHelper(Set<Comment> comments, Set<CommentDto> commentDtos) {
		Iterator<Comment> it = comments.iterator();
		while(it.hasNext()) {
			Comment comment = it.next();
			CommentDto commentDto = commentMapper.mapToCommentDto(comment);
			commentDtos.add(commentDto);
			if (comment.getComments() != null) {
				commentDto.setComments(new TreeSet<>());
				getCommentsForPostHelper(comment.getComments(), commentDto.getComments());
			}
		}
	}



	public List<CommentDto> getCommentsForUser(String userName) {
		User user = userRepo.findByUsername(userName)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", userName)));
		return commentRepo.findAllByUser(user).stream().map(commentMapper::mapToCommentDto).collect(Collectors.toList());
	}
}
