package org.technbolts.scoop.data;

public class HasNameImpl implements HasName {
    
    private String name;
    public void setName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
