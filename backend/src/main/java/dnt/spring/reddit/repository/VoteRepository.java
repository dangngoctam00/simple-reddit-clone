package dnt.spring.reddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.User;
import dnt.spring.reddit.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

	Optional<Vote> findByPostAndUserOrderByVoteIdDesc(Post post, User user);

	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

}
