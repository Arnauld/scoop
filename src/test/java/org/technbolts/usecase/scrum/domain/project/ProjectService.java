package org.technbolts.usecase.scrum.domain.project;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.technbolts.scoop.annotation.Async;
import org.technbolts.scoop.annotation.AsyncResult;

@Service
public class ProjectService {
    
    @Autowired
    private ProjectAssembly projectAssembly;

    @Async
    public Future<Project> createProject(ProjectId projectId, String name, String description) {
        Project project = projectAssembly.createProject(projectId, name, description);
        return new AsyncResult<Project>(project);
    }
}
