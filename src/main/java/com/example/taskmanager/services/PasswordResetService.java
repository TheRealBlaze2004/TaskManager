package com.example.taskmanager.services;

import com.example.taskmanager.entities.User;
import com.example.taskmanager.exceptions.EmailDoesntExist;
import com.example.taskmanager.exceptions.UserNotFoundException;
import com.example.taskmanager.repositories.UserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetService {
    public final UserRepository userRepository;
    public final StringRedisTemplate redisTemplate;
    public final EmailService emailService;
    public final PasswordEncoder passwordEncoder;

    private static final long RESET_TOKEN_TTL_MINUTES=15;
    public PasswordResetService(UserRepository userRepository, StringRedisTemplate redisTemplate, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void forgotPassword(String email){
        userRepository.findByEmail(email).ifPresent(user->{
            String token= UUID.randomUUID().toString();
            String redisKey="password-reset: "+token;
            redisTemplate.opsForValue().set(
                    redisKey,
                    user.getEmail(),
                    RESET_TOKEN_TTL_MINUTES,
                    TimeUnit.MINUTES
            );
            String resetLink="http://localhost:5173/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(email,resetLink);
        });
    }

    public ResponseEntity<String> resetPassword(String token, String newPassword){
        String redisKey="password-reset: "+token;
        String email=redisTemplate.opsForValue().get(redisKey);

        if(email==null){
            throw new EmailDoesntExist("Invalid Token!");
        }
        User user=userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found!"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        redisTemplate.delete(redisKey);
        return ResponseEntity.ok("Password Reset Successfully!");
    }

}
