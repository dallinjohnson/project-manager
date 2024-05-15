package com.dallinjohnson.projectmanager.mapper;

import com.dallinjohnson.projectmanager.domain.Project;
import com.dallinjohnson.projectmanager.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ProjectMapperImpl implements ProjectMapper {

    private final TaskMapper taskMapper;

    @Autowired
    public ProjectMapperImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public ProjectDTO mapToDTO(Project project) {
        if (project == null) { return null; }

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setComplete(project.isComplete());

        if (project.getTasks() != null) {
            projectDTO.setTasks(project.getTasks().stream()
                    .map(taskMapper::mapToDTO)
                    .collect(Collectors.toList()));
        } else {
            projectDTO.setTasks(Collections.emptyList());
        }

        return projectDTO;
    }

    @Override
    public Project mapToEntity(ProjectDTO projectDTO) {
        if (projectDTO == null) { return null; }

        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setComplete(projectDTO.isComplete());

        return project;
    }
}
