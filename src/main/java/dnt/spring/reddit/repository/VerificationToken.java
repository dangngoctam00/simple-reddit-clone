package dnt.spring.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationToken extends JpaRepository<VerificationToken, Long>{

}