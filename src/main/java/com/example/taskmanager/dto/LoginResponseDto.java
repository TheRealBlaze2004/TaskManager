package com.example.taskmanager.dto;

import com.example.taskmanager.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private long userId;
    private String username;
    private String token;
    private Roles roles;
}
