package dnt.spring.reddit.mapper;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import dnt.spring.reddit.exception.SpringRedditException;
import dnt.spring.reddit.repository.CommentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import dnt.spring.reddit.dto.CommentDto;
import dnt.spring.reddit.exception.PostNotFoundException;
import dnt.spring.reddit.model.Comment;
import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.User;
import dnt.spring.reddit.repository.PostRepository;
import dnt.spring.reddit.service.AuthService;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

	@Autowired
	private PostRepository postRepo;
	@Autowired
	private AuthService authService;
	@Autowired
	private CommentRepository commentRepo;
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "text", source = "commentDto.text")
	@Mapping(target = "post", expression = "java(mapPost(commentDto.getPostId()))")
	@Mapping(target = "user", expression = "java(mapUser(commentDto.getUserName()))")
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "comments", ignore = true)
	@Mapping(target = "parentComment", expression = "java(mapComment(commentDto.getParentCommentId()))")
	public abstract Comment mapDtoToComment(CommentDto commentDto);
	
	@Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
	@Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
	@Mapping(target = "parentCommentId", ignore = true)
	@Mapping(target = "comments", ignore = true)
	@Mapping(target = "duration", expression = "java(getDuration(comment))")
	public abstract CommentDto mapToCommentDto(Comment comment);

	String getDuration(Comment comment) {
		return TimeAgo.using(comment.getCreatedDate().toEpochMilli());
	}
	
	Post mapPost(Long postId) {
		return postRepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found", postId)));
	}
	
	User mapUser(String userName) {
		return authService.getCurrentUser();
	}

	Comment mapComment(Long parentCommentId) {
		if (parentCommentId != null) {
			return commentRepo.findById(parentCommentId)
					.orElseThrow(() -> new SpringRedditException(String.format("Comment with id %s not found", parentCommentId)));
		}
		return null;
	}
}
