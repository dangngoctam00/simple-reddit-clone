package dnt.spring.reddit.dto;

import dnt.spring.reddit.model.VoteType;
import lombok.Data;

@Data
public class VoteDto {
	private VoteType voteType;
	private Long postId;
}
