package com.dallinjohnson.projectmanager.controller;

import com.dallinjohnson.projectmanager.domain.Project;
import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.domain.User;
import com.dallinjohnson.projectmanager.service.ProjectService;
import com.dallinjohnson.projectmanager.service.TaskService;
import com.dallinjohnson.projectmanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, TaskService taskService, UserService userService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjectsOrByIsComplete(@RequestParam(name = "isComplete", required = false) Boolean isComplete) {
        List<Project> projects;
        if (isComplete != null) {
            projects = projectService.getProjectsByIsComplete(isComplete);
        } else {
            projects = projectService.findAll();
        }
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        Project project = projectService.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasksForProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksForProject(projectId));
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(project));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId, @RequestBody Project project) {
        return ResponseEntity.ok(projectService.update(project));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteById(projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<Task> addTaskToProject(@PathVariable Long projectId, @RequestBody Task task) {
        task = taskService.create(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.addTaskToProject(projectId, task));
    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    @Transactional
    public ResponseEntity<Void> removeTaskFromProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        projectService.removeTaskFromProject(projectId, taskId);
        taskService.deleteById(taskId);
        return ResponseEntity.noContent().build();
    }
}
