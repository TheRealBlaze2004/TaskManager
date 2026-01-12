package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.UserRegisterDto;
import com.example.taskmanager.dto.UserResponseDto;
import com.example.taskmanager.dto.UserUpdateDto;
import com.example.taskmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegisterDto userRegisterDto){
        return userService.registerUser(userRegisterDto);
    }

    @GetMapping("/get-all-users")
    public Page<UserResponseDto> getAllUsers(@PageableDefault(page = 0,size = 10) Pageable pageable){
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/get-user/{id}")
    public UserResponseDto getUser(@PathVariable long id){
        return userService.getUser(id);
    }

    @PostMapping("/update-user")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateDto userUpdateDto){
        return userService.updateUser(userUpdateDto);
    }
}
