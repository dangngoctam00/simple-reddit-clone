package dnt.spring.reddit.repository;

import java.util.List;
import java.util.TreeSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dnt.spring.reddit.model.Comment;
import dnt.spring.reddit.model.Post;
import dnt.spring.reddit.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	TreeSet<Comment> findAllByPost(Post post);

	List<Comment> findAllByUser(User user);

}
