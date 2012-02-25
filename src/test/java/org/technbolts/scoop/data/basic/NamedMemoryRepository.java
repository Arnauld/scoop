package org.technbolts.scoop.data.basic;

import org.technbolts.scoop.data.HasName;

public class NamedMemoryRepository extends MemoryRepository implements HasName {

    private String name;
    public NamedMemoryRepository(String name) {
        this.name = name;
    }
    
    public String name() {
        return name;
    }
}
