package com.dallinjohnson.projectmanager.service;

import com.dallinjohnson.projectmanager.domain.Project;
import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Optional<Project> findById(Long projectId) {
        return projectRepository.findById(projectId);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findByName(String name) {
        return projectRepository.findByName(name);
    }

    public List<Project> getProjectsByIsComplete(boolean isComplete) {
        return projectRepository.findByIsComplete(isComplete);
    }
    public List<Project> findProjectsByUserId(Long userId) {
        return projectRepository.findProjectsByUserId(userId);
    }

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    public Project update(Project project) {
        return projectRepository.save(project);
    }

    public void deleteById(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    public Task addTaskToProject(Long projectId, Task task) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        task.setProject(project);

        List<Task> tasks = project.getTasks();
        tasks.add(task);

        projectRepository.save(project);

        return task;
    }

    @Transactional
    public void removeTaskFromProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        List<Task> tasks = project.getTasks();
        tasks.removeIf(task -> task.getId().equals(taskId));
    }
}