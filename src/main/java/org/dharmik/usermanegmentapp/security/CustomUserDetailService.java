package org.dharmik.usermanegmentapp.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dharmik.usermanegmentapp.Entity.Role;
import org.dharmik.usermanegmentapp.Entity.User;
import org.dharmik.usermanegmentapp.Repositary.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService, UserDetailsPasswordService {

    private final UserRepo userRepo;

    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<Role> roles = new HashSet<>(user.getRoles());

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }


    public UserDetails updatePassword(UserDetails user, String newPassword) {
        User existingUser = userRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        existingUser.setPassword(newPassword);
        userRepo.save(existingUser);

        return new org.springframework.security.core.userdetails.User(
                existingUser.getUsername(),
                existingUser.getPassword(),
                existingUser.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
        );
    }


}
