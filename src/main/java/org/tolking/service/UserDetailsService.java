package org.tolking.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.tolking.dto.LoginDTO;
import org.tolking.exception.UserNotFoundException;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UserNotFoundException;
    String signIn(LoginDTO loginDTO) throws AuthenticationException;
}
