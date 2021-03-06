package dnt.spring.reddit.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Username is required")
	private String username;
	private String password;
	
	@ToString.Exclude
    @EqualsAndHashCode.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Post> posts = new HashSet<Post>();
	
	@ToString.Exclude
    @EqualsAndHashCode.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Vote> votes = new HashSet<Vote>();
	
	@ToString.Exclude
    @EqualsAndHashCode.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Comment> comments = new HashSet<Comment>();
	
	@ToString.Exclude
    @EqualsAndHashCode.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<SubReddit> subReddits = new HashSet<SubReddit>();
	
	@ToString.Exclude
    @EqualsAndHashCode.Exclude
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	private VerificationToken verificationToken;
	
	@Email
    @NotEmpty(message = "Email is required")
    private String email;
    private Instant created;
    private boolean enabled;
}
