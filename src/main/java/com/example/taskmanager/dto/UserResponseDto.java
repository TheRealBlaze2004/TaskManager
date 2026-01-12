package com.example.taskmanager.dto;

import com.example.taskmanager.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@AllArgsConstructor
public class UserResponseDto implements Serializable {
    private long userId;
    private String username;
    private String email;
    private Roles roles;
}
