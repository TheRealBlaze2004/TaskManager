package com.example.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateDto {
    private long userId;
    private String username;
    private String email;
}
