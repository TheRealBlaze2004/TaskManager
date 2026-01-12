package com.example.taskmanager.mappers;

import com.example.taskmanager.dto.TaskResponseDto;
import com.example.taskmanager.dto.UserSummaryDto;
import com.example.taskmanager.entities.Task;

public class TaskMapper {
    private TaskMapper(){
        throw new IllegalArgumentException("Mapper Class!");
    }
    public static TaskResponseDto toDto(Task task){
        return new TaskResponseDto(
                task.getTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getTaskStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                new UserSummaryDto(
                        task.getUser().getUserId(),
                        task.getUser().getUsername()
                )
        );
    }
}
