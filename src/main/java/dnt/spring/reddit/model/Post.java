package dnt.spring.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postId;
    
    @NotBlank(message = "Post Name cannot be empty or Null")
    private String postName;
    
    @Nullable
    private String url;
    
    @Nullable
    @Lob
    private String description;
   
    private Integer voteCount;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;
    
    
    private Instant createdDate;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "subRedditId")
    private SubReddit subReddit;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = LAZY, mappedBy = "post")
    private Set<Vote> votes = new HashSet<>();
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = LAZY, mappedBy = "post")
    private Set<Comment> comments = new HashSet<>();

//	@Override
//	public String toString() {
//		return "Post [postId=" + postId + ", postName=" + postName + ", url=" + url + ", description=" + description
//				+ ", voteCount=" + voteCount + ", user=" + user + ", createdDate=" + createdDate + ", subReddit="
//				+ subReddit + ", votes=" + votes + ", comments=" + comments + "]";
//	}
    
    
}