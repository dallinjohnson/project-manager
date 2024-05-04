package com.dallinjohnson.projectmanager.service;

import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Get active tasks by user id

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Long taskId, Task task) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        existingTask.setName(task.getName());
        existingTask.setStartDate(task.getStartDate());
        existingTask.setEndDate(task.getEndDate());
        existingTask.setComplete(task.isComplete());

        return taskRepository.save(existingTask);
    }

    public Optional<Task> findById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        } else {
            throw new EntityNotFoundException("Task not found with ID: " + taskId);
        }
    }

    public List<Task> getTasksForProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> getTasksByAssignedUserId(Long userId) {
        return taskRepository.findTasksByUserId(userId);
    }
}
