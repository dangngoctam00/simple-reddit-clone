package dnt.spring.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class SubReddit {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    @NotBlank(message = "Community name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    
	@ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = LAZY, mappedBy = "subReddit")
    private List<Post> posts;
	
    private Instant createdDate;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;

	@Override
	public String toString() {
		return "SubReddit [id=" + id + ", name=" + name + ", description=" + description + ", posts=" + posts
				+ ", createdDate=" + createdDate + ", user=" + user + "]";
	}
}