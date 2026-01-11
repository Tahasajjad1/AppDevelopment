package com.vertex.service;

import com.vertex.model.User;
import com.vertex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String vrxId) throws UsernameNotFoundException {
        // 1. Find the user by VRX-ID (Not email!)
        User user = userRepository.findByVrxId(vrxId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + vrxId));

        // 2. Return a Spring Security User object
        return new org.springframework.security.core.userdetails.User(
                user.getVrxId(),           // Username
                user.getPasswordHash(),    // Password (Hashed)
                Collections.emptyList()    // Authorities (Roles) - Empty for now
        );
    }
}