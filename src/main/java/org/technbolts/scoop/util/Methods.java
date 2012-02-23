package org.technbolts.scoop.util;

import static org.technbolts.scoop.util.Equals.areEquals;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Methods {
    
    public static boolean sameNames(Method m, Method method) {
        return m.getName().equals(method.getName());
    }
    
    public static boolean sameArgumentTypes(Method m, Method method) {
        return Arrays.equals(m.getParameterTypes(), method.getParameterTypes());
    }
    
    public static boolean sameReturnType(Method m1, Method m2) {
        return areEquals(m1.getReturnType(), m2.getReturnType());
    }
    
    /**
     * Shortcut for {@link #sameNames(Method, Method)}, {@link #sameArgumentTypes(Method, Method)}
     * and {@link #sameReturnType(Method, Method)}.
     */
    public static boolean sameSignature(Method m1, Method m2) {
        return  sameNames(m1, m2) && //
                sameArgumentTypes(m1, m2) && //
                sameReturnType(m1, m2);
    }
}
