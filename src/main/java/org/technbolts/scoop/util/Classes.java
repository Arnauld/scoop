package org.technbolts.scoop.util;

import static fj.data.List.iterableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fj.F;

public class Classes {
    
    public static Class<?>[] toArray(Class<?>...classes) {
        return classes;
    }
    
    /**
     * Mutable version of {@link #toList(Class...)}.
     */
    public static ArrayList<Class<?>> toArrayList(Class<?>...classes) {
        return New.arrayList(classes);
    }
    
    /**
     * Returns a fixed-size list backed by the specified array.
     * 
     * @see Arrays#asList(Object...)
     */
    public static List<Class<?>> toList(Class<?>...classes) {
        return Arrays.asList(classes);
    }

    public static F<Class<?>, String> simpleNameF () {
        return new F<Class<?>, String>() {
            @Override
            public String f(Class<?> a) {
                return a.getSimpleName();
            }
        };
    }

    public static String toSimpleNames(Class<?>...interfaces) {
        return toSimpleNames(fj.data.Array.array(interfaces));
    }

    public static String toSimpleNames(Iterable<Class<?>> interfaces) {
        return toSimpleNames(iterableList(interfaces));
    }
    
    public static String toSimpleNames(fj.data.List<Class<?>> list) {
        StringBuilder builder = list.map(simpleNameF()).foldLeft(Strings.joinF(", "), new StringBuilder());
        return builder.toString();
    }

}
