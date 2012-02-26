package org.technbolts.scoop.factory;

import java.util.List;

import org.technbolts.util.Classes;

public class ClassCombinaison implements Combinaison {

    public static ClassCombinaison from(List<Class<?>> list) {
        return new ClassCombinaison(list.toArray(new Class<?>[list.size()]));
    }

    private final Class<?>[] values;

    public ClassCombinaison(Class<?>[] values) {
        super();
        this.values = values;
    }
    
    public int size() {
        return values.length;
    }

    public Class<?>[] getValues() {
        return values;
    }
    
    @Override
    public String toString() {
        return "ClassCombinaison [" + Classes.toSimpleNames(values) + "]";
    }

    public Class<?> valueAt(int i) {
        return values[i];
    }
    
}