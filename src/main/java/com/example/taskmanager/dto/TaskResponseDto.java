package com.example.taskmanager.dto;

import com.example.taskmanager.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter @Setter
@AllArgsConstructor
public class TaskResponseDto implements Serializable {
    long taskId;
    String title;
    String description;
    TaskStatus taskStatus;
    Instant createdAt;
    Instant updatedAt;
    UserSummaryDto summaryDto;
}
