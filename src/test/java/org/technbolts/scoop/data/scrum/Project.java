package org.technbolts.scoop.data.scrum;

public interface Project {
    void changeProjectName(String name);

    void changeProjectDescription(String description);

    void addTeamMember(Role role, User user);

    void createStory(String title, String description);
}
