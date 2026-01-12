package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.TaskResponseDto;
import com.example.taskmanager.dto.UserResponseDto;


import com.example.taskmanager.mappers.TaskMapper;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public AdminController(UserRepository userRepository,
                           TaskRepository taskRepository, UserService userService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @GetMapping("/users")
    public Page<UserResponseDto> getAllUsers(@PageableDefault(page = 0,size = 5)Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/all-tasks")
    public Page<TaskResponseDto> getAllTasks(@PageableDefault(page = 0,size = 5) Pageable pageable){
        return taskRepository.findAll(pageable).map(TaskMapper::toDto);
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return Map.of(
                "users", userRepository.count(),
                "tasks", taskRepository.count()
        );
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        return userService.deleteUser(id);
    }
}

