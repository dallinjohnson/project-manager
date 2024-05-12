package com.dallinjohnson.projectmanager.mapper;

import com.dallinjohnson.projectmanager.domain.Task;
import com.dallinjohnson.projectmanager.dto.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {
    @Mapping(target = "users", source = "users")
    TaskDTO taskToTaskDTO(Task task);
    Task taskDTOToTask(TaskDTO taskDTO);
}
