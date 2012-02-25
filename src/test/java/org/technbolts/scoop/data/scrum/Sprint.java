package org.technbolts.scoop.data.scrum;

import org.joda.time.LocalDateTime;

public interface Sprint {
    void defineDateRange(LocalDateTime begDate, LocalDateTime endDate);
    void affectTask(TaskId taskId);
}
