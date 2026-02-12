package org.dharmik.usermanegmentapp.security;

import lombok.RequiredArgsConstructor;
import org.dharmik.usermanegmentapp.Entity.User;
import org.dharmik.usermanegmentapp.Repositary.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService, UserDetailsPasswordService {

    private final UserRepo userRepo;

    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
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
