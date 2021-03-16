package dnt.spring.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dnt.spring.reddit.model.SubReddit;

@Repository
public interface SubRedditRepository  extends JpaRepository<SubReddit, Long>{

}
