package dnt.spring.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SubRedditDto {
	private Long id;
	private String name;
	private String description;
	private Integer numberOfPosts;
	
}
