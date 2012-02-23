package org.technbolts.scoop.util;

public class Equals {
    
    public static boolean areEquals(Object o1, Object o2) {
        if(o1==o2)
            return true;
        if(o1==null || o2==null)
            return false;
        return o1.equals(o2);
    }

}
