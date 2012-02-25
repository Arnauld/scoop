package org.technbolts.scoop.data;

public class HasNameAndIdImpl implements HasId, HasName {
    
    private long id;
    private String name;

    public void setId(long id) {
        this.id = id;
    }
    public long id() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String name() {
        return name;
    }
}
