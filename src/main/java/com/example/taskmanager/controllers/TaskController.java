package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.TaskResponseDto;
import com.example.taskmanager.entities.Task;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.exceptions.TaskNotFoundException;
import com.example.taskmanager.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    public final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/createTask")
    public ResponseEntity<String> createTask(@RequestBody Task task){
        return taskService.createTask(task);
    }

    @GetMapping
    public Page<TaskResponseDto> getMyTasks(@PageableDefault(page = 0,size = 5) Pageable pageable){
        return taskService.getMyTasks(pageable);
    }

    @GetMapping("/get-task-by-status")
    public ResponseEntity<Page<TaskResponseDto>> getTaskByStatus(@RequestParam(required = false) TaskStatus taskStatus, Pageable pageable){
        if(taskStatus!=null){
            return ResponseEntity.ok(taskService.getTaskByStatus(taskStatus,pageable));
        }else{
            throw new TaskNotFoundException("Task Status Not Mentioned!");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable long id){
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable long id,@RequestBody Task task){
        taskService.updateTask(id,task);
        return ResponseEntity.ok("Task Updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id){
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task Deleted!");
    }
}
