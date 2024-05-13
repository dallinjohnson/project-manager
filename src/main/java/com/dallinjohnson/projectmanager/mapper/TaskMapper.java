package com.dallinjohnson.projectmanager.mapper;

import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.dto.TaskDTO;

public interface TaskMapper {
    TaskDTO mapToDTO(Task task);
    Task mapToEntity(TaskDTO taskDTO);
}
