package dnt.spring.reddit.dto;

import java.time.Instant;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Comparable<CommentDto> {
	private Long id;
	private String text;
	private Long postId;
	private String userName;
	private Instant createdDate;
	private Set<CommentDto> comments;
	private Long parentCommentId;

	@Override
	public int compareTo(@NotNull CommentDto other) {
		int comparedValue = this.getCreatedDate().compareTo(other.getCreatedDate());
		if (comparedValue == 0) {
			comparedValue = this.getId().compareTo(other.getId());
		}
		return comparedValue;
	}
}
