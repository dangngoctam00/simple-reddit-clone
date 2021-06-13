package dnt.spring.reddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dnt.spring.reddit.model.SubReddit;

@Repository
public interface SubRedditRepository  extends JpaRepository<SubReddit, Long>{
	Optional<SubReddit> findByName(String name);
}
