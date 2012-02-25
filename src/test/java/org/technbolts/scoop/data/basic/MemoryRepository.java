package org.technbolts.scoop.data.basic;

import java.util.Map;

import org.technbolts.scoop.data.HasId;
import org.technbolts.scoop.util.New;

public class MemoryRepository implements Repository {
    
    private Map<Long,HasId> data = New.hashMap();

    public void save(HasId o) {
        data.put(o.id(), o);
    }
    
    public HasId load(long id) {
        return data.get(id);
    }
}
