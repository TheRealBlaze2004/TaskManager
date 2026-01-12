package com.example.taskmanager.services;

import com.example.taskmanager.dto.UserRegisterDto;
import com.example.taskmanager.dto.UserResponseDto;
import com.example.taskmanager.dto.UserUpdateDto;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Roles;
import com.example.taskmanager.exceptions.UserAlreadyExistsException;
import com.example.taskmanager.exceptions.UserNotFoundException;
import com.example.taskmanager.mappers.UserMapper;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final TaskRepository taskRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TaskRepository taskRepository){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.taskRepository = taskRepository;
    }

    @CacheEvict(value = "users",allEntries = true)
    public ResponseEntity<UserResponseDto> registerUser(UserRegisterDto userRegisterDto){
        if(userRepository.existsByEmail(userRegisterDto.getEmail().trim()))throw new UserAlreadyExistsException("User By This Email Already Exists!");
        if(userRepository.existsByUsername(userRegisterDto.getUsername().trim()))throw new UserAlreadyExistsException("Username Already Taken");
        if (userRegisterDto.getEmail() == null || userRegisterDto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (userRegisterDto.getUsername() == null || userRegisterDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (userRegisterDto.getPassword() == null || userRegisterDto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        User user=new User();
        user.setEmail(userRegisterDto.getEmail().trim());
        user.setUsername(userRegisterDto.getUsername().trim());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setRoles(Roles.ROLE_USER);
        User savedUser=userRepository.save(user);
        UserResponseDto responseDto=new UserResponseDto(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRoles()
        );
        return ResponseEntity.ok(responseDto);
    }


    public UserResponseDto getUser(long userId){
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User Not Found!"));
        return UserMapper.toDTO(user);
    }

    @Cacheable(value = "users",key = "'page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize")
    public Page<UserResponseDto> getAllUsers(Pageable pageable){
        Page<User> users=userRepository.findAll(pageable);
        return users.map(UserMapper::toDTO);
    }

    @CacheEvict(value = {"users","user"},allEntries = true)
    public ResponseEntity<String> updateUser(UserUpdateDto userUpdateDto){
        User user=userRepository.findById(userUpdateDto.getUserId()).orElseThrow(()->new UserNotFoundException("User Doesn't Exist"));
        if(userUpdateDto.getUsername()!=null&&!userRepository.existsByUsername(userUpdateDto.getUsername().trim())){
            user.setUsername(userUpdateDto.getUsername().trim());
        }
        if(userUpdateDto.getEmail()!=null&&!userRepository.existsByEmail(userUpdateDto.getEmail().trim())){
            user.setEmail(userUpdateDto.getEmail().trim());
        }
        userRepository.save(user);
        return ResponseEntity.ok("User Has Been Updated!");
    }

    @Transactional
    @CacheEvict(value = {"users", "user"}, key = "#id", allEntries = true)
    public ResponseEntity<String> deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found: " + id);
        }
        taskRepository.deleteByUser_UserId(id);
        userRepository.deleteById(id);
        return ResponseEntity.ok("User has been deleted");
    }
}
