package org.technbolts.usecase.scrum.domain.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.technbolts.scoop.annotation.This;
import org.technbolts.scoop.data.Callback;
import org.technbolts.usecase.scrum.domain.story.Story;
import org.technbolts.usecase.scrum.domain.story.StoryAssembly;
import org.technbolts.usecase.scrum.domain.user.UserId;
import org.technbolts.usecase.scrum.infra.AggregateRoot;

public class ProjectMixin implements Project {
    
    @Autowired
    private TeamMemberAssembly teamMemberAssembly;
    
    @Autowired
    private StoryAssembly storyAssembly;
    
    @This
    public AggregateRoot<Project, ProjectId> aggregateRoot;

    private String description;
    private String name;

    /* (non-Javadoc)
     * @see org.technbolts.scoop.data.scrum.Project#changeProjectName(java.lang.String)
     */
    public void changeProjectName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.technbolts.scoop.data.scrum.Project#changeProjectDescription(java.lang.String)
     */
    public void changeProjectDescription(String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see org.technbolts.scoop.data.scrum.Project#addTeamMember(org.technbolts.scoop.data.scrum.Role, org.technbolts.scoop.data.scrum.User, org.technbolts.scoop.data.Callback)
     */
    public void addTeamMember(Role role, UserId userId, Callback<TeamMember> cb) {
        TeamMember teamMember = teamMemberAssembly.createTeamMember(
                aggregateRoot.getAggregateId(), role, userId);
        cb.onSuccess(teamMember);
    }

    /* (non-Javadoc)
     * @see org.technbolts.scoop.data.scrum.Project#createStory(java.lang.String, java.lang.String, org.technbolts.scoop.data.Callback)
     */
    public void createStory(String title, String description, Callback<Story> cb) {
        
    }

}
