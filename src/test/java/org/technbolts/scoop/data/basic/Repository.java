package org.technbolts.scoop.data.basic;

import org.technbolts.scoop.data.HasId;

public interface Repository {
    void save(HasId o);
    HasId load(long id);
}
