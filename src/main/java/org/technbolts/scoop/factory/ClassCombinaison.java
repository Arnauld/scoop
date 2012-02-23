package org.technbolts.scoop.factory;

import java.util.List;

import org.technbolts.scoop.util.Classes;

public class ClassCombinaison {
    
    private final Class<?>[] values;

    public ClassCombinaison(Class<?>[] values) {
        super();
        this.values = values;
    }
    
    public int count() {
        return values.length;
    }

    public Class<?>[] getValues() {
        return values;
    }
    
    @Override
    public String toString() {
        return Classes.toSimpleNames(values);
    }

    public static ClassCombinaison from(List<Class<?>> list) {
        return new ClassCombinaison(list.toArray(new Class<?>[list.size()]));
    }

    public Class<?> valueAt(int i) {
        return values[i];
    }
    
}