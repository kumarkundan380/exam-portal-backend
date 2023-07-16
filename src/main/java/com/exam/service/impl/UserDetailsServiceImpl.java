package com.exam.service.impl;

import com.exam.model.User;
import com.exam.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername method invoking");
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User Not exist With this: " + username));
        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(),
                user.getIsVerified(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName().getValue()))
                        .collect(Collectors.toList()));
    }

}

