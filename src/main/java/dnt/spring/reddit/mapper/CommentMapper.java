package dnt.spring.reddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "text", source = "commentDto.text")
	@Mapping(target = "post", expression = "java(mapPost(commentDto.getPostId()))")
	@Mapping(target = "user", expression = "java(mapUser(commentDto.getUserName()))")
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	public abstract Comment mapDtoToComment(CommentDto commentDto);
	
	@Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
	@Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
	public abstract CommentDto mapToCommentDto(Comment comment);
	
	Post mapPost(Long postId) {
		return postRepo.findById(postId)
				.orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found", postId)));
	}
	
	User mapUser(String userName) {
		return authService.getCurrentUser();
	}
}
