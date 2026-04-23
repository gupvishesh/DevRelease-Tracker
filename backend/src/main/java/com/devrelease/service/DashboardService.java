package com.devrelease.service;

import com.devrelease.dto.response.DashboardStatsResponse;
import com.devrelease.repository.DeploymentRepository;
import com.devrelease.repository.ProjectRepository;
import com.devrelease.repository.ReleaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final ReleaseRepository releaseRepository;
    private final DeploymentRepository deploymentRepository;

    public DashboardService(ProjectRepository projectRepository,
                            ReleaseRepository releaseRepository,
                            DeploymentRepository deploymentRepository) {
        this.projectRepository = projectRepository;
        this.releaseRepository = releaseRepository;
        this.deploymentRepository = deploymentRepository;
    }

    public DashboardStatsResponse getStats() {
        long totalProjects = projectRepository.count();
        long totalReleases = releaseRepository.count();
        long deploymentsToday = deploymentRepository.countDeploymentsToday(LocalDate.now());
        long totalDeployments = deploymentRepository.count();
        double successRate = totalDeployments == 0 ? 0.0
                : (double) deploymentRepository.countSuccessful() / totalDeployments * 100.0;
        return new DashboardStatsResponse(totalProjects, totalReleases, deploymentsToday, successRate);
    }
}
