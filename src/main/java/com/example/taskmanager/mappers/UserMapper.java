package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.UserResponseDto;
import com.example.taskmanager.entities.User;

import java.io.Serializable;

public class UserMapper implements Serializable {
    private UserMapper(){
        throw new IllegalStateException("Utility class");
    }

    public static UserResponseDto toDTO(User user){
        return new UserResponseDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
