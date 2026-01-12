package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.LoginRequestDto;
import com.example.taskmanager.dto.LoginResponseDto;
import com.example.taskmanager.services.AuthService;
import com.example.taskmanager.services.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public final AuthService authService;
    public final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return authService.userLogin(loginRequestDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email){
        passwordResetService.forgotPassword(email);
        return ResponseEntity.ok("If the email exists, a reset link has been sent!");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        return passwordResetService.resetPassword(token,newPassword);
    }
}
