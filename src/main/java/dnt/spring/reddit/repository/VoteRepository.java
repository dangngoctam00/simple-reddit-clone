package dnt.spring.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dnt.spring.reddit.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
