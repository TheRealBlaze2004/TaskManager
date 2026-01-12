package com.example.taskmanager.services;

import com.example.taskmanager.dto.LoginRequestDto;
import com.example.taskmanager.dto.LoginResponseDto;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.exceptions.LoginFailed;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.utilities.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final JwtUtil jwtUtil;
    public final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<LoginResponseDto> userLogin(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new LoginFailed("Invalid Username or Password"));

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRoles()   // must be ROLE_USER / ROLE_ADMIN
        );

        LoginResponseDto response = new LoginResponseDto(
                user.getUserId(),
                user.getUsername(),
                token,
                user.getRoles()
        );
        System.out.println("LOGIN CONTROLLER HIT");
        return ResponseEntity.ok(response);
    }
}
