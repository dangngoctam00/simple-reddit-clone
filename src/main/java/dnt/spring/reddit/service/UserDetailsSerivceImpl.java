package dnt.spring.reddit.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dnt.spring.reddit.model.User;
import dnt.spring.reddit.repository.UserRepository;

@Service
public class UserDetailsSerivceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOpt = userRepo.findByUsername(username);
		User user = userOpt.orElseThrow(() -> 
							new UsernameNotFoundException(String.format("Username %s not found", username)));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.isEnabled(), true, true, true, getAuthoriries("USER_ROLE"));		
	}

	private Collection<? extends GrantedAuthority> getAuthoriries(String role) {
		return Arrays.asList(new SimpleGrantedAuthority(role));
	}
	
}
