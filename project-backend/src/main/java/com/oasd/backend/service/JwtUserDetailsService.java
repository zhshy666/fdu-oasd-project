package com.oasd.backend.service;

import com.oasd.backend.domain.User;
import com.oasd.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author LBW
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user!=null){ return user; }
        throw new UsernameNotFoundException("User: '" + username + "' not found.");
    }

    public User findUserById(Long userId){
        return userRepository.findUserById(userId);
    }
}
