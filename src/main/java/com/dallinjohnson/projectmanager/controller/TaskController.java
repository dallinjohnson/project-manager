package com.dallinjohnson.projectmanager.controller;

import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.domain.User;
import com.dallinjohnson.projectmanager.service.TaskService;
import com.dallinjohnson.projectmanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public ResponseEntity<List<Task>> getTasksByAssignedUserId(@RequestParam @Positive Long userId) {
        return ResponseEntity.ok(taskService.getTasksByAssignedUserId(userId));
    }

    @PutMapping("/{taskId}/users/{userId}")
    public ResponseEntity<Task> assignUserToTask(@PathVariable @Positive Long taskId, @PathVariable @Positive Long userId) {
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

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable @Positive Long taskId) {
        taskService.deleteById(taskId);
        return ResponseEntity.noContent().build();
    }
}
