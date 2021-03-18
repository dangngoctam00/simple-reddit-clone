package dnt.spring.reddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import dnt.spring.reddit.dto.PostRequest;
import dnt.spring.reddit.dto.PostResponse;
import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.SubReddit;
import dnt.spring.reddit.model.User;
import dnt.spring.reddit.model.Vote;
import dnt.spring.reddit.model.VoteType;
import dnt.spring.reddit.repository.VoteRepository;
import dnt.spring.reddit.service.AuthService;

import static dnt.spring.reddit.model.VoteType.*;

import java.util.Optional;


@Mapper(componentModel = "spring")
public abstract class PostMapper {
	
	@Autowired
	private AuthService authService;
	@Autowired
	private VoteRepository voteRepo;
	
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "voteCount", constant = "0")
	@Mapping(target = "description", source = "postRequest.description")
	@Mapping(target = "postName", source = "postRequest.postName")
	@Mapping(target = "subReddit", source = "subReddit")
	@Mapping(target = "user", source = "user")
	public abstract Post mapToPost(PostRequest postRequest, User user, SubReddit subReddit);
	
	@Mapping(target = "id", source = "postId")
    @Mapping(target = "subRedditName", source = "subReddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
	public abstract PostResponse mapToPostResponse(Post post);
	
	Integer commentCount(Post post) {
		return post.getComments().size();
	}
	
	String getDuration(Post post) {		
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

	boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

	boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }
	
	boolean checkVoteType(Post post, VoteType type) {
		if (authService.isLoggined()) {
			User user = authService.getCurrentUser();
			Optional<Vote> votes = voteRepo.findByPostAndUserOrderByVoteIdDesc(post, user);
			return votes.filter(vote -> vote.getVoteType().equals(type)).isPresent();
		}
		return false;
	}
}
