package org.technbolts.usecase.scrum.domain.sprint;

import org.joda.time.LocalDateTime;
import org.technbolts.usecase.scrum.domain.task.TaskId;

public interface Sprint {
    void defineDateRange(LocalDateTime begDate, LocalDateTime endDate);
    void affectTask(TaskId taskId);
}
