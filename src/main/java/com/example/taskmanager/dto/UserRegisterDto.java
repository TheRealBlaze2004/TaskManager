package com.example.taskmanager.dto;

import com.example.taskmanager.enums.Roles;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRegisterDto {
    private String username;
    private String email;
    private String password;
}
