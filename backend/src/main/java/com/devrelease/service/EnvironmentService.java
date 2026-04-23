package com.devrelease.service;

import com.devrelease.dto.request.EnvironmentRequest;
import com.devrelease.dto.response.EnvironmentResponse;
import com.devrelease.enums.EnvironmentStatus;
import com.devrelease.exception.ResourceNotFoundException;
import com.devrelease.model.Environment;
import com.devrelease.model.Project;
import com.devrelease.repository.EnvironmentRepository;
import com.devrelease.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;
    private final ProjectRepository projectRepository;

    public EnvironmentService(EnvironmentRepository environmentRepository, ProjectRepository projectRepository) {
        this.environmentRepository = environmentRepository;
        this.projectRepository = projectRepository;
    }

    public EnvironmentResponse create(Long projectId, EnvironmentRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        Environment env = new Environment();
        env.setName(request.getName());
        env.setType(request.getType());
        env.setUrl(request.getUrl());
        env.setStatus(EnvironmentStatus.ACTIVE);
        env.setProject(project);
        return toResponse(environmentRepository.save(env));
    }

    public List<EnvironmentResponse> listByProject(Long projectId) {
        return environmentRepository.findByProjectId(projectId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public EnvironmentResponse getById(Long id) {
        return toResponse(environmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Environment not found")));
    }

    public EnvironmentResponse update(Long id, EnvironmentRequest request) {
        Environment env = environmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Environment not found"));
        env.setName(request.getName());
        env.setType(request.getType());
        env.setUrl(request.getUrl());
        return toResponse(environmentRepository.save(env));
    }

    public void delete(Long id) {
        environmentRepository.deleteById(id);
    }

    private EnvironmentResponse toResponse(Environment e) {
        EnvironmentResponse r = new EnvironmentResponse();
        r.setId(e.getId());
        r.setName(e.getName());
        r.setType(e.getType());
        r.setUrl(e.getUrl());
        r.setStatus(e.getStatus());
        r.setProjectId(e.getProject().getId());
        return r;
    }
}
