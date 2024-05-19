package com.dallinjohnson.projectmanager.controller;

import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.domain.User;
import com.dallinjohnson.projectmanager.dto.TaskDTO;
import com.dallinjohnson.projectmanager.mapper.TaskMapper;
import com.dallinjohnson.projectmanager.service.TaskService;
import com.dallinjohnson.projectmanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.userService = userService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<TaskDTO>> getTasksByAssignedUserId(@RequestParam @Positive Long userId) {
        List<Task> tasks = taskService.getTasksByAssignedUserId(userId);
        List<TaskDTO> taskDTOs = tasks.stream().map(taskMapper::mapToDTO).toList();
        return ResponseEntity.ok(taskDTOs);
    }

    @PutMapping("/{taskId}/users/{userId}")
    public ResponseEntity<TaskDTO> assignUserToTask(@PathVariable @Positive Long taskId, @PathVariable @Positive Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Task task = taskService.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        task.getAssignedUsers().add(user);
        user.getAssignedTasks().add(task);

        userService.save(user);
        Task updatedTask = taskService.save(task);
        TaskDTO updatedTaskDTO = taskMapper.mapToDTO(updatedTask);

        return ResponseEntity.ok(updatedTaskDTO);
    }

    @PutMapping("/{taskId}/users/remove/{userId}")
    public ResponseEntity<TaskDTO> removeUserFromTask(@PathVariable @Positive Long taskId, @PathVariable @Positive Long userId) {
        Task task = taskService.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        List<User> users = task.getAssignedUsers();
        users.removeIf(user -> Objects.equals(user.getId(), userId));

        Task savedTask = taskService.save(task);
        TaskDTO savedTaskDTO = taskMapper.mapToDTO(savedTask);

        return ResponseEntity.ok(savedTaskDTO);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable @Positive Long taskId) {
        taskService.deleteById(taskId);
        return ResponseEntity.noContent().build();
    }
}
