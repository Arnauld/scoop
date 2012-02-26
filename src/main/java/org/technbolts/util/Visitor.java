package org.technbolts.util;

public abstract class Visitor<E> {

    private volatile boolean isDone;
    
    public abstract void visit(E element);
    
    public boolean isDone() {
        return isDone;
    }
    
    protected void markDone() {
        this.isDone = true;
    }
    
}
