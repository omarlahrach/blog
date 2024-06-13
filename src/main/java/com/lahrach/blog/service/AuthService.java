package com.lahrach.blog.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lahrach.blog.dto.AuthResponse;
import com.lahrach.blog.dto.LoginRequest;
import com.lahrach.blog.dto.RegisterRequest;
import com.lahrach.blog.model.Role;
import com.lahrach.blog.model.User;
import com.lahrach.blog.repository.RoleRepository;
import com.lahrach.blog.repository.UserRepository;
import com.lahrach.blog.util.enums.RoleName;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final AuthenticationManager authenticationManager;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final CookieService cookieService;

        @Transactional
        public AuthResponse register(RegisterRequest regiterRequest, HttpServletResponse registerResponse) {
                Role defaultRole = roleRepository.findByName(RoleName.ROLE_READER).orElseGet(() -> {
                        Role role = new Role();
                        role.setName(RoleName.ROLE_READER);
                        return roleRepository.save(role);
                });

                User user = User.builder()
                                .firstName(regiterRequest.firstName())
                                .lastName(regiterRequest.lastName())
                                .email(regiterRequest.email())
                                .roles(Set.of(defaultRole))
                                .password(passwordEncoder.encode(regiterRequest.password()))
                                .build();
                User registeredUser = userRepository.save(user);

                String accessToken = jwtService.generateAccessToken(registeredUser);
                String refreshToken = jwtService.generateRefreshToken(registeredUser);

                cookieService.saveRefreshToken(refreshToken, registerResponse);

                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .expiresIn(jwtService.getAccessTokenExpirationTime())
                                .firstName(registeredUser.getFirstName())
                                .lastName(registeredUser.getLastName())
                                .email(registeredUser.getEmail())
                                .roles(registeredUser.getRoles().stream().map(role -> role.getName())
                                                .collect(Collectors.toSet()))
                                .build();
        }

        @Transactional
        public AuthResponse login(LoginRequest loginRequest, HttpServletResponse loginResponse) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginRequest.email(),
                                                loginRequest.password()));

                User loggedUser = userRepository.findByEmail(loginRequest.email())
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                String accessToken = jwtService.generateAccessToken(loggedUser);
                String refreshToken = jwtService.generateRefreshToken(loggedUser);

                cookieService.saveRefreshToken(refreshToken, loginResponse);

                return AuthResponse.builder()
                                .accessToken(accessToken)
                                .expiresIn(jwtService.getAccessTokenExpirationTime())
                                .firstName(loggedUser.getFirstName())
                                .lastName(loggedUser.getLastName())
                                .email(loggedUser.getEmail())
                                .roles(loggedUser.getRoles().stream().map(role -> role.getName())
                                                .collect(Collectors.toSet()))
                                .build();
        }

        public String refreshToken(String oldRefreshToken) {
                String username = jwtService.extractUsername(oldRefreshToken);
                User user = userRepository.findByEmail(username)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                if (!jwtService.isTokenValid(oldRefreshToken, user)) {
                        throw new IllegalArgumentException("Refresh token is invalid or expired");
                }

                String newAccessToken = jwtService.generateAccessToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);

                // cookieService.saveRefreshToken(newRefreshToken, refreshResponse);

                return newAccessToken;
        }
}
