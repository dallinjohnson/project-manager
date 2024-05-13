package com.dallinjohnson.projectmanager.mapper;

import com.dallinjohnson.projectmanager.domain.Project;
import com.dallinjohnson.projectmanager.dto.ProjectDTO;

public interface ProjectMapper {
    ProjectDTO mapToDTO(Project project);
    Project mapToEntity(ProjectDTO projectDTO);
}
