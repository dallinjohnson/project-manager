package com.dallinjohnson.projectmanager.mapper;

import com.dallinjohnson.projectmanager.domain.Project;
import com.dallinjohnson.projectmanager.dto.ProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface ProjectMapper {
    @Mapping(target = "tasks", source = "tasks")
    ProjectDTO projectToProjectDTO(Project project);

    Project projectDTOToProject(ProjectDTO projectDTO);
}
