package com.thoughtworks.buddiee.service;

import com.thoughtworks.buddiee.dto.JwtUser;
import com.thoughtworks.buddiee.entity.User;
import com.thoughtworks.buddiee.exception.AuthorizationException;
import com.thoughtworks.buddiee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AuthorizationException("验证失败！"));
        return new JwtUser(user);
    }
}
