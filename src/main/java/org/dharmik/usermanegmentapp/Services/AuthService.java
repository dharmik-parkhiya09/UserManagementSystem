package org.dharmik.usermanegmentapp.Services;

import lombok.AllArgsConstructor;
import org.dharmik.usermanegmentapp.Entity.Role;
import org.dharmik.usermanegmentapp.Entity.User;
import org.dharmik.usermanegmentapp.Repositary.RoleRepo;
import org.dharmik.usermanegmentapp.Repositary.UserRepo;
import org.dharmik.usermanegmentapp.exception.ResourceNotFoundException;
import org.dharmik.usermanegmentapp.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;


    public void register(User user) {

        String username = user.getUsername().trim().toLowerCase();

        if (userRepo.existsByUsername(username)){
            throw new RuntimeException("User already exists");
        }

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepo.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        user.setRoles(Set.of(role));

        user.setDate(LocalDateTime.now());
        userRepo.save(user);
    }

    public Map<String,Object> login(User user) {

        String username = user.getUsername().trim().toLowerCase();


        if (!userRepo.existsByUsername(username)) {
            throw new RuntimeException("User not found");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User newUser = userRepo.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not Found !"));

            String token = jwtTokenProvider.generateAccessToken(newUser);

            return Map.of(
                    "id",newUser.getId(),
                    "username",newUser.getUsername(),
                    "roles",newUser.getRoles(),
                    "token",token
            );



        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid Username or password");
        }
    }


}
