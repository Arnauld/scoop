package org.technbolts.scoop.data;

public class HasIdImpl implements HasId {
    
    private long id;
    public void setId(long id) {
        this.id = id;
    }

    public long id() {
        return id;
    }
}
