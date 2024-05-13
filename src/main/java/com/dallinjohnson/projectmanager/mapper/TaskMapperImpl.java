package com.dallinjohnson.projectmanager.mapper;

import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TaskMapperImpl implements TaskMapper {

    private final UserMapper userMapper;

    @Autowired
    public TaskMapperImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public TaskDTO mapToDTO(Task task) {
        if (task == null) { return null; }

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setStartDate(task.getStartDate());
        taskDTO.setEndDate(task.getEndDate());
        taskDTO.setComplete(task.isComplete());
        taskDTO.setAssignedUsers(task.getAssignedUsers().stream()
                .map(userMapper::mapToDTO)
                .collect(Collectors.toList()));

        return taskDTO;
    }

    @Override
    public Task mapToEntity(TaskDTO taskDTO) {
        if (taskDTO == null) { return null; }

        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setName(taskDTO.getName());
        task.setStartDate(taskDTO.getStartDate());
        task.setEndDate(taskDTO.getEndDate());
        task.setComplete(taskDTO.isComplete());

        return task;
    }
}
