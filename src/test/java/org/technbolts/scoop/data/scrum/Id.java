package org.technbolts.scoop.data.scrum;

public interface Id<T extends Id<T>> {
    String uuid();
}
