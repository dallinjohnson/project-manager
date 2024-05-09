package com.dallinjohnson.projectmanager.controller;

import com.dallinjohnson.projectmanager.domain.Project;
import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.service.ProjectService;
import com.dallinjohnson.projectmanager.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;

    @Autowired
    public ProjectController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/completed/")
    public ResponseEntity<List<Project>> getCompletedProjects() {
        return ResponseEntity.ok(projectService.getProjectsByIsComplete(true));
    }

    @GetMapping("/in-progress/")
    public ResponseEntity<List<Project>> getInProgressProjects() {
        return ResponseEntity.ok(projectService.getProjectsByIsComplete(false));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable @Positive Long projectId) {
        Project project = projectService.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{projectId}/tasks/")
    public ResponseEntity<List<Task>> getTasksForProject(@PathVariable @Positive Long projectId) {
        return ResponseEntity.ok(taskService.getTasksForProject(projectId));
    }

    @PostMapping("/")
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(project));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable @Positive Long projectId, @Valid @RequestBody Project project) {
        return ResponseEntity.ok(projectService.update(projectId, project));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable @Positive Long projectId) {
        projectService.deleteById(projectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable @Positive Long taskId, @Valid @RequestBody Task task) {
        Task updatedTask = taskService.update(taskId, task);
        return ResponseEntity.ok(updatedTask);
    }

    @PostMapping("/{projectId}/tasks/")
    public ResponseEntity<Task> addTaskToProject(@PathVariable @Positive Long projectId, @Valid @RequestBody Task task) {
        Project project = projectService.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
        task.setProject(project);
        project.getTasks().add(task);
        Task savedTask = taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<Project>> getProjectsByAssignedUserId(@RequestParam @Positive Long userId) {
        return ResponseEntity.ok(projectService.findProjectsByUserId(userId));
    }
}
