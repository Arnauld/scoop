package org.technbolts.usecase.scrum.domain.project;

import org.technbolts.scoop.data.Callback;
import org.technbolts.usecase.scrum.domain.story.Story;
import org.technbolts.usecase.scrum.domain.user.UserId;

public interface Project {
    void changeProjectName(String name);

    void changeProjectDescription(String description);

    void addTeamMember(Role role, UserId user, Callback<TeamMember> cb);

    void createStory(String title, String description, Callback<Story> cb);
}
