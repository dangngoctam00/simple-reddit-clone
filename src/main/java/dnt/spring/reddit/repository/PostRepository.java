package dnt.spring.reddit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dnt.spring.reddit.dto.PostResponse;
import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.SubReddit;
import dnt.spring.reddit.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllBySubReddit(SubReddit subReddit);

	List<Post> findAllByUser(User user);

}
