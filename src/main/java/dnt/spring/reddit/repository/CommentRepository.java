package dnt.spring.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dnt.spring.reddit.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
