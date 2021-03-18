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
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long postId;
    
    @NotBlank(message = "Post Name cannot be empty or Null")
    private String postName;
    
    @Nullable
    private String url;
    
    @Nullable
    @Lob
    private String description;
   
    private Integer voteCount;
    
    
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;
    
    
    private Instant createdDate;
    
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "subRedditId")
    private SubReddit subReddit;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = LAZY, mappedBy = "post")
    private Set<Vote> votes;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = LAZY, mappedBy = "post")
    private Set<Comment> comments;
}