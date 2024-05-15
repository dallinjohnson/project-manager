package com.dallinjohnson.projectmanager.controller;

import com.dallinjohnson.projectmanager.domain.Project;
import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.dto.ProjectDTO;
import com.dallinjohnson.projectmanager.dto.TaskDTO;
import com.dallinjohnson.projectmanager.mapper.ProjectMapper;
import com.dallinjohnson.projectmanager.mapper.TaskMapper;
import com.dallinjohnson.projectmanager.service.ProjectService;
import com.dallinjohnson.projectmanager.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;

    @Autowired
    public ProjectController(ProjectService projectService, TaskService taskService, ProjectMapper projectMapper, TaskMapper taskMapper) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.projectMapper = projectMapper;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Project> projects = projectService.findAll();
        List<ProjectDTO> projectDTOs = projects.stream().map(projectMapper::mapToDTO).toList();
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/completed/")
    public ResponseEntity<List<ProjectDTO>> getCompletedProjects() {
        List<Project> projects = projectService.getProjectsByIsComplete(true);
        List<ProjectDTO> projectDTOs = projects.stream().map(projectMapper::mapToDTO).toList();
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/in-progress/")
    public ResponseEntity<List<ProjectDTO>> getInProgressProjects() {
        List<Project> projects = projectService.getProjectsByIsComplete(false);
        List<ProjectDTO> projectDTOs = projects.stream().map(projectMapper::mapToDTO).toList();
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable @Positive Long projectId) {
        Project project = projectService.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        ProjectDTO projectDTO = projectMapper.mapToDTO(project);
        return ResponseEntity.ok(projectDTO);
    }

    @GetMapping("/{projectId}/tasks/")
    public ResponseEntity<List<Task>> getTasksForProject(@PathVariable @Positive Long projectId) {
        return ResponseEntity.ok(taskService.getTasksForProject(projectId));
    }

    @PostMapping("/")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        Project project = projectMapper.mapToEntity(projectDTO);
        Project savedProject = projectService.create(project);
        ProjectDTO resultDTO = projectMapper.mapToDTO(savedProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultDTO);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable @Positive Long projectId, @Valid @RequestBody ProjectDTO projectDTO) {
        Project project = projectMapper.mapToEntity(projectDTO);
        project = projectService.update(projectId, project);
        projectDTO = projectMapper.mapToDTO(project);
        return ResponseEntity.ok(projectDTO);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable @Positive Long projectId) {
        projectService.deleteById(projectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable @Positive Long taskId, @Valid @RequestBody TaskDTO taskDTO) {
        Task task = taskMapper.mapToEntity(taskDTO);
        Task updatedTask = taskService.update(taskId, task);
        TaskDTO updatedTaskDTO = taskMapper.mapToDTO(updatedTask);
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @PostMapping("/{projectId}/tasks/")
    public ResponseEntity<TaskDTO> addTaskToProject(@PathVariable @Positive Long projectId, @Valid @RequestBody TaskDTO taskDTO) {
        Task task = taskMapper.mapToEntity(taskDTO);
        Project project = projectService.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        task.setProject(project);
        project.getTasks().add(task);

        Task savedTask = taskService.save(task);
        TaskDTO savedTaskDTO = taskMapper.mapToDTO(savedTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTaskDTO);
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<ProjectDTO>> getProjectsByAssignedUserId(@RequestParam @Positive Long userId) {
        List<Project> projects = projectService.findProjectsByUserId(userId);
        List<ProjectDTO> projectDTOs = projects.stream().map(projectMapper::mapToDTO).toList();
        return ResponseEntity.ok(projectDTOs);
    }
}
