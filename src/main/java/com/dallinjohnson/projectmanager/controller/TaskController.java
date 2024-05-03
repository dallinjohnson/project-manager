package com.dallinjohnson.projectmanager.controller;

import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.domain.User;
import com.dallinjohnson.projectmanager.service.TaskService;
import com.dallinjohnson.projectmanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PutMapping("/{taskId}/users/{userId}")
    public ResponseEntity<Task> assignUserToTask(@PathVariable Long taskId, @PathVariable Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Task task = taskService.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        task.getAssignedUsers().add(user);
        user.getAssignedTasks().add(task);

        userService.save(user);
        Task updatedTask = taskService.save(task);

        return ResponseEntity.ok(updatedTask);
    }
}
