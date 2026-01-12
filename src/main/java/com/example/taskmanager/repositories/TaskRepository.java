package com.example.taskmanager.repositories;

import com.example.taskmanager.entities.Task;
import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    public Optional<Task> findByTitle(String title);
    public Page<Task> findByTaskStatus(TaskStatus status, Pageable pageable);
    public Page<Task> findByUser(User user,Pageable pageable);
    public Optional<Task> findByTaskIdAndUser(Long id, User user);
    public Page<Task> findByTaskStatusAndUser(TaskStatus taskStatus,User user, Pageable pageable);
    public void deleteByUser_UserId(long id);
}
