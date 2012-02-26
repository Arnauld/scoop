package org.technbolts.usecase.scrum.infra;

public interface Id<T extends Id<T>> {
    String uuid();
}
