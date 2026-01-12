package com.example.taskmanager.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendPasswordResetEmail(String to, String resetLink){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Link For Password Reset!");
        message.setText("Click the link to reset your password:\n\n" + resetLink +
                "\n\nThis link expires in 15 minutes.");
        javaMailSender.send(message);
    }
}
