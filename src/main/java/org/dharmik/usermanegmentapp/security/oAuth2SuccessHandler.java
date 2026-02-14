package org.dharmik.usermanegmentapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dharmik.usermanegmentapp.Entity.Role;
import org.dharmik.usermanegmentapp.Entity.User;
import org.dharmik.usermanegmentapp.Repositary.RoleRepo;
import org.dharmik.usermanegmentapp.Repositary.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class oAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        OAuth2AuthenticationToken token =
                (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User =
                (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        if (email == null) {
            throw new RuntimeException("Email not found from Google");
        }

        String username = email.split("@")[0];

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {

                    Role role = roleRepository.findByName("ROLE_USER")
                            .orElseThrow(() ->
                                    new RuntimeException("Role not found"));

                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setPassword(
                            passwordEncoder.encode("oauth2_user"));
                    newUser.setProvider("GOOGLE");
                    newUser.getRoles().add(role);

                    return userRepository.save(newUser);
                });

        String jwt = jwtService.generateToken(user);

        response.setContentType("application/json");
        response.getWriter()
                .write("{\"token\": \"" + jwt + "\"}");
    }
}
