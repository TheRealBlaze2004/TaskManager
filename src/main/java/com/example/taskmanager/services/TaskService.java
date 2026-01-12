package com.example.taskmanager.services;

import com.example.taskmanager.dto.TaskResponseDto;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.exceptions.NotAuthenticated;
import com.example.taskmanager.exceptions.TaskNotFoundException;
import com.example.taskmanager.exceptions.UserNotFoundException;
import com.example.taskmanager.mappers.TaskMapper;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.repositories.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    public final TaskRepository taskRepository;
    public final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    public User getCurrentUser(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        if(auth==null||!auth.isAuthenticated()){
            throw new NotAuthenticated("Not Authenticated!");
        }
        return userRepository.findByUsername(auth.getName()).orElseThrow(()->new UserNotFoundException("User Not Found!"));
    }


    @CacheEvict(value = {"tasks","tasks_page","tasks_status"},allEntries = true)
    public ResponseEntity<String> createTask(Task task){
        User user=getCurrentUser();
        task.setUser(user);
        taskRepository.save(task);
        return ResponseEntity.ok("Task Created!");
    }

    @Cacheable(value = "tasks_page",key = "#pageable.pageNumber + ':' + #pageable.pageSize + ':' + #root.target.getCurrentUser().username")
    public Page<TaskResponseDto> getMyTasks(Pageable pageable){
        User user=getCurrentUser();
        return taskRepository.findByUser(user,pageable).map(TaskMapper::toDto);
    }

    @Cacheable(value="tasks",key = "#root.target.getCurrentUser().username")
    public TaskResponseDto getTaskById(Long taskId){
        User user=getCurrentUser();
        return taskRepository.findByTaskIdAndUser(taskId,user).map(TaskMapper::toDto).orElseThrow(()->new TaskNotFoundException("No Tasks Found!"));
    }

    @CacheEvict(value ={"tasks","tasks_page","tasks_status"},allEntries = true)
    public ResponseEntity<String> updateTask(Long taskId, Task updateTask){
        User user=getCurrentUser();
        Task task=taskRepository.findByTaskIdAndUser(taskId,user).orElseThrow(()->new TaskNotFoundException("No Task Found!"));
        task.setTitle(updateTask.getTitle());
        task.setDescription(updateTask.getDescription());
        task.setTaskStatus(updateTask.getTaskStatus());
        taskRepository.save(task);
        return ResponseEntity.ok("Task Has Been Updated!");
    }

    @CacheEvict(
            value = { "tasks", "tasks_page","tasks_status" },
            allEntries = true
    )
    public ResponseEntity<String> deleteTask(Long taskId) {
        User user = getCurrentUser();
        Task task = taskRepository.findByTaskIdAndUser(taskId, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
        return ResponseEntity.ok("Task Has Been Deleted!");
    }

    @Cacheable(value = "tasks_status",key = "#taskStatus + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #root.target.getCurrentUser().username")
    public Page<TaskResponseDto> getTaskByStatus(TaskStatus taskStatus, Pageable pageable){
        User user=getCurrentUser();
        return taskRepository.findByTaskStatusAndUser(taskStatus,user,pageable).map(TaskMapper::toDto);
    }
}
