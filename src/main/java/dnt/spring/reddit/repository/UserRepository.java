package dnt.spring.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dnt.spring.reddit.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
