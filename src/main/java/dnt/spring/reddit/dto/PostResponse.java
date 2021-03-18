package dnt.spring.reddit.dto;

import lombok.Data;

@Data
public class PostResponse {
	private Long id;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private String subRedditName;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;
    private boolean upVote;
    private boolean downVote;
}
