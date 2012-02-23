package org.technbolts.scoop.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  Various factory methods to reduce java's generic verbosity by inference.
 *
 */
public class New {

    public static <E> ArrayList<E> arrayList() {
        return new ArrayList<E>();
    }
    
    public static <E> ArrayList<E> arrayList(List<E> values) {
        return new ArrayList<E>(values);
    }
    
    public static <E> ArrayList<E> arrayList(E...values) {
        ArrayList<E> list = new ArrayList<E>(values.length);
        for(E value : values)
            list.add(value);
        return list;
    }
    
    public static <K,V> HashMap<K, V> hashMap() {
        return new HashMap<K, V>();
    }

    public static <K,V> ConcurrentHashMap<K,V> concurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }

    public static <T> HashSet<T> hashSet() {
        return new HashSet<T>();
    }

}
